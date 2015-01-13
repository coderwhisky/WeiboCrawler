package com.dyk.weiboCrawler.proxy;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import java.net.URLEncoder;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * 如果爬虫需要抽取Javascript生成的数据，可以使用HtmlUnitDriver HtmlUnitDriver可以用page.getDriver来生成
 */
public class GetProxysByJS extends DeepCrawler {

	public GetProxysByJS(String crawlPath) {
		super(crawlPath);
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		HtmlUnitDriver driver = page.getDriver(BrowserVersion.CHROME);
		List<WebElement> divInfos = driver
				.findElementsByCssSelector("tr > td:nth-child(3)");
		for (WebElement divInfo : divInfos) {
			System.out.println("---------");
			System.out.println(divInfo.getText());
			System.out.println("---------");
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		GetProxysByJS crawler = new GetProxysByJS("js");
		crawler.addSeed("http://www.xici.net.co/nn/");
		// crawler.addSeed("http://www.xici.net.co/nt/");
		// crawler.addSeed("http://www.xici.net.co/wn/");
		// crawler.addSeed("http://www.xici.net.co/wt/");
		// crawler.addSeed("http://www.xici.net.co/qq/");
		crawler.start(1);
	}
}
