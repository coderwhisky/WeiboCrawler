package com.dyk.weiboCrawler.utils.other;

/*转换规则是，url串值从最后往前，每四个字符为一组，作为一个62进制数，然后将各个62进制数转换成对应的10进制数，
再将最终结果连接起来，就是该微博的id。*/
public class MidFormat {
	// static HashMap<Character, Integer> dict = new HashMap<Character,
	// Integer>();
	String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/*	public static void get_dict(String alp) {
			for (int i = 0; i < alp.length(); i++) {
				dict.put(alp.charAt(i), i);
			}
		}*/

	// 62 to 10
	public int key62_to_key10(String str_62) {
		int value = 0;
		for (int i = 0; i < str_62.length(); i++) {
			// value = value * 62 + dict.get(str_62.charAt(i));
			value = value * 62 + alphabet.indexOf(str_62.charAt(i));
		}
		return value;
	}

	public String getMid(String murl) {
		int length = murl.length();
		String mid = "";
		int group = length / 4;
		int last_count = length % 4;
		for (int i = 0; i < group; i++) {
			int value = key62_to_key10(murl.substring(length - (i + 1) * 4, length - i * 4));
			mid = value + mid;
		}
		if (last_count != 0) {
			int value = key62_to_key10(murl.substring(0, length - group * 4));
			mid = value + mid;
		}
		return mid;
	}

	/*public static void main(String[] args) {
		// get_dict(alphabet);
		MidFormat mid = new MidFormat();
		System.out.println(mid.getMid("BvKQk01mq"));// 3374860781248756
	}*/
}
