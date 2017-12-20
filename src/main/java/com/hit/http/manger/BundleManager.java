/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.manger;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import com.hit.http.manger.BundleQueue.RequestMeta;
import com.wiitrans.base.bundle.BundleParam;
import com.wiitrans.base.bundle.IBundle;
import com.wiitrans.base.bundle.IResponse;
import com.wiitrans.base.log.Log4j;
import com.wiitrans.base.misc.BundleClassLoader;
import com.wiitrans.base.misc.Const;
import com.wiitrans.base.xml.AppConfig;
import com.wiitrans.base.xml.WiitransConfig;
import com.wiitrans.conf.Conf;

public class BundleManager extends Thread implements IResponse {

	private ClientQueue _clientQueue = null;
	private BundleQueue _bundleQueue = null;
	private ExecutorService _threadPool = null;
	// Bundle session.
	private ConcurrentHashMap<String, IBundle> _bundleMap = null;

	public BundleManager(BundleQueue bundleQueue, ClientQueue clientQueue) {
		_bundleQueue = bundleQueue;
		_clientQueue = clientQueue;
	}

	private int Init() {
		int ret = Const.FAIL;

		_bundleMap = new ConcurrentHashMap<String, IBundle>();

		_threadPool = Executors.newFixedThreadPool(Conf.REQUEST_THREAD_NUM);
		for (int index = 0; index < Conf.REQUEST_THREAD_NUM; ++index) {
			_threadPool.execute(this);
		}

		ret = LoadBundles();

		return ret;
	}

	private int UnInit() {
		int ret = Const.FAIL;

		return ret;
	}

	public int Start() {
		int ret = Const.FAIL;

		ret = Init();

		return ret;
	}

	public int Stop() {
		int ret = Const.FAIL;

		ret = UnInit();

		return ret;
	}

	private int RunBundle(IBundle bundle) {
		int ret = Const.FAIL;
		if (bundle != null) {
			if (Const.SUCCESS == bundle.Start()) {
				String bundleId = bundle.GetBundleId();
				if (!_bundleMap.containsKey(bundleId)) {
					bundle.SetResponse(this);
					_bundleMap.put(bundleId, bundle);

					Log4j.log("Bundle[" + bundleId + "] is loaded.");

					ret = Const.SUCCESS;
				} else {
					// TODO : Bundle id is exist.
					Log4j.log("Bundle[" + bundleId + "] is exist.");
				}
			} else {
				Log4j.error("Start bundle error.");
			}
		}

		return ret;
	}

	private int LoadClass(String filePath, String fileName) {
		int ret = Const.FAIL;

		IBundle bundle = new BundleClassLoader<IBundle>().Load(filePath,
				fileName);
		ret = RunBundle(bundle);
		if (ret != Const.SUCCESS) {
			String msg = "Load bundle class[" + filePath + "][" + fileName
					+ "] error.";
			Log4j.error(msg);
		}

		return ret;
	}

	private int LoadJar(String filePath, String fileName) {
		int ret = Const.FAIL;

		try {
			URL url = new URL(filePath);
			URLClassLoader loader = new URLClassLoader(new URL[] { url },
					Thread.currentThread().getContextClassLoader());
			Class<?> bClass = loader.loadClass(fileName);
			IBundle bundle = (IBundle) bClass.newInstance();

			ret = RunBundle(bundle);
			if (ret != Const.SUCCESS) {
				String msg = "Load bundle jar[" + filePath + "][" + fileName
						+ "] error.";
				Log4j.error(msg);
			}

		} catch (Exception e) {
			Log4j.error(e);
		}

		return ret;
	}

	private int LoadBundles() {
		int ret = Const.FAIL;

		// AppConfig app = new AppConfig();
		// app.ParseBundle();
		//
		// Set<String> bundlenames = app._bundles.keySet();

		for (BundleParam param : WiitransConfig.getInstance(1)
				.GetBundelParams()) {
			switch (param.BUNDLE_FILE_TYPE) {
			case "class": {
				ret = LoadClass(param.BUNDLE_CLASS_FILEPATH,
						param.BUNDLE_CLASS_FILENAME);
				if (ret != Const.SUCCESS) {
					break;
				}
				break;
			}
			case "jar": {
				ret = LoadJar(param.BUNDLE_JAR_FILENAME,
						param.BUNDLE_CLASS_FILENAME);
				if (ret != Const.SUCCESS) {
					break;
				}
				break;
			}
			default:
				break;
			}
		}

		return ret;
	}

	public int Response(String clientId, byte[] msg) {
		return _clientQueue.Push(clientId, msg);
	}

	private void Request() {
		RequestMeta meta = _bundleQueue.Pop();
		String bundleId = meta._client.GetBundleId();
		JSONObject bundleInfo = meta._client.GetBundleInfoJSON();

		if ((bundleId != null) && (!bundleId.isEmpty()) && (bundleInfo != null)) {
			if (_bundleMap.containsKey(bundleId)) {
				try {
					_bundleMap.get(bundleId).Request(bundleInfo);

				} catch (Exception e) {
					Log4j.error("Bundle [" + bundleId + "] is exception.");
					Log4j.error(e);
				}
			} else {
				Log4j.error("Bundle [" + bundleId + "] is not register.");
			}
		} else {
			Log4j.error("BundleManager receive invalid msg.");
		}
	}

	public void run() {
		while (true) {
			Request();
		}
	}
}
