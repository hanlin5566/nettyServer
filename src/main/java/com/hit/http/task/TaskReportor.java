/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.task;

import org.json.JSONObject;

import com.wiitrans.base.interproc.BaseClient;
import com.wiitrans.base.interproc.IClient;
import com.wiitrans.base.interproc.Server;
import com.wiitrans.base.interproc.ThreadUtility;
import com.wiitrans.base.misc.Const;

public class TaskReportor extends Thread implements IClient {
	
	private BaseClient _client = null;
	private Server _server = null;
	private String _ip = null;
	private int _port = -1;
	boolean _isSupportFrameLength = true;
	
	public TaskReportor(String ip, int port) {
		_ip = ip;
		_port = port;
	}
	
	public TaskReportor(String ip, int port, boolean isSupportFrameLength) {
		_ip = ip;
		_port = port;
		_isSupportFrameLength = isSupportFrameLength;
	}
	
	public int Start()
	{
		int ret = Const.FAIL;
		
		this.start();
		// TODO : check start status.
		ret = Const.SUCCESS;
		
		return ret;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			if(Const.FAIL == ConnectToBundle())
			{
				ThreadUtility.Sleep(3000);
			}
		}
	}
	
	private int ConnectToBundle()
	{
		int ret = Const.FAIL;
		
		_client = new BaseClient(_ip, _port, _isSupportFrameLength);
		_client.SetNewServerCallBack(this);
		ret = _client.Run();
		
		return ret;
	}
	
	public int Report(JSONObject obj)
	{
		int ret = Const.FAIL;
		
		if(_server != null)
		{
			ret = _server.Response(obj.toString().getBytes());
		}
		
		return ret;
	}

	@Override
	public int NewServer(Server server) {
		
		int ret = Const.FAIL;
		
		_server = server;
		
		return ret;
	}

	@Override
	public int NewMsg(Object msg)
	{
		int ret = Const.FAIL;
				
		return ret;
	}
}
