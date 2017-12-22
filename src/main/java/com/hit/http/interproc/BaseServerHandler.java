package com.hit.http.interproc;

import com.hit.http.misc.Const;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

@Sharable
/**
 * 封装 netty InboundHandler，（client上报的信息）
 * 
 * @author THINK
 *
 */
public class BaseServerHandler extends ChannelInboundHandlerAdapter {
	
	private Boolean _isHttp = false;
	private IServer _newClient = null;
	
	public BaseServerHandler(Boolean isHttp)
	{
		_isHttp = isHttp;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
	{
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
		if(_newClient != null)
		{			
			if(_isHttp)
			{
				if(msg instanceof HttpRequest)
				{
					Client client = new Client();
					client.SetContext(ctx);
					client.SetObject(msg);
					_newClient.NewClient(client);
				}else if(msg instanceof LastHttpContent)
				{
					//HttpContent hc = (HttpContent)msg;
					_newClient.SetContent(ctx.toString(), ((HttpContent)msg).content(), false);
					
				}else if(msg instanceof DefaultHttpContent)
				{
					//HttpContent hc = (HttpContent)msg;
					_newClient.SetContent(ctx.toString(), ((HttpContent)msg).content(), true);
					
				}else {
					// Nothing to do.
				}
				
			}else
			{
				// Not http.
				Client client = new Client();
				client.SetContext(ctx);
				client.SetObject(msg);
				_newClient.NewClient(client);
				
			}
		}else {
			System.out.println("BaseServerHandler client is null.");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
	
	public int SetNewClientCallBack(IServer newClient)
	{
		int ret = Const.FAIL;
		
		_newClient = newClient;
		
		return ret;
	}
}
