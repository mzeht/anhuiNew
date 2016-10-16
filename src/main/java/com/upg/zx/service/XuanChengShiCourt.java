package com.upg.zx.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
 * 宣城市中级人民法院 
 * 
 * @author litaotao
 * 
 */
@Service
public class XuanChengShiCourt {
	private final String host="www.ahxccourt.gov.cn";
	@Autowired
	private CourtDao xcHandl;
	private String updatedate="";
	
	
	
	//只取2015年九月一号以后的数据
	public void geturl(){
		HttpClientImp hc=new HttpClientImp();
		 CheckSiteModule ckm = new CheckSiteModule(xcHandl);
		for(int i=1;i<=1;i++){
			String msg=hc.getTest("utf-8", "http://www.ahxccourt.gov.cn/content/channel/54912e5a9a05c2f01ba89104/page-"+i+"/", "", host,"",80);
			ArrayList<String> link=gethref(msg);
			if (!ckm.getUpdate(updatedate, "宣城市中级人民法院")){
				break;
			}
			for(int j=0;j<link.size();j++){
				String htmlInfo=hc.getTest("utf-8", "http://www.ahxccourt.gov.cn"+link.get(j), "", host,"",80);
				getHtml(htmlInfo);
			}
		}
	}
	
	
	//取网址
	public ArrayList<String> gethref(String content){
		String fixedTime = "2015-09-01";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> linkList= new ArrayList<String>();
		Document doc =null;
		doc=Jsoup.parse(content);
		Elements eles=doc.select("ul.is-listnews").get(0).getElementsByTag("a");
		for(int i=0;i<eles.size();i++){
			String time=eles.get(i).parent().getElementsByTag("span").text();
			String temp=eles.get(0).parent().getElementsByTag("span").text();
			int startIndex=time.indexOf("[");
			int endIndex=time.indexOf("]");
			if(startIndex!=-1&&endIndex!=-1){
				time=time.substring(startIndex+1, endIndex);
			}
			if(temp.indexOf("]") - temp.indexOf("[")>1){
				updatedate=temp.substring(temp.indexOf("["), temp.indexOf("]")+1);
			}
			try {
				Date a=df.parse(time);
				Date b=df.parse(fixedTime);
				//if(b.before(a)){
					if(eles.get(i).text().contains("表")){
						linkList.add(eles.get(i).attr("href"));
					}
				//}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return linkList;
		
	}
	
	
	//判断表头的位置
	public int getTableTitle(String content){
		Document doc =null;
		doc=Jsoup.parse(content);
		int num=0;
		Elements eles=doc.select("div.is-newscontnet table");
		if(eles.size()>0){
			for(int i=0;i<eles.select("tr").size();i++){
				String test=eles.select("tr").get(i).text();
				if(test.contains("开庭法庭")||test.contains("开庭法院"))
				{
					num=i;
					break;
				}
			}
		}
			
		return num;
	}
	
	
	//取数据 
	public  void getHtml(String content){
		ArrayList<Court> array= new ArrayList<Court>();
		Document doc =null;
		doc=Jsoup.parse(content);
		int num=getTableTitle(content);
		if(doc.select("div.is-newscontnet").get(0).select("table").size()>0){
			Elements tableTitle = doc.select("div.is-newscontnet").get(0).select("table").select("tr").get(num).select("td");
			Elements tableSize  = doc.select("div.is-newscontnet").get(0).select("table").select("tr");
			for(int i=num+1;i<tableSize.size();i++){
				Elements tablemsg=doc.select("div.is-newscontnet").get(0).select("table").select("tr").get(i).select("td");
				Court court =new Court();
				court.setDataFrom("宣城市中级人民法院");
				for(int j=0;j<tableTitle.size();j++){
					
					String msg=tableTitle.get(j).text()+"<>"+tablemsg.get(j).text();
					msg=msg.replaceAll(" |\\s", "").replaceAll("；", ";");
					if(msg!=null&&msg.split("<>").length>1){
						if(msg.contains("开庭法")){
							
							court.setCourt(msg.split("<>")[1]);
							
						}
						else if(msg.contains("开庭时间")){
							court.setStartTime(msg.split("<>")[1]);
						}
						else if(msg.contains("案由")){
							
							court.setCase_res(msg.split("<>")[1]);
							
						}
						else if(msg.contains("当事人")){
							
							court.setPerson(msg.split("<>")[1]);
							
						}
					}
				}
				array.add(court);
			}
		}
		xcHandl.insertCourt(array);
	}	
	public static void main(String ages[]){
			XuanChengShiCourt ls=new XuanChengShiCourt();
			ls.geturl();
	}
}
