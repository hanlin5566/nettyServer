/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.bundle;

import com.alibaba.fastjson.JSONObject;

/**
 * 类似于处理storm请求的Server接口
 * 
 * 
 * @author THINK
 *
 */
public interface IBundle {
	/**
	 * BundleManager
	 * @param res
	 * @return
	 */
	int SetResponse(IResponse res);
	
	/**
	 * Start
	 * 1.初始化bundleRquest，监听队列，从队列中popRequest（Server发送的请求）
	 * 2.线程启动，调用自身的run方法，启动了一个BaseServer（NettyServer）,不已http方式连接（isHttp=false）
	 * @return
	 */
	int Start();

	int Stop();

	// This is multi-thread invoke.
	/**
	 * 线程监控bundleQueue.Pop
	 * @param msg
	 * @return
	 */
	int Request(JSONObject msg);

	int Request(String msg);

	String GetBundleId();
}