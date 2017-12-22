/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.task;

import java.util.concurrent.LinkedBlockingQueue;

import com.wiitrans.base.log.Log4j;
import com.wiitrans.base.misc.Const;

public class TaskQueue {
	
	private LinkedBlockingQueue<Object> _queue = new LinkedBlockingQueue<Object>();
	
	public int Push(Object task)
	{
		int ret = Const.FAIL;
		
		try {
			_queue.put(task);			
			ret = Const.SUCCESS;
			
		} catch (Exception e) {
			Log4j.error(e);
		}
		
		return ret;
	}

	public Object Pop()
	{		
		Object task = null;
		
		try {
			task = _queue.poll();
			
		} catch (Exception e) {
			Log4j.error(e);
		}
		
		return task;
	}
	
	public Object Take()
	{		
		Object task = null;
		
		try {
			task = _queue.take();
			
		} catch (Exception e) {
			Log4j.error(e);
		}
		
		return task;
	}
}
