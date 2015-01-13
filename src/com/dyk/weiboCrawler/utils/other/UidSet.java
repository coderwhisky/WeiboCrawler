package com.dyk.weiboCrawler.utils.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeSet;

import com.dyk.weiboCrawler.utils.fileRW.FileOperation;

public class UidSet {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// 文件存储对象
		FileOperation fo = new FileOperation();
		FileReader fr = new FileReader(new File("d:/rel_22.txt"));
		BufferedReader br = new BufferedReader(fr);
		TreeSet<String> tr = new TreeSet<>();
		String userId = null;
		// 读取用户ID
		while ((userId = br.readLine()) != null) {
			String left = userId.split("\t")[0];
			String right = userId.split("\t")[1];
			if (!tr.contains(left)) {
				tr.add(left);
				fo.writeTxtFile(left, "D:/WCData/user_id.txt");
			}
			if (!tr.contains(right)) {
				tr.add(right);
				fo.writeTxtFile(right, "D:/WCData/user_id.txt");
			}
		}
		System.out.println("成功写入！");
	}
}
