package com.hit.http.interproc;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSONObject;
import com.hit.http.misc.Const;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * client封装了netty的ChannelHandlerContext，包含解析等操作
 * @attribute
 *  ChannelHandlerContext ctx --channelHandler与channelPipeline通信时的context。
 *  JSONObject bundleInfoJSON -- 传递的json信息
 *  HttpMethod method--请求协议的方法
 *  boolean isCompleted--是否完成
 *  String id--
 *  String url--
 *  ByteBuf contentBuf
 * @method
 * 	IsCompleted() 是否完成，返回boolean标识
 * 
 *  int Response(byte[] content) 
 *  向此ctx.writeAndFlush(response);中写入应答并刷新。FullHttpResponse response = new DefaultFullHttpResponse(HTTP11, OK, buf);
 *  
 *  int Request(byte[] content)
 * 	读取ctx中写入并刷新 
 * 
 * int SetContent(ByteBuf content, boolean isSplit)
 * 读取content 写入bundleInfoJSON
 * 
 * Parse(Object obj)
 * 将httprequest或者传入的byte转为bundleInfoJSON
 * 
 * GetBundleInfo(String key)
 * 获取bundleInfoJSON的值
 * 
 * GetBundleId()
 * 获取bid
 * 
 * ..其他对属性的get，set方法，比如 getsessionId，getCxt，setCxt等等。
 * 
 * @author THINK
 *
 */
public class Client {
	/**
	 * channelRead时，当前的ctx
	 */
	private ChannelHandlerContext ctx = null;
	private JSONObject bundleInfoJSON = null;
	private HttpMethod method = null;
	private boolean isCompleted = false;
	private String id = null;
	private String url = null;
	private ByteBuf contentBuf = null;
	
	public boolean IsCompleted()
	{
		return isCompleted;
	}
	
	public int Response(byte[] content)
	{
		int ret = Const.FAIL;
		
		//Log4j.log("-- SEND : " + content.length + " Bytes");
		
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeBytes(content);
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response);
        
        ret = Const.SUCCESS;
		
		return ret;
	}
	
	public int Request(byte[] content)
	{
		int ret = Const.FAIL;
		
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeBytes(content);
        
        ctx.writeAndFlush(buf);
        
        ret = Const.SUCCESS;
		
		return ret;
	}
	
	public int SetContent(ByteBuf content, boolean isSplit)
	{
		int ret = Const.FAIL;
		
		if((content != null) && (content.isReadable()))
		{
			if(null == contentBuf)
			{
				contentBuf = ctx.alloc().buffer();
			}
			if(isSplit)
			{
				contentBuf.writeBytes(content);
				//Log4j.log("++ RECV SPLIT PARA : " + contentBuf);
				isCompleted = false;
			}else {
				
				contentBuf.writeBytes(content);
				String cntBuf = contentBuf.toString(io.netty.util.CharsetUtil.UTF_8);
				if(bundleInfoJSON == null)
				{
					bundleInfoJSON = new JSONObject();
				}
				
				JSONObject appObj = JSONObject.parseObject(cntBuf);
				for (String key : appObj.keySet()) {
					bundleInfoJSON.put(key, appObj.get(key));
				}
				contentBuf = null;
				isCompleted = true;
			}			
		}else
		{
			isCompleted = true;
		}
		
		ret = Const.SUCCESS;
		
		return ret;
	}
	
	private int Parse(Object obj)
	{
		int ret = Const.FAIL;
		
		if(obj instanceof HttpRequest)
		{
			// uri: /service/recom/login
			
			HttpRequest req = (HttpRequest)obj;
			String url = req.getUri();
			url = url;
			//Log4j.log("++ RECV CONN [" + id + "] : " + url);
			String parts[] = url.split(Const.URL_SEP);
			if(parts.length > 3)
			{
				String verb = parts[1];
				if(0 == verb.compareTo(Const.URI_TYPE_SERVICE))
				{
					if(bundleInfoJSON == null)
					{
						bundleInfoJSON = new JSONObject();
					}
					
					bundleInfoJSON.put(Const.BUNDLE_INFO_BUNDLE_ID, parts[2]);
					bundleInfoJSON.put(Const.BUNDLE_INFO_ACTION_ID, parts[3]);
					
					for(int index = 4; index < parts.length; index = index + 2)
					{
						bundleInfoJSON.put(parts[index], parts[index + 1]);
					}
					
					if(id != null)
					{
						bundleInfoJSON.put(Const.BUNDLE_INFO_ID, id);
					}
					
					method = req.getMethod();
					
					if((HttpMethod.POST.equals(method))
							|| (HttpMethod.PUT.equals(method))
							|| (HttpMethod.DELETE.equals(method))
							 || (HttpMethod.GET.equals(method)))
					{
						isCompleted = false;
					}else {
						isCompleted = true;
					}
					
					bundleInfoJSON.put("method", method.toString());
					
					ret = Const.SUCCESS;
				}else {
					// Not service, nothing to do.
				}
			}else {
				// Not service, nothing to do.
			}
			
		}else if(obj instanceof ByteBuf){
			
			ByteBuf bb = (ByteBuf)obj;
			String msg = bb.toString(Charset.forName(Const.DEFAULT_CHARSET));
			bundleInfoJSON = JSONObject.parseObject(msg);
			bb.release();
			isCompleted = true;
		}
		
		return ret;
	}
	
	private String GetBundleInfo(String key)
	{
		String val = null;
		if(bundleInfoJSON.containsKey(key))
		{
			val = bundleInfoJSON.getString(key);
		}
		return val;
	}
	
	public String GetBundleId()
	{
		return GetBundleInfo(Const.BUNDLE_INFO_BUNDLE_ID);
	}
	
	public String getBundleInfoString()
	{
		return bundleInfoJSON.toString();
	}
	
	public JSONObject GetBundleInfoJSON()
	{
		return bundleInfoJSON;
	}
	
	public String GetSessionId()
	{
		return GetBundleInfo(Const.BUNDLE_INFO_SESSION_ID);
	}
	
	public String GetId()
	{
		return id;
	}
	
	public ChannelHandlerContext GetContext()
	{
		return ctx;
	}
	
	public void SetContext(ChannelHandlerContext ctx)
	{
		ctx = ctx;
		id = ctx.toString();
	}
	
	public int SetObject(Object obj)
	{
		return Parse(obj);
	}
	
	@Override
	public void finalize()
	{
		if(ctx != null)
		{
			ctx.close();
			ctx = null;
		}
	}
}
