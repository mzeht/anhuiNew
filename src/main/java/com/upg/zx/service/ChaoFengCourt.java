package com.upg.zx.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upg.zx.clientDaoImpl.CheckSiteModule;
import com.upg.zx.clientDaoImpl.HttpClientImp;
import com.upg.zx.dao.CourtDao;
import com.upg.zx.dto.CheckSite;
import com.upg.zx.dto.Court;

/**
 * 长丰法院网
 * 
 * @author litaotao
 * 
 */
@Service
public class ChaoFengCourt {
	
	@Autowired
	private CourtDao cfHandl;
	private int testTime = 0;
	

	public void getUrl() {
		testTime = getTime();
		HttpClientImp hc = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(cfHandl);
		String msg = hc.httpGet("utf-8",
				"http://fy.changfeng.gov.cn/ktgg/index.jsp?pageNumber=2", "",
				80);
		Document doc = Jsoup.parse(msg);
		String time = doc.select("div.list span").text();
		if (getMatcherStr(time, "\\d+\\D\\d+\\D\\d+\\D") != null) {
			time = getMatcherStr(time, "\\d+\\D\\d+\\D\\d+\\D");
		}
		if (ckm.getUpdate(time, "长丰法院网")) {
			ArrayList<String> link = getHref(msg);
			for (int j = 0; j < link.size(); j++) {
				System.out.println("第" + j + "个");
				String detail = hc.httpGet("utf-8", link.get(j),
						"", 80);
				System.out.println(link.get(j));
				analyHtml(detail);
			}
		}
	}

	private int getTime() {
		CheckSite ck = new CheckSite();
		ck.setDataFrom("长丰法院网");
		ck.setProvince("安徽省");
		String time = cfHandl.queryForUpdateDate(ck);
		if(StringUtils.isNotEmpty(time)){
			return Integer.parseInt(time);
		}
		return 0;
	}

	private ArrayList<String> getHref(String content) {
		ArrayList<String> array = new ArrayList<String>();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("div.list [href~=\\?articleId=\\d+]");
		for (int i = 0; i < eles.size(); i++) {
			String time = eles.get(i).previousElementSibling().text();
			if(Integer.parseInt(translationData(time))>=testTime){
				array.add(eles.get(i).attr("href"));
			}
		}
		return array;
	}

	private void analyHtml(String content) {
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("article#info_content");
		if (doc.select("article#info_content p.MsoNormal").size() > 0) {
			eles = doc.select("article#info_content p.MsoNormal");
			cut1(eles);
		} else if (doc.select("article#info_content p").size() > 0) {
			eles = doc.select("article#info_content p");
			cut2(eles);
		}

	}

	private void cut1(Elements eles) {
		ArrayList<Court> list = new ArrayList<Court>();
		Court court = new Court();
		court.setDataFrom("长丰法院网");
		boolean flag = false;
		for (int i = 0; i < eles.size(); i++) {
			String info = eles.get(i).text().replaceAll("\\s", "")
					.replaceAll(" ", "").replace(":", "：");
			if (info.contains("案号")) {
				court.setCase_no(StringUtils.substringAfter(info, "："));
			} else if (info.contains("案由")) {
				court.setCase_res(StringUtils.substringAfter(info, "："));
			} else if (info.contains("当事人")) {
				court.setPerson(StringUtils.substringAfter(info, "："));
			} else if (info.contains("原告") && !info.contains("被告")) {
				court.setPlaintiff(StringUtils.substringAfter(info, "："));
			} else if (info.contains("被告") && !info.contains("原告")) {
				court.setDefandant(StringUtils.substringAfter(info, "："));
			} else if (info.contains("地点")) {
				court.setCourt(StringUtils.substringAfter(info, "："));
				if (court.getStartTime() != null) {
					flag = true;
					;
				}
			} else if (info.contains("时间")) {
				court.setStartTime(StringUtils.substringAfter(info, "："));
				if (court.getCourt() != null) {
					flag = true;
					;
				}
			}
			if (flag) {
				list.add(court);
				court = new Court();
				court.setDataFrom("长丰法院网");
				flag = false;
			}
		}
		cfHandl.insertCourt(list);
	}

	private void cut2(Elements eles) {
		ArrayList<Court> list = new ArrayList<Court>();
		Court court = new Court();
		court.setDataFrom("长丰法院网");
		boolean flag = false;
		for (int i = 0; i < eles.size(); i++) {
			if (eles.get(i).html().contains("<br />")) {
				String msg[] = eles.get(i).html().split("<br />");
				for (int j = 0; j < msg.length; j++) {
					String info = Jsoup.parse(msg[j]).text().replace(":", "：");
					if (info.contains("案号")) {
						court.setCase_no(StringUtils.substringAfter(info, "："));
					} else if (info.contains("案由")) {
						court.setCase_res(StringUtils.substringAfter(info, "："));
					} else if (info.contains("原告")) {
						court.setPlaintiff(StringUtils
								.substringAfter(info, "："));
					} else if (info.contains("被告")) {
						court.setDefandant(StringUtils
								.substringAfter(info, "："));
					} else if (info.contains("地点")) {
						court.setCourt(StringUtils.substringAfter(info, "：")
								+ "");
						if (court.getStartTime() != null) {
							flag = true;
							;
						}
					} else if (info.contains("时间")) {
						court.setStartTime(StringUtils
								.substringAfter(info, "："));
						if (court.getCourt() != null) {
							flag = true;
							;
						}
					}
					if (flag) {
						list.add(court);
						court = new Court();
						court.setDataFrom("长丰法院网");
						flag = false;
					}
				}
			}
		}
		cfHandl.insertCourt(list);
	}

	public String getMatcherStr(String msg, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(msg);
		if (m.find()) {
			return m.group();
		}
		return null;
	}

	public String translationData(String strDate) {
		Date date = null;
		String dateString = "";
		if(getMatcherStr(strDate,"\\d{4}[-.年]\\d+\\D\\d+日?")!=null){
			strDate = getMatcherStr(strDate,"\\d{4}[-.年]\\d+\\D\\d+日?");
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
				if(strDate.contains("日")){
					date = format7.parse(strDate);
					dateString = format2.format(date);
				}else {
					date = format7.parse(strDate+"日");
					dateString = format2.format(date);
				}
			}else {
				return strDate;
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return dateString;
	}
	
	

	public static void main(String[] args) {
		ChaoFengCourt ls = new ChaoFengCourt();
		ls.getUrl();
	}

}
