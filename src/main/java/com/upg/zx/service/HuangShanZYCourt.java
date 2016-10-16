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
 * 黄山市中级人民法院
 * 
 * @author litaotao
 * 
 */
@Service
public class HuangShanZYCourt {
	private final String host = "hszy.chinacourt.org";
	@Autowired
	private CourtDao hssHandl;
	private String updatedate;
	

	
	public void getUrl(){
		HttpClientImp hc=new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(hssHandl);
		for(int i=3;i>1;i--){
			String msg=hc.getTest("UTF-8", "http://hszy.chinacourt.org/article/index/id/MyhONzCwNDAwMSACAAA%3D/page/"+i+".shtml", "", host,"",80);
			ArrayList link=getHref(msg);
			if (!ckm.getUpdate(updatedate, "黄山市中级人民法院")){
				break;
			}
			for(int j=0;j<link.size();j++){
				String detail=hc.getTest("UTF-8", "http://hszy.chinacourt.org"+String.valueOf(link.get(j)), "", host,"",80);
				analyHtmlForDetail(detail);
			}
		}
	}
	
	
	public ArrayList getHref(String content){
		ArrayList linkList= new ArrayList();
		Document doc =null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select("div.paginationControl").get(0).parent().getElementsByTag("li");
		updatedate=doc.select("span.right").get(0).text();
		for(int i=0;i<eles.size();i++){
				linkList.add(eles.get(i).getElementsByTag("a").attr("href"));
		}
		return linkList;
	}
	
	
		//切割
	public ArrayList<Court> cut(String msg){
		ArrayList<Court> list= new ArrayList<Court>();
		Court court=new Court();
		court.setDataFrom("黄山市中级人民法院");
		int startTime=msg.indexOf("年");
		int endTime=msg.indexOf("日");
		int startRes=msg.indexOf("审理");
		int startRes1=msg.indexOf("公开");
		int ednRes=msg.indexOf("一案");
		int startCourt=msg.indexOf("在");
		if(startTime-4!=-1&&endTime!=-1){
			court.setStartTime(msg.substring(startTime-4, endTime+1));
		}else if(endTime!=-1){
			court.setStartTime(msg.substring(0, endTime+1));
		}
		
		if(startRes!=-1&&ednRes!=-1){
			court.setCase_res(msg.substring(startRes+2, ednRes));
		}else if(startRes!=-1){
			court.setCase_res(msg.substring(startRes+2));
		}else if(startRes1!=-1&&ednRes!=-1){
			court.setCase_res(msg.substring(startRes1+2,ednRes));
		}
		if(startCourt!=-1&&startRes1!=-1&&startCourt<startRes1){
			court.setCourt(msg.substring(startCourt+1, startRes1));
		}
		list.add(court);
		return list;
	}
	
    
	public void analyHtmlForDetail(String content){

		ArrayList<Court> array= new ArrayList<Court>();
		Document doc =null;
		doc=Jsoup.parse(content);
		String info=doc.select("div.text").text();
		info=info.replace("宣判", "审理");
		String detail[]=doc.select("div.text").html().split("<br />");
		Elements eles=doc.select("div.text").get(0).getElementsByTag("p");
		String title=doc.select("div.b_title").text();
		title=title.replace("宣判", "审理");
		if(eles.size()>1){
			String caseNo="";
			for(int i=0;i<eles.size();i++){
				String msg=eles.get(i).text();
				msg=msg.replace("宣判", "审理");
				if(msg.contains("字第")){
					caseNo=eles.get(i).text();
				}
				if(msg.contains("审理")||eles.get(i).text().contains("听证")){
					array=cut(eles.get(i).text());
					array.get(0).setCase_no(caseNo);
				}
				
			}
			if(array.get(0).getCase_res()!=null){
				 
				hssHandl.insertCourt(array);
			}
		}
		String time=doc.select("div.sth_a").text();
		int startIndex=time.indexOf("-");
		time=time.substring(startIndex-4, startIndex+6);
		if(info.contains("审理")&&info.contains("：")){
			if(title.contains("审理")){
				array=cut(title);
				if(array.get(0).getCase_res()!=null){
					 
					hssHandl.insertCourt(array);
				}
			}
			else {
				Court court=new Court();
				court.setDataFrom("黄山市中级人民法院");
				court.setStartTime(time);
				info=info.replace("你们", "你");
				String newInfo[]=info.split("：");
				int startRes=newInfo[1].indexOf("受理");
				int endRes=newInfo[1].indexOf("一案");
				int startNo=newInfo[1].indexOf("（2");
				int endNo=newInfo[1].indexOf("号");
				int startCourt=newInfo[1].lastIndexOf("在");
				int endCourt=newInfo[1].lastIndexOf("法庭");
				if(startRes!=-1&&endRes!=-1){
					court.setCase_res(newInfo[1].substring(startRes+2, endRes).replace("你", newInfo[0]));
				}
				if(startNo!=-1&&endNo!=-1){
					court.setCase_no(newInfo[1].substring(startNo, endNo+1));
				}
				if(startCourt!=-1&&endCourt!=-1&&startCourt<endCourt){
					court.setCase_no(newInfo[1].substring(startCourt+1, endCourt-2));
				}
				array.add(court);
				if(array.get(0).getCase_res()!=null){
					 
					hssHandl.insertCourt(array);
				}
			}
		}else if(detail.length>1&&info.contains("审理")){
			for(int i=0;i<detail.length;i++){
				String msg=Jsoup.parse(detail[i]).text();
				if(msg.contains("审理")){
					array=cut(msg);
					if(array.get(0).getCase_res()!=null){
						hssHandl.insertCourt(array);
					}
				}
			}
		}else if(detail.length>1&&!info.contains("审理")){
			Court court=new Court();
			court.setDataFrom("黄山市中级人民法院");
			for(int i=0;i<detail.length;i++){
				String msg=Jsoup.parse(detail[i]).text();
				msg=msg.replaceAll("：", ":").replaceAll("　", "").replaceAll("；", ":");
				if(msg.split(":").length>1){
					if(msg.contains("案号")){
						court.setCase_no(msg.split(":")[1]);
					}else if(msg.contains("案由")){
						court.setCase_res(msg.split(":")[1]);
					}else if(msg.contains("被告")||msg.contains("被上诉人")){
						court.setDefandant(msg.split(":")[1]);
					}else if(msg.contains("原告")||msg.contains("上诉人")){
						court.setPlaintiff(msg.split(":")[1]);
					}else if(msg.contains("地点")){
						court.setCourt(msg.split(":")[1]);
					}else if(msg.contains("日期")){
						court.setStartTime(msg.split(":")[1]);
					}
				}
			}
			array.add(court);
			if(array.get(0).getCase_res()!=null){
				 
				hssHandl.insertCourt(array);
			}
		}
		
	}
	
	public static void main(String ages[]){
		HuangShanZYCourt ls=new HuangShanZYCourt();
		ls.getUrl();
	}
	
	
}
