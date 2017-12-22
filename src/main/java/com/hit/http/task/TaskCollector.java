/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.task;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.hit.http.bundle.BaseClient;
import com.hit.http.bundle.IClient;
import com.hit.http.interproc.ThreadUtility;
import com.hit.http.misc.Const;
import com.hit.http.server.Service;

public class TaskCollector extends Thread implements IClient {

	private TaskQueue _queue = null;
	private BaseClient _client = null;
	protected Service _server = null;
	private String _ip = null;
	private int _port = 0;
	protected String _bundleId = null;
	private boolean _isRegister = false;

	public TaskCollector(String ip, int port, String bundleId) {

		_queue = new TaskQueue();
		_ip = ip;
		_port = port;
		_bundleId = bundleId;
	}

	public ArrayList<JSONObject> GetTasks() {
		ArrayList<JSONObject> tasks = null;

		Object obj = _queue.Pop();
		while (obj != null) {
			if (null == tasks) {
				tasks = new ArrayList<JSONObject>();
			}
			tasks.add((JSONObject) obj);
			obj = _queue.Pop();
		}

		return tasks;
	}

	public ArrayList<JSONObject> TakeTasks() {
		ArrayList<JSONObject> tasks = null;

		Object obj = _queue.Take();
		while (obj != null) {
			if (null == tasks) {
				tasks = new ArrayList<JSONObject>();
			}
			tasks.add((JSONObject) obj);
			obj = _queue.Pop();
		}

		return tasks;
	}

	public JSONObject GetTask() {
		JSONObject task = null;
		Object obj = _queue.Pop();
		if (obj != null) {
			task = (JSONObject) obj;
		}

		return task;
	}

	public int Start() {
		int ret = Const.FAIL;

		this.start();
		ret = Const.SUCCESS;

		return ret;
	}

	@Override
	public void run() {
		while (true) {
			if (Const.FAIL == ConnectToBundle()) {
				_isRegister = false;
				ThreadUtility.Sleep(3000);
			}
		}
	}

	private int ConnectToBundle() {
		int ret = Const.FAIL;

		_client = new BaseClient(_ip, _port);
		_client.SetNewServerCallBack(this);
		ret = _client.Run();

		return ret;
	}

	public int Register() {
		int ret = Const.FAIL;

		if (_server != null) {
			JSONObject obj = new JSONObject();
			obj.put(Const.BUNDLE_INFO_BUNDLE_ID, _bundleId);
			obj.put(Const.BUNDLE_INFO_STATE, Const.BUNDLE_REGISTER);
			obj.put(Const.BUNDLE_INFO_SESSION_ID, "");
			obj.put(Const.BUNDLE_INFO_ID, "");

			_server.Response(obj.toString().getBytes());

			ret = Const.SUCCESS;
		}

		return ret;
	}

	@Override
	public int NewServer(Service server) {

		int ret = Const.FAIL;

		_server = server;
		if (server.GetBundleInfoObj() != null) {
			_queue.Push(server.GetBundleInfoObj());
		} else {
			if (false == _isRegister) {
				if (Const.SUCCESS == Register()) {
					_isRegister = true;
				}
			}
		}
		ret = Const.SUCCESS;

		return ret;
	}

	@Override
	public int NewMsg(Object obj) {
		int ret = Const.FAIL;

		if (_server != null) {
			_queue.Push(_server.MsgToJSon(obj));
			ret = Const.SUCCESS;
		}

		return ret;
	}
}
