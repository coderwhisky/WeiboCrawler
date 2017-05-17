package com.cj.tianya;

import java.util.List;

import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;

import org.jsoup.nodes.Document;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.cj.weiboCrawler.utils.fileRW.FileOperation;
import com.gargoylesoftware.htmlunit.BrowserVersion;

public class Tianya extends DeepCrawler {
	public String title = null;
	public String author = null;
	public String date = null;
	public String content = null;
	public String result = null;

	public Tianya(String crawlPath) {
		super(crawlPath);
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		Document doc = page.getDoc();
		String title = doc.title();
		System.out.println("\nURL:" + page.getUrl() + "\n标题:" + title + "\n");

		HtmlUnitDriver driver = page.getDriver(BrowserVersion.CHROME);

		List<WebElement> infos = driver
				.findElementsByCssSelector("div.atl-info");
		List<WebElement> comments = driver
				.findElementsByCssSelector("div[class=bbs-content]");
		// System.out.println("\n"+infos.get(0).getText()+"\n");
		System.out.println(comments.size());
		for (int i = 1; i < comments.size(); i++) {
			String info = infos.get(i + 1).getText();
			author = info.substring(3, info.indexOf("时间：") - 1);
			date = info.substring(info.indexOf("时间：") + 3);
			content = comments.get(i).getText().replace("\n", "。");
			result = content + "	" + date + "	" + author;
			try {
				new FileOperation().writeTxtFile(result,
						"D:/WCData/zq/tianya2.txt");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(result);
			System.out
					.println("-----------------------------------------------");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		Tianya crawler = new Tianya("crawl_tianya");
		HttpRequesterImpl myRequester = (HttpRequesterImpl) crawler
				.getHttpRequester();
		myRequester
				.setCookie("__cid=1; __guid=1762862575; __guid2=1762862575; time=ct=1422253551.468; __ptime=1422253552267; Hm_lvt_bc5755e0609123f78d0e816bf7dee255=1421999385,1422247092; Hm_lpvt_bc5755e0609123f78d0e816bf7dee255=1422253552; ds_ids=27fF");
		crawler.setThreads(1);
		for (int i = 10; i < 79; i++) {
			crawler.addSeed("http://bbs.tianya.cn/post-news-309752-" + i
					+ ".shtml");
		}
		crawler.setResumable(false);
		crawler.start(1);
	}

}
