package com.hit.http.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.wiitrans.base.bundle.BundleConf;
import com.wiitrans.base.bundle.ConfigNode;
import com.wiitrans.base.log.Log4j;
import com.wiitrans.base.misc.Const;
import com.wiitrans.base.xml.WiitransConfig;

public class PushServer {
    private Map<Integer, TaskReportor> pushServers = new HashMap<Integer, TaskReportor>();

    public PushServer() {
	try {
	    WiitransConfig.getInstance(0);
	    Set<Integer> set = BundleConf.BUNDLE_Node.keySet();
	    for (Integer nodeId : set) {
		ConfigNode node = BundleConf.BUNDLE_Node.get(nodeId);
		String ip = node.push.split(":")[0];
		String port = node.push.split(":")[1];
		TaskReportor _reportor = new TaskReportor(ip,
			Integer.parseInt(port), false);
		_reportor.Start();
		pushServers.put(nodeId, _reportor);
	    }
	} catch (Exception ex) {
	    Log4j.error(ex);
	}
    }

    public int Report(Integer nid, JSONObject obj) {
	int ret = Const.FAIL;
	try {
	    if (pushServers.containsKey(nid)) {
		return pushServers.get(nid).Report(obj);
	    }else{
		pushServers.get(BundleConf.DEFAULT_NID).Report(obj);
	    }
	} catch (Exception e) {
	    Log4j.error(e);
	}
	return ret;
    }

    public int Report(JSONObject obj) {
	int ret = Const.FAIL;
	try {
	    Integer nid_int = Integer.valueOf(BundleConf.DEFAULT_NID);
	    if (pushServers.containsKey(nid_int)) {
		return pushServers.get(nid_int).Report(obj);
	    }
	} catch (Exception e) {
	    Log4j.error(e);
	}
	return ret;
    }
}
