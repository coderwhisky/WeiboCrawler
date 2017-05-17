package com.cj.weiboCrawler.comment;

import java.text.ParseException;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cj.weiboCrawler.utils.fileRW.FileOperation;
import com.cj.weiboCrawler.utils.time.WeiboTimeUtil;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;

public class Comment extends DeepCrawler {
	public String splitCharOfDateAndSource=" ";
	
	public boolean isHot=false;
	public String name=null;
	public String uid=null;
	public String content=null;
	public int praiseNum=-1;
	public String publishDate=null;
	public long timeStamp=-1;
	public String source=null;
	public String result=null;
	
	public Comment(String crawlPath) {
		super(crawlPath);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		// TODO Auto-generated method stub
		Document doc = page.getDoc();
		Elements div_c=doc.select("div.c");
		int div_c_num=div_c.size();
		for(int i=1; i<div_c_num; i++){
			Element element=div_c.get(i);
			if(!element.text().equals("查看更多热门>>") && element.hasAttr("id") && element.attr("id").contains("C") && !element.select("span[class=kt]").text().equals("[热门]")){
				if(page.getUrl().contains("hot")){
					isHot=true;
				}else{
					isHot=false;
				}
				name=element.select("a").first().text();
				uid=element.select("div>a").get(1).attr("href");
				uid=uid.substring(uid.indexOf("fuid=")+5, uid.indexOf("&type="));
				content=element.select("span[class=ctt]").text();
				String praiseStr=element.select("span[class=cc]").text();
				praiseStr=praiseStr.substring(praiseStr.indexOf("[")+1, praiseStr.indexOf("]"));
				praiseNum=Integer.parseInt(praiseStr);
				String []temp=element.select("span[class=ct]").text().split(splitCharOfDateAndSource);
				WeiboTimeUtil weiboTimeUtil=new WeiboTimeUtil();
				publishDate=weiboTimeUtil.getFormatTime(temp[0]);
				try {
					timeStamp=weiboTimeUtil.getTimeStamp(publishDate);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				source=temp[1].substring(2);
				result=isHot+"	"+name+"	"+uid+"	"+content+"	"+praiseNum+"	"+publishDate+"	"+timeStamp+"	"+source;
				try {
					new FileOperation().writeTxtFile(result, "D:/WCData/zq/weibo.txt");
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(result);
			}
		}
		// 随机延时设置
		Random random = new Random();
		try {
			Thread.sleep((Math.abs(random.nextInt()) % 2) * 1000 + 3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static void main(String[] args) throws Exception {
		String uid="2656274875";
		String encrypted_mid="BfPyQj1GS";
		Comment comment =new Comment("crawl");
		/*获取新浪微博的cookie，账号密码以明文形式传输，请使用小号*/  
        String cookie="SUS=SID-5335685568-1422211254-XD-cnsm8-0c16495cbf1cbf5891e760b72d28ac93; path=/; domain=.weibo.com;SUS=SID-5335685568-1422211254-XD-cnsm8-0c16495cbf1cbf5891e760b72d28ac93; path=/; domain=.weibo.com; httponly;SUE=es%3Da393ce486022ead58d74648efb21e11e%26ev%3Dv1%26es2%3D9090eab0d7a6c2a0732c0b25fa9436fd%26rs0%3DYz2sHkGVjONVV6bxkxdh5shPGAyDHeezCPNowwPzfOAgdO5auvqgzxsFHwdF%252FSIQAC3yesGRcZVxhLPE1QG4FmvdQFUOe2okWjv6n4AzylPfdH3STwv6Rl%252BHjdcNW%252BCVx6MxbIw%252FBHrW8tfSn0ez1wTnucL7i4L6T0PC1p1HqAM%253D%26rv%3D0;path=/;domain=.weibo.com;Httponly;SUP=cv%3D1%26bt%3D1422211254%26et%3D1422297654%26d%3Dc909%26i%3Dac93%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D0%26uid%3D5335685568%26name%3Ddfnaskdnf%2540163.com%26nick%3Ddfnaskdnf%26fmp%3D%26lcp%3D;path=/;domain=.weibo.com;SUB=_2A255wUjmDeTxGeNN6FcX-CvJzTSIHXVatz0urDV8PUNbuNAPLXn8kW9ZyUIaltVD10WfSw9Dw5r1YCpLZA..; path=/; domain=.weibo.com; httponly;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WW2hdhmrLDdjuNb9YZar6sp5JpX5K2t; expires=Monday, 25-Jan-16 18:40:54 GMT; path=/; domain=.weibo.com;SUHB=0FQ7TmYeRobowh; expires=Monday, 25-Jan-16 18:40:54 GMT; path=/; domain=.weibo.com;SRT=E.vAfsJqJsiZXsiORtvbbfInmBvXvCvXM4oO8-vnmBBvzvvv4m54_DjXVmPJT1iXfCvvAivOmKvAmLvAmMvXvC*B.vAflW-P9Rc0lR-ykADvnJqiQVbiRVPBtS!r3JZPQVqbgVdWiMZ4siOzu4DbmKPWQJcY!TFspOmM!TePKRZbSdcEpieini49ndDPIJeA7; expires=Wednesday, 22-Jan-25 18:40:54 GMT; path=/; domain=.passport.weibo.com; httponly;SRF=1422211254; expires=Wednesday, 22-Jan-25 18:40:54 GMT; path=/; domain=.passport.weibo.com;ALF=1453747254; expires=Mon, 25-Jan-2016 18:40:54 GMT; path=/; domain=.weibo.com;myuid=5335685568;SinaRot_wb_r_topic=39;UV5PAGE=usr511_179;; UV5=usr319_182;;SSOLoginState=1422211254;;UOR=,,login.sina.com.cn;;_s_tentry=login.sina.com.cn;";  
        HttpRequesterImpl myRequester=(HttpRequesterImpl) comment.getHttpRequester();  
        myRequester.setCookie(cookie);
        /*for(int i=5;i<36;i++){  
			comment.addSeed("http://weibo.cn/comment/hot/"+encrypted_mid+"?rl=2&page="+i);  
        }*/
		for(int i=1;i<40;i++){  
			comment.addSeed("http://weibo.cn/comment/"+encrypted_mid+"?&uid="+uid+"&rl=1&page="+i);  
        }  
		comment.setThreads(1);
		comment.start(1);
	}
}
