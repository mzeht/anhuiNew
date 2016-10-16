package com.upg.zx.service;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upg.zx.clientDaoImpl.CheckSiteModule;
import com.upg.zx.clientDaoImpl.HttpClientImp;
import com.upg.zx.clientDaoImpl.TemplateImp;
import com.upg.zx.dao.CourtDao;
import com.upg.zx.dto.Court;

/**
 * 滁州市中级人民法院
 * 
 * @author lisheng
 */
@Service
public class CzzyCourt {

	private final static String HOST = "www.czfy.gov.cn";
	private String updatedate;

	@Autowired
	private CourtDao czzyHandl;



	private TemplateImp templateHandl;
	private HttpClientImp httpclientHandl;

	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}

	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}

	public TemplateImp getTemplateHandl() {
		return templateHandl;
	}

	public void setTemplateHandl(TemplateImp templateHandl) {
		this.templateHandl = templateHandl;
	}

	public void getInfo() {

		HttpClientImp httpclientHandl = new HttpClientImp();
		TemplateImp templateHandl = new TemplateImp();
		CheckSiteModule ckm = new CheckSiteModule(czzyHandl);
		ArrayList linkList = new ArrayList();
		// boolean flag = false;
		String response = "";
		for (int i = 7; i > 1; i--) {
			// if (!flag) {
			// response = httpclientHandl.getTest("utf-8",
			// "http://www.czfy.gov.cn/html/ktgg/index.html", "", HOST, "",
			// 80);
			// flag = true;
			//
			// } else {
			String url = "";
			if (i != 1) {
				url = "http://www.czfy.gov.cn/html/ktgg/index_" + i + ".html";
			} else {
				url = "http://www.czfy.gov.cn/html/ktgg/index.html";
			}

			response = httpclientHandl.getTest("utf-8", url, "", HOST, "", 80);

			// HashMap<String,String> hashMap = new
			// HashMap<String,String>();
			// hashMap.put("tag","a.a6");
			// hashMap.put("name","字第");
			// linkList= templateHandl.getLink(response,hashMap);
			linkList = getLink(response);
			if (!ckm.getUpdate(updatedate, "滁州市中级人民法院")) {
				break;
			}
			for (int j = 0; j < linkList.size(); j++) {
				ArrayList<Court> list = new ArrayList<Court>();

				String detailResponse = httpclientHandl.getTest("utf-8", String.valueOf(linkList.get(j)), "", HOST, "",
						80);
				list = getDetailContent(detailResponse);
			
				if(list.get(0).getStartTime()!=null&&list.get(0)!=null){
					czzyHandl.insertCourt(list);
				}
				
			}
		}
	}

	private ArrayList<Court> getDetailContent(String content) {

		ArrayList<Court> list = new ArrayList<Court>();
		Document doc = null;
		content = content.replace("<br />", "@");
		doc = Jsoup.parse(content);
		Elements eles = doc.select("td.a6").get(0).getElementsByTag("div");
		String title = doc.select("td.a8").text();

		boolean flag = false;
		String detail = "";
		int index;
		if (eles.size() > 0) {
			flag = true;
		} else {
			detail = doc.select("td.a6").get(0).text();
		}
		Court court = new Court();
		if (!flag) {

			String result[] = detail.split("@");
			for (int j = 0; j < result.length; j++) {
				if (j == 0) {
					if (!result[j].contains("案　号")) {
						court.setCase_no(title);
						continue;
					}
				}
				if (result[j].contains("案　号")) {
					index = result[j].indexOf("案　号");
					court.setCase_no(result[j].substring(index + 4));
				} else if (result[j].contains("案　由")) {
					index = result[j].indexOf("案　由");
					court.setCase_res(result[j].substring(index + 4));
				} else if (result[j].contains("当事人")) {
					index = result[j].indexOf("当事人");
					court.setPerson(result[j].substring(index + 4));
				} else if (result[j].contains("被　告")) {
					index = result[j].indexOf("被　告");
					court.setDefandant(result[j].substring(index + 4));
				} else if (result[j].contains("被上诉人")) {
					index = result[j].indexOf("被上诉人");
					court.setDefandant(result[j].substring(index + 4));
				} else if (result[j].contains("日　期")) {
					index = result[j].indexOf("日　期");
					court.setStartTime(result[j].substring(index + 4));
				} else if (result[j].contains("地　点")) {
					index = result[j].indexOf("地　点 ");
					court.setCourt(result[j].substring(index + 5));
				} else if (result[j].contains("审判长 ")) {
					index = result[j].indexOf("审判长");
					court.setJudge(result[j].substring(index + 4));
				} else if (result[j].contains("上诉人")) {
					index = result[j].indexOf("上诉人");
					court.setPlaintiff(result[j].substring(index + 4));
				} else if (result[j].contains("上诉人")) {
					index = result[j].indexOf("上诉人");
					court.setPlaintiff(result[j].substring(index + 4));
				} else if (result[j].contains("原　告")) {
					index = result[j].indexOf("原　告");
					court.setPlaintiff(result[j].substring(index + 4));
				}
			}
		} else {
			for (int j = 0; j < eles.size(); j++) {
				if (eles.get(j).text().contains("案") && eles.get(j).text().contains("由")) {
					index = eles.get(j).text().indexOf("案　由");
					court.setCase_res(eles.get(j).text().substring(index + 4));
				}
				if (j == 0) {
					if (!eles.get(j).text().contains("案　号")) {
						String ti = title.substring(title.indexOf("字第") - 10, title.indexOf("字第") + 8);
						court.setCase_no(ti);
						continue;
					}
				}

				if (eles.get(j).text().contains("案　号")) {
					index = eles.get(j).text().indexOf("案　号");
					court.setCase_no(eles.get(j).text().substring(index + 4));
				} else if (eles.get(j).text().contains("案　由")) {
					index = eles.get(j).text().indexOf("案　由");
					court.setCase_res(eles.get(j).text().substring(index + 4));
				} else if (eles.get(j).text().contains("被　告")) {
					index = eles.get(j).text().indexOf("被　告");
					court.setDefandant(eles.get(j).text().substring(index + 4));
				} else if (eles.get(j).text().contains("被上诉人")) {
					index = eles.get(j).text().indexOf("被上诉人");
					court.setDefandant(eles.get(j).text().substring(index + 4));
				} else if (eles.get(j).text().contains("日　期")) {
					index = eles.get(j).text().indexOf("日　期");
					court.setStartTime(eles.get(j).text().substring(index + 4));
				} else if (eles.get(j).text().contains("地　点")) {
					index = eles.get(j).text().indexOf("地　点 ");
					court.setCourt(eles.get(j).text().substring(index + 5));
				} else if (eles.get(j).text().contains("审判长")) {
					index = eles.get(j).text().indexOf("审判长");
					court.setJudge(eles.get(j).text().substring(index + 4));
				} else if (eles.get(j).text().contains("上诉人")) {
					index = eles.get(j).text().indexOf("上诉人");
					court.setPlaintiff(eles.get(j).text().substring(index + 4));
				} else if (eles.get(j).text().contains("当事人")) {
					index = eles.get(j).text().indexOf("当事人");
					court.setPerson(eles.get(j).text().substring(index + 4));
				} else if (eles.get(j).text().contains("上诉人")) {
					index = eles.get(j).text().indexOf("上诉人");
					court.setPlaintiff(eles.get(j).text().substring(index + 4));
				} else if (eles.get(j).text().contains("原　告")) {
					index = eles.get(j).text().indexOf("原　告");
					court.setPlaintiff(eles.get(j).text().substring(index + 4));
				}
			}
		}
		court.setDataFrom("滁州市中级人民法院");
		list.add(court);
		System.out.println(court.toString());

		return list;
	}

	/**
	 * 得到详细链接地址
	 * 
	 * @return ArrayList
	 * @author lis
	 **/

	public ArrayList<String> getLink(String content) {

		ArrayList<String> list = new ArrayList<String>();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements getTime = doc.select("table[width=745] tr td");
		updatedate = getTime.get(2).text();
		Elements eles = doc.select("table[width=745] tr td").select("a");
		for (int i = 0; i < eles.size(); i++) {
			if (eles.get(i).text().indexOf("案　号") > -1 || eles.get(i).text().indexOf("字第") > -1) {
				list.add(eles.get(i).attr("href"));
			} else if (eles.get(i).text().indexOf("案　号") > -1 || eles.get(i).text().indexOf("皖") > -1) {
				list.add(eles.get(i).attr("href"));
			}

		}
		return list;

	}

	public static void main(String[] args) {
		CzzyCourt court = new CzzyCourt();
		court.getInfo();
	}

}
