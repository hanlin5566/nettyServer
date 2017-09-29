package util;

import org.apache.log4j.Logger;

public class StringUtil {
	private static final Logger logger = Logger.getLogger(StringUtil.class);
	
	public static String bytesToHexString(byte[] src){       
        StringBuilder stringBuilder = new StringBuilder();       
        if (src == null || src.length <= 0) {       
            return null;       
        }       
        for (int i = 0; i < src.length; i++) {       
            int v = src[i] & 0xFF;       
            String hv = Integer.toHexString(v);       
            if (hv.length() < 2) {       
                stringBuilder.append(0);       
            }       
            stringBuilder.append(hv); 
            stringBuilder.append(' ');
        }       
        return stringBuilder.toString();       
    }
    
    public static String bytesToString(byte[] msg){       
    	String str = "";
        if (msg == null || msg.length <= 0) {       
            return null;       
        }       
        try {
        	str = new String(msg,"utf-8");
		} catch (Exception e) {
			logger.info("bytesToString",e);
		}
        return str;
    }
}
