package com.hit.http.manger;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;
import com.hit.http.bundle.Bundle;
import com.hit.http.bundle.BundleParam;
import com.hit.http.bundle.IBundle;
import com.hit.http.bundle.IResponse;
import com.hit.http.manger.BundleQueue.RequestMeta;
import com.hit.http.misc.Conf;
import com.hit.http.misc.Const;

/**
 * @attribute like {@link ClientManager} attribute description
 * 		private ClientQueue _clientQueue = null;
		private BundleQueue _bundleQueue = null;
		private ExecutorService _threadPool = null;
		// Bundle session.
		private ConcurrentHashMap<String, IBundle> _bundleMap = null;
 * @method
 * 	Init()
 * 	 1.初始化线程池与map并调用自身的run方法，pop队列，接收Request。
 * 	 2.;{@link LoadBundles()} 加载bundle，storm的节点
 *  LoadBundles()
 *   1.通过配置决定是由jar包方式读取，还是class方式读取Bundle，具体客户端的入口，向客户端push。
 *   2.调用RunBundle(bundle);
 *  RunBundle(IBundle bundle)
 *   1.调用bundle.start(); bundle本身为线程，调用run方法，启动了一个BaseServer()
 *	 2.线程启动，调用自身的run方法，启动了一个BaseServer（NettyServer）,不已http方式连接（isHttp=false）
 * @author THINK
 * 
 *
 */

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

	/**
	 * 通过配置文件加载bundle，调用Start
	 * @param bundle
	 * @return
	 */
	private int RunBundle(IBundle bundle) {
		int ret = Const.FAIL;
		if (bundle != null) {
				String bundleId = bundle.GetBundleId();
				if (Const.SUCCESS == bundle.Start()) {
				if (!_bundleMap.containsKey(bundleId)) {
					bundle.SetResponse(this);
					_bundleMap.put(bundleId, bundle);

					System.out.println("Bundle[" + bundleId + "] is loaded.");

					ret = Const.SUCCESS;
				} else {
					// TODO : Bundle id is exist.
					System.out.println("Bundle[" + bundleId + "] is exist.");
				}
			} else {
				System.out.println("Start bundle error.");
			}
		}

		return ret;
	}

	private int LoadClass(String filePath, String fileName) {
		int ret = Const.FAIL;

		IBundle bundle = new Bundle();
		ret = RunBundle(bundle);
		if (ret != Const.SUCCESS) {
			String msg = "Load bundle class[" + filePath + "][" + fileName
					+ "] error.";
			System.out.println(msg);
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
				System.out.println(msg);
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return ret;
	}

	private int LoadBundles() {
		int ret = Const.FAIL;

		 List<BundleParam> bundlenames = new ArrayList<>();
		
		for (BundleParam param : bundlenames) {
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
					System.out.println("Bundle [" + bundleId + "] is exception.");
					System.out.println(e);
				}
			} else {
				System.out.println("Bundle [" + bundleId + "] is not register.");
			}
		} else {
			System.out.println("BundleManager receive invalid msg.");
		}
	}

	public void run() {
		while (true) {
			Request();
		}
	}
}
