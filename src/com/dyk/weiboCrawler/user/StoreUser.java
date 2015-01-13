package com.dyk.weiboCrawler.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;

import com.dyk.weiboCrawler.DB.WeiboDB;
import com.dyk.weiboCrawler.utils.fileRW.FileOperation;
import com.dyk.weiboCrawler.congifure.SystemParas;

/**
 * @Author:      dyk
 * @DateTime:    2014-12-12
 * @Description: 爬取用户基本资料数据
 */

public class StoreUser {

	public static void main(String[] args) throws Exception {
		// 初始参数
		int cnCookieIndex = SystemParas.cnCookieIndex;
		int comCookieIndex = SystemParas.comCookieIndex;
		int userAgentIndex = SystemParas.userAgentIndex;
		int timeInternal=SystemParas.timeInternal;
		// 文件存储对象
		String userType=SystemParas.userType;
		String idsPath=SystemParas.idsPath;
		String cnCookiePath=SystemParas.cnCookiePath;
		String comCookiePath=SystemParas.comCookiePath;
		String uaPath=SystemParas.uaPath;
		String crawlPath=SystemParas.crawlPath;
		// 数据库设置
		String dbName = SystemParas.dbName;
		String tablename = SystemParas.tablename;
		String insertSql=SystemParas.insertSql;
						
		//文件操作
		FileOperation fo = new FileOperation();
		java.awt.List cn_cookies = fo.getParaList(cnCookiePath);
		java.awt.List com_cookies = fo.getParaList(comCookiePath);
		java.awt.List userAgents = fo.getParaList(uaPath);
		FileReader fr = new FileReader(new File(idsPath));
		BufferedReader br = new BufferedReader(fr);
		
		WeiboDB db = new WeiboDB(dbName, tablename);
		db.setQuery_pst(db.getConnect().prepareStatement("select * from " + tablename + " where uid= ?"));
		db.setInsert_pst(db.getConnect().prepareStatement(insertSql));
		
		//开始循环爬取
		try {
			int lineNum = 0;
			String uerid = null;
			while ((uerid = br.readLine()) != null) {
				System.out.println("##############################第  " + (++lineNum)
						+ " 行####################################");
				if(lineNum%6==0){
					cnCookieIndex++;
					comCookieIndex++;
					userAgentIndex++;
					if(cnCookieIndex%SystemParas.cnCookieRange==0){
						cnCookieIndex=0;
					}
					if(comCookieIndex%SystemParas.comCookieRange==0){
						comCookieIndex=0;
					}
					if(userAgentIndex%SystemParas.userAgentRange==0){
						userAgentIndex=0;
					}
					System.out.println("cnCookieIndex:"+cnCookieIndex);
					System.out.println("comCookieIndex:"+comCookieIndex);
					System.out.println("userAgent_index:"+userAgentIndex);
					//Thread.sleep(2*60 * 1000);
				}
				// 随机延时设置
				Random random = new Random();
				Thread.sleep((Math.abs(random.nextInt()) % 2) * 1000);

				// 每获取一个用户的数据，需要重新获取新的对象，否则会出现重叠！
				String cn_cookie=cn_cookies.getItem(cnCookieIndex);
				String com_cookie=com_cookies.getItem(comCookieIndex);
				String userAgent=userAgents.getItem(userAgentIndex);
				
				//初始化四个爬虫对象
				InfoURL infoURL=new InfoURL(crawlPath);
				HttpRequesterImpl myRequester=(HttpRequesterImpl) infoURL.getHttpRequester();  
		        myRequester.setCookie(com_cookie);
		        myRequester.setUserAgent("");
		        
				CreateTime createTime=new CreateTime(crawlPath);
				HttpRequesterImpl myRequester2=(HttpRequesterImpl) createTime.getHttpRequester();  
		        myRequester2.setCookie(com_cookie);
		        myRequester2.setUserAgent("");
				
				UserProfile userProfile = new UserProfile(crawlPath);
				HttpRequesterImpl myRequester3=(HttpRequesterImpl) userProfile.getHttpRequester();  
		        myRequester3.setCookie(cn_cookie);
		        myRequester3.setUserAgent(userAgent);
				
				FansAndFollsList fansAndFollsList = new FansAndFollsList(crawlPath);
				HttpRequesterImpl myRequester4=(HttpRequesterImpl) fansAndFollsList.getHttpRequester();  
		        myRequester4.setCookie(cn_cookie);
		        myRequester4.setUserAgent(userAgent);
		        
		        //设置网页访问时间间隔
				userProfile.setTimeInterval(timeInternal);
				fansAndFollsList.setTimeInternal(timeInternal);
				
				// 启动3个爬虫
				infoURL.startCrawler(uerid);
				//判断账号是否存在或正常
				if(infoURL.getIsNomal4URL()){
					String url=infoURL.getAbsUrl();
					createTime.startCrawler(url);
					if(createTime.getIsNormal4Time()){
						userProfile.startCrawler(uerid);
						fansAndFollsList.startCrawler(uerid);
						db.insertUser(userProfile, fansAndFollsList, createTime, tablename,userType);
					}else{
						new FileOperation().writeTxtFile(uerid, "ids/abnomal.txt");
					}
					//控制台显示
					String userInfo = createTime.getCreateTime()+createTime.getTags()+"\n"+userProfile.getResult() +"\n"+ fansAndFollsList.getFollowersList()+"\n"+fansAndFollsList.getFriendsList();
					System.out.println(userInfo);
				}else{
					new FileOperation().writeTxtFile(uerid, "resources/ids/abnomal.txt");
				}
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
