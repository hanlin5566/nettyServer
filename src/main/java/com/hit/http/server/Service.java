/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.server;

import com.hit.http.interproc.BaseServer;
import com.hit.http.interproc.Client;
import com.hit.http.interproc.IServer;
import com.hit.http.manger.ClientManager;
import com.hit.http.misc.Const;

import io.netty.buffer.ByteBuf;

public class Service implements IServer {
	
	private ClientManager _clientManager = null;

	public Service(ClientManager clientManager) 
	{
		_clientManager = clientManager;
	}

	public int Start()
	{
		int ret = Const.FAIL;
		
		ret = Run();
		
		return ret;
	}
	
	public int Stop()
	{
		int ret = Const.FAIL;
		
		
		return ret;
	}
	
	private int Run()
	{
		int ret = Const.FAIL;
		
		BaseServer svr = new BaseServer();
		svr.SetPort(Conf.SERVICE_PORT);
		svr.SetNewClientCallBack(this);
		svr.Run(true);		
		
		return ret;
	}

	@Override
	public int NewClient(Client client)
	{
		return _clientManager.Request(client);
	}
	
	@Override
	public int SetContent(String clientId, ByteBuf content, boolean isSplit)
	{
		return _clientManager.SetContent(clientId, content, isSplit);
	}
}
