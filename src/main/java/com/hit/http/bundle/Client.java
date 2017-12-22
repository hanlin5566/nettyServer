/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.bundle;

import java.util.Iterator;

import org.json.JSONObject;

import com.wiitrans.base.bundle.BundleConf;
import com.wiitrans.base.log.Log4j;
import com.wiitrans.base.misc.Const;
import com.wiitrans.base.misc.Util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

public class Client {
	
	private ChannelHandlerContext _ctx = null;
	private JSONObject _bundleInfoJSON = null;
	private HttpMethod _method = null;
	private boolean _isCompleted = false;
	private String _id = null;
	private String _url = null;
	private ByteBuf _contentBuf = null;
	
	public boolean IsCompleted()
	{
		return _isCompleted;
	}
	
	public int Response(byte[] content)
	{
		int ret = Const.FAIL;
		
		//Log4j.log("-- SEND : " + content.length + " Bytes");
		
		ByteBuf buf = _ctx.alloc().buffer();
		buf.writeBytes(content);
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, buf);
        response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        //response.headers().set(CONNECTION, Values.KEEP_ALIVE);
        
        _ctx.writeAndFlush(response);
        
        Log4j.log("-- SEND : " + _id);
        
        if(BundleConf.DEBUG)
        {
        	Log4j.log(new String(content));
        }
        
        ret = Const.SUCCESS;
		
		return ret;
	}
	
	public int Request(byte[] content)
	{
		int ret = Const.FAIL;
		
		ByteBuf buf = _ctx.alloc().buffer();
		buf.writeBytes(content);
        
        _ctx.writeAndFlush(buf);
        
        ret = Const.SUCCESS;
		
		return ret;
	}
	
	public int SetContent(ByteBuf content, boolean isSplit)
	{
		int ret = Const.FAIL;
		
		if((content != null) && (content.isReadable()))
		{
			if(null == _contentBuf)
			{
				_contentBuf = _ctx.alloc().buffer();
			}
			if(isSplit)
			{
				_contentBuf.writeBytes(content);
				//Log4j.log("++ RECV SPLIT PARA : " + _contentBuf);
				_isCompleted = false;
			}else {
				
				_contentBuf.writeBytes(content);
				String cntBuf = _contentBuf.toString(io.netty.util.CharsetUtil.UTF_8);
				Log4j.log("++ RECV COMPLETE PARA : [" + _id + "] " + "[" + _url + "] " + cntBuf);
				if(_bundleInfoJSON == null)
				{
					_bundleInfoJSON = new JSONObject();
				}
				
				JSONObject appObj = new JSONObject(cntBuf);
				Iterator it = appObj.keys();
				while(it.hasNext())
				{
					String key = (String)it.next();
					_bundleInfoJSON.put(key, appObj.get(key));
				}
				_contentBuf = null;
				_isCompleted = true;
			}			
		}else
		{
			_isCompleted = true;
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
			_url = url;
			//Log4j.log("++ RECV CONN [" + _id + "] : " + url);
			String parts[] = url.split(Const.URL_SEP);
			if(parts.length > 3)
			{
				String verb = parts[1];
				if(0 == verb.compareTo(Const.URI_TYPE_SERVICE))
				{
					if(_bundleInfoJSON == null)
					{
						_bundleInfoJSON = new JSONObject();
					}
					
					_bundleInfoJSON.put(Const.BUNDLE_INFO_BUNDLE_ID, parts[2]);
					_bundleInfoJSON.put(Const.BUNDLE_INFO_ACTION_ID, parts[3]);
					
					for(int index = 4; index < parts.length; index = index + 2)
					{
						_bundleInfoJSON.put(parts[index], parts[index + 1]);
					}
					
					if(_id != null)
					{
						_bundleInfoJSON.put(Const.BUNDLE_INFO_ID, _id);
					}
					
					_method = req.getMethod();
					
					if((HttpMethod.POST.equals(_method))
							|| (HttpMethod.PUT.equals(_method))
							|| (HttpMethod.DELETE.equals(_method))
							 || (HttpMethod.GET.equals(_method)))
					{
						_isCompleted = false;
					}else {
						_isCompleted = true;
					}
					
					_bundleInfoJSON.put("method", _method.toString());
					
					ret = Const.SUCCESS;
				}else {
					// Not service, nothing to do.
				}
			}else {
				// Not service, nothing to do.
			}
			
		}else if(obj instanceof ByteBuf){
			
			ByteBuf bb = (ByteBuf)obj;
			_bundleInfoJSON = Util.ByteBufToJSon(bb);
			bb.release();
			
			_isCompleted = true;
		}
		
		return ret;
	}
	
	private String GetBundleInfo(String key)
	{
		String val = null;
		try {
			
			if(_bundleInfoJSON.has(key))
			{
				val = _bundleInfoJSON.getString(key);
			}else {
				
				Log4j.error("Key " + key + " is not exist in bundleInfo.");
			}
			
		} catch (Exception e) {
			
			Log4j.error(e);
		}
		
		return val;
	}
	
	public String GetBundleId()
	{
		return GetBundleInfo(Const.BUNDLE_INFO_BUNDLE_ID);
	}
	
	public String getBundleInfoString()
	{
		return _bundleInfoJSON.toString();
	}
	
	public JSONObject GetBundleInfoJSON()
	{
		return _bundleInfoJSON;
	}
	
	public String GetSessionId()
	{
		return GetBundleInfo(Const.BUNDLE_INFO_SESSION_ID);
	}
	
	public String GetId()
	{
		return _id;
	}
	
	public ChannelHandlerContext GetContext()
	{
		return _ctx;
	}
	
	public void SetContext(ChannelHandlerContext ctx)
	{
		_ctx = ctx;
		_id = ctx.toString();
	}
	
	public int SetObject(Object obj)
	{
		return Parse(obj);
	}
	
	@Override
	public void finalize()
	{
		if(_ctx != null)
		{
			//Log4j.log(">>>> finalize : " + _id);
			
			_ctx.close();
			_ctx = null;
		}
	}
}
