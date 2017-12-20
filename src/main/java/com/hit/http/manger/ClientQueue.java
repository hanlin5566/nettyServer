/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.manger;

import java.util.concurrent.LinkedBlockingQueue;

import com.hit.http.misc.Const;
/**
 * 用于封装客户端队列，
 * 其中包含ResponseMeta
 * ResponseMeta:
 * String _clientId
 * byte[] _res
 * 
 * @method
 * 	Push(String clientId, byte[] msg) 向队列中放入 ResponseMeta
 *  Pop() 从队列中take ResponseMeta
 * @author THINK
 */
public class ClientQueue {
	
	private LinkedBlockingQueue<ResponseMeta> _queue = new LinkedBlockingQueue<ResponseMeta>();

	// Meta data.
	public class ResponseMeta {

		public String _clientId = null;
		public byte[] _res = null;

		public ResponseMeta(String clientId, byte[] res) {
			_clientId = clientId;
			_res = res;
		}
	}
	public int getSize(){
	   return _queue.size();
	}
	public int Push(String clientId, byte[] msg) {
		
		int ret = Const.FAIL;
		
		try {
			_queue.put(new ResponseMeta(clientId, msg));			
			ret = Const.SUCCESS;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	public ResponseMeta Pop() {
		
		ResponseMeta res = null;
		
		try {
			res = _queue.take();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
