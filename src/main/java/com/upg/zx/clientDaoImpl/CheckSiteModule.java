package com.upg.zx.clientDaoImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.upg.zx.dao.CourtDao;
import com.upg.zx.dto.CheckSite;

public class CheckSiteModule {
	private final String province = "安徽省";

	public CourtDao daoHandl;

	public CheckSiteModule(CourtDao daoHandl) {
		this.daoHandl = daoHandl;
	}

	/**
	 * 取得明细页面url
	 * 
	 * @author lisheng
	 * 
	 */
	public boolean getUpdate(String content, String strCourt) {
		CheckSite obj = new CheckSite(province, strCourt);
		boolean flag = false;
		CheckSite court = new CheckSite();
		String updateDate = content;
		if (!"".equals(content) || content != null) {
			daoHandl.insertTime(obj);
			if (content.indexOf("(") > -1) {
				updateDate = StringUtils.substringBetween(content, "(", ")");
			} else if (content.indexOf("[") > -1) {
				updateDate = StringUtils.substringBetween(content, "[", "]");
			}
			updateDate = translationData(updateDate);
			court.setDataFrom(strCourt);
			court.setProvince(province);
			String oldDate = daoHandl.queryForUpdateDate(court);
			if (!"".equals(oldDate) && oldDate != null) {
				if (Integer.parseInt(updateDate) > Integer.parseInt(oldDate)) {
					court.setUpdateDate(updateDate);
					daoHandl.insertUpdateDate(court);
					flag = true;
				}
			} else {
				court.setUpdateDate(updateDate);
				daoHandl.insertUpdateDate(court);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 取得明细页面url
	 * 
	 * @author lisheng
	 * 
	 */
	public boolean getUpdateForpage(String page, String strCourt) {
		CheckSite obj = new CheckSite(province, strCourt);
		boolean flag = false;
		CheckSite court = new CheckSite();
		String updateDate = page;
		court.setDataFrom(strCourt);
		court.setProvince(province);
		if(StringUtils.isNotEmpty(page)){
			daoHandl.insertTime(obj);
		}
			
		String oldDate = daoHandl.queryForUpdateDate(court);
		if (!"".equals(oldDate) && oldDate != null) {
			if (Integer.parseInt(updateDate) > Integer.parseInt(oldDate)) {
				court.setUpdateDate(updateDate);
				daoHandl.insertUpdateDate(court);
				
				flag = true;
			}
		} else {
			court.setUpdateDate(updateDate);
			daoHandl.insertUpdateDate(court);
		}

		return flag;
	}

	public String translationData(String strDate) {
		Date date = null;
		String dateString = "";
		if (getMatcherStr(strDate, "\\d{4}\\D\\d+\\D\\d+\\D?") != null) {
			strDate = getMatcherStr(strDate, "\\d{4}\\D\\d+\\D\\d+\\D?");
		}
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
		DateFormat format3 = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		DateFormat format4 = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat format6 = new SimpleDateFormat("yyyy.MM.dd");
		DateFormat format7 = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			if (strDate.contains("-")) {
				date = format1.parse(strDate);
				dateString = format2.format(date);
			} else if (strDate.contains(":") && strDate.contains("/")) {
				date = format3.parse(strDate);
				dateString = format2.format(date);
			} else if (strDate.contains("/")) {
				date = format4.parse(strDate);
				dateString = format2.format(date);
			} else if (strDate.contains(":") && strDate.contains("-")) {
				date = format5.parse(strDate);
				dateString = format2.format(date);
			} else if (strDate.contains(".")) {
				date = format6.parse(strDate);
				dateString = format2.format(date);
			} else if (strDate.contains("年")) {
				date = format7.parse(strDate);
				dateString = format2.format(date);
			} else {
				return strDate;
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return dateString;
	}

	private String getMatcherStr(String msg, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(msg);
		if (m.find()) {
			return m.group();
		}
		return null;
	}

}
