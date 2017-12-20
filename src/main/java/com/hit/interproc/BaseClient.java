/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.wiitrans.base.interproc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.wiitrans.base.log.Log4j;
import com.wiitrans.base.misc.Const;

public class BaseClient {
	
	private String _ip = null;
	private int _port = -1;
	private IClient _newServer = null;
	private boolean _isSupportFrameLength = true;
	
	public BaseClient(String ip, int port)
	{
		_ip = ip;
		_port = port;
	}
	
	public BaseClient(String ip, int port, boolean isSupportFrameLength)
	{
		_ip = ip;
		_port = port;
		_isSupportFrameLength = isSupportFrameLength;
	}
	
	public int SetNewServerCallBack(IClient newServer)
	{
		int ret = Const.FAIL;
		
		_newServer = newServer;
		
		return ret;
	}

	public int Run() {
		
		int ret = Const.FAIL;
		
		EventLoopGroup group = new NioEventLoopGroup();

		BaseClientHandler handler = new BaseClientHandler();
		handler.SetNewServerCallBack(_newServer);
		
		ChannelHandler childHandler = new BaseClientInitializer(handler, _isSupportFrameLength);

		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(childHandler);

			ChannelFuture future;

			future = bootstrap.connect(_ip, _port).sync();
			future.channel().closeFuture().sync();
			ret = Const.SUCCESS;

		} catch (Exception e) {
			Log4j.error(e);
		} finally {
			group.shutdownGracefully();
		}

		return ret;
	}
}
