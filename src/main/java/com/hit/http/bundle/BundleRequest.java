/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.bundle;

import java.util.concurrent.LinkedBlockingQueue;

import com.hit.http.interproc.Client;
import com.hit.http.misc.Const;

/**
 * 向spout发送的Request
 * @author THINK
 *
 */
public class BundleRequest extends Thread {

	private LinkedBlockingQueue<String> _queue = new LinkedBlockingQueue<String>();
	/**
	 * 此client与bundle共用。
	 */
	private Client _client = null;

	public int SetClient(Client client) {
		int ret = Const.FAIL;

		if (_client != null) {
			_client.GetContext().close();
		}

		_client = client;
		ret = Const.SUCCESS;

		return ret;
	}

	public int Start() {
		int ret = Const.FAIL;

		this.start();
		ret = Const.SUCCESS;

		return ret;
	}

	public int Stop() {
		int ret = Const.FAIL;

		return ret;
	}

	public int Push(String req) {
		int ret = Const.FAIL;

		try {

			if (req == null) {
				System.err.println("BundleRequest.Push()  req is null. ");
			} else {
				//TODO:读到了这里
				_queue.put(req);
			}
			ret = Const.SUCCESS;

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public String Pop() {
		String req = null;

		try {
			req = _queue.take();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return req;
	}

	private int Request() {
		int ret = Const.FAIL;

		String msg = Pop();

		if (_client != null) {
			_client.Request(msg.getBytes());
		} else {
			System.err.println("_client is null.");
		}

		return ret;
	}

	public void run() {
		while (true) {
			Request();
		}
	}
}
