package com.dyk.weiboCrawler.utils.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeiboTimeUtil {
	
	// 特殊字符
		String splitChar = " ";
		String splitDate = " ";
		String splitNameChar = " ";
		String oriDateSpace = " ";// 2011-04-07 01:22:00
		String formatSpace = " ";
	

	// 时间格式化
	public String getFormatTime(String createtimeStr) {
		String formatTime = null;
		int timeType = createtimeStr.split("-").length;
		// 2014年之前发布的微博：2010-03-12 10:31:51
		if (timeType != 3) {
			// 当天发布的微博：11月07日 17:58
			if (createtimeStr.split(splitDate)[0].indexOf("日") > 0) {
				String left = createtimeStr.split(splitDate)[0];
				String right = createtimeStr.split(splitDate)[1];
				int monthIndex = left.indexOf("月");
				int dayIndex = left.indexOf("日");
				String month = createtimeStr.substring(0, monthIndex);
				String day = createtimeStr.substring(monthIndex + 1, dayIndex);
				String time = right + ":00";
				formatTime = "2014-" + month + "-" + day + formatSpace + time;
				// 今天之前发布的微博：今天 18:19
			} else if (createtimeStr.contains("今天")) {
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String todayDate = sdf.format(date);
				String time = createtimeStr.split(splitDate)[1];
				formatTime = todayDate + formatSpace + time + ":00";
				// 一小时之前发布的微博：6分钟前
			} else {
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String todayDate = sdf.format(date);
				int preMinute = Integer.parseInt(createtimeStr.substring(0, createtimeStr.indexOf("分钟")));
				int hour = date.getHours();
				int curMinute = date.getMinutes();
				int minute = curMinute - preMinute;
				if (minute < 0) {
					hour -= 1;
					minute = (60 + minute);
				}
				String time = hour + ":" + minute + ":00";
				formatTime = todayDate + formatSpace + time;
			}
		} else if(createtimeStr.length()==10){
			formatTime = createtimeStr + formatSpace + "00:00:00";
		} else {
			formatTime = createtimeStr.split(oriDateSpace)[0] + formatSpace + createtimeStr.split(oriDateSpace)[1];
		}
		return formatTime;
	}

	// 格式化时间转时间戳
	public long getTimeStamp(String time) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse(time);
		long timeStamp = date.getTime() / 1000;
		return timeStamp;
	}
}
