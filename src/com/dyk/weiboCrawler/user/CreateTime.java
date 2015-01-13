package com.dyk.weiboCrawler.user;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import cn.edu.hfut.dmic.webcollector.util.Config;

import java.io.IOException;
import java.text.ParseException;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dyk.weiboCrawler.utils.time.WeiboTimeUtil;

public class CreateTime extends DeepCrawler {
	
	Boolean isNormal4Time=false;

	String createTime=null;
	long timeStamp=-1;
	String tags=null;
	
	WeiboTimeUtil tu=new WeiboTimeUtil();
	
	
	public Boolean getIsNormal4Time() {
		return isNormal4Time;
	}

	public void setIsNormal4Time(Boolean isNormal) {
		this.isNormal4Time = isNormal;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public CreateTime(String crawlPath) {
		super(crawlPath);
    }

    @Override
    public Links visitAndGetNextLinks(Page page) {
        //System.out.println(page.getHtml());
    	String preHtml=page.getHtml();
    	//String postHtml=StringEscapeUtils.unescapeJavaScript(preHtml);
    	Document doc = Jsoup.parse(preHtml);
    	Elements scripts=doc.select("script");
    	if(scripts.size()<15){
    		isNormal4Time=false;
    		System.out.println("\n########### 页面返回异常！请注意检查原因！ ###########\n"+preHtml);
    	}else{
    		isNormal4Time=true;
    		String scriptHtml=scripts.get(14).html();
    		String jsonStr=scriptHtml.substring(8, scriptHtml.length()-1);
    		
    		if(!jsonStr.contains("注册时间")){
    			System.out.println("\n#########该用户为认证机构，注册时间无法获得！！###########\n");
    			//System.out.println(preHtml);
    		}else{
    			JSONObject jsonObj=JSONObject.fromObject(jsonStr);
    			String innerHtml=jsonObj.getString("html");
    			Document innerDoc=Jsoup.parse(innerHtml);
    			Elements elementsOfLi=innerDoc.select("ul[class=clearfix]").select("li");
    			for(Element ele:elementsOfLi){
    				String content=ele.text();
    				if(content.contains("注册时间")){
    					String oldCreateTime=content.split("：")[1].trim();
    					createTime=tu.getFormatTime(oldCreateTime);
    					try {
    						timeStamp=tu.getTimeStamp(createTime);
    					} catch (ParseException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					System.out.println("\n"+content);
    				}
    				if(content.contains("标签")){
    					tags=content.split("：")[1].trim();
    					System.out.println(content+"\n");
    				}
    			}
    		}
    	}
    	return null;
    }

    /*public void startCrawler(String uid) throws Exception{
        this.addSeed("http://weibo.com/"+uid);
        this.addRegex(".*");
        this.setThreads(1);
        System.out.println("\n启动第一个爬虫！\n");
        this.start(1);
    }*/
    public void startCrawler(String infoURL) throws Exception{
        this.addSeed(infoURL);
        this.setThreads(1);
        System.out.println("\n启动第一个爬虫！\n");
        this.start(1);
    }
    
    /*public static void main(String[] args) throws Exception{
    	CreateTime ceateTime=new CreateTime("crawl");
		HttpRequesterImpl myRequester=(HttpRequesterImpl) ceateTime.getHttpRequester();  
        myRequester.setCookie("SINAGLOBAL=946221128106.1172.1406881265019; __utma=15428400.699599543.1409129694.1416893925.1419577094.10; __utmz=15428400.1419577094.10.3.utmcsr=d.weibo.com|utmccn=(referral)|utmcmd=referral|utmcct=/102803; TC-Page-G0=b1761408ab251c6e55d3a11f8415fc72; TC-V5-G0=31f4e525ed52a18c5b2224b4d56c70a1; _s_tentry=login.sina.com.cn; Apache=5546892299316.823.1420349151396; ULV=1420349151404:104:1:1:5546892299316.823.1420349151396:1419995001841; TC-Ugrow-G0=5eac345e6f37d928953d6a363df28df7; WBtopGlobal_register_version=12daef8d794dec10; login_sid_t=ca7e6b7a76c1f106aa344240991754a0; WBStore=c3bdbfd6d521a8d8|undefined; un=skddyk@sina.com; myuid=2907246291; UOR=,,login.sina.com.cn; SSOLoginState=1420448212; SUS=SID-2907246291-1420448212-XD-l9rmi-b724a8e3d9fc9319c20ce65d2f1aac93; SUE=es%3De6a158477f84b195f1b906e851e671c9%26ev%3Dv1%26es2%3D7d0e1c466cc5406ed88b45cb7c0506fb%26rs0%3DWwhXm9ZyM358gGLWWJYWZpKBLdEDtVB2Pjm6idZPBTg7bGT0dPBFc2tTH0AwXlIz3BtXv7fJa%252FSQM240hOCny5dDPhjRuQ7T9Qpd48ISaZvSTCkI28VoMhYfcIbcvxjVIRLziCb6Pa%252F0WmvzsP5CNXOwFOOKgbrQIQjqbyPBgHg%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1420448212%26et%3D1420534612%26d%3Dc909%26i%3Dac93%26us%3D1%26vf%3D%26vt%3D%26ac%3D%26st%3D0%26uid%3D2907246291%26name%3Dskddyk%2540sina.com%26nick%3D%25E7%2594%25A8%25E6%2588%25B72907246291%26fmp%3D%26lcp%3D2014-08-18%252021%253A59%253A55; SUB=_2A255riGEDeTxGeRH61UT9CjOwj2IHXVbUU_MrDV8PUJbrdAPLUz-kW0Rh-9QyvSr5YoAdlhaJ2f3Hy-O2A..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WF_axwD.LDI_xke3z308QGv5JpX5oz7; wvr=6");
        myRequester.setUserAgent("");
        ceateTime.startCrawler("http://weibo.com/p/1005051790597487/info?mod=pedit_more");
    }*/

}