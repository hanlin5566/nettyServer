
package com.hit.http.manger;

import java.util.concurrent.LinkedBlockingQueue;

import com.hit.http.interproc.Client;
import com.hit.http.misc.Const;

/**
 * 用于封装request队列
 * 其中包含RequestMeta(String id, Client client)
 * @method
 * 	Push(String id, @see Client client) 向队列中放入 ResponseMeta
 *  Pop() 从队列中take ResponseMeta
 */
public class BundleQueue {
	

    private LinkedBlockingQueue<RequestMeta> _queue = new LinkedBlockingQueue<RequestMeta>();

    public class RequestMeta {

	public String _id = null;
	public Client _client = null;

	public RequestMeta(String id, Client client) {
	    _id = id;
	    _client = client;
	}
    }

    public int getSize() {
	return _queue.size();
    }

    public int Push(String id, Client client) {
	int ret = Const.FAIL;

	try {
	    _queue.put(new RequestMeta(id, client));
	    ret = Const.SUCCESS;

	} catch (InterruptedException e) {
		e.printStackTrace();
	}

	return ret;
    }

    public RequestMeta Pop() {
	RequestMeta req = null;

	try {
	    req = _queue.take();

	} catch (InterruptedException e) {
		e.printStackTrace();
	}

	return req;
    }
}
