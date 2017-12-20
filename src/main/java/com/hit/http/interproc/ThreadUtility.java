/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.wiitrans.base.interproc;

import com.wiitrans.base.log.Log4j;

public class ThreadUtility {
	
	public static void Sleep(int ms)
	{
		try {
			Thread.sleep(ms);
			
		} catch (InterruptedException e) {
			Log4j.error(e);
		}
	}
}
