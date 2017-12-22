/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.manger;

import com.hit.http.misc.Const;
import com.hit.http.server.Service;

/**
 * 消息中心主函数，
 * 1.初始化队列 ManageQuque @see ClientQueue @see BundleQueue
 * ClientQueue,相当于缓存外部的请求（Request)
 * 2.ManageBundles() @see {@link BundleManager}
 * 3.ManageClients() @see ClientManager(_bundleQueue, _clientQueue)
 * 4.new Service(_clientManager); {@link Service}
 * 	 相当于启动了一个nettyServer等待输入，接收到输入后，向clientManager的Client setContent并缓存在本地，之后将Clientpush到bundleQueue中。
 *   
 *   
 * ClientQueue:相当于缓存外部的请求（Request)，监听队列中的Response。
 * ClientManager:push给Bundle，pop,并且监听Bundle回传的Response。
 * BundleQueue:相当于Server的请求（Request)，监听队列中的Response。
 * BundleManager:动态loadBundle并为其建立非http请求的NettyServer（isHttp=false）
 * Service:启动了一个BaseServer（NettyServer）,以http方式连接（isHttp=true）
 * 相当于所有的请求发送至manager的Netty，之后由manger封装为RequestMeat发送至BundleQueue，发送给Bundle。交由BunudleManager相应的NettyServer处理。
 * 详解{@link BundleManager}
 * @author THINK
 *
 */
public class Manager {

	private Service _service = null;
	private ClientQueue _clientQueue = null;
	private BundleQueue _bundleQueue = null;
	private ClientManager _clientManager = null;
	private BundleManager _bundleManager = null;

	public int ManageQuque() {
		int ret = Const.FAIL;

		if (null == _clientQueue) {
			_clientQueue = new ClientQueue();
		}

		if (null == _bundleQueue) {
			_bundleQueue = new BundleQueue();
		}

		ret = Const.SUCCESS;

		return ret;
	}

	public int ManageClients() {
		if (null == _clientManager) {
			_clientManager = new ClientManager(_bundleQueue, _clientQueue);
		}

		return _clientManager.Start();
	}

	public int ManageBundles() {
		if (null == _bundleManager) {
			_bundleManager = new BundleManager(_bundleQueue, _clientQueue);
		}

		return _bundleManager.Start();
	}

	public int ManageService() {
		if (null == _service) {
			_service = new Service(_clientManager);
		}

		return _service.Start();
	}

	public int Debug() {
		int ret = Const.FAIL;

		ret = Const.SUCCESS;

		return ret;
	}

	public static void main(String[] args) {

		int ret = Const.FAIL;

		Manager man = new Manager();

		ret = man.ManageQuque();

		if (Const.SUCCESS == ret) {
			ret = man.ManageBundles();
		}

		if (Const.SUCCESS == ret) {
			ret = man.ManageClients();
		}

		if (Const.SUCCESS == ret) {
			ret = man.Debug();
		}

		if (Const.SUCCESS == ret) {
			ret = man.ManageService();
		}
	}
}
