package com.hit.message.handler;

import org.apache.log4j.Logger;

import com.hit.netty.TCPServerNetty;

import util.StringUtil;

/**
 * 心跳报文
 * 终端上传：@B#@,V01,44,111112222233333,8888888888888888,@E#@
 * 服务端回复：21号报文：@B#@,V01,21,@E#@
 * @author huhl
 * @fullPath com.hit.netty.handler.XTHandler.java
 * @createTime 2017年9月12日 上午11:43:08
 *
 */
public class MSG44Handler implements MessageHandler{
	private static Logger logger = Logger.getLogger(MSG44Handler.class);
	public byte[] handler(byte[] msg) {
		String recMsg = StringUtil.bytesToString(msg);
		String [] strArray = recMsg.split(",");
		String msgLog = String.format("收到IP:%s,IMEI:%s,IMSI:%s,的心跳包",TCPServerNetty.getIMEI_IPMap().get(strArray[3]),strArray[3],strArray[4]);
		logger.info(msgLog);
		return MsgContentTemplate.msg_21.getBytes();
	}
}
