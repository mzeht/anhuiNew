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
 *  安徽法院诉讼网
 * @author litaotao
 * 
 */
@Service
public class AnHuiCourt {
	private  final String host ="www.ahgyss.cn";
	
	@Autowired
	private CourtDao anHandl;
	private HttpClientImp  httpclientHandl;
	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}
	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}


	
	// 1 抓取网页    
	public void getUrl(){
		HttpClientImp  hc = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(anHandl);
		for(int i=1;i<=1;i++){
			try {
				
		            Thread.sleep(1000);
		        } catch (InterruptedException e) {
		            e.printStackTrace(); 
		        }
			String msg=hc.getTest("utf-8", "http://www.ahgyss.cn/ktgg/index_"+i+".jhtml", "", host,"",80);
			ArrayList<String>  link=getHref(msg);
			String strcount=getPageByjsoup(msg);
			if (!ckm.getUpdateForpage(strcount, "安徽法院诉讼网")){
				break;
			}
			for(int j=0;j<link.size();j++){
				String detail=hc.getTest("utf-8", link.get(j), "", host,"",80);
				analyHtml(detail);
			}
		}
	}
	
	private ArrayList<String> getHref(String content){
		ArrayList<String> array= new ArrayList<String>();
		Document doc =null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select("[href~=ktggInfo\\.jspx\\?fyid=]");
		if(eles.size()>0){
			for(int i =0; i <eles.size(); i++){
				if(eles.get(i).text().contains("字第") || eles.get(i).text().contains("民初")){
					array.add(eles.get(i).attr("href"));
				}
			}
		}
		return array;
	}
	/**
	 *得到page数
	 * 
	 *@return int
	 *@author 
	 **/
	private String getPageByjsoup(String content){
		Document doc =null;	
		doc=Jsoup.parse(content);
		String count="";
		Elements eles=doc.select("div.turn_page").select("p");
	
			Elements ele = eles.select("a");
			for(int i =0; i <ele.size(); i++){
			if (ele.get(i).className().equals("sp_next")) {
			     count=ele.get(i-1).text();
			}
			
		}
			return count;		
	}
	
	private void analyHtml(String content){
		ArrayList<Court> array= new ArrayList<Court>();
		Court court = new Court();
		court.setDataFrom("安徽法院诉讼网");
		Document doc =null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select("div.sswy_sub_con p");
		String info=doc.select("input#hiddenTxt").attr("value");
		String courtName="";
		String caseNo="";
		if(eles.size()>1){
			for(int i=0;i<eles.size();i++){
				String msg=eles.get(i).text();
				if(msg.contains("来源:")){
					int startCourt=msg.indexOf("来源:");
					courtName=msg.substring(startCourt+3);
				}else if(msg.contains("字第")){
					caseNo=msg;
					break;
				}
			}
		}
		int startTime=info.indexOf("定于");
		int endTime=info.indexOf("在");
		int startRes=info.indexOf("审理");
		int endRes=info.indexOf("一案");
		int endCourt=info.indexOf("法庭");
		int endCourt1=info.indexOf("依法");
		if(startTime!=-1&&endTime!=-1&&startTime<endTime){
			court.setStartTime(info.substring(startTime+2, endTime));
		}
		if(startRes!=-1&&endRes!=-1&&startRes<endRes){
			court.setCase_res(info.substring(startRes+2, endRes));
		}
		if(endTime!=-1&&endCourt!=-1&&endTime<endCourt){
			court.setCourt(courtName+info.substring(endTime+1, endCourt+2).replace("本院", ""));
		}else if(endTime!=-1&&endCourt1!=-1&&endTime<endCourt1){
			court.setCourt(courtName+info.substring(endTime+1, endCourt1).replace("本院", ""));
		}
		court.setCase_no(caseNo);
		array.add(court);
//		prin？t(array);
		System.out.println(court.toString());
		anHandl.insertCourt(array);
	}
		
//	public void print(ArrayList<Court> array){
//		for(Court c:array){
//			System.out.println("案由："+c.getCase_res());
//			System.out.println("时间："+c.getStartTime());
//			System.out.println("案号："+c.getCase_no());
//			System.out.println("法庭："+c.getCourt());
//			System.out.println("-------------------------");
//		}
//	}

	public static void main(String[] args){
		AnHuiCourt ls=new AnHuiCourt();
		ls.getUrl();
	}	
	
}

