package com.cj.weiboCrawler.congifure;

/**
 * 系统变量定义
 */
public class ParasSet {
	// 登陆参数
	public static int cnCookieIndex = 10;
	public static int comCookieIndex = 2;
	public static int userAgentIndex = 5;
	public static int cnCookieRange = 15;
	public static int comCookieRange = 5;
	public static int userAgentRange = 21;
	public static int fixedSeconds = 2;
	public static int randomSeconds = 2;
	public static int loopLinesNum = 6;
	
	// 微博内容起始页和结束页
	public static int startPage = 1;
	public static int endPage = 2;

	// 文件路径参数
	public static String userType = "unknown";// unknown,auto,advertise,common,zombie,
	public static String u_idsPath = "resources/ids/user_id.txt";
	public static String s_idsPath = "resources/ids/test.txt";
	public static String accountsPath = "resources/accounts/accounts3.txt";
	public static String cnCookiePath = "resources/accounts/cnCookie.txt";
	public static String comCookiePath = "resources/accounts/comCookie.txt";
	public static String uaPath = "resources/userAgents/UAForCn.txt";
	public static String u_crawlPath = "crawl_user";
	public static String s_crawlPath = "crawl_status";

	// 数据库设置
	public static String dbName = "user_db";
	public static String u_tablename = "user_tb";
	public static String u_querySql = "select * from " + u_tablename + " where uid= ?";
	public static String u_insertSql = "insert into "
			+ u_tablename
			+ "(uid,name,sex,location,isOnline,uRank,isMember,isDaren,isVerified,verifiedReson,description,createTime,timeStamp,statusNum,friendNum,followerNum,memRank,medalNum,birthday,sexPrefer,feeling,interest,tag,domain,experience,avatar,fetchTime,friendsList,followersList,class) "
			+ "values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String s_tablename = "status_tb";
	public static String s_querySql = "select * from " + s_tablename + " where mid= ?";
	public static String s_insertSql = "insert into "
			+ s_tablename
			+ "(mid,encrypted_mid,isReposted,userId,userName,rootUserId,rootUserName,rootMid,encrypted_rootMid,statusText,hasPic,picsNum,multiPicsUrl,sigPicUrl,rootPraiseNum,rootRepostNum,rootCommentNum,repostReson,praiseNum,repostNum,commentNum,favouriteNum,createTime,timeStamp,source,fetchTime,fetchTimeFormat,class) "
			+ "values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
}
