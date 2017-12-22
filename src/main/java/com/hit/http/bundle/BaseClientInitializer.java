/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.bundle;

import com.wiitrans.base.misc.Const;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class BaseClientInitializer extends ChannelInitializer<SocketChannel> {

	private ChannelHandler _handler = null;
	boolean _isSupportFrameLength = true;

	public BaseClientInitializer(ChannelHandler handler,
			boolean isSupportFrameLength) {
		_handler = handler;
		_isSupportFrameLength = isSupportFrameLength;
	}

	@Override
	public void initChannel(SocketChannel channel) {

		ChannelPipeline pipeline = channel.pipeline();

		if (Const.DEBUG) {
			pipeline.addLast(new DebugHandler());
		}
		if (_isSupportFrameLength) {
			pipeline.addLast(new LengthFieldBasedFrameDecoder(
					Const.MAX_PACKET_LEN, 0, 4, 0, 4));
			pipeline.addLast(new LengthFieldPrepender(4));
		}
		pipeline.addLast(_handler);
	}
}
