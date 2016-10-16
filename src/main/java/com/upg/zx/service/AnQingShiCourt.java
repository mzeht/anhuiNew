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
 * 安庆市中级人民法院
 * @author litaotao
 * 
 */
@Service
public class AnQingShiCourt {
	private  final String host ="www.aqzy.gov.cn";
	@Autowired
	private CourtDao aqsHandl;
	private HttpClientImp  httpclientHandl;
	private String updatedate="";

	
	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}
	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}
	
	
	public void getUrl(){
		HttpClientImp  hc = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(aqsHandl);
		for(int i=1;i<=1;i++){
			String msg=hc.getTest("utf-8", "http://www.aqzy.gov.cn/content/channel/529fe19e259534f80e000004/page-"+i+"/", "", host, "", 80);
			ArrayList<String>  link=getHref(msg);
			if (!ckm.getUpdate(updatedate, "安庆市中级人民法院")){
	        	break;
	        }
			for(int j=0;j<link.size();j++){
				String detail=hc.gethtmlByGet("utf-8", "http://www.aqzy.gov.cn"+String.valueOf(link.get(j)), "", host);
				analyHtml(detail);
			}
		}
	}
	
	
	public  ArrayList<String> getHref(String content){
		ArrayList<String> link=new ArrayList<String>();
		Document doc=null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select("ul.is-listnews").get(0).getElementsByTag("li");		
		for(int i=0;i<eles.size();i++){
			String msg=eles.get(i).text();
			String temp=eles.get(0).text();
			if(temp.indexOf("]") - temp.indexOf("[")>1){
				updatedate=temp.substring(temp.indexOf("["), temp.indexOf("]")+1);
			}
			if(msg.contains("开庭")||msg.contains("听证")){
				link.add(eles.get(i).getElementsByTag("a").attr("href"));
			}
		}
		return link;
	}
	
	
	public void analyHtml(String content){
		TaiHuXianCourt thx=new TaiHuXianCourt();
		ArrayList<Court> array= new ArrayList<Court>();
		Document doc =null;
		doc=Jsoup.parse(content);
		String  msg=doc.select("div.is-newscontnet").text();
		array=thx.cutMsg(msg);
 		array.get(0).setDataFrom("安庆市中级人民法院");
		aqsHandl.insertCourt(array);
		}
	
	public static void main(String[] args){
		AnQingShiCourt ls=new AnQingShiCourt();
		ls.getUrl();
	}	

}
