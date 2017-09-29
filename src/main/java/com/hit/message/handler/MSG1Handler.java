package com.hit.message.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.hit.netty.TCPServerNetty;

import util.StringUtil;

/**
 * 时间同步报文
 * 终端上传：@B#@,V01, 1,111112222233333,8888888888888888,@E#@
 * 服务端回复：38号报文： @B#@,V01, 38,20150312010203,@E#@
 * @author huhl
 * @fullPath com.hit.message.handler.MSG01Handler.java
 * @createTime 2017年9月13日 上午10:02:03
 *
 */
public class MSG1Handler implements MessageHandler{
	private static Logger logger = Logger.getLogger(MSG1Handler.class);
	public byte[] handler(byte[] msg) {
		String recMsg = StringUtil.bytesToString(msg);
		String [] strArray = recMsg.split(",");
		String msgLog = String.format("收到IP:%s,IMEI:%s,IMSI:%s,的心跳包",TCPServerNetty.getIMEI_IPMap().get(strArray[3]),strArray[3],strArray[4]);
		logger.info(msgLog);
		//年月日时分秒  20150313180820
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String currentTime = sdf.format(new Date(System.currentTimeMillis()));
		String ret = String.format(MsgContentTemplate.msg_38, currentTime);
		return ret.getBytes();
	}
}
