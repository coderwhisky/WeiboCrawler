package com.cj.weiboCrawler.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;

import com.cj.weiboCrawler.DB.WeiboDB;
import com.cj.weiboCrawler.utils.fileRW.FileOperation;
import com.cj.weiboCrawler.congifure.ParasSet;
import com.cj.weiboCrawler.cookie.cnCookie.CNCookie;
import com.cj.weiboCrawler.cookie.comCookie.ComCookie;

/**
 * @Author:      cj
 * @DateTime:    2014-12-12
 * @Description: 爬取用户基本资料数据
 */

public class StoreUser {

	public static void main(String[] args) throws Exception {
		// 初始参数
		Boolean ifRefreshCookie = false;
		int cnCookieIndex = ParasSet.cnCookieIndex;
		int comCookieIndex = ParasSet.comCookieIndex;
		int userAgentIndex = ParasSet.userAgentIndex;
		
		//更新cookie
		CNCookie cnCookie=new CNCookie();
		ComCookie comCookie=new ComCookie();
		ArrayList<String> cn_cookies = null;
		ArrayList<String> com_cookies = null;
		if(ifRefreshCookie){
			cn_cookies = cnCookie.getCnCookies(ParasSet.accountsPath, ParasSet.cnCookiePath);
			com_cookies = comCookie.getComCookies(ParasSet.accountsPath, ParasSet.comCookiePath);
		}
		
		//文件操作
		FileOperation fo = new FileOperation();
		if(!ifRefreshCookie){
			cn_cookies = fo.getParaList(ParasSet.cnCookiePath);
			com_cookies = fo.getParaList(ParasSet.comCookiePath);
		}
		ArrayList<String> userAgents = fo.getParaList(ParasSet.uaPath);
		FileReader fr = new FileReader(new File(ParasSet.u_idsPath));
		BufferedReader br = new BufferedReader(fr);
		
		// 数据库设置
		WeiboDB db = new WeiboDB(ParasSet.dbName, ParasSet.u_tablename);
		db.setQuery_pst(db.getConnect().prepareStatement(ParasSet.u_querySql));
		db.setInsert_pst(db.getConnect().prepareStatement(ParasSet.u_insertSql));
		
		//开始循环爬取
		try {
			int lineNum = 0;
			String uerid = null;
			while ((uerid = br.readLine()) != null) {
				//uerid=uerid.split("\t")[0];
				ResultSet r = db.QueryUser(uerid);
				if (r.next() == false){//去重
					System.out.println("\n------第  " + (++lineNum) + " 个账号------\n");
					// 定时更新cookie
					if(lineNum % 600 == 0){
						cn_cookies = cnCookie.getCnCookies(ParasSet.accountsPath, ParasSet.cnCookiePath);
						com_cookies = comCookie.getComCookies(ParasSet.accountsPath, ParasSet.comCookiePath);
					}
					
					if(lineNum % ParasSet.loopLinesNum == 0){
						cnCookieIndex++;
						comCookieIndex++;
						userAgentIndex++;
						if(cnCookieIndex%ParasSet.cnCookieRange==0){
							cnCookieIndex=0;
						}
						if(comCookieIndex%ParasSet.comCookieRange==0){
							comCookieIndex=0;
						}
						if(userAgentIndex%ParasSet.userAgentRange==0){
							userAgentIndex=0;
						}
						System.out.println("cnCookieIndex:"+cnCookieIndex);
						System.out.println("comCookieIndex:"+comCookieIndex);
						System.out.println("userAgent_index:"+userAgentIndex);
						Thread.sleep(30 * 1000);
					}
					/*// 随机延时设置
					Random random = new Random();
					Thread.sleep((Math.abs(random.nextInt()) % 2) * 1000 + 1000);
					*/
					// 每获取一个用户的数据，需要重新获取新的对象，否则会出现重叠！
					String cn_cookie=cn_cookies.get(cnCookieIndex);
					String com_cookie=com_cookies.get(comCookieIndex);
					String userAgent=userAgents.get(userAgentIndex);
					
					//初始化四个爬虫对象
					InfoURL infoURL=new InfoURL(ParasSet.u_crawlPath);
					HttpRequesterImpl myRequester=(HttpRequesterImpl) infoURL.getHttpRequester();  
					myRequester.setCookie(com_cookie);
					myRequester.setUserAgent("");
					
					CreateTime createTime=new CreateTime(ParasSet.u_crawlPath);
					HttpRequesterImpl myRequester2=(HttpRequesterImpl) createTime.getHttpRequester();  
					myRequester2.setCookie(com_cookie);
					myRequester2.setUserAgent("");
					
					UserProfile userProfile = new UserProfile(ParasSet.u_crawlPath);
					HttpRequesterImpl myRequester3=(HttpRequesterImpl) userProfile.getHttpRequester();  
					myRequester3.setCookie(cn_cookie);
					myRequester3.setUserAgent(userAgent);
					
					FansAndFollsList fansAndFollsList = new FansAndFollsList(ParasSet.u_crawlPath);
					HttpRequesterImpl myRequester4=(HttpRequesterImpl) fansAndFollsList.getHttpRequester();  
					myRequester4.setCookie(cn_cookie);
					myRequester4.setUserAgent(userAgent);
					
					//设置网页访问时间间隔
					userProfile.setRandomSeconds(ParasSet.randomSeconds);
					userProfile.setFixSeconds(ParasSet.fixedSeconds);
					fansAndFollsList.setRandomSeconds(ParasSet.randomSeconds);
					fansAndFollsList.setFixedSeconds(ParasSet.fixedSeconds);
					
					// 启动4个爬虫模块
					infoURL.startCrawler(uerid);
					String url=infoURL.getAbsUrl();
					//System.out.println("\n获取的URL： "+url+"\n");
					// 排除认证机构账号
					if(!url.contains("about")){
						//判断账号是否存在或正常
						if(infoURL.getIsNomal4URL()){
							url=infoURL.getAbsUrl();
							createTime.startCrawler(url);
							if(createTime.getIsNormal4Time()){
								userProfile.startCrawler(uerid);
								//获取粉丝数和关注数，以便于确定页数
								int friendNum=userProfile.getFriendNum();
								int followerNum=userProfile.getFollowerNum();
								fansAndFollsList.startCrawler(uerid, friendNum, followerNum);
								//入库
								db.insertUser(userProfile, fansAndFollsList, createTime);
							}else{
								new FileOperation().writeTxtFile(uerid, "resources/ids/abnomal.txt");
							}
							//控制台显示
							String userInfo = createTime.getCreateTime()+createTime.getTags()+"\n"+userProfile.getResult() +"\n"+ fansAndFollsList.getFollowersList()+"\n"+fansAndFollsList.getFriendsList();
							System.out.println(userInfo);
						}else{
							new FileOperation().writeTxtFile(uerid, "resources/ids/abnomal.txt");
						}
					}
				} else {
					System.out.println("\n————该用户数据已经存在于数据库————\n");
				}
				/*if (r != null) {
					r.close();
					r = null;
				}*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}
}

