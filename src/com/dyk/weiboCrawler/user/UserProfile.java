/*
 * 2014年11月4日
 */
package com.dyk.weiboCrawler.user;

import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;

//获取用户：昵称，等级，性别，位置，是否被关注，认证原因，简介，微博数，关注数，粉丝数，分组
public class UserProfile extends DeepCrawler {
	
	//基本参数变量
	final String splitChar = " ";
	final String splitChar2 = " ";
	int fixSeconds=1;
	int timeInterval=6;
	String result = "";
	
	//用户属性变量
	String uid = "";
	String name = null;
	int sex = -1;
	String location = "";
	int isOnline = -1;
	int uRank = -1;
	int isMember = -1;
	int isDaren = 0;
	int isVerified = -1;
	String verifiedReson = null;
	String description = null;
	int statuseNum = -1;
	int friendNum = -1;
	int followerNum = -1;

	int memRank = -1;
	int medalNum = -1;
	String birthday = null;
	String sexPrefer = null;
	String feeling = null;
	String interest = null;
	String tag = null;
	String domain = null;
	String experience = null;
	String avatar=null;
	long fetchTime=-1;

	public UserProfile(String crawlPath) {
		super(crawlPath);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public int getuRank() {
		return uRank;
	}

	public void setuRank(int uRank) {
		this.uRank = uRank;
	}

	public int getIsMember() {
		return isMember;
	}

	public void setIsMember(int isMember) {
		this.isMember = isMember;
	}

	public int getIsDaren() {
		return isDaren;
	}

	public void setIsDaren(int isDaren) {
		this.isDaren = isDaren;
	}

	public int getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(int isVerified) {
		this.isVerified = isVerified;
	}

	public String getVerifiedReson() {
		return verifiedReson;
	}

	public void setVerifiedReson(String verifiedReson) {
		this.verifiedReson = verifiedReson;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatuseNum() {
		return statuseNum;
	}

	public void setStatuseNum(int statuseNum) {
		this.statuseNum = statuseNum;
	}

	public int getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}

	public int getFollowerNum() {
		return followerNum;
	}

	public void setFollowerNum(int followerNum) {
		this.followerNum = followerNum;
	}

	public int getMemRank() {
		return memRank;
	}

	public void setMemRank(int memRank) {
		this.memRank = memRank;
	}

	public int getMedalNum() {
		return medalNum;
	}

	public void setMedalNum(int medalNum) {
		this.medalNum = medalNum;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSexPrefer() {
		return sexPrefer;
	}

	public void setSexPrefer(String sexPrefer) {
		this.sexPrefer = sexPrefer;
	}

	public String getFeeling() {
		return feeling;
	}

	public void setFeeling(String feeling) {
		this.feeling = feeling;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public long getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(long fetchTime) {
		this.fetchTime = fetchTime;
	}
	
	@Override
	public Links visitAndGetNextLinks(Page page) {
		// 访问网页随机延时设置
		Random random = new Random();
		try {
			Thread.sleep((Math.abs(random.nextInt()) % timeInterval) * 1000+fixSeconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String urlFlag = page.getUrl().split("/")[4];
		Document doc = page.getDoc();
		Elements divcs = doc.select("div.c");
		
		if (!urlFlag.equals("info")) {
			Elements divs = doc.select("div[class=u]");
			String ffs = divs.select("div.tip2").text();
			String tmp1 = ffs.split(splitChar)[0];
			String tmp2 = ffs.split(splitChar)[1];
			String tmp3 = ffs.split(splitChar)[2];
			statuseNum = Integer.parseInt(tmp1.substring(tmp1.indexOf("[") + 1, tmp1.indexOf("]")));
			friendNum = Integer.parseInt(tmp2.substring(tmp2.indexOf("[") + 1, tmp2.indexOf("]")));
			followerNum = Integer.parseInt(tmp3.substring(tmp3.indexOf("[") + 1, tmp3.indexOf("]")));

			Element div = null;
			if (divs.size() == 0) {
				div = divs.first();
			} else {
				div = divs.get(0);
			}
			// 姓名、在线情况、等级、性别
			String firstLine = div.select("span.ctt").first().text();
			String nameTmp = firstLine.split(splitChar)[0];
			if (nameTmp.indexOf("在线") == -1) {
				name = nameTmp;
				isOnline = 0;
			} else {
				name = nameTmp.substring(0, nameTmp.indexOf("在线") - 1);
				isOnline = 1;
			}
			
			//String Rank = firstLine.split(splitChar)[1];
			//uRank = Integer.parseInt(Rank.substring(0, Rank.indexOf("级")));
			
			// sex:0表示女;1表示男
			String sexStr = firstLine.split(splitChar)[2].split("/")[0];
			if (sexStr.equals("女")) {
				sex = 0;
			} else if (sexStr.equals("男")) {
				sex = 1;
			}
			// 所在城市
			// location = firstLine.split(splitChar)[2].split("/")[1];

			// 用户uid,头像地址，是否会员
			Elements firstLineWithA = div.select("span.ctt").first().select("a");
			if (firstLineWithA.size() == 1) {
				uid = firstLineWithA.get(0).toString().split("/")[1];
				isMember = 0;
			} else if (firstLineWithA.size() >= 2) {
				Boolean hasVip=firstLineWithA.get(0).attr("href").contains("vip.weibo.cn");
				if (hasVip) {
					uid = firstLineWithA.get(1).toString().split("/")[1];
					isMember = 1;
				} else {
					uid = firstLineWithA.get(0).toString().split("/")[1];
					isMember = 0;
				}
			}
			// 是否是达人
			int imgCount = divs.select("span[class=ctt]").select("img").size();
			Elements img = divs.select("span[class=ctt]").select("img");
			if (imgCount >= 1) {
				String imgId = img.first().toString().split("/")[7].substring(0, 4);
				if (imgId.equals("5547")) {
					isDaren = 1;
				}
			}
			// 认证原因，简介
			Element secondLine = div.select("span.ctt").get(1);
			int numOFCtt=div.select("span.ctt").size();
			if ( numOFCtt== 2) {
				if (secondLine.text().split("：")[0].equals("认证")) {
					verifiedReson = secondLine.text().split("：")[1];
				} /*else if (secondLine.hasText()) {
					description = secondLine.text();
					}*/
			}
			if (numOFCtt == 3) {
				verifiedReson = secondLine.text().split("：")[1];
				// description = div.select("span.ctt").get(2).text();
			}

			// 是否认证
			if (verifiedReson != null) {
				String imgSrc=div.select("span.ctt").first().select("img").first().attr("src");
				if(imgSrc.contains("5338.gif")){
					isVerified = 1;
				}else if(imgSrc.contains("5337.gif")){
					isVerified = 2;
				}
			} else {
				isVerified = 0;
			}
			//数据抓取时间
			fetchTime=System.currentTimeMillis();
		} else {
			//头像地址
			avatar=divcs.get(0).select("img").first().attr("src");
			
			//微博等级
			Element firstBlock = divcs.get(1);
			String Rank = firstBlock.select("a").first().text();
			uRank = Integer.parseInt(Rank.substring(0, Rank.indexOf("级")));
			//System.out.println(uRank);
			
			// 会员等级、勋章个数
			String memRankStr = firstBlock.text().split("：")[2].split(splitChar)[0];
			if (memRankStr.indexOf("级") != -1) {
				memRank = Integer.parseInt(memRankStr.substring(0, memRankStr.indexOf("级")));
			}
			medalNum = firstBlock.select("img").size();

			// 生日、性取向、感情状况、标签
			String secondBlock = divcs.get(2).text();
			String[] segments = secondBlock.split(splitChar2);
			for (int i = 1; i < segments.length; i++) {
				String tmp = segments[i];
				if (tmp.contains("地区")) {
					if(i==segments.length-1){
						location = tmp.substring(3);
					}else{
						if(segments[i+1].contains(":")){
							location = tmp.substring(3);
						}else{
							location = tmp.substring(3)+"-"+segments[i+1];
						}
					}
				} else if (tmp.contains("简介")) {
					description = tmp.substring(3);
				} else if (tmp.contains("生日")) {
					birthday = tmp.substring(3);
				} else if (tmp.contains("性取向")) {
					sexPrefer = tmp.substring(4);
				} else if (tmp.contains("感情状况")) {
					feeling = tmp.substring(5);
				} else if (tmp.contains("达人")) {
					interest = secondBlock.substring(secondBlock.indexOf("达人") + 3, secondBlock.indexOf("性别"));
				} else if (tmp.contains("标签")) {
					tag = "[";
					String[] tagArr = tmp.substring(3).split(splitChar);
					int tagNum = tagArr.length - 1;
					for (int ti = 0; ti < tagNum; ti++) {
						if (ti == tagNum - 1) {
							tag += tagArr[ti] + "]";
						} else {
							tag += tagArr[ti] + ",";
						}
					}
				}
			}
			// 个性域名
			int blockNum = divcs.size();
			String firstLine = divcs.get(blockNum - 3).text().split(splitChar2)[0];
			String flag = firstLine.split("/")[3];
			int lineLength = firstLine.length();
			if (flag.equals("u")) {
				domain = firstLine.substring(23, lineLength);
			} else {
				domain = firstLine.substring(21, lineLength);
			}

			// 其他资料
			Elements tips = doc.select("div.tip");
			int tipNum = tips.size();
			if (tipNum > 2) {
				experience = "";
				for (int tt = 1; tt < tipNum - 1; tt++) {
					experience += "[" + tips.get(tt).text() + ":" + divcs.get(tt + 2).text() + "]";
				}
			}
		}
		// uer最终结果
		result = "uid:" + uid + ",name:" + name + ",sex:" + sex +",uRank:" + uRank + ",statuseNum:" + statuseNum
				+ ",friendNum:" + friendNum + ",followerNum:" + followerNum + ",medalNum:" + medalNum + ",isOnline:"
				+ isOnline + ",isMember:" + isMember + ",memRank:" + memRank + ",isDaren:" + isDaren + ",isVerified:"
				+ isVerified + ",verifiedReson:" + verifiedReson + ",description:" + description + ",location:"
				+ location + ",birthday:" + birthday + ",sexPrefer:" + sexPrefer + ",feeling:" + feeling + "interest:"
				+ interest + ",tag:" + tag + ",experience:" + experience + ",domain:" + domain + ",avatar:"+avatar+ ",fetchTime:"+fetchTime+",";
		return null;
	}

	public void startCrawler(String uid) throws Exception {
		//this.setUid(uid);
		this.addSeed("http://weibo.cn/u/" + uid);
		this.addSeed("http://weibo.cn/" + uid + "/info");
		this.setThreads(1);
		System.out.println("\n启动第二个爬虫！\n");
		this.start(1);
	}

	/*public static void main(String[] args) throws  Exception {
		UserProfile userProfile = new UserProfile("crawl");
		HttpRequesterImpl myRequester3=(HttpRequesterImpl) userProfile.getHttpRequester();  
        myRequester3.setCookie("_T_WM=0ad402eccc4ac9e9d9ad1db3807caeb8; M_WEIBOCN_PARAMS=rl%3D1; SUB=_2A255t0KFDeTxGeRH61UT9CjOwj2IHXVbWG7NrDV6PUJbrdANLUbCkW0suEa9C3VRQUYN-NWP-rWmoqKXhA..; gsid_CTandWM=4uzIea251JEVUvslY2EaSccj44H");
        myRequester3.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; MANMJS; rv:11.0) like Gecko");
        userProfile.startCrawler("2937067297");
	}*/
}