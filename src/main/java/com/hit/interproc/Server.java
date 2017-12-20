/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.wiitrans.base.interproc;

import java.nio.charset.Charset;

import org.json.JSONObject;

import com.wiitrans.base.misc.Const;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class Server {
	
	private String _bundleId = null;
	private String _bundleInfo = null;
	private String _sessionId = null;
	private ChannelHandlerContext _ctx = null;	
	private JSONObject _bundleInfoObj = null;
	
	public Server(ChannelHandlerContext ctx, Object msg)
	{
		_ctx = ctx;
	}
	
	public JSONObject MsgToJSon(Object obj)
	{
		JSONObject jObj = null;
		if(obj != null)
		{
			ByteBuf bb = (ByteBuf)obj;
			String msg = bb.toString(Charset.forName(Const.DEFAULT_CHARSET));
			jObj = new JSONObject(msg);
			bb.release();
		}
		
		return jObj;
	}
	
	public int Response(String content)
	{
		int ret = Const.FAIL;
		
		ByteBuf _buf = _ctx.alloc().buffer();
    	_buf.writeBytes(content.getBytes());
    	_ctx.writeAndFlush(_buf);
        
        ret = Const.SUCCESS;
        
        return ret;
	}
	
	public int Response(byte[] content)
	{
		int ret = Const.FAIL;
        
		ByteBuf _buf = _ctx.alloc().buffer();
    	_buf.writeBytes(content);
    	_ctx.writeAndFlush(_buf);
        
        ret = Const.SUCCESS;
		
		return ret;
	}
	
	public String GetBundleId()
	{
		return _bundleId;
	}
	
	public String GetBundleInfo()
	{
		return _bundleInfo;
	}
	
	public JSONObject GetBundleInfoObj()
	{
		return _bundleInfoObj;
	}
	
	public String GetSessionId()
	{
		return _sessionId;
	}
	
	public ChannelHandlerContext GetContext()
	{
		return _ctx;
	}
	
	public void SetContext(ChannelHandlerContext ctx)
	{
		_ctx = ctx;
	}
}
