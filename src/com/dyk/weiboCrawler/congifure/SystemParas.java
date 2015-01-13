package com.dyk.weiboCrawler.congifure;

/**
 * 系统变量定义
 */
public class SystemParas {
	// 登陆参数
	public static int cnCookieIndex = 20;
	public static int comCookieIndex = 2;
	public static int userAgentIndex = 5;
	public static int cnCookieRange = 28;
	public static int comCookieRange = 6;
	public static int userAgentRange = 21;
	public static int timeInternal = 2;

	// 文件路径参数
	public static String userType = "unknown";// unknown,auto,advertise,common,zombie,
	public static String idsPath = "resources/ids/test.txt";
	public static String cnCookiePath = "resources/accounts/cnCookies.txt";
	public static String comCookiePath = "resources/accounts/comCookie.txt";
	public static String uaPath = "resources/userAgents/UAForCn.txt";
	public static String crawlPath = "crawl";

	// 数据库设置
	public static String dbName = "user_db";
	public static String tablename = "user_tb";
	public static String insertSql = "insert into "
			+ "user_tb"
			+ "(uid,name,sex,location,isOnline,uRank,isMember,isDaren,isVerified,verifiedReson,description,createTime,timeStamp,statuseNum,friendNum,followerNum,memRank,medalNum,birthday,sexPrefer,feeling,interest,tag,domain,experience,avatar,fetchTime,friendsList,followersList,class) "
			+ "values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
}
