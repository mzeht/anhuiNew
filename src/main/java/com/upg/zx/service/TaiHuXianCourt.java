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
 * 太湖县人民法院
 * @author litaotao
 * 
 */
@Service
public class TaiHuXianCourt {
	private  final String host ="hnhhzy.chinacourt.org";
	@Autowired
	private CourtDao thxHandl;
	private HttpClientImp  httpclientHandl;
	private String updatedate;
	
	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}
	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}
	
	
	public void getUrl(){
		HttpClientImp  hc = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(thxHandl);
		for(int i=1;i<=1;i++){
			String msg=hc.gethtmlByGet("utf-8", "http://th.aqzy.gov.cn/content/channel/52ce10ba7f8b9aa30daad4f6/page-"+i+"/", "", host);
			ArrayList<String>  link=getHref(msg);
			if (!ckm.getUpdate(updatedate, "太湖县人民法院")){
				break;
			}
			for(int j=0;j<link.size();j++){
				String detail=hc.gethtmlByGet("utf-8", "http://th.aqzy.gov.cn"+String.valueOf(link.get(j)), "", host);
				analyHtml(detail);
			}
		}
	}
	
	
	public  ArrayList<String> getHref(String content){
		ArrayList<String> link=new ArrayList<String>();
		Document doc=null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select("ul.is-listnews").get(0).getElementsByTag("li");
		String temp=eles.get(0).text();
		if(temp.indexOf("-") > -1){
			updatedate=temp.substring(0, temp.lastIndexOf("-")+3);
		}
		for(int i=0;i<eles.size();i++){
			link.add(eles.get(i).getElementsByTag("a").attr("href"));
		}
		return link;
	}
    
	
	public ArrayList<Court> cutMsg(String msg){
		msg=msg.replaceAll("被告人", "被告").replaceAll("与", "诉").replaceAll("受理", "审理");
		ArrayList<Court> list= new ArrayList<Court>();
		Court court=new  Court();
		if(!msg.contains("一案")){
			msg=msg.replaceAll("案", "一案");
		}
		int endRes=msg.indexOf("一案");
		int startTime=msg.indexOf("定于");
		int endTime=msg.indexOf("在");
		int endCourt=msg.indexOf("公开");
		int endP=msg.indexOf("诉");
		int startP=msg.indexOf("原告");
		int startRes=msg.indexOf("审理");
		if(startTime!=-1&&endTime!=-1){
			court.setStartTime(msg.substring(startTime+2, endTime));
		}
		if(startRes!=-1&&endRes!=-1){
			court.setCase_res(msg.substring(startRes+2, endRes));
		}else if(endCourt!=-1&&endRes!=-1){
			court.setCase_res(msg.substring(endCourt+2, endRes));
		}
		if(startP!=-1&&endP!=-1){
			court.setPlaintiff(msg.substring(startRes+2, endP));
		}
		if(endTime!=-1&&endCourt!=-1){
			court.setCourt(msg.substring(endTime+1, endCourt));
		}
		list.add(court);
		return list;
	}
	
	
	public void analyHtml(String content){
		ArrayList<Court> array= new ArrayList<Court>();
		Document doc =null;
		doc=Jsoup.parse(content);
		String  msg=doc.select("div.is-newscontnet").text();
		String caseNo=doc.select("div.is-newstitle").text();
		array=cutMsg(msg);
 		if(caseNo.contains("字第")||caseNo.contains("号")){
 			array.get(0).setCase_no(caseNo);
 		}
 		array.get(0).setDataFrom("太湖县人民法院");
		thxHandl.insertCourt(array);
		}
	
	public static void main(String[] args){
		TaiHuXianCourt ls=new TaiHuXianCourt();
		ls.getUrl();
	}	

}
