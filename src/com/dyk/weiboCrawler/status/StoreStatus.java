package com.dyk.weiboCrawler.status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.dyk.weiboCrawler.DB.WeiboDB;
import com.dyk.weiboCrawler.utils.fileRW.FileOperation;

public class StoreStatus {

	public static void main(String[] args) throws Exception {

		int startPage = 1;
		int endPage = 1;
		int cookieIndex = 0;
		int userAgentIndex = 0;
		int timeInternal=3;

		// 文件存储对象
		FileOperation fo = new FileOperation();
		java.awt.List cookies = fo.getParaList("configure/cn_cookie.txt");
		java.awt.List userAgents = fo.getParaList("configure/UAForCn.txt");
		FileReader fr = new FileReader(new File("ids/test.txt"));
		BufferedReader br = new BufferedReader(fr);

		//数据库设置
		String dbName = "user_db";
		String tablename = "zombie";
		WeiboDB db = new WeiboDB(dbName, tablename);
		db.setQuery_pst(db.getConnect().prepareStatement("select * from " + tablename + " where mid= ?"));
		db.setInsert_pst(db
				.getConnect()
				.prepareStatement(
						"insert into "
								+ tablename
								+ "(mid,encrypted_mid,isReposted,userId,userName,rootUserId,rootUserName,rootMid,encrypted_rootMid,statusText,hasPic,rootPraiseNum,rootRepostNum,rootCommentNum,repostReson,praiseNum,repostNum,commentNum,favouriteNum,createTime,timeStamp,source,fetchTime,fetchTime2) "
								+ "values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"));

		try {
			int userNum = 1;
			String uerId = null;
			// 读取用户ID
			while ((uerId = br.readLine()) != null) {
				if(userNum%6==0){
					cookieIndex++;
					userAgentIndex++;
					if(cookieIndex%25==0){
						cookieIndex=0;
					}
					if(userAgentIndex%21==0){
						userAgentIndex=0;
					}
					System.out.println("cookieIndex:"+cookieIndex);
					System.out.println("userAgent_index:"+userAgentIndex);
					Thread.sleep(6 * 1000);
				}
				// 实例化微博对象
				// Status status = new Status(cookies.getItem(cookie_index),
				// userAgents.getItem(userAgent_index));
				// 有问题！！！！！！！！！！！
				Status status = new Status("crawl");
				status.setMycookie(cookies.getItem(cookieIndex));
				status.setMyuserAgent(userAgents.getItem(userAgentIndex));
				status.setWbdb(db);
				status.setTableName(tablename);
				status.setStartPageNum(startPage);
				status.setEndPageNum(endPage);
				status.setTimeInternal(timeInternal);
				String result = status.getStatus(uerId);
				System.out.println("######第" + (++userNum) + "个人微博总数：" + status.getStatusCount());
				System.out.println(result);
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
