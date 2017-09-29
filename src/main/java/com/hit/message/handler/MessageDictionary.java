package com.hit.message.handler;

import java.util.HashMap;
import java.util.Map;
/**
 * 报文字典
 * @author huhl
 * @fullPath com.hit.netty.handler.MessageDictionary.java
 * @createTime 2017年9月12日 上午11:47:36
 *
 */
public class MessageDictionary {
	public static final String msgTemplate = "com.hit.message.handler.MSG%sHandler";
	@Deprecated
	public static Map<Integer,String> dic = new HashMap<Integer,String>();
	static {
		dic.put(1, "com.hit.message.handler.MSG1Handler");
		dic.put(44, "com.hit.message.handler.MSG44Handler");
	}
}
