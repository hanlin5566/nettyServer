package com.hit.http.interproc;

import io.netty.buffer.ByteBuf;
/**
 * 对nettyServer的封装，类似于接收客户端请求的Server封装
 * @author THINK
 *
 */
public interface IServer {
	/**
	 * 当netty channelRead时，将ChannelHandlerContext,与传入的object封装为client；
	 * @param client
	 * @return
	 */
	public int NewClient(Client client);
	/**
	 * 当netty channelRead时，读取的内容
	 * @param clientId
	 * @param content
	 * @param isSplit
	 * @return
	 */
	public int SetContent(String clientId, ByteBuf content, boolean isSplit);
}
