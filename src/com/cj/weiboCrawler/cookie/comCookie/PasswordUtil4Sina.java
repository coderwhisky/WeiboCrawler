package com.cj.weiboCrawler.cookie.comCookie;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class PasswordUtil4Sina {
	public static Invocable invoke = null;
	static {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine jsEngine = manager.getEngineByName("javascript");
		// 指定md5加密文件
		// String jsFileName = "resources.qq/password.js";
		String jsFileName = "password4Sina.js";
		Reader reader;
		try {
			InputStream in = PasswordUtil4Sina.class.getClassLoader()
					.getResourceAsStream(jsFileName);
			reader = new InputStreamReader(in);
			jsEngine.eval(reader);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		invoke = (Invocable) jsEngine;
	}

	public static String getPassEncoding(String publicKey, String serverTime,
			String nonce, String password) {
		String pass = null;
		try {
			pass = (String) invoke.invokeFunction("getPassEncoding",
					new Object[] { publicKey, serverTime, nonce, password });
		} catch (Exception e) {
			System.out.println("为登陆密码加密时,出现异常!");
			e.printStackTrace();
		}
		return pass;
	}

	// public static String getHexString(String hexCode) {
	// String pass = null;
	// try {
	// pass = (String) invoke.invokeFunction("hexchar2bin",
	// new Object[] { hexCode });
	// } catch (Exception e) {
	// System.out.println("将字符串转化为16进制时,出现异常!");
	// e.printStackTrace();
	// }
	// return pass;
	// }
}
