/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.wiitrans.base.interproc;

import com.wiitrans.base.misc.Const;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class BaseClientHandler extends ChannelInboundHandlerAdapter {
	
	private IClient _newServer = null;
	
	public BaseClientHandler()
	{
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx)
	{
		_newServer.NewServer(new Server(ctx, null));
    }

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
	{
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
		_newServer.NewMsg(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
	
	public int SetNewServerCallBack(IClient newServer)
	{
		int ret = Const.FAIL;
		
		_newServer = newServer;
		
		return ret;
	}
}
