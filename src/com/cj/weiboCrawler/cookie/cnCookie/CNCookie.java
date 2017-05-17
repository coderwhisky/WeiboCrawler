package com.cj.weiboCrawler.cookie.cnCookie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
				sb.append(new StringBuilder().append(cookie.getName()).append("=").append(cookie.getValue()).append(";").toString());
			}
		    String result = sb.toString();
		    if (result.contains("gsid_CTandWM")){
		    	return result;
		    }
		    throw new Exception("weibo login failed");
		  }
	
	/*public static void main(String[] args) throws Exception {
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
			bw1.flush();
			System.out.println("------成功获取第"+(count++)+"个cookie------");
			// 随机延时设置
			Random random = new Random();
			Thread.sleep((Math.abs(random.nextInt()) % 2) * 1000+2000);
		}
		bw1.close();
		br.close();
	}*/
	public ArrayList<String> getCnCookies(String srcFile, String objFile) throws Exception {
		//CNCookie cnCookie=new CNCookie();
		FileReader fr = new FileReader(new File(srcFile));
		BufferedReader br = new BufferedReader(fr);
		BufferedWriter bw = new BufferedWriter(new FileWriter(objFile, true));
		
		//String [] cookies = new String [4];
		ArrayList<String>  cookies = new ArrayList<String> ();
		int count=1;
		String user = null;
		while ((user = br.readLine()) != null) {
			String[] temp = user.split("\t");
			String username = temp[0];
			String password = temp[1];
			String cookie =getSinaCookie(username,password);
			cookies.add(cookie);
			if(count==1){
				Date date=new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = sdf.format(date);
				System.out.println("\n" + time + "------开始更新cookie\n");
				bw.write("\n" + time + "------开始更新cookie\n");
			}
			bw.write(cookie + "\n");
			bw.flush();
			System.out.println("------成功获取第"+(count++)+"个cookie------");
			// 随机延时设置
			Random random = new Random();
			Thread.sleep((Math.abs(random.nextInt()) % 6) * 1000 + 6000);
		}
		bw.close();
		br.close();
		return cookies;
	}
}