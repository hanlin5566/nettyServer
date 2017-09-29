package com.hit.netty.handler;

import org.apache.log4j.Logger;

import com.hit.message.handler.MessageDictionary;
import com.hit.message.handler.MessageHandler;
import com.hit.netty.TCPServerNetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import util.StringUtil;

public class InBoundHandler extends SimpleChannelInboundHandler<byte[]> {
	private static Logger logger = Logger.getLogger(InBoundHandler.class);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		
		logger.info("CLIENT"+getRemoteAddress(ctx)+" 接入连接");
		//往channel map中添加channel信息
		TCPServerNetty.getMap().put(getIPString(ctx), ctx.channel());	
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//删除Channel Map中的失效Client
		TCPServerNetty.getMap().remove(getIPString(ctx));	
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg)
			throws Exception {
		String str = StringUtil.bytesToString(msg);
		logger.info("来自设备的信息："+str);
		String [] strArray = str.split("_");
		String ip = getIPString(ctx);
		String msgVersion = strArray[1];
		String msgType = strArray[2];
		String msgIMEI = strArray[3];//设备码
		String msgIMSI = strArray[4];//电话卡码
		String msgLog = String.format("来自 %s 设备上报信息，版本：%s 类型： %s IMEI： %s IMSI：%s", 
				ip,msgVersion,msgType,msgIMEI,msgIMSI);
		logger.info(msgLog);
		putIMEI(ip,msgIMEI);
		putIMSI(ip,msgIMSI);
		//根据msgType调用相应处理类，并返回报文
		String handlerClazz = String.format(MessageDictionary.msgTemplate, msgType.trim());
		MessageHandler msgHandler = (MessageHandler) Class.forName(handlerClazz).newInstance();
		byte[] retMsg = msgHandler.handler(msg);
		ctx.write(retMsg);
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		String socketString = ctx.channel().remoteAddress().toString();
		
		if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
            	logger.info("Client: "+socketString+" READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
            	logger.info("Client: "+socketString+" WRITER_IDLE 写超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
            	logger.info("Client: "+socketString+" ALL_IDLE 总超时");
                ctx.disconnect();
            }
        }
	}
	public static String getIPString(ChannelHandlerContext ctx){
		String ipString = "";
		String socketString = ctx.channel().remoteAddress().toString();
		int colonAt = socketString.indexOf(":");
		ipString = socketString.substring(1, colonAt);
		return ipString;
	}
	
	
	public static String getRemoteAddress(ChannelHandlerContext ctx){
		String socketString = "";
		socketString = ctx.channel().remoteAddress().toString();
		return socketString;
	}
	

//	private String getKeyFromArray(byte[] addressDomain) {
//		StringBuffer sBuffer = new StringBuffer();
//		for(int i=0;i<5;i++){
//			sBuffer.append(addressDomain[i]);
//		}
//		return sBuffer.toString();
//	}

	protected String to8BitString(String binaryString) {
		int len = binaryString.length();
		for (int i = 0; i < 8-len; i++) {
			binaryString = "0"+binaryString;
		}
		return binaryString;
	}
	
	protected void putIMSI(String ip,String imsi) {
		if(TCPServerNetty.getIMSI_IPMap().containsKey(imsi) && !TCPServerNetty.getIMSI_IPMap().get(imsi).equals(ip)){
			//查看IP是否变更
			String msgLog = String.format("设备IP变更，IMSI：%s 原IP： %s 现IP: %s", 
					imsi,TCPServerNetty.getIMSI_IPMap().get(imsi),ip);
			logger.info(msgLog);
		}
		TCPServerNetty.getIMSI_IPMap().put(imsi, ip);
	}
	protected void putIMEI(String ip,String imei) {
		if(TCPServerNetty.getIMEI_IPMap().containsKey(imei) && !TCPServerNetty.getIMEI_IPMap().get(imei).equals(ip)){
			//查看IP是否变更
			String msgLog = String.format("设备IP变更，IMEI：%s 原IP： %s 现IP: %s", 
					imei,TCPServerNetty.getIMSI_IPMap().get(imei),ip);
			logger.info(msgLog);
		}
		TCPServerNetty.getIMEI_IPMap().put(imei, ip);
	}

	protected static byte[] combine2Byte(byte[] bt1, byte[] bt2){
    	byte[] byteResult = new byte[bt1.length+bt2.length];
    	System.arraycopy(bt1, 0, byteResult, 0, bt1.length);
    	System.arraycopy(bt2, 0, byteResult, bt1.length, bt2.length);
    	return byteResult;
    }
}