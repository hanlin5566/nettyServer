/*
 * @author	: ECI
 * @date	: 2015-4-7
 */

package com.hit.http.interproc;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class DebugHandler extends LoggingHandler {
	public DebugHandler() {
		super(LogLevel.INFO);
	}

}
