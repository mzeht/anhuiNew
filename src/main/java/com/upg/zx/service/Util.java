package com.upg.zx.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

public class Util {

	public static String getDate(String str) {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + str + "MM" + str
				+ "dd");
		return sdf.format(d);
	}

	public static String getDate() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}

	public static String getMatcherStr(String msg, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(msg);
		if (m.find())
			return m.group();
		return null;
	}

	public static String getMatcherStrAll(String msg, String regex) {
		StringBuffer sb = new StringBuffer(msg.length());
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(msg);
		while (m.find())
			sb.append(m.group());
		return sb.toString();
	}

	public static boolean judgeHref(Element eles, String oldTime) {
		oldTime = getMatcherStrAll(oldTime, "\\d");
		if ("".equals(oldTime))
			oldTime = "20100101";
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String currentTime = eles.text();
		String time = "";
		if (currentTime.indexOf("-") > 0)
			time = Util.getMatcherStr(currentTime, "\\d{4}-\\d{1,2}-\\d{1,2}");
		else
			time = Util
					.getMatcherStr(eles.text(),
							"(?<=[\\pZ+\\[]|^)\\d{4}[年/.]\\d{1,2}[月/.]\\d{1,2}(?=[日\\pZ+\\]]|$)");
		if (time == null)
			return false;
		time = Util.cleanTime(time.replaceAll("\\D", "-"));
		try {
			Date b = df.parse(time);
			Date a = df.parse(String.valueOf(oldTime));
			if (b.after(a))
				return true;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static int getPage(int page, int pageSize) {
		if (page == 0)
			return 0;
		else if (page > 0 && page <= pageSize)
			return 1;
		else if (page % pageSize == 0)
			return page / pageSize;
		else
			return page / pageSize + 1;
	}

	public static String cleanTime(String time) {
		StringBuffer sb = new StringBuffer(10);
		String[] str = time.split("-");
		if (str.length == 3) {
			sb.append(str[0]);
			if (str[1].length() == 1) {
				str[1] = "0" + str[1];
			}
			if (str[2].length() == 1) {
				str[2] = "0" + str[2];
			}
			sb.append(str[1]).append(str[2]);
		}
		return sb.toString();
	}
	
	/**
	 * 获得当前服务器IP地址
	 * @return
	 */
	public static String getServerLocalIPAddr() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		return ipAddrStr;
	}

}
