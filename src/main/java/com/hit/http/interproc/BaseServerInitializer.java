/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.wiitrans.base.interproc;

import com.wiitrans.base.misc.Const;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpServerCodec;


public class BaseServerInitializer extends ChannelInitializer<SocketChannel> {
	
	private ChannelHandler _handler = null;
	private Boolean _isHttp = false;
	
	public BaseServerInitializer(ChannelHandler handler, Boolean isHttp)
	{
		_handler = handler;
		_isHttp = isHttp;
	}
	
	@Override
    public void initChannel(SocketChannel channel) {
		
    	ChannelPipeline pipeline = channel.pipeline();
    	if(_isHttp)
    	{
    		pipeline.addLast(new HttpServerCodec());
    	}else {
    		pipeline.addLast(new LengthFieldBasedFrameDecoder(Const.MAX_PACKET_LEN, 0, 4, 0, 4));
            pipeline.addLast(new LengthFieldPrepender(4));
		}
    	pipeline.addLast(_handler);
    }
}
