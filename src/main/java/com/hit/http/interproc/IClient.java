/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.wiitrans.base.interproc;

public interface IClient {
	
	public int NewServer(Server server);
	
	public int NewMsg(Object msg);
}
