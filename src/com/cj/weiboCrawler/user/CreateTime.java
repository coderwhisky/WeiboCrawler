package com.cj.weiboCrawler.user;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;

import java.text.ParseException;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cj.weiboCrawler.utils.time.WeiboTimeUtil;

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
    			Boolean timeFlag = false;
				Boolean tagFlag = false;
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
    						timeFlag = true;
    					} catch (ParseException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					System.out.println("\n获取注册时间---正常---"+content+"\n");
    				}
    				if(content.contains("标签")){
    					tags=content.split("：")[1].trim();
    					tagFlag = true;
    					System.out.println("\n获取标签---正常---"+content+"\n");
    				}
    			}
    			if(timeFlag == false){
					System.out.println("获取注册时间---异常---\n");
				}
				if(tagFlag == false){
					System.out.println("获取标签---无标签---\n");
				}
    		}
    	}
    	return null;
    }

    public void startCrawler(String infoURL) throws Exception{
        this.addSeed(infoURL);
        this.setThreads(1);
        //System.out.println("\n启动第一个爬虫！\n");
        this.start(1);
    }
    
    /*public static void main(String[] args) throws Exception{
    	CreateTime ceateTime=new CreateTime("crawl");
		HttpRequesterImpl myRequester=(HttpRequesterImpl) ceateTime.getHttpRequester();  
        myRequester.setCookie("SINAGLOBAL=946221128106.1172.1406881265019; __utma=15428400.699599543.1409129694.1416893925.1419577094.10; __utmz=15428400.1419577094.10.3.utmcsr=d.weibo.com|utmccn=(referral)|utmcmd=referral|utmcct=/102803; TC-Page-G0=6fdca7ba258605061f331acb73120318; TC-V5-G0=06f20d05fbf5170830ff70a1e1f1bcae; _s_tentry=login.sina.com.cn; Apache=6406244470272.213.1421232896116; ULV=1421232896178:108:5:3:6406244470272.213.1421232896116:1421131380396; TC-Ugrow-G0=1ae767ccb34a580ffdaaa3a58eb208b8; login_sid_t=1ec21254d798fe762d6bdf3d2bc71743; myuid=5308324691; UOR=,,login.sina.com.cn; SUS=SID-2907246291-1421282673-XD-pmric-741e2b5d28496c416fe52df5f8a0ac93; SUE=es%3D56250128e6c43200352207b9d4a83b22%26ev%3Dv1%26es2%3De50fa1e1aabdedce327d41efa17ac83e%26rs0%3DhtFw6txRfWqEWfxapQ9pVut5uKuzlVi%252BfHW2r3qZKfkfS8sPV1AD4viNWM3ne35PaxzHanee5VyV2UXBofubk0kNyojWwIl4UEL4XbUevWmozaHFAUzvB%252F7K3H5lwcQtXzvG783x8vR%252Buwmiffbc1pMkGaBoXGqYFfbMcNXeLxY%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1421282673%26et%3D1421369073%26d%3Dc909%26i%3Dac93%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D0%26uid%3D2907246291%26name%3Dskddyk%2540sina.com%26nick%3D%25E7%2594%25A8%25E6%2588%25B72907246291%26fmp%3D%26lcp%3D2014-08-18%252021%253A59%253A55; SUB=_2A255s30hDeTxGeRH61UT9CjOwj2IHXVayAdprDV8PUNbuNAPLUr2kW9gcsc4fATIPqwtSYrv0hW2W9pdiA..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WF_axwD.LDI_xke3z308QGv5JpX5K2t; ALF=1452818673; SSOLoginState=1421282673; un=skddyk@sina.com; wvr=6");
        myRequester.setUserAgent("");
        ceateTime.startCrawler("http://weibo.com/p/1005052689778237/info?mod=pedit_more");
    }*/

}