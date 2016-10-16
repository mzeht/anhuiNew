package com.upg.zx.service;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upg.zx.clientDaoImpl.CheckSiteModule;
import com.upg.zx.clientDaoImpl.HttpClientImp;
import com.upg.zx.dao.CourtDao;
import com.upg.zx.dto.Court;
import com.upg.zx.dto.HttpClientParam;

/**
 * 合肥市中级人民法院
 * 
 * @author litaotao
 * 
 */
@Service
public class HeFeiShiCourt {
	private final String host = "court.hefei.gov.cn";
	@Autowired
	private CourtDao hfHandl;
	private String updatedate;

	private HttpClientImp httpclientHandl;

	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}

	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}

	// 取得数据
	public void getUrl() {
		HttpClientImp hc = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(hfHandl);
		ArrayList<HttpClientParam> paramList = new ArrayList<HttpClientParam>();
		String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
		String detail = hc.getTest("utf-8", "http://court.hefei.gov.cn/channels/17.html", "", host,"",80);
		for (int page = 1; page <= 1; page++) {
			paramList = SetParamForHttpClient(page, detail);
			String msg = hc.gethtmlByPost(
					"http://court.hefei.gov.cn/sitefiles/services/cms/dynamic/output.aspx?publishmentSystemID=1&",
					"utf-8", "", host, accept, paramList);
			ArrayList linkList = analyHtml(msg);
			if (!ckm.getUpdate(updatedate, "合肥市中级人民法院")) {
				break;
			}
			for (int i = 0; i < linkList.size(); i++) {
				System.out.println(linkList.get(i));
				String detailHtml = hc.getTest("utf-8", "http://court.hefei.gov.cn/" + linkList.get(i), "", host,"",80);
				// String detailHtml=hc.gethtmlByGet("utf-8",
				// "http://court.hefei.gov.cn/contents/17/116.html", "",
				// host,"115.159.5.247",8080);

				// 得到了包括法庭等在内的有用信息
				analyDetail(detailHtml);

			}
		}
	}

	// 切割参数
	public String[] cut(String detail) {
		Document doc = null;
		doc = Jsoup.parse(detail);
		Elements eles = doc.select("script");
		String noval = eles.get(5).html();// 写死的只能取到第五条script!!
		int indexStart = noval.indexOf("pageNodeID");
		int indexEnd = noval.indexOf("if (pageNum && pageNum > 0)");
		if (indexStart != -1 & indexEnd != -1)
			;
		{
			String url = noval.substring(indexStart, indexEnd);
			url = url.replace("\";", "");
			String[] parameter = url.split("&");
			return parameter;

		}
	}

	// 传参数
	public ArrayList<HttpClientParam> SetParamForHttpClient(int page, String parameter) {
		String para[] = cut(parameter);
		ArrayList<HttpClientParam> arrylist = new ArrayList<HttpClientParam>();
		for (int i = 0; i < para.length + 1; i++) {
			HttpClientParam param = new HttpClientParam();
			if (i == 0) {
				param.setParamName("pageNodeID");
				param.setParamValue(para[0].split("=")[1]);
			} else if (i == 1) {
				param.setParamName("pageContentID");
				param.setParamValue(para[1].split("=")[1]);
			} else if (i == 2) {
				param.setParamName("pageTemplateID");
				param.setParamValue(para[2].split("=")[1]);
			} else if (i == 3) {
				param.setParamName("isPageRefresh");
				param.setParamValue(para[3].split("=")[1]);
			} else if (i == 4) {
				param.setParamName("pageUrl");
				param.setParamValue(para[4].split("=")[1]);
			} else if (i == 5) {
				param.setParamName("ajaxDivID");
				param.setParamValue(para[5].split("=")[1]);
			} else if (i == 6) {
				param.setParamName("templateContent");
				param.setParamValue(para[6].split("=")[1]);
			} else if (i == 7) {
				param.setParamName("pageNum");
				param.setParamValue(String.valueOf(page));
			}
			arrylist.add(param);
		}
		return arrylist;
	}

	// 取网址
	private ArrayList analyHtml(String content) {
		ArrayList array = new ArrayList();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("span.wm_title").select("a");
		updatedate = doc.select("span").get(0).text();
		for (int i = 0; i < eles.size(); i++) {
			array.add(eles.get(i).attr("href"));
		}
		return array;
	}

	// 分析细节
	private void analyDetail(String content) {
		ArrayList<Court> array = new ArrayList<Court>();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("div#endText p");
		Court court = new Court();
		court.setDataFrom("合肥市中级人民法院 ");
		boolean flag = false;
		if (eles.size() > 0) {
			for (Element ele : eles) {
				String msg = ele.text().replaceAll("\\s", "").replaceAll("　", "").replaceAll("：", ":");
				if (msg.split(":").length > 0) {
					if (msg.contains("案号")) {
						court.setCase_no(msg.split(":")[1]);
					} else if (msg.contains("当事人")) {
						court.setPerson(msg.split("当事人:")[1]);
					} else if (msg.contains("案由")) {
						court.setCase_res(msg.split(":")[1]);
					} else if (msg.contains("地点")) {
						court.setCourt(msg.split(":")[1]);
					} else if (msg.contains("日期")) {
						court.setStartTime(msg.split("日期:")[1]);
						flag = true;
					}
					if (flag) {
						array.add(court);
						court = new Court();
						court.setDataFrom("合肥市中级人民法院 ");
						flag = false;
					}
				}
			}
		}
		
		hfHandl.insertCourt(array);
	}

	public static void main(String[] args) {
		HeFeiShiCourt a = new HeFeiShiCourt();
		a.getUrl();
	}

}
