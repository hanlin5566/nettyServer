/*
 * @date	: 2015-4-7
 */

package com.hit.http.interproc;

public class ThreadUtility {
	
	public static void Sleep(int ms)
	{
		try {
			Thread.sleep(ms);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
