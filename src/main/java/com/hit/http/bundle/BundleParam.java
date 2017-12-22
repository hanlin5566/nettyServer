package com.hit.http.bundle;

import java.util.ArrayList;

public class BundleParam {

	public String BUNDLE_NAME = "";
	public String BUNDLE_FILE_TYPE = ""; // class jar
	public boolean BUNDLE_IS_LOCALCLUSTER = false;
	public boolean BUNDLE_IS_DEBUG = false;
	public String BUNDLE_CLASS_FILEPATH = "";
	public String BUNDLE_CLASS_FILENAME = "";
	public String BUNDLE_JAR_FILENAME = "";
	public int BUNDLE_SPOUT_COUNT = 1;
	public ArrayList<BundleBolt> BUNDLE_BOLT_COUNT = new ArrayList<BundleBolt>();
	public String BUNDLE_TEMPFILE_PATH = "";
	public int BUNDLE_TRANSLATOR_ORDER_MAXCOUNT = 10;
	public boolean BUNDLE_ORDER_SYN = true;
	public int BUNDLE_ORDER_SYN_CYCLE = 600;
	public ArrayList<BundleMT> BUNDLE_MACHINE_TRANSLATION = new ArrayList<BundleMT>();
	public String BUNDLE_MSG_URL = "";
	public String BUNDLE_RECOM_URL = "";
	public String BUNDLE_OPERA_URL = "";
	public String BUNDLE_STATE_URL = "";
	public int BUNDLE_MSG_TIMEOUT = 0;
	public int BUNDLE_RECOM_TIMEOUT = 0;
	public int BUNDLE_OPERA_TIMEOUT = 0;
	public int BUNDLE_STATE_TIMEOUT = 0;
	public int BUNDLE_WORKER_NUM = 3;
	public int BUNDLE_ONLINE_SHOWCOUNT = 10;
	public int BUNDLE_MONITOR_CYCLE = 60;
	public int BUNDLE_MONITOR_TIMEOUT = 300;
	public boolean BUNDLE_TM_LEVENSHTEIN = false;
	public int BUNDLE_DICTTERM_TIMEOUT = 3600;
	// public String TMSVR_MYBATIS = "";
	public String TMSVR_COMMAND = "";
	public String TMSVR_API = "";
	public ArrayList<BundleMatchVar> BUNDLE_MATCH_VARIABLE = new ArrayList<BundleMatchVar>();
	public boolean BUNDLE_FRAG_CHECKWORD = false;
	public ArrayList<LearnApi> BUNDLE_FRAG_LEARN_API = new ArrayList<LearnApi>();
	// exam
	public int EXAM_COUNT = 10;
	public int EXAM_TIMEOUT = 15;
	public int EXAM_THRESHOLD = 5;
	public int EXAM_MAX = 7;
	public int EXAM_MONTH_TIEMS = 3;//每月最大考试次数
	// public ArrayList<ConfigNode> BUNDLE_SYNC_URL = new
	// ArrayList<ConfigNode>();
}
