package com.upg.zx.clientDaoImpl;

import java.util.ArrayList;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.upg.zx.dto.Court;
import com.upg.zx.dto.HttpClientParam;

public class TemplateImp implements ITemplate {

	public  ArrayList<Court> analyzeHtml(String content){
		
		Document doc =null;
		doc=Jsoup.parse(content);
		ArrayList<Court> arrylist = new ArrayList<Court>();
		Elements eles=doc.select("table#tsxxTableId").get(0).getElementsByTag("tr");
		
		for (int i =1; i<eles.size(); i++ ){
			Court court = new Court();
			//法院
			court.setDataFrom(eles.get(i).getElementsByTag("td").get(0).text());
			//法庭
			court.setCourt(eles.get(i).getElementsByTag("td").get(1).text());
			//开庭时间
			court.setStartTime(eles.get(i).getElementsByTag("td").get(2).text());
			//案由
			court.setCase_res(eles.get(i).getElementsByTag("td").get(3).text());
			//法官
			court.setJudge(eles.get(i).getElementsByTag("td").get(4).text());
			//原告
			court.setPlaintiff(eles.get(i).getElementsByTag("td").get(5).text());
			//被告
			court.setDefandant(eles.get(i).getElementsByTag("td").get(6).text());
			arrylist.add(court);
		}

		return arrylist;
	}
	
	public ArrayList<HttpClientParam> SetParamForHttpClient(int page,String courtNo){
		 
		 ArrayList<HttpClientParam> arrylist = new ArrayList<HttpClientParam>();
		 ArrayList array= new ArrayList();

		 int num =6;
		 for (int i=0; i< num; i++){
			 HttpClientParam param = new HttpClientParam();
			 if (i == 0){
				 param.setParamName("tsxx.court_no"); 
				 param.setParamValue(courtNo);
			 }else if (i == 1){
				 param.setParamName("kssj_start"); 
				 param.setParamValue("");
			 }else if (i == 2){
				 param.setParamName("kssj_end"); 
				 param.setParamValue("");
			 }else if (i == 3){
				 param.setParamName("yg"); 
				 param.setParamValue("");
			 }else if (i == 4){
				 param.setParamName("bg"); 
				 param.setParamValue("");
			 }else if (i == 5){
				 param.setParamName("curPage"); 
				 param.setParamValue(String.valueOf(page));
			 }
			 arrylist.add(param);
		 }

		return arrylist;
	 }
	
	/**
	 *得到page数
	 * 
	 *@return int
	 *@author lis
	 **/
	public int getPageByjsoup(String content){
		
		int page=0;
		Document doc =null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select("div.pagination");
		String strPage=eles.select("li.last").get(0).text();
		page=Integer.parseInt(strPage);	
		System.out.println(page);
		return page;		
	}
	
	/**
	 *得到详细链接地址
	 * 
	 *@return ArrayList
	 *@author lis
	 **/
	
	public  ArrayList  getLink(String content){
		
		ArrayList list = new ArrayList();
		Document doc =null;
		doc=Jsoup.parse(content);
		Elements eless=doc.select("div.news-con").select("span");
		String updatedate=eless.get(0).text();
		Elements eles=doc.select("div.news-con").get(0).getElementsByTag("a");
		
		for (int i =0; i < eles.size(); i++){
//			if (eles.get(i).text().indexOf("庭审安排") >-1 ||
//				eles.get(i).text().indexOf("开庭公告") >-1	){
				list.add(eles.get(i).attr("href"));
			//}
		}
		
		return list;
	}
	
	
	public  ArrayList  getLink(String content,Map<String,String> map){
		
		ArrayList list = new ArrayList();
		Document doc =null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select(map.get("tag"));

		for (int i =0; i < eles.size(); i++){
			if (!"".equals(map.get("name")) && map.get("name") !=null){
				if (eles.get(i).getElementsByTag("a").text().contains(map.get("name"))){
					list.add(eles.get(i).getElementsByTag("a").attr("href"));
				}
			}else{
				list.add(eles.get(i).getElementsByTag("a").attr("href"));
			}

		}
		
		return list;
	}
     public  String  getUpdatedate(String content){
		

		Document doc =null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select("table");
		return content;
	
     }
	/**
	 *得到page数(php模板)
	 * 
	 *@return int
	 *@author lis
	 **/
	
	public int getPageforPhptemplate(String content,String tag){
		
		int page=0;
		Document doc =null;
		doc=Jsoup.parse(content);

		Elements eles=doc.select(tag);
		
		String strPage=doc.select(tag).text();
		int index=strPage.indexOf("共");
		strPage=strPage.substring(index+1);
		int indexend=strPage.indexOf("页");
		strPage=strPage.substring(0,indexend);
		page=Integer.parseInt(strPage);	
		System.out.println(page);
		return page;		
	}
	
}
