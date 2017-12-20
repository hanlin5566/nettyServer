/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.manger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.hit.http.interproc.Client;
import com.hit.http.manger.ClientQueue.ResponseMeta;
import com.hit.http.misc.Const;

import io.netty.buffer.ByteBuf;

public class ClientManager extends Thread {

	private BundleQueue _bundleQueue = null;
	private ExecutorService _threadPool = null;
	private ClientQueue _clientQueue = null;
	private ConcurrentHashMap<String, Client> _clientMap = null;
	
	public ClientManager(BundleQueue bundleQueue, ClientQueue clientQueue)
	{
		_bundleQueue = bundleQueue;
		_clientQueue = clientQueue;
	}
	
	private int Init()
	{
		int ret = Const.FAIL;
		
		_clientMap = new ConcurrentHashMap<String, Client>();
		
		_threadPool = Executors.newFixedThreadPool(Conf.RESPONSE_THREAD_NUM);
		for(int index = 0; index < Conf.RESPONSE_THREAD_NUM; ++index)
		{
			_threadPool.execute(this);
		}
		/**
		 * 添加打印线程池激活线程总数
		 */
		try {
		    new Thread(new Runnable() {
		        long sleepTime = 1000*60*60*5;
		        @Override
		        public void run() {
		    	// TODO Auto-generated method stub
		            while(true){
		        	try {
		        	    int activeCount = ((ThreadPoolExecutor)_threadPool).getActiveCount();
		        	    Log4j.info("###### ClientManager ThreadPool Active:"+activeCount +"  ######");
		        	    Log4j.info("###### _clientQueue.size():"+_clientQueue.getSize() +"  ######");
		        	    Log4j.info("###### _bundleQueue.size():"+_bundleQueue.getSize() +"  ######");
				    Thread.sleep(sleepTime);
				} catch (Exception e) {
				    // TODO Auto-generated catch block
				    Log4j.log(e);
				}
		            }
		        }
		    }).start();
		} catch (Exception e) {
		    // TODO: handle exception
		}
		ret = Const.SUCCESS;
		
		return ret;
	}
	
	private int UnInit()
	{
		int ret = Const.FAIL;
		
		return ret;
	}
	
	public int Start()
	{
		int ret = Const.FAIL;
		
		ret = Init();
		
		return ret;
	}
	
	public int Stop()
	{
		int ret = Const.FAIL;
		
		ret = UnInit();
		
		return ret;
	}
	
	private int Send(Client client)
	{
		int ret = Const.FAIL;
		
		if((client != null) && (client.IsCompleted()))
		{
			_bundleQueue.Push(client.GetId(), client);
		}
		
		ret = Const.SUCCESS;
		
		return ret;
	}
	
	public int Request(Client client)
	{
		String clientId = client.GetContext().toString();
		
		if(clientId != null)
		{
			_clientMap.put(clientId, client);
		}
		
		return Send(client);
	}
	
	public int SetContent(String clientId, ByteBuf content, boolean isSplit)
	{
		Client client = null;
		if(clientId != null)
		{
			if(_clientMap.containsKey(clientId))
			{
				client = _clientMap.get(clientId);
				client.SetContent(content, isSplit);
			}
		}		
		
		return Send(client);
	}
	
	private void Response()
	{
		ResponseMeta meta = _clientQueue.Pop();
		if(_clientMap.containsKey(meta._clientId))
		{
			try {
				Client cli = _clientMap.remove(meta._clientId);
				if(cli != null)
				{
					cli.Response(meta._res);
				}else {
					if(BundleConf.DEBUG)
			        {
			        	Log4j.log("Client is null.");
			        }
				}
				
			} catch (Exception e) {
				Log4j.error(e);
			}
						
		}else {
			if(BundleConf.DEBUG)
	        {
				Log4j.error("SessionId [" + meta._clientId +"] is not exist.");
				Log4j.log("----------- Res begin -----------");
				if(meta._res == null)
				{
					Log4j.log("meta._res is null.");
				}else
				{
					Log4j.log(new String(meta._res));					
				}
				Log4j.log("----------- Res end -----------");
	        }
		}
	}
	
	public void run()
	{
		while(true)
		{
			Response();
		}
	}
}
