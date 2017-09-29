package com.hit.netty;


import org.apache.log4j.Logger;

import com.hit.netty.handler.OutBoundHandler;
import com.hit.netty.handler.TcpServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class TcpServer {
	private static final Logger logger = Logger.getLogger(TcpServer.class);
	private static final int PORT = 9999;
	/**用于分配处理业务线程的线程组个数 */
	protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors()*2;	//默认
	/** 业务出现线程大小*/
	protected static final int BIZTHREADSIZE = 4;
	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);
	protected static void run() throws Exception {
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup);
		b.channel(NioServerSocketChannel.class);
		b.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				 // Decoders
                //ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
                ch.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());
               	// Encoder
               	//ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
               	ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());                        	
            	ch.pipeline().addLast(new OutBoundHandler());
                ch.pipeline().addLast(new IdleStateHandler(0,0,300), new TcpServerHandler());
			}
		});

		b.bind(PORT).sync();
		logger.info("TCP服务器已启动");
	}

	protected static void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

	public static void main(String[] args) throws Exception {
		logger.info("开始启动TCP服务器...");
		TcpServer.run();
//		TcpServer.shutdown();
	}
}