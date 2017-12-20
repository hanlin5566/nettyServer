package com.hit.http.misc;

public class Const {

	public static int CHAR_NORMAL = 0;

	public static int CHAR_SEPARATE = 1;

	public static int CHAR_EMPTY = 2;

	public static int CHAR_SPACE = 3;

	public static int SUCCESS = 0;

	public static int FAIL = -1;

	public static int NOT_IMPLEMENTED = -2;

	public static String URI_TYPE_SERVICE = "service";

	public static String DEFAULT_CHARSET = "UTF-8";

	public static final String BUNDLE_REGISTER = "bundle_register";

	public static final String BUNDLE_UNREGISTER = "bundle_unregister";

	public static final String BUNDLE_REPORT = "bundle_report";

	public static int MAX_PACKET_LEN = Integer.MAX_VALUE;

	public static String BUNDLE_INFO_ID = "id";

	// Bundle Id.
	public static String BUNDLE_INFO_BUNDLE_ID = "bid";

	public static String BUNDLE_INFO_USER_ID = "uid";

	public static String BUNDLE_INFO_ACTION_ID = "aid";

	public static String BUNDLE_INFO_STATE = "state";

	// Session Id.
	public static String BUNDLE_INFO_SESSION_ID = "sid";

	public static String ID_SEP = "-";

	public static String URL_SEP = "/";

	public static int ID_PART_COUNT = 3;

	public static int ID_PART_FILE_ID = 0;

	public static int ID_PART_FRAG_ID = 1;

	public static int ID_PART_SENT_ID = 2;

	public static int FRAG_SIZE = 10;

	public static boolean DEBUG = false;

	public static boolean TEST = true;

	public static int TASK_DELETE_N = 0;

	public static int TASK_DELETE_Y = 1;

	public static int TASK_STATUS_FINISHED = 1;

	public static int TASK_STATUS_ACTIVE = 0;

	public final static int EXAM_QUES_TYPE_KNOWNJUDGE = 1;
	public final static int EXAM_QUES_TYPE_UNKNOWNJUDGE = 2;
	public final static int EXAM_QUES_TYPE_SUBJECTIVE = 3;
	public final static int EXAM_JUDGE_RIGHT = 1;
	public final static int EXAM_JUDGE_WRONG = 2;

	public final static int WIITRANS_CONFIG_LOG_STORM = 0;
	public final static int WIITRANS_CONFIG_LOG_MANAGER = 1;
	public final static int WIITRANS_CONFIG_LOG_TMSVR = 2;

	public final static String PREFIX_LANGPAIR = "lp";
	public final static String PREFIX_LANGPAIR_INDUSTRY = "lpi";
	public final static String PREFIX_LANGPAIR_GRADE = "lpg";
	public final static String PREFIX_LANGPAIR_GRADE_INDUSTRY = "lpgi";

	public final static String PREFIX_TRANS_MYORDER_LIST = "mol";
	public final static String PREFIX_TRANS_ORDER_LIST_T = "ot";
	public final static String PREFIX_TRANS_ORDER_LIST_E = "oe";
	public final static String PREFIX_ORDRE_TRANS_LIST = "ol";
	public final static String PREFIX_ORDRE_LOCK = "lock_";
	//price level 实用级
	public final static int PRICE_LEVEL_PRACTICAL = 1;
	//price level 标准级
	public final static int PRICE_LEVEL_STANDARD = 2;
	//price level 出版级
	public final static int PRICE_LEVEL_PUBLISH = 3;
	
	//RESERVE_TYPE T可抢订单
	public final static int RESERVE_TYPE_T = 1;
	//RESERVE_TYPE T可抢订单
	public final static int RESERVE_TYPE_E = 2;
	//RESERVE_TYPE T可抢订单
	public final static int RESERVE_TYPE_TE = 3;
	
	//正常推送
	public final static int ORDER_MATCH_TYPE_NORMAL = 0;
	//指定推荐人推送T
	public final static int ORDER_MATCH_TYPE_RECOM_T = 4;
	//指定推荐人推送E
	public final static int ORDER_MATCH_TYPE_RECOM_E = 5;
	//指定推荐人推送T+E
	public final static int ORDER_MATCH_TYPE_RECOM_TE = 6;
	//专属团队
	public final static int ORDER_MATCH_TYPE_TEAM = 7;

	//订单状态 进行中
	public final static int ORDER_STATUS_UNDERWAY = 20;
	// public static String ANALYSIS_TOPOLOGY_FILE_WORDCOUNT = "filewordcount";
}
