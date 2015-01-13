package com.dyk.weiboCrawler.utils.other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @declare: unicode 帮助类
 * @author: cphmvp
 * @version: 1.0
 * @date: 2014年6月10日上午11:45:34
 */
public class UnicodeUtils {
	public static void main(String[] args) {
		String testStr = "2014&#24180;&#26149;&#22799;&#23395;";
		System.out.println(getStr(testStr));
	}

	/**
	 * @declare:得到可见的字符
	 * @param str
	 *            ：&#24180;
	 * @return 年
	 * @author cphmvp
	 */
	public static String getStr(String str) {
		String regex = "&#(\\w{5});";
		Pattern pa = Pattern.compile(regex);
		String str0 = str;
		Matcher matcher = pa.matcher(str);
		String tmstr0 = null, tmstr = null, zhuanhuanstr = null;
		while (matcher.find()) {
			tmstr0 = matcher.group();
			tmstr = matcher.group(1);
			// System.out.println(tmstr0);
			zhuanhuanstr = (char) Integer.parseInt(tmstr) + "";
			str0 = str0.replace(tmstr0, zhuanhuanstr);
		}
		return str0;
	}

}