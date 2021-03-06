/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.wiitrans.base.interproc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.wiitrans.base.log.Log4j;
import com.wiitrans.base.misc.Const;

public class BaseServer {

	private int _port = -1;
	private IServer _newClient = null;

	public int SetPort(int port) {
		int ret = Const.FAIL;

		_port = port;
		ret = Const.SUCCESS;

		return ret;
	}

	public int SetNewClientCallBack(IServer newClient) {
		int ret = Const.FAIL;

		_newClient = newClient;

		return ret;
	}

	public int Run(Boolean isHttp) {
		int ret = Const.FAIL;

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			BaseServerHandler handler = new BaseServerHandler(isHttp);
			handler.SetNewClientCallBack(_newClient);

			ChannelHandler childHandler = new BaseServerInitializer(handler,
					isHttp);

			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

			if (Const.DEBUG) {
				bootstrap.group(bossGroup, workerGroup)
						.channel(NioServerSocketChannel.class)
						.handler(new DebugHandler()).childHandler(childHandler);
			} else {
				bootstrap.group(bossGroup, workerGroup)
						.channel(NioServerSocketChannel.class)
						.childHandler(childHandler);
			}

			Channel channel = bootstrap.bind(_port).sync().channel();

			channel.closeFuture().sync();

			ret = Const.SUCCESS;

		} catch (Exception e) {

			Log4j.error(e);
		} finally {

			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

		return ret;
	}
}
