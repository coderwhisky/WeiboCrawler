package com.cj.weiboCrawler.user;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;


import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class InfoURL extends DeepCrawler {
	String uid=null;
	String absUrl="";
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
    						System.out.println("\n获取用户资料页面URL---正常---"+absUrl+"\n");
    					}else if(hasAboutURL){
    						absUrl=curBlock.attr("href");
    						System.out.println("\n获取用户资料页面URL---正常---"+absUrl+"\n");
    					}else {
    						//System.out.println("找不到【更多】模块的URL");
    					}
    				}
    				break;
    			}else{
        			isNomal4URL=false;
        			//System.out.println("找不到【更多】模块");
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
        this.addSeed("http://weibo.com/u/"+uid);
        //this.addSeed("http://weibo.com/"+uid);
        this.setThreads(1);
        this.start(1);
        //Thread.sleep(1000*2);
    }
    
    /*public static void main(String[] args) throws Exception{
    	InfoURL infoURL=new InfoURL("crawl");
		HttpRequesterImpl myRequester=(HttpRequesterImpl) infoURL.getHttpRequester();  
        myRequester.setCookie("SINAGLOBAL=946221128106.1172.1406881265019; __utma=15428400.699599543.1409129694.1416893925.1419577094.10; __utmz=15428400.1419577094.10.3.utmcsr=d.weibo.com|utmccn=(referral)|utmcmd=referral|utmcct=/102803; TC-Page-G0=6fdca7ba258605061f331acb73120318; TC-V5-G0=06f20d05fbf5170830ff70a1e1f1bcae; _s_tentry=login.sina.com.cn; Apache=6406244470272.213.1421232896116; ULV=1421232896178:108:5:3:6406244470272.213.1421232896116:1421131380396; TC-Ugrow-G0=1ae767ccb34a580ffdaaa3a58eb208b8; login_sid_t=1ec21254d798fe762d6bdf3d2bc71743; myuid=5308324691; UOR=,,login.sina.com.cn; SUS=SID-2907246291-1421282673-XD-pmric-741e2b5d28496c416fe52df5f8a0ac93; SUE=es%3D56250128e6c43200352207b9d4a83b22%26ev%3Dv1%26es2%3De50fa1e1aabdedce327d41efa17ac83e%26rs0%3DhtFw6txRfWqEWfxapQ9pVut5uKuzlVi%252BfHW2r3qZKfkfS8sPV1AD4viNWM3ne35PaxzHanee5VyV2UXBofubk0kNyojWwIl4UEL4XbUevWmozaHFAUzvB%252F7K3H5lwcQtXzvG783x8vR%252Buwmiffbc1pMkGaBoXGqYFfbMcNXeLxY%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1421282673%26et%3D1421369073%26d%3Dc909%26i%3Dac93%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D0%26uid%3D2907246291%26name%3Dskddyk%2540sina.com%26nick%3D%25E7%2594%25A8%25E6%2588%25B72907246291%26fmp%3D%26lcp%3D2014-08-18%252021%253A59%253A55; SUB=_2A255s30hDeTxGeRH61UT9CjOwj2IHXVayAdprDV8PUNbuNAPLUr2kW9gcsc4fATIPqwtSYrv0hW2W9pdiA..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WF_axwD.LDI_xke3z308QGv5JpX5K2t; ALF=1452818673; SSOLoginState=1421282673; un=skddyk@sina.com; wvr=6");
        myRequester.setUserAgent("");
        infoURL.startCrawler("3230317920");
    }*/

}