package com.hit.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.hit.netty.handler.InBoundHandler;
import com.hit.netty.handler.OutBoundHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;


public class TCPServerNetty{
	private static Logger logger = Logger.getLogger(InBoundHandler.class);
	
	private int port;
	private static Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();
	private static Map<String, String> IMEI_IPMap = new ConcurrentHashMap<String, String>();
	private static Map<String, String> IMSI_IPMap = new ConcurrentHashMap<String, String>();
	private static Map<String, byte[]> messageMap = new ConcurrentHashMap<String, byte[]>();
	
	public TCPServerNetty(int port){
		this.port = port;
	}
	
	public TCPServerNetty(){}
	
	public void start() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                       	    // Decoders
	                        //ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
	                        ch.pipeline().addLast("bytesDecoder", new ByteArrayDecoder());
	                       	// Encoder
	                       	//ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
	                       	ch.pipeline().addLast("bytesEncoder", new ByteArrayEncoder());                        	
                        	ch.pipeline().addLast(new OutBoundHandler());
                            ch.pipeline().addLast(new IdleStateHandler(0,0,300), new InBoundHandler());
                        }
                    });

            b.bind(port);
            // Start the server.
            //ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            //f.channel().closeFuture().sync();
        } catch (Exception e) {
        	logger.error(e);
        } finally {
            // Shut down all event loops to terminate all threads.
            //bossGroup.shutdownGracefully();
            //workerGroup.shutdownGracefully();
        }
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{
		new TCPServerNetty(8888).start();
	}

	public static Map<String, Channel> getMap() {
		return map;
	}

	public static void setMap(Map<String, Channel> map) {
		TCPServerNetty.map = map;
	}
	/**
	 * @return the messageMap
	 */
	public static Map<String, byte[]> getMessageMap() {
		return messageMap;
	}
	
	
	
	/**
	 * @param messageMap the messageMap to set
	 */
	public static void setMessageMap(Map<String, byte[]> messageMap) {
		TCPServerNetty.messageMap = messageMap;
	}

	public static Map<String, String> getIMEI_IPMap() {
		return IMEI_IPMap;
	}

	public static void setIMEI_IPMap(Map<String, String> iMEI_IPMap) {
		IMEI_IPMap = iMEI_IPMap;
	}

	public static Map<String, String> getIMSI_IPMap() {
		return IMSI_IPMap;
	}

	public static void setIMSI_IPMap(Map<String, String> iMSI_IPMap) {
		IMSI_IPMap = iMSI_IPMap;
	}
}
