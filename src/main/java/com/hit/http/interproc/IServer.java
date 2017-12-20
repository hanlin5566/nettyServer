/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.wiitrans.base.interproc;

import io.netty.buffer.ByteBuf;

public interface IServer {
	
	public int NewClient(Client client);
	
	public int SetContent(String clientId, ByteBuf content, boolean isSplit);
}
