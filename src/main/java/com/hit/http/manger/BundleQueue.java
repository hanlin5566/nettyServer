/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.manger;

import java.util.concurrent.LinkedBlockingQueue;

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
	    Log4j.error(e);
	}

	return ret;
    }

    public RequestMeta Pop() {
	RequestMeta req = null;

	try {
	    req = _queue.take();

	} catch (InterruptedException e) {
	    Log4j.error(e);
	}

	return req;
    }
}
