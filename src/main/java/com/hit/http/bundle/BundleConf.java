package com.hit.http.bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BundleConf {

	public static String BUNDLE_INFO_FILE_COUNT = "filecount";
	public static String BUNDLE_INFO_FILE_ID = "fileid";
	public static String BUNDLE_INFO_WORD_COUNT = "wordcount";
	public static String BUNDLE_INFO_FILE_LANGUAGE = "filelanguage";

	public static int ANALYSIS_BUNDLE_PORT = 10001;
	public static int FRAGMENTATION_BUNDLE_PORT = 10002;
	public static int RECOMMEND_BUNDLE_PORT = 10003;
	public static int TERM_BUNDLE_PORT = 10004;
	public static int MSG_BUNDLE_PORT = 10005;
	public static int STATE_BUNDLE_PORT = 10006;
	public static int OPERATION_BUNDLE_PORT = 10007;
	public static int AUTOMATION_BUNDLE_PORT = 10008;
	public static int TM_BUNDLE_PORT = 10009;
	public static int TMSVR_BUNDLE_PORT = 10010;
	public static int EXAM_BUNDLE_PORT = 10011;
	public static int MATCH_BUNDLE_PORT = 10012;
	public static int ORDERCENTER_BUNDLE_PORT = 10013;
	public static int API_BUNDLE_PORT = 10014;

	public static String ANALYSIS_BUNDLE_ID = "analysis";
	public static String FRAGMENTATION_BUNDLE_ID = "frag";
	public static String MSG_BUNDLE_ID = "msg";
	public static String RECOMMEND_BUNDLE_ID = "recom";
	public static String STATE_BUNDLE_ID = "state";
	public static String TERM_BUNDLE_ID = "term";
	public static String OPERATION_BUNDLE_ID = "opera";
	public static String AUTOMATION_BUNDLE_ID = "automation";
	public static String TM_BUNDLE_ID = "tm";
	public static String TMSVR_BUNDLE_ID = "tmsvr";
	public static String EXAM_BUNDLE_ID = "exam";
	public static String MATCH_BUNDLE_ID = "match";
	public static String ORDERCENTER_BUNDLE_ID = "ordercenter";
	public static String API_BUNDLE_ID = "api";

	// 以下通过配置文件获取
	public static boolean DEBUG = false;
	public static int DEFAULT_NID = 0;
	public static int RECOM_NID = 0;
	public static Map<Integer, ConfigNode> BUNDLE_Node = new ConcurrentHashMap<Integer, ConfigNode>();
	public static String FILTER_WORD_CONF_URL = "";
	public static String FILTER_REGX_CONF_URL = "";
	public static String FILTER_MAIL_CONF_URL = "";
	// public static Map<Integer, String> DB_CONF_URL = new HashMap<Integer,
	// String>();
	public static String HBASE_CONF_URL = "";
	public static String BUNDLE_REPORT_IP = "";
	public static String BUNDLE_REDIS_IP = "";
	public static int BUNDLE_REDIS_PORT = 6379;
	public static int BUNDLE_REDIS_TIMEOUT = 4000;
	public static String BUNDLE_PUSHSERVER_IP = "";
	public static String BUNDLE_CHATSERVER_IP = "";
	public static int BUNDLE_CHATSERVER_PORT = 0;
	public static int BUNDLE_PUSHSERVER_PORT = 0;
	public static String LOG4J_CONFIGURE_URL = "";
	public static String LOG4J_PRIORITY = "";
	public static HashMap<String, Map<String, String>> REQ_TEMPLATE = new HashMap<String, Map<String, String>>();
	public static HashMap<String, String> FILTER_REGXS_MAP = new HashMap<String, String>();
	public static boolean BUNDLE_MATCH_INDUSTRY = false;
	public static boolean BUNDLE_WRITE_TRANSLATED = false;
	public static String BUNDLE_TMSVR_API = "";
	public static String BUNDLE_TMSVR_MYBATIS = "";
	public static int BUNDLE_OTHERS_INDUSTRYID = -1;
}
