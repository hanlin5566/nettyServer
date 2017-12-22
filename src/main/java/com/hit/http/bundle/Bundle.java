package com.hit.http.bundle;

import javax.rmi.CORBA.Util;

import com.alibaba.fastjson.JSONObject;
import com.hit.http.interproc.BaseServer;
import com.hit.http.interproc.Client;
import com.hit.http.interproc.IServer;
import com.hit.http.misc.Const;

import io.netty.buffer.ByteBuf;

/**
 * 每个storm的bundle，
 * @attribute
 * 	private IResponse _res = null;
    private Client _client = null;
    private String _id = null;
    private BundleRequest _spout = null;
    
    Start()
    	调用自身的线程run，
    run()
    	BaseServer svr = new BaseServer(); 按照配置端口，启动nettyserver
    	
    BundleRequest--》
 * @author THINK
 *
 */
public class Bundle extends Thread implements IBundle, IServer {
    private IResponse _res = null;
    private Client _client = null;
    private String _id = null;
    private BundleRequest _spout = null;

    @Override
    /**
     * TODO:写入client与BundleRequest中的client为同一个client，共用。作为XXX传递数据的管道
     */
    public int NewClient(Client client) {
	return Response(client);
    }

    @Override
    public int SetResponse(IResponse res) {
	int ret = Const.FAIL;

	_res = res;
	return ret;
    }

    private int Init() {
	int ret = Const.FAIL;
	_spout = new BundleRequest();
	ret = Const.SUCCESS;

	return ret;
    }

    @Override
    public int Start() {
	int ret = Const.FAIL;

	ret = Init();
	if (Const.SUCCESS == ret) {
	    this.start();
	}

	if (Const.SUCCESS == ret) {
	    ret = _spout.Start();
	}

	return ret;
    }

    @Override
    public int Stop() {
	int ret = Const.FAIL;

	return ret;
    }

    private int Invalid(String exam) {
	int ret = Const.FAIL;

	JSONObject obj = JSONObject.parseObject(exam);
	String id = obj.get(Const.BUNDLE_INFO_ID).toString();
	JSONObject resObj = new JSONObject();
	resObj.put("result", "FAILED");
	resObj.put(Const.BUNDLE_INFO_STATE, Const.BUNDLE_REPORT);
	resObj.put(Const.BUNDLE_INFO_ID, id);
	resObj.put(Const.BUNDLE_INFO_ACTION_ID,obj.get(Const.BUNDLE_INFO_ACTION_ID).toString());

	_res.Response(id, resObj.toString().getBytes());

	return ret;
    }

    @Override
    public int Request(String exam) {

	int ret = Const.FAIL;
	if (_client != null) {
	    ret = _spout.Push(exam);
	} else {
	    ret = Invalid(exam);
	}
	return ret;
    }

    @Override
    public String GetBundleId() {
	return BundleConf.EXAM_BUNDLE_ID;
    }

    @Override
    public int Request(JSONObject exam) {
	System.out.println("exam bundle " + exam.toString());
	return Request(exam.toString());
    }

    public int Response(Client client) {
	int ret = Const.FAIL;

	JSONObject obj = client.GetBundleInfoJSON();
	String state = obj.getString(Const.BUNDLE_INFO_STATE);
	String id = obj.getString(Const.BUNDLE_INFO_ID);

	switch (state) {
	case Const.BUNDLE_REGISTER: {
	    String bid = obj.getString(Const.BUNDLE_INFO_BUNDLE_ID);
	    // Registe bundle.
	    if (0 == (bid.compareTo(BundleConf.EXAM_BUNDLE_ID))) {
		if (_client != null) {
		    _client.GetContext().close();
		}

		_client = client;
		_id = bid;
		_spout.SetClient(client);

		System.out.println("Bundle[" + _id + "] is actived.");
	    } else {
		System.out.println("Registe bundle[" + _id + "] is mismatch.");
	    }

	    break;
	}
	case Const.BUNDLE_REPORT: {

	    String result = obj.getString("result");
	    if (result != null) {

		switch (result) {
		case "1111111111": {
		    break;
		}
		default: {
		    // 回复消息到PHP
		    if (_res != null) {
			_res.Response(id, obj.toString().getBytes());
		    } else {
			System.out.println("exam service bundle callback is null");
		    }
		    break;
		}
		}
	    } else {
		System.out.println("The report exam result is null.");
	    }
	    break;
	}
	default:
	    System.out.println("exam service bundle state[" + state + "] error");
	    break;
	}

	return ret;
    }

    public void run() {
		BaseServer svr = new BaseServer();
		svr.SetPort(BundleConf.EXAM_BUNDLE_PORT);
		svr.SetNewClientCallBack(this);
		svr.Run(false);
    }

    @Override
    public int SetContent(String clientId, ByteBuf content, boolean isSplit) {
	return Const.NOT_IMPLEMENTED;
    }
}
