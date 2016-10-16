package com.upg.zx.service;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upg.zx.clientDaoImpl.CheckSiteModule;
import com.upg.zx.clientDaoImpl.HttpClientImp;
import com.upg.zx.dao.CourtDao;
import com.upg.zx.dto.Court;

/**
 * 宿州市中级人民法院
 * 
 * @author litaotao
 * 
 */
@Service
public class SuZhouShiCourt {
	private final String host = "www.szzjfy.com";
	@Autowired
	private CourtDao szHandl;
	private String updatedate;
	
	//取数据
	public void getUrl(){
		for(int i=1;i<=1;i++){
			HttpClientImp hc=new HttpClientImp();
			CheckSiteModule ckm = new CheckSiteModule(szHandl);
			String msg=hc.getTest("utf-8", "http://www.szzjfy.com/gonggao.asp?second_id=10001&page="+i, "", host,"",80);
			ArrayList<String> link=getHref(msg);
			if (!ckm.getUpdate(updatedate, "宿州市中级人民法院")){
	        	break;
	        }
			for(int j=0;j<link.size();j++){
				String htmlInfo=hc.getTest("utf-8", "http://www.szzjfy.com/"+link.get(j), "", host,"",80);
				analyHtmlForDetail(htmlInfo);
			}
		}		
	}
		
		
	//取网址 
	private ArrayList<String> getHref(String content){
		ArrayList<String> linkList= new ArrayList<String>();
		Document doc =null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select("div.list_news_02").get(0).select("a");
		updatedate=doc.select("span.date").get(0).text();
		for(int i=0;i<eles.size();i++){
			linkList.add(eles.get(i).attr("href"));
		}
		return linkList;
	}
	
	
	//切割
	public ArrayList<Court> cutMsg(String content,String no){
		ArrayList<Court> array= new ArrayList<Court>();
		Court court=new Court();
		court.setDataFrom("宿州市中级人民法院");
		court.setCase_no(no);
		int startTime=content.indexOf("于");
		int endTime=content.indexOf("在");
		int endCourt=content.indexOf("公开");
		int startRes=content.indexOf("审理");
        int startRes1=content.indexOf("宣判");
		int endRes=content.indexOf("一案");
		if(startTime!=-1&&endTime!=-1){
			court.setStartTime(content.substring(startTime+1, endTime));
		}
		if(endCourt!=-1&&endTime!=-1){
			court.setCourt(content.substring(endTime+1, endCourt));
		}
		if(startRes!=-1&&endRes!=-1){
			court.setCase_res(content.substring(startRes+2, endRes));
		}else if(startRes1!=-1&&endRes!=-1&&startRes1<endRes){
			court.setCase_res(" "+content.substring(startRes1+2, endRes));
		}
		array.add(court);
		return array;
	}
	
	
	//取数据
	private void analyHtmlForDetail(String content){
		ArrayList<Court> array= new ArrayList<Court>();
		Document doc =null;
		doc=Jsoup.parse(content);
		String msg=doc.select("div.article_02").text();
		String no=doc.select("h1").text();
		array=cutMsg(msg,no);
		szHandl.insertCourt(array);
	}
	
		
	public static void main(String ages[]){
		SuZhouShiCourt ls=new SuZhouShiCourt();
		ls.getUrl();
	}
	
}
