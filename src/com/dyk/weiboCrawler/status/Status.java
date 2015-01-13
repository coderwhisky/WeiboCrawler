package com.dyk.weiboCrawler.status;

/*
 * 2014年11月4日
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dyk.weiboCrawler.DB.WeiboDB;
import com.dyk.weiboCrawler.utils.other.MidFormat;
import com.dyk.weiboCrawler.utils.time.WeiboTimeUtil;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import cn.edu.hfut.dmic.webcollector.util.RegexRule;

public class Status extends DeepCrawler {
	
	RegexRule regexRule = new RegexRule();
	
	int timeInternal=6;
	WeiboDB wbdb;
	String tableName = null;
	int startPageNum = -1;
	int endPageNum = -1;
	String mycookie = null;
	String myuserAgent = null;
	int statusCount = 0;
	String allStatus = "";
	WeiboTimeUtil timeUtils = new WeiboTimeUtil();
	// 特殊字符
	String splitChar = " ";
	String splitDate = " ";
	String splitNameChar = " ";
	String oriDateSpace = " ";// 2011-04-07 01:22:00
	String formatSpace = " ";
	// 基本属性
	int isReposted = -1;
	String userId = null;
	String userName = null;
	String rootUserName = null;
	String rootUserId = null;
	String mid = null;
	String encrypted_mid = null;
	String rootMid = null;
	String encrypted_rootMid = null;
	String statusText = null;
	int hasPic = -1;

	int picsNum=-1;
	String multiPicsUrl=null;
	String sigPicsUrl=null;
	
	
	int rootPraiseNum = -1;
	int rootRepostNum = -1;
	int rootCommentNum = -1;
	String repostReson = null;
	int praiseNum = -1;
	int repostNum = -1;
	int commentNum = -1;
	int favouriteNum = -1;
	String createTime = null;
	long timeStamp = -1;
	String source = null;
	long fetchTime = -1;
	String fetchTimeFormat = null;

	/*public Status(String cookie, String userAgent) {
		Config.topN = 0;
		setUseragent(userAgent);
		setCookie(cookie);
	}*/

	public Status(String crawlPath) {
		super(crawlPath);
		regexRule.addRule(".*");
	}

	public int getTimeInternal() {
		return timeInternal;
	}

	public void setTimeInternal(int timeInternal) {
		this.timeInternal = timeInternal;
	}

	public WeiboDB getWbdb() {
		return wbdb;
	}

	public void setWbdb(WeiboDB wbdb) {
		this.wbdb = wbdb;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getStartPageNum() {
		return startPageNum;
	}

	public void setStartPageNum(int startPageNum) {
		this.startPageNum = startPageNum;
	}

	public int getEndPageNum() {
		return endPageNum;
	}

	public void setEndPageNum(int endPageNum) {
		this.endPageNum = endPageNum;
	}

	public String getMycookie() {
		return mycookie;
	}

	public void setMycookie(String mycookie) {
		this.mycookie = mycookie;
	}

	public String getMyuserAgent() {
		return myuserAgent;
	}

	public void setMyuserAgent(String myuserAgent) {
		this.myuserAgent = myuserAgent;
	}

	public int getStatusCount() {
		return statusCount;
	}

	public void setStatusCount(int statusCount) {
		this.statusCount = statusCount;
	}

	public int getIsReposted() {
		return isReposted;
	}

	public void setIsReposted(int isReposted) {
		this.isReposted = isReposted;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRootUserName() {
		return rootUserName;
	}

	public void setRootUserName(String rootUserName) {
		this.rootUserName = rootUserName;
	}

	public String getRootUserId() {
		return rootUserId;
	}

	public void setRootUserId(String rootUserId) {
		this.rootUserId = rootUserId;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getRootMid() {
		return rootMid;
	}

	public void setRootMid(String rootMid) {
		this.rootMid = rootMid;
	}

	public String getEncrypted_mid() {
		return encrypted_mid;
	}

	public void setEncrypted_mid(String encrypted_mid) {
		this.encrypted_mid = encrypted_mid;
	}

	public String getEncrypted_rootMid() {
		return encrypted_rootMid;
	}

	public void setEncrypted_rootMid(String encrypted_rootMid) {
		this.encrypted_rootMid = encrypted_rootMid;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public int getHasPic() {
		return hasPic;
	}

	public void setHasPic(int hasPic) {
		this.hasPic = hasPic;
	}
	
	
	public int getPicsNum() {
		return picsNum;
	}

	public void setPicsNum(int picsNum) {
		this.picsNum = picsNum;
	}

	public String getMultiPicsUrl() {
		return multiPicsUrl;
	}

	public void setMultiPicsUrl(String multiPicsUrl) {
		this.multiPicsUrl = multiPicsUrl;
	}

	public String getSigPicsUrl() {
		return sigPicsUrl;
	}

	public void setSigPicsUrl(String sigPicsUrl) {
		this.sigPicsUrl = sigPicsUrl;
	}


	public int getRootPraiseNum() {
		return rootPraiseNum;
	}

	public void setRootPraiseNum(int rootPraiseNum) {
		this.rootPraiseNum = rootPraiseNum;
	}

	public int getRootRepostNum() {
		return rootRepostNum;
	}

	public void setRootRepostNum(int rootRepostNum) {
		this.rootRepostNum = rootRepostNum;
	}

	public int getRootCommentNum() {
		return rootCommentNum;
	}

	public void setRootCommentNum(int rootCommentNum) {
		this.rootCommentNum = rootCommentNum;
	}

	public String getRepostReson() {
		return repostReson;
	}

	public void setRepostReson(String repostReson) {
		this.repostReson = repostReson;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public int getRepostNum() {
		return repostNum;
	}

	public void setRepostNum(int repostNum) {
		this.repostNum = repostNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public int getFavouriteNum() {
		return favouriteNum;
	}

	public void setFavouriteNum(int favouriteNum) {
		this.favouriteNum = favouriteNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public long getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(long fetchTime) {
		this.fetchTime = fetchTime;
	}

	public String getFetchTimeFormat() {
		return fetchTimeFormat;
	}

	public void setFetchTimeFormat(String fetchTime2) {
		this.fetchTimeFormat = fetchTime2;
	}

	/**
	 * 定义爬取成功时对页面的操作
	 * 
	 * @param page
	 */

	@Override
	public Links visitAndGetNextLinks(Page page) {
		// 随机延时设置
		Random random = new Random();
		try {
			Thread.sleep((Math.abs(random.nextInt()) % timeInternal) * 1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 网页数据爬取时间
		fetchTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fetchTimeFormat = sdf.format(new Date(fetchTime));

		Elements outDivs = page.getDoc().select("div.c");
		// 该用户昵称
		String rNameLine = page.getDoc().select("div.u").select("div.ut").text();
		int rNameIndex = -1;
		if (rNameLine.contains("[在线]")) {
			rNameIndex = rNameLine.indexOf("[在线]") + 1;
		} else {
			rNameIndex = rNameLine.indexOf("的微博") + 1;
		}
		if (rNameIndex < 1) {
			userName = rNameLine.split(splitChar)[0];
		} else {
			userName = rNameLine.substring(0, rNameIndex - 1);
		}
		// 当前页面的微博
		String curPageStatus = "";
		// 判断是否有微博发布
		String firstStatus = outDivs.first().text();
		if (firstStatus.contains("没发过微博") && outDivs.size() == 3) {
			allStatus = "there is not status";
		} else {
			// 遍历该页每条微博
			/*int i = 0;
			//过滤最近一条微博或置顶微博
			if(statusCount==0){
				i=1;
			}*/
			for (int i = 0; i < outDivs.size() - 2; i++) {
				Element outDiv = outDivs.get(i);
				Elements innerDivs = outDiv.select("div");
				// 微博MID
				MidFormat mf = new MidFormat();
				String oriMid = outDiv.attr("id");
				oriMid = oriMid.substring(2, oriMid.length());
				mid = mf.getMid(oriMid);
				encrypted_mid = oriMid;

				int lineNum = innerDivs.size();
				// 1.原创无图
				if (lineNum == 2) {
					// System.out.println("原创无图");
					isReposted = 0;
					rootUserId = null;
					rootUserName = null;
					rootMid = null;
					rootPraiseNum = -1;
					rootRepostNum = -1;
					rootCommentNum = -1;
					repostReson = null;
					hasPic = 0;
					multiPicsUrl=null;
					sigPicsUrl=null;
					picsNum=-1;

					Element curDiv = innerDivs.get(1);
					statusText = curDiv.select("span.ctt").text().trim();

					Elements divWithA = curDiv.select("a");
					int numOfA = divWithA.size();
					// 发布来源有超链接
					if (curDiv.select("span.ct").select("a").size() > 0) {
						numOfA -= 1;
					}

					String praiseStr = divWithA.get(numOfA - 4).text();
					String repostStr = divWithA.get(numOfA - 3).text();
					String commentStr = divWithA.get(numOfA - 2).text();
					String favouriteStr = divWithA.get(numOfA - 1).text();
					int[] prcf = get_PRCF_Num(praiseStr, repostStr, commentStr, favouriteStr);
					praiseNum = prcf[0];
					repostNum = prcf[1];
					commentNum = prcf[2];
					favouriteNum = prcf[3];

					String dicWithCt = outDiv.select("span.ct").text();
					String createTimeStr = null;
					if (dicWithCt.indexOf("来") != -1) {
						source = dicWithCt.substring(dicWithCt.indexOf("来") + 2, dicWithCt.length());
						createTimeStr = dicWithCt.substring(0, dicWithCt.indexOf("来") - 1);
					} else {
						source = null;
						createTimeStr = dicWithCt.substring(0, dicWithCt.length());
					}
					createTime = timeUtils.getFormatTime(createTimeStr);
					try {
						timeStamp = timeUtils.getTimeStamp(createTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (lineNum == 3) {
					Elements cmts = innerDivs.get(1).select("span.cmt");
					int numOfCmt = cmts.size();
					// 2.原创有图
					if (numOfCmt == 0 || cmts.text().equals("会员特权")) {
						// System.out.println("原创有图");
						isReposted = 0;
						rootUserId = null;
						rootUserName = null;
						rootMid = null;
						rootPraiseNum = -1;
						rootRepostNum = -1;
						rootCommentNum = -1;
						repostReson = null;
						statusText = innerDivs.get(1).text().trim();
						///////////////////////////////////////////////////////////////////////////
						//图片地址
						Elements href=innerDivs.get(1).select("div>a");
						String tempURL=href.attr("href");
						if(!tempURL.isEmpty()){
							hasPic=1;
							multiPicsUrl=tempURL;
							sigPicsUrl=innerDivs.get(2).select("div>a").first().select("img").attr("src");
							String str=href.text();
							picsNum=Integer.parseInt(str.substring(3, 4));
						}else if (innerDivs.get(2).text().contains("原图")) {
							hasPic = 1;
							multiPicsUrl=null;
							sigPicsUrl=innerDivs.get(2).select("div>a").first().select("img").attr("src");
							picsNum=1;
						} else {
							hasPic = 0;
							multiPicsUrl=null;
							sigPicsUrl=null;
							picsNum=-1;
						}

						String[] line2 = innerDivs.get(2).text().split(splitChar);
						int[] prcf = get_PRCF_Num(line2[2], line2[3], line2[4], line2[5]);
						praiseNum = prcf[0];
						repostNum = prcf[1];
						commentNum = prcf[2];
						favouriteNum = prcf[3];

						String createTimeStr = line2[6];
						createTime = timeUtils.getFormatTime(createTimeStr);
						try {
							timeStamp = timeUtils.getTimeStamp(createTime);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (line2.length == 8) {
							source = line2[7].substring(2);
						} else {
							source = null;

						}
						// 3.转发无图
					} else if (numOfCmt > 0 && !cmts.text().equals("会员特权")) {
						// System.out.println("转发无图");
						isReposted = 1;
						Element div1 = innerDivs.get(1);
						Elements div1_cmt_a = div1.select("span.cmt").select("a");
						if (div1_cmt_a.size() == 0) {
							rootUserName = null;
							rootUserId = null;
						} else {
							rootUserName = div1_cmt_a.text();
							// 原微博删除或无权限查看，导致原微博用户昵称不显示。
							String[] uIDStr = div1_cmt_a.toString().split("\"")[1].split("/");
							if (uIDStr.length == 3) {
								rootUserId = null;
							} else {
								String uFlag = uIDStr[3];
								int uIndex = -1;
								if (uFlag.equals("u")) {
									uIndex = 4;
								} else {
									uIndex = 3;
								}
								if (uIDStr[uIndex].contains("\\?")) {
									rootUserId = uIDStr[uIndex].split("\\?")[0];
								} else {
									rootUserId = uIDStr[uIndex];
								}
							}
						}

						// 若为转发，原微博MID
						int numOfA = innerDivs.get(1).select("a").size();
						String commentHtml = innerDivs.get(1).select("a").get(numOfA - 1).toString();
						String oriRootMid = commentHtml.split("/")[4].split("\\?")[0];
						rootMid = mf.getMid(oriRootMid);
						encrypted_rootMid = oriRootMid;

						statusText = div1.select("span.ctt").text().trim();
						hasPic = 0;
						multiPicsUrl=null;
						sigPicsUrl=null;
						picsNum=-1;

						Elements cmt = div1.select("span.cmt");
						String rptmp = cmt.get(1).text();
						String rrtmp = cmt.get(1).text();
						String rctmp = innerDivs.get(1).select("a.cc").text();

						int prc[] = get_PRCF_Num(rptmp, rrtmp, rctmp);
						rootPraiseNum = prc[0];
						rootRepostNum = prc[1];
						rootCommentNum = prc[2];

						String repostResonTmp = innerDivs.get(2).text();
						repostReson = repostResonTmp.substring(0, repostResonTmp.indexOf("赞")).trim();

						int aCount = innerDivs.get(2).select("a").size();
						// 发布来源有超链接
						if (innerDivs.get(2).select("span.ct").select("a").size() > 0) {
							aCount -= 1;
						}
						Elements frcf = outDiv.select("div").get(2).select("a");
						String praiseStr = frcf.get(aCount - 4).toString();
						String repostStr = frcf.get(aCount - 3).toString();
						String commentStr = frcf.get(aCount - 2).toString();
						String favouriteStr = frcf.get(aCount - 1).toString();
						int[] prcf = get_PRCF_Num(praiseStr, repostStr, commentStr, favouriteStr);
						praiseNum = prcf[0];
						repostNum = prcf[1];
						commentNum = prcf[2];
						favouriteNum = prcf[3];

						String csTmp = innerDivs.get(2).select("span.ct").text();
						String createTimeStr = null;
						if (csTmp.indexOf("来") != -1) {
							source = csTmp.substring(csTmp.indexOf("来") + 2, csTmp.length());
							createTimeStr = csTmp.substring(0, csTmp.indexOf("来") - 1);
						} else {
							source = null;
							createTimeStr = csTmp.substring(0, csTmp.length());
						}
						createTime = timeUtils.getFormatTime(createTimeStr);
						try {
							timeStamp = timeUtils.getTimeStamp(createTime);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// 4.转发有图
				} else if (lineNum == 4) {
					// System.out.println(" 转发有图");
					isReposted = 1;
					rootUserName = innerDivs.get(1).select("span.cmt").select("a").text();
					String uFlag = innerDivs.get(1).select("span.cmt").select("a").toString().split("\"")[1].split("/")[3];
					String[] uIDStr = innerDivs.get(1).select("span.cmt").select("a").toString().split("\"")[1]
							.split("/");

					if (uFlag.equals("u")) {
						rootUserId = uIDStr[4];
					} else {
						rootUserId = uIDStr[3];
					}

					// 若为转发，原微博MID
					int numOfA = innerDivs.get(2).select("a").size();
					String commentHtml = innerDivs.get(2).select("a").get(numOfA - 1).toString();
					String oriRootMid = commentHtml.split("/")[4].split("\\?")[0];
					rootMid = mf.getMid(oriRootMid);
					encrypted_rootMid = oriRootMid;

					statusText = innerDivs.get(1).select("span.ctt").text().trim();
					/////////////////////////////////////////////////////////////////////////////////
					Elements href=innerDivs.get(1).select("div>a");
					String tempURL=href.attr("href");
					if(!tempURL.isEmpty()){
						hasPic=1;
						multiPicsUrl=tempURL;
						sigPicsUrl=innerDivs.get(2).select("div>a").first().select("img").attr("src");
						String str=href.text();
						picsNum=Integer.parseInt(str.substring(3, 4));
					}else if (innerDivs.get(2).text().contains("原图")) {
						hasPic = 1;
						multiPicsUrl=null;
						sigPicsUrl=innerDivs.get(2).select("div>a").first().select("img").attr("src");
						picsNum=1;
					} else {
						hasPic = 0;
						multiPicsUrl=null;
						sigPicsUrl=null;
						picsNum=-1;
					}
					
					String[] line2 = innerDivs.get(2).text().split(splitChar);
					int prc[] = get_PRCF_Num(line2[2], line2[3], line2[4]);
					rootPraiseNum = prc[0];
					rootRepostNum = prc[1];
					rootCommentNum = prc[2];

					String repostResonTmp = outDiv.select("div").get(3).text();
					repostReson = repostResonTmp.substring(0, repostResonTmp.indexOf("赞")).trim();

					int aCount = innerDivs.get(3).select("a").size();
					// 发布来源有超链接
					if (innerDivs.get(3).select("span.ct").select("a").size() > 0) {
						aCount -= 1;
					}

					Element line3 = outDiv.select("div").get(3);
					String praiseStr = line3.select("a").get(aCount - 4).toString();
					String repostStr = line3.select("a").get(aCount - 3).toString();
					String commentStr = line3.select("a").get(aCount - 2).toString();
					String favouriteStr = line3.select("a").get(aCount - 1).toString();
					int[] prcf = get_PRCF_Num(praiseStr, repostStr, commentStr, favouriteStr);
					praiseNum = prcf[0];
					repostNum = prcf[1];
					commentNum = prcf[2];
					favouriteNum = prcf[3];

					String csTmp = line3.select("span.ct").text();
					String createTimeStr = null;
					if (csTmp.indexOf("来") != -1) {
						source = csTmp.substring(csTmp.indexOf("来") + 2, csTmp.length());
						createTimeStr = csTmp.substring(0, csTmp.indexOf("来") - 1);
					} else {
						source = null;
						createTimeStr = csTmp.substring(0, csTmp.length());
					}

					createTime = timeUtils.getFormatTime(createTimeStr);
					try {
						timeStamp = timeUtils.getTimeStamp(createTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				statusCount++;
				curPageStatus += "userId:" + userId + ",userName:" + userName + ",mid:" + mid + ",encrypted_mid:"
						+ encrypted_mid + ",isReposted:" + isReposted + ",rootUserName:" + rootUserName
						+ ",rootUserId:" + rootUserId + ",rootMid:" + rootMid + ",encrypted_rootMid:"
						+ encrypted_rootMid + ",statusText:" + statusText + ",hasPic:" + hasPic 
						+ ",picsNum:"+picsNum+",multiPicsUrl:"+multiPicsUrl+",sigPicsUrl:"+sigPicsUrl+",rootPraiseNum:"
						+ rootPraiseNum + ",rootRepostNum:" + rootRepostNum + ",rootCommentNum:" + rootCommentNum
						+ ",repostReson:" + repostReson + ",praiseNum:" + praiseNum + ",repostNum:" + repostNum
						+ ",commentNum:" + commentNum + ",favouriteNum:" + favouriteNum + ",createTime:" + createTime
						+ ",timeStamp:" + timeStamp + ",source:" + source + ",fetchTime:" + fetchTime + ",fetchTime2:"
						+ fetchTimeFormat + "\n";
				//wbdb.insertStatus(this, tableName);
			}
		}
		allStatus += curPageStatus;
		return null;
	}

	// 获取赞、转发、评论、收藏 数量
	public int[] get_PRCF_Num(String praiseStr, String repostStr, String commentStr, String favouriteStr) {
		int[] prcf = new int[4];
		prcf[0] = Integer.parseInt(praiseStr.substring(praiseStr.indexOf("[") + 1, praiseStr.indexOf("]")));
		prcf[1] = Integer.parseInt(repostStr.substring(repostStr.indexOf("[") + 1, repostStr.indexOf("]")));
		prcf[2] = Integer.parseInt(commentStr.substring(commentStr.indexOf("[") + 1, commentStr.indexOf("]")));
		if (favouriteStr.indexOf("[") < 0) {
			prcf[3] = 0;
		} else {
			prcf[3] = Integer
					.parseInt(favouriteStr.substring(favouriteStr.indexOf("[") + 1, favouriteStr.indexOf("]")));
		}

		return prcf;
	}

	// 获取赞、转发、评论 数量
	public int[] get_PRCF_Num(String praiseStr, String repostStr, String commentStr) {
		int[] prcf = new int[3];
		prcf[0] = Integer.parseInt(praiseStr.substring(praiseStr.indexOf("[") + 1, praiseStr.indexOf("]")));
		prcf[1] = Integer.parseInt(repostStr.substring(repostStr.indexOf("[") + 1, repostStr.indexOf("]")));
		prcf[2] = Integer.parseInt(commentStr.substring(commentStr.indexOf("[") + 1, commentStr.indexOf("]")));

		return prcf;
	}

	public String getStatus(String uid) throws Exception {
		userId = uid;
		// allStatus = "";
		for (int i = startPageNum; i <= endPageNum; i++) {
			this.addSeed("http://weibo.cn/u/" + uid + "?page=" + i);
		}
		HttpRequesterImpl myRequester=(HttpRequesterImpl) this.getHttpRequester();  
        myRequester.setCookie(mycookie);
        myRequester.setUserAgent(myuserAgent);
		this.setThreads(1);
		this.start(1);
		return allStatus;
	}
}
