package com.cj.weiboCrawler.proxy;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class ProxyServer {

	public static void main(String[] args) throws Exception {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// 创建HttpClient
		CloseableHttpClient httpclient = httpClientBuilder.build();
		// 创建HttpClient
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 请求地址
			String url = "http://www.ip138.com/ip2city.asp";
			HttpGet httpGet = new HttpGet(url);
			// 创建代理服务器，依次是代理地址，代理端口号，协议类型
			HttpHost proxy = new HttpHost("202.106.169.228", 8080, "http");
			// 创建请求配置
			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			httpGet.setConfig(config);
			// 获得响应
			CloseableHttpResponse response = httpclient.execute(httpGet);

			System.out.println("----------------------------------------");
			// 获得响应状态：如 HTTP/1.1 200 OK
			System.out.println(response.getStatusLine());
			// 获得响应标头
			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
			}
			System.out.println("----------------------------------------");
			// 获得响应实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				System.out.println("contentEncoding:" + entity.getContentEncoding());
				System.out.println("response content:" + EntityUtils.toString(entity,"gb2312"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
			httpclient.close();
		}
	}
}
