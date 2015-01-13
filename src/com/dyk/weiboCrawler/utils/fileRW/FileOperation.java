package com.dyk.weiboCrawler.utils.fileRW;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

public class FileOperation {

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean createFile(File fileName) throws Exception {
		boolean flag = false;
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 读TXT文件内容
	 * 
	 * @param fileName
	 * @return
	 */
	public String readTxtFile(File fileName) throws Exception {
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			try {
				String read = null;
				while ((read = bufferedReader.readLine()) != null) {
					if (read.length() != 0) {
						result = result + read + "\r\n";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}
		System.out.println("读取出来的文件内容是：" + "\r\n" + result);
		return result;
	}

	/**
	 * 写入TXT文件内容,不覆盖
	 * 
	 * @param content
	 * @param fileName
	 * @return
	 */
	public boolean writeTxtFile(String content, String fileName) throws Exception {
		boolean flag = false;
		try {
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(fileName, true));
			bw1.write(content + "\n");
			bw1.flush();
			bw1.close();
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
		/*
		 * RandomAccessFile mm = null; boolean flag = false; FileOutputStream o
		 * = null; try { o = new FileOutputStream(new File(fileName));
		 * o.write(content.getBytes("GBK")); o.close(); // mm=new
		 * RandomAccessFile(fileName,"rw"); // mm.writeBytes(content); flag =
		 * true; } catch (Exception e) { // TODO: handle exception
		 * e.printStackTrace(); } finally { if (mm != null) { mm.close(); } }
		 */
	}
	
	/**
	 * 写入TXT文件内容,覆盖
	 * 
	 * @param content
	 * @param fileName
	 * @return
	 */
	public boolean writeTxtFile(String content, String fileName,Boolean ifOverWrite) throws Exception {
		boolean flag = false;
		try {
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(fileName, ifOverWrite));
			bw1.write(content + "\n");
			bw1.flush();
			bw1.close();
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public void contentToTxt(String filePath, String content) {
		String str = new String(); // 原有txt内容
		String s1 = new String();// 内容更新
		try {
			File f = new File(filePath);
			if (f.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				f.createNewFile();// 不存在则创建
			}
			BufferedReader input = new BufferedReader(new FileReader(f));

			while ((str = input.readLine()) != null) {
				s1 += str + "\n";
			}
			System.out.println(s1);
			input.close();
			s1 += content;

			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(s1);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public List getParaList(String fileName) throws IOException {
		FileReader fr = new FileReader(new File(fileName));
		BufferedReader br = new BufferedReader(fr);
		List list = new List();
		String line = null;
		while ((line = br.readLine()) != null) {
			list.add(line);
		}
		br.close();
		return list;
	}

	public LinkedHashMap<String, Integer> getProxyMap(String fileName) throws NumberFormatException, IOException {
		FileReader fr = new FileReader(new File(fileName));
		BufferedReader br = new BufferedReader(fr);
		LinkedHashMap<String, Integer> hm = new LinkedHashMap<>();
		String line = null;
		while ((line = br.readLine()) != null) {
			String ip = line.split(":")[0];
			int port = Integer.parseInt(line.split(":")[1]);
			hm.put(ip, port);
		}
		br.close();
		return hm;
	}

	/*public static void main(String[] args) throws IOException, Exception { //
		// 读取cookie
		FileOperation fo = new FileOperation();
		LinkedHashMap<String, Integer> proxy = fo.getProxyMap("proxy.txt");
		Object[] ips = proxy.keySet().toArray();
		System.out.println(String.valueOf(ips[0]));
		Object ip = null;
		Iterator key = proxy.keySet().iterator();
		while (key.hasNext()) {
			ip = key.next();
			System.out.println("ip:" + ip + "port:" + proxy.get(ip));
		}
		fo.writeTxtFile("dd", "D:/test.txt");
	}*/

}