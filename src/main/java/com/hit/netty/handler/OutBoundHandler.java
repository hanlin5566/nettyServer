package com.hit.netty.handler;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import util.StringUtil;

public class OutBoundHandler extends ChannelOutboundHandlerAdapter {
	private static Logger logger = Logger.getLogger(OutBoundHandler.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

		if (msg instanceof byte[]) {
			byte[] bytesWrite = (byte[]) msg;
			ByteBuf buf = ctx.alloc().buffer(bytesWrite.length);
			logger.info("向设备下发的信息为：" + StringUtil.bytesToString(bytesWrite));

			buf.writeBytes(bytesWrite);
			ctx.writeAndFlush(buf).addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future) throws Exception {
					logger.info("下发成功！");
				}
			});
		}
	}
}
