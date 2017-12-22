/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.manger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hit.http.interproc.Client;
import com.hit.http.manger.ClientQueue.ResponseMeta;
import com.hit.http.misc.Conf;
import com.hit.http.misc.Const;

import io.netty.buffer.ByteBuf;

/**
 * 管理client的pop和send（push到bundleQueue）
 * 
 * @attribute
 * @see	BundleQueue _bundleQueue = null; 
 *	ExecutorService _threadPool = null; ？？线程池
 * @see	ClientQueue _clientQueue = null;
 * @see	ConcurrentHashMap<String, Client> _clientMap = null; //线程
 * 
 * @method
 * Init() 初始化线程池与map并调用自身的run方法，pop队列，接收Response。
 * Send(Client client) 像bundleQueue推送消息
 * _bundleQueue.Push(client.GetId(), client);
 * Request(Client client) 读取client中的内容，并且放入map，推送至bundleQueue
 * SetContent(String clientId, ByteBuf content, boolean isSplit) 通过clinetId查找map中的client并将内容推送至bundleQueue 
 * Response _clientQueue.Pop() 异步从队列中拿出 ResponseMeta，并通过clientId拿到相应的client，并通过ctx写入相应信息，之后从map中移除client释放连接。
 * 
 * 
 * @author THINK
 */

public class ClientManager extends Thread {

	private BundleQueue _bundleQueue = null;
	private ExecutorService _threadPool = null;
	private ClientQueue _clientQueue = null;
	private ConcurrentHashMap<String, Client> _clientMap = null;

	public ClientManager(BundleQueue bundleQueue, ClientQueue clientQueue) {
		_bundleQueue = bundleQueue;
		_clientQueue = clientQueue;
	}

	private int Init() {
		int ret = Const.FAIL;

		_clientMap = new ConcurrentHashMap<String, Client>();

		_threadPool = Executors.newFixedThreadPool(Conf.RESPONSE_THREAD_NUM);
		for (int index = 0; index < Conf.RESPONSE_THREAD_NUM; ++index) {
			_threadPool.execute(this);
		}
		ret = Const.SUCCESS;

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

	private int Send(Client client) {
		int ret = Const.FAIL;

		if ((client != null) && (client.IsCompleted())) {
			_bundleQueue.Push(client.GetId(), client);
		}

		ret = Const.SUCCESS;

		return ret;
	}

	public int Request(Client client) {
		String clientId = client.GetContext().toString();

		if (clientId != null) {
			_clientMap.put(clientId, client);
		}

		return Send(client);
	}

	public int SetContent(String clientId, ByteBuf content, boolean isSplit) {
		Client client = null;
		if (clientId != null) {
			if (_clientMap.containsKey(clientId)) {
				client = _clientMap.get(clientId);
				client.SetContent(content, isSplit);
			}
		}

		return Send(client);
	}

	private void Response() {
		ResponseMeta meta = _clientQueue.Pop();
		if (_clientMap.containsKey(meta._clientId)) {
			try {
				Client cli = _clientMap.remove(meta._clientId);
				if (cli != null) {
					cli.Response(meta._res);
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		}
	}

	public void run() {
		while (true) {
			Response();
		}
	}
}
