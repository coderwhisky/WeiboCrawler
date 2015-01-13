package com.dyk.weiboCrawler.cookie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.dyk.weiboCrawler.utils.other.ShowIp;

public class CNCookieWithProxy {
	public String getSinaCookie(String username, String password,String ip, int port) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		// 设置代理服务器地址
		Proxy proxy = new Proxy();
		proxy.setHttpProxy(ip + ":" + port);
		DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		
		HtmlUnitDriver driver = new HtmlUnitDriver(capabilities);
		driver.setJavascriptEnabled(true);
		driver.get("http://login.weibo.cn/login/");
		

		// 显示实际使用IP
		//ShowIp si =new ShowIp();
		//System.out.println("实际使用的IP："+si.getIp());

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
		for (Cookie cookie : cookieSet) {
			sb.append(new StringBuilder().append(cookie.getName()).append("=")
					.append(cookie.getValue()).append(";").toString());
		}
		String result = sb.toString();
		if (result.contains("gsid_CTandWM")) {
			return result;
		}
		throw new Exception("weibo login failed");
	}
	public static void main(String[] args) throws Exception {
		//CNCookie cnCookie=new CNCookie();
		CNCookieWithProxy weiboCN=new CNCookieWithProxy();
		
		FileReader fr = new FileReader(new File("resources/accounts/accounts2.txt"));
		BufferedReader br = new BufferedReader(fr);
		BufferedWriter bw1 = new BufferedWriter(new FileWriter("resources/accounts/cnCookies.txt", true));
		String user = null;
		int count = 0;
		while ((user = br.readLine()) != null) {
			String[] temp = user.split("\t");
			String username = temp[0];
			String password = temp[1];
			String cookie =weiboCN.getSinaCookie(username,password,"202.108.15.130",9999);
			bw1.write(cookie + "\n");
			System.out.println("第"+(++count)+"个\n"+cookie);
			// 随机延时设置
			Random random = new Random();
			Thread.sleep((Math.abs(random.nextInt()) % 6) * 1000+1000);
		}
		bw1.flush();
		bw1.close();
		br.close();
	}
}
