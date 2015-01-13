package com.dyk.weiboCrawler.cookie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class CNCookie {
	public String getSinaCookie(String username, String password)throws Exception
		  {
		    StringBuilder sb = new StringBuilder();
		    HtmlUnitDriver driver = new HtmlUnitDriver();
		    driver.setJavascriptEnabled(true);
		    driver.get("http://login.weibo.cn/login/");

		    WebElement mobile = driver.findElementByCssSelector("input[name=mobile]");
		    mobile.sendKeys(new CharSequence[] { username });
		    WebElement pass = driver.findElementByCssSelector("input[name^=password]");
		    pass.sendKeys(new CharSequence[] { password });
		    WebElement rem = driver.findElementByCssSelector("input[name=remember]");
		    rem.click();
		    WebElement submit = driver.findElementByCssSelector("input[name=submit]");
		    submit.click();

		    Set<Cookie> cookieSet = driver.manage().getCookies();
		    driver.close();
		    
		    /*for (Iterator localIterator = cookieSet.iterator(); localIterator.hasNext(); ) { 
		    	Cookie cookie = (Cookie)localIterator.next();
		    	sb.append(new StringBuilder().append(cookie.getName()).append("=").append(cookie.getValue()).append(";").toString());
		    }*/
		    for (Cookie cookie : cookieSet) {
				sb.append(new StringBuilder().append(cookie.getName()).append("=")
						.append(cookie.getValue()).append(";").toString());
			}
		    String result = sb.toString();
		    if (result.contains("gsid_CTandWM")){
		    	return result;
		    }
		    throw new Exception("weibo login failed");
		  }
	
	public static void main(String[] args) throws Exception {
		CNCookie cnCookie=new CNCookie();
		FileReader fr = new FileReader(new File("resources/accounts/accounts2.txt"));
		BufferedReader br = new BufferedReader(fr);
		BufferedWriter bw1 = new BufferedWriter(new FileWriter("resources/accounts/cnCookies.txt", true));
		int count=1;
		String user = null;
		while ((user = br.readLine()) != null) {
			String[] temp = user.split("\t");
			String username = temp[0];
			String password = temp[1];
			String cookie =cnCookie.getSinaCookie(username,password);
			bw1.write(cookie + "\n");
			System.out.println("------成功获取第"+(count++)+"个cookie------");
			// 随机延时设置
			Random random = new Random();
			Thread.sleep((Math.abs(random.nextInt()) % 2) * 1000+1000);
		}
		bw1.flush();
		bw1.close();
		br.close();
	}
}