/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.bundle;
/**
 * BundleManager统一发送Response
 * Response 向response中发送报文。
 * _clientQueue.Push(clientId, msg);
 * 
 * @author THINK
 *
 */
public interface IResponse {
	/**
	 * clientQueue.Push
	 * @param sessionId
	 * @param msg
	 * @return
	 */
	public int Response(String sessionId, byte[] msg);

}
