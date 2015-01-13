package com.dyk.weiboCrawler.user;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;
import cn.edu.hfut.dmic.webcollector.util.Config;


import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dyk.weiboCrawler.utils.fileRW.FileOperation;


public class InfoURL extends DeepCrawler {
	String uid=null;
	String absUrl=null;
	Boolean isNomal4URL=true;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Boolean getIsNomal4URL() {
		return isNomal4URL;
	}

	public void setIsNomal4URL(Boolean isAbnomal) {
		this.isNomal4URL = isAbnomal;
	}

	public String getAbsUrl() {
		return absUrl;
	}

	public void setAbsUrl(String absUrl) {
		this.absUrl = absUrl;
	}

	public InfoURL(String crawlPath) {
		super(crawlPath);
    }

    @Override
    public Links visitAndGetNextLinks(Page page) {
    	String preHtml=page.getHtml();
    	//System.out.println(preHtml);
    	if(preHtml.contains("帐号异常")){
    		System.out.println("\n########### 抱歉，您当前访问的帐号异常，暂时无法访问！ ###########\n");
    		isNomal4URL=false;
    	}else{
    		Document doc = Jsoup.parse(preHtml);
    		Elements scripts=doc.select("script");
    		for(int i=0;i<scripts.size();i++){
    			String scriptHtml=scripts.get(i).html();
    			//System.out.println(scriptHtml);
    			Boolean isInfoScript=scriptHtml.contains("微博等级") && (scriptHtml.contains("info?mod=pedit_more") || scriptHtml.contains("/about"));
    			if(isInfoScript){
    				isNomal4URL=true;
    				String jsonStr=scriptHtml.substring(8, scriptHtml.length()-1);
    				JSONObject jsonObj=JSONObject.fromObject(jsonStr);
    				String innerHtml=jsonObj.getString("html");
    				Document innerDoc=Jsoup.parse(innerHtml);
    				Elements boocksOfA=innerDoc.select("div.PCD_person_info").select("a");
    				for(int j=0;j<boocksOfA.size();j++){
    					Element curBlock=boocksOfA.get(j);
    					Boolean hasInfoURL=curBlock.toString().contains("info?mod=pedit_more");
    					Boolean hasAboutURL=curBlock.toString().contains("/about");
    					if(hasInfoURL){
    						String relativeUrl=curBlock.attr("href");
    						absUrl="http://weibo.com"+relativeUrl;
    						System.out.println("\n获取用户资料页面URL--正常--"+absUrl+"\n");
    					}else if(hasAboutURL){
    						absUrl=curBlock.attr("href");
    						System.out.println("\n获取用户资料页面URL--正常--"+absUrl+"\n");
    					}
    				}
    				break;
    			}else{
        			isNomal4URL=false;
        		}
    		}
    		if(isNomal4URL==false){
    			System.out.println("\n########### 没有找到包含创建时间的script模块！ ###########\n");
    		}
    	}
    	return null;
    }
    public void startCrawler(String uid) throws Exception{
    	setUid(uid);
        this.addSeed("http://weibo.com/"+uid);
        this.setThreads(1);
        this.start(1);
        //Thread.sleep(1000*2);
    }
    
    /*public static void main(String[] args) throws Exception{
    	InfoURL infoURL=new InfoURL("crawl");
		HttpRequesterImpl myRequester=(HttpRequesterImpl) infoURL.getHttpRequester();  
        myRequester.setCookie("SINAGLOBAL=946221128106.1172.1406881265019; __utma=15428400.699599543.1409129694.1416893925.1419577094.10; __utmz=15428400.1419577094.10.3.utmcsr=d.weibo.com|utmccn=(referral)|utmcmd=referral|utmcct=/102803; TC-Page-G0=b1761408ab251c6e55d3a11f8415fc72; TC-V5-G0=31f4e525ed52a18c5b2224b4d56c70a1; _s_tentry=login.sina.com.cn; Apache=5546892299316.823.1420349151396; ULV=1420349151404:104:1:1:5546892299316.823.1420349151396:1419995001841; TC-Ugrow-G0=5eac345e6f37d928953d6a363df28df7; WBtopGlobal_register_version=12daef8d794dec10; login_sid_t=ca7e6b7a76c1f106aa344240991754a0; WBStore=c3bdbfd6d521a8d8|undefined; un=skddyk@sina.com; myuid=2907246291; UOR=,,login.sina.com.cn; SSOLoginState=1420448212; SUS=SID-2907246291-1420448212-XD-l9rmi-b724a8e3d9fc9319c20ce65d2f1aac93; SUE=es%3De6a158477f84b195f1b906e851e671c9%26ev%3Dv1%26es2%3D7d0e1c466cc5406ed88b45cb7c0506fb%26rs0%3DWwhXm9ZyM358gGLWWJYWZpKBLdEDtVB2Pjm6idZPBTg7bGT0dPBFc2tTH0AwXlIz3BtXv7fJa%252FSQM240hOCny5dDPhjRuQ7T9Qpd48ISaZvSTCkI28VoMhYfcIbcvxjVIRLziCb6Pa%252F0WmvzsP5CNXOwFOOKgbrQIQjqbyPBgHg%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1420448212%26et%3D1420534612%26d%3Dc909%26i%3Dac93%26us%3D1%26vf%3D%26vt%3D%26ac%3D%26st%3D0%26uid%3D2907246291%26name%3Dskddyk%2540sina.com%26nick%3D%25E7%2594%25A8%25E6%2588%25B72907246291%26fmp%3D%26lcp%3D2014-08-18%252021%253A59%253A55; SUB=_2A255riGEDeTxGeRH61UT9CjOwj2IHXVbUU_MrDV8PUJbrdAPLUz-kW0Rh-9QyvSr5YoAdlhaJ2f3Hy-O2A..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WF_axwD.LDI_xke3z308QGv5JpX5oz7; wvr=6");
        myRequester.setUserAgent("");
        infoURL.startCrawler("1790597487");
    }*/

}