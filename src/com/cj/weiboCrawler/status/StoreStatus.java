package com.cj.weiboCrawler.status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;

import com.cj.weiboCrawler.DB.WeiboDB;
import com.cj.weiboCrawler.congifure.ParasSet;
import com.cj.weiboCrawler.cookie.cnCookie.CNCookie;
import com.cj.weiboCrawler.user.UserProfile;
import com.cj.weiboCrawler.utils.fileRW.FileOperation;

public class StoreStatus {

	public static void main(String[] args) throws Exception {
		// 初始参数
		Boolean ifRefreshCookie = false;
		int cookieIndex = ParasSet.cnCookieIndex;
		int userAgentIndex = ParasSet.userAgentIndex;
		// 更新cookie
		CNCookie cnCookie = new CNCookie();
		ArrayList<String> cn_cookies = null;
		if (ifRefreshCookie) {
			cn_cookies = cnCookie.getCnCookies(ParasSet.accountsPath,
					ParasSet.cnCookiePath);
		}
		// 文件存储对象
		FileOperation fo = new FileOperation();
		if (!ifRefreshCookie) {
			cn_cookies = fo.getParaList(ParasSet.cnCookiePath);
		}
		ArrayList<String> userAgents = fo.getParaList(ParasSet.uaPath);
		FileReader fr = new FileReader(new File(ParasSet.s_idsPath));
		BufferedReader br = new BufferedReader(fr);

		// 数据库设置
		WeiboDB db = new WeiboDB(ParasSet.dbName, ParasSet.s_tablename);
		db.setQuery_pst(db.getConnect().prepareStatement(ParasSet.s_querySql));
		db.setInsert_pst(db.getConnect().prepareStatement(ParasSet.s_insertSql));

		// 开始循环爬取
		try {
			int lineNum = 1;
			String uerId = null;
			while ((uerId = br.readLine()) != null) {
				// 定时更新cookie
				if (lineNum % 600 == 0) {
					cn_cookies = cnCookie.getCnCookies(ParasSet.accountsPath,
							ParasSet.cnCookiePath);
				}

				if (lineNum % ParasSet.loopLinesNum == 0) {
					cookieIndex++;
					userAgentIndex++;
					if (cookieIndex % ParasSet.cnCookieRange == 0) {
						cookieIndex = 0;
					}
					if (userAgentIndex % ParasSet.userAgentRange == 0) {
						userAgentIndex = 0;
					}
					System.out.println("cookieIndex:" + cookieIndex);
					System.out.println("userAgent_index:" + userAgentIndex);
					Thread.sleep(30 * 1000);
				}
				// 实例化微博对象
				// Status status = new Status(cookies.getItem(cookie_index),
				// userAgents.getItem(userAgent_index));
				// 有问题！！！！！！！！！！！
				UserProfile userProfile = new UserProfile(ParasSet.s_crawlPath);
				HttpRequesterImpl myRequester = (HttpRequesterImpl) userProfile
						.getHttpRequester();
				myRequester.setCookie(cn_cookies.get(cookieIndex));
				myRequester.setUserAgent(userAgents.get(userAgentIndex));

				Status status = new Status(ParasSet.s_crawlPath);
				status.setMycookie(cn_cookies.get(cookieIndex));
				status.setMyuserAgent(userAgents.get(userAgentIndex));
				status.setWbdb(db);
				status.setTableName(ParasSet.s_tablename);
				status.setStartPageNum(ParasSet.startPage);
				status.setEndPageNum(ParasSet.endPage);
				status.setTimeInternal(ParasSet.randomSeconds);
				status.setFixSeconds(ParasSet.fixedSeconds);

				userProfile.startCrawler(uerId);
				int statusNum = userProfile.getStatuseNum();
				String result = status.getStatus(uerId, statusNum);

				System.out.println("######第" + (lineNum++) + "个人微博总数："
						+ status.getStatusCount());
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
