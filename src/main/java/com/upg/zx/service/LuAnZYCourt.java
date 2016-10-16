package com.upg.zx.service;

import java.util.ArrayList;
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
 * 六安市中级人民法院
 * 
 * @author litaotao
 * 
 */
@Service
public class LuAnZYCourt {
	
	@Autowired
	private CourtDao lazyHandl;
	private int testTime = 0;

	public void getUrl() {
//		testTime = getTime();
		HttpClientImp hc = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(lazyHandl);
		String msg = hc.httpGet("gb2312",
				"http://www.lafy.gov.cn/main.php?sub=article&cid=10&page=1",
				"", 80);
		Document doc = Jsoup.parse(msg);
		String time = doc.select("td[align=left]").text();
		if (getMatcherStr(time, "\\d{4}\\D\\d+\\D\\d+") != null) {
			time = getMatcherStr(time, "\\d{4}\\D\\d+\\D\\d+");
		}
		if (ckm.getUpdate(time, "六安市中级人民法院")) {
			ArrayList<String> link = getHref(msg);
			for (int j = 0; j < link.size(); j++) {
				String detail = hc.httpGet("gb2312", link.get(j), "", 80);
				analyHtml(detail);
			}
		}
	}

	private int getTime() {
		CheckSite ck = new CheckSite();
		ck.setDataFrom("六安市中级人民法院");
		ck.setProvince("安徽省");
		String time = lazyHandl.queryForUpdateDate(ck);
		if (StringUtils.isNotEmpty(time)) {
			return Integer.parseInt(time);
		}
		return 0;
	}

	private ArrayList<String> getHref(String content) {
		ChaoFengCourt cf = new ChaoFengCourt();
		ArrayList<String> array = new ArrayList<String>();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("a.news");
		for (int i = 0; i < eles.size(); i++) {
			String text = eles.get(i).text();
			String time = eles.get(i).parent().nextElementSibling().text();
			if (text.contains("开庭")
					&& Integer.parseInt(cf.translationData(time)) >= testTime) {
				array.add("http://www.lafy.gov.cn/"
						+ eles.get(i).getElementsByTag("a").attr("href"));
			}
		}
		return array;
	}

	private void analyHtml(String content) {
		Document doc = Jsoup.parse(content);
		Court court = new Court();
		court.setDataFrom("六安市中级人民法院");
		Elements eles = doc.select("td[align=left] p");
		if (eles.size() > 0) {
			String[] str = eles.html().split("<br />");
			if (str.length > 3) {
				cut(str);
			} else {
				for (int i = 0; i < eles.size(); i++) {
					String info = eles.get(i).text();
					if (getMatcherStr(info, "\\D\\d{4}\\D+\\d+号") != null) {
						court.setCase_no(getMatcherStr(info,
								"\\D\\d{4}\\D+\\d+号"));
					} else if (info.contains("定于")) {
						int startTime = info.indexOf("定于");
						int endTime = info.indexOf("在");
						int endCourt = info.indexOf("公开");
						int startRes = info.indexOf("审理");
						int endRes = info.indexOf("一案");
						if (startTime != -1 && endTime != -1
								&& startTime < endTime) {
							court.setStartTime(info.substring(startTime + 2,
									endTime));
						}
						if (endCourt != -1 && endTime != -1
								&& endTime < endCourt) {
							court.setCourt("六安市中级人民法院"
									+ info.substring(endTime + 1, endCourt));
						}
						if (startRes != -1 && endRes != -1 && startRes < endRes) {
							court.setCase_res(info.substring(startRes + 2,
									endRes));
						}
					}
				}
				lazyHandl.insertCourt(court);
			}
		}
	}

	private void cut(String[] str) {
		ArrayList<Court> list = new ArrayList<Court>();
		for (int i = 0; i < str.length; i++) {
			str[i] = str[i].replaceAll("：", ":");
			String msg = Jsoup.parse(str[i]).text().replaceAll(" ", "");
			Court court = new Court();
			court.setDataFrom("六安市中级人民法院");
			court.setCourt("六安市中级人民法院"
					+ StringUtils.substringBetween(msg, "地点:", "时间:"));
			court.setStartTime(StringUtils.substringBetween(msg, "时间:", "案由:"));
			court.setCase_res(StringUtils.substringBetween(msg, "案由:", "当事人:"));
			court.setPerson(StringUtils.substringAfter(msg, "当事人:"));
			list.add(court);
		}
		lazyHandl.insertCourt(list);
	}

	public String getMatcherStr(String msg, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(msg);
		if (m.find()) {
			return m.group();
		}
		return null;
	}

	public void print(ArrayList<Court> array) {
		for (Court c : array) {
			System.out.println("案由：" + c.getCase_res());
			System.out.println("时间：" + c.getStartTime());
			System.out.println("当事人：" + c.getPerson());
			System.out.println("法庭：" + c.getCourt());
			System.out.println("-------------------------");
		}
	}

	public static void main(String[] args) {
		LuAnZYCourt ls = new LuAnZYCourt();
		ls.getUrl();
	}

}
