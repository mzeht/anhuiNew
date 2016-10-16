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
 * 马鞍山中级人民法院
 * @author lyf
 * 
 */
@Service
public class MaanshanCourt {
	private  final String host ="http://maszy.chinacourt.org/index.shtml";
	ArrayList linkList = new ArrayList();
	@Autowired
	private CourtDao masHandl;
	private String updatedate;

	private HttpClientImp  httpclientHandl;

	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}


	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}



	// 1 抓取网页
	public void getHtml() {
	
	    HttpClientImp  httpclientHandl = new HttpClientImp();
	    CheckSiteModule ckm = new CheckSiteModule(masHandl);
		ArrayList<Court> list = new ArrayList<Court>();
		for (int i =1; i<= 1; i++){
			String url="http://maszy.chinacourt.org";
			String msg=httpclientHandl.getTest("utf-8", "http://maszy.chinacourt.org/article/index/id/MyhONDBIMyAOAAA%3D/page/"+i+".shtml", "", host,"",80);			
			
			linkList = analyHtml(msg);
			if (!ckm.getUpdate(updatedate, "马鞍山中级人民法院")){
	        	break;
	        }
			for (int j =1; j<linkList.size(); j++){
				String detail=httpclientHandl.getTest("UTF-8", url+linkList.get(j), "", host,"",80);
			    list=analyHtmlForDetail(detail);
                masHandl.insertCourt(list);
			}
		}
	}
	
	private ArrayList analyHtml(String content){
		ArrayList array= new ArrayList();
		Document doc =null;
		doc=Jsoup.parse(content);
		
		Elements eles=doc.select("span.left");
		updatedate=doc.select("span.right").get(7).text();
		for(int i =0; i <eles.size(); i++){
			Elements ele=doc.select("span.left").get(i).getElementsByTag("a");
			if(ele.get(0).attr("title").contains("开庭公告")){
				array.add(ele.get(0).attr("href"));
			}
		}
		return array;
	}
	
	private ArrayList<Court> analyHtmlForDetail(String content){
		ArrayList<Court> array= new ArrayList<Court>();
		String detail [] = null;
		Document doc =null;
		String textString = content.replace("<br />","myline");
		doc=Jsoup.parse(textString);
		//
		Elements eles=doc.select("div.text").get(0).getElementsByTag("p");
		int l = 1;
		if(eles.text()==null||"".equals(eles.text())){
			eles=doc.select("div.text").get(0).getElementsByTag("td");
			for(int i=1;i<eles.size();i++){
				if(eles.get(i).text().contains("字第")){
					l=0;
					Court court = new Court();
					court.setCourt(eles.get(i-3).text());
					court.setStartTime(eles.get(i-2).text()+" "+eles.get(i-1).text());
					court.setCase_no(eles.get(i).text());
					court.setCase_res(eles.get(i+1).text());
					court.setPerson(eles.get(i+2).text());
					String tif = eles.get(i+2).text();
					if(tif.contains("上诉人")&&tif.contains("被上诉人")){
						String dant = tif.substring(tif.indexOf("被上诉人")+5,tif.length());
						court.setDefandant(dant);
					}
					if(tif.contains("原告")&&tif.contains("被告")){
						String dant = tif.substring(tif.indexOf("被告")+3,tif.length());
						court.setDefandant(dant);
					}
					court.setDataFrom("马鞍山中级人民法院");
					array.add(court);
				}
			}
		}
		
		if(eles.contains("开庭日期") || eles.contains("承办法官")){
			for(int i=1;i<eles.size();i++){
				if(eles.get(i).text().contains("字第")){
					Court court = new Court();
					court.setCourt(eles.get(i-3).text());
					court.setStartTime(eles.get(i-2).text()+" "+eles.get(i-1).text());
					court.setCase_no(eles.get(i).text());
					court.setCase_res(eles.get(i+1).text());
					court.setPerson(eles.get(i+2).text());
					String tif = eles.get(i+2).text();
					if(tif.contains("上诉人")&&tif.contains("被上诉人")){
						String dant = tif.substring(tif.indexOf("被上诉人")+5,tif.length());
						court.setDefandant(dant);
					}
					if(tif.contains("原告")&&tif.contains("被告")){
						String dant = tif.substring(tif.indexOf("被告")+3,tif.length());
						court.setDefandant(dant);
					}
					court.setDataFrom("马鞍山中级人民法院");
					array.add(court);
				}
			}
		}
		if(!eles.contains("开庭日期")&&l==1){
			for(int i=1;i<eles.size();i++){
				if(eles.get(i).text().contains("字第")){
					Court court = new Court();
					if(eles.get(i-2).text().contains("法庭")){
						court.setCourt(eles.get(i-2).text());
						court.setStartTime(eles.get(i-1).text());
					}
					if(!eles.get(i-2).text().contains("法庭")){
						if(eles.contains("开庭法庭")){
							court.setCourt(eles.get(i-3).text());
						}
						court.setStartTime(eles.get(i-2).text()+" "+eles.get(i-1).text());
					}
					court.setCase_no(eles.get(i).text());
					court.setCase_res(eles.get(i+1).text());
					court.setPerson(eles.get(i+2).text());
					String tif = eles.get(i+2).text();
					if(tif.contains("上诉人")&&tif.contains("被上诉人")){
						String dant = tif.substring(tif.indexOf("被上诉人")+5,tif.length());
						court.setDefandant(dant);
					}
					if(tif.contains("原告")&&tif.contains("被告")){
						String dant = tif.substring(tif.indexOf("被告")+3,tif.length());
						court.setDefandant(dant);
					}
					court.setDataFrom("马鞍山中级人民法院");
					array.add(court);
				}
			}
		}
		if(eles.text()==null||"".equals(eles.text())){
			int s=1;
			eles = doc.select("div.text");
			String[] ary = eles.text().split("myline");
			if(!eles.text().contains("合议庭成员")){
				for(int i =0;i<ary.length;i++){
					if(ary[i].contains("字第")&&ary[i].contains("法庭")&&(s==1)){
						Court court = new Court();
						s=0;
		                court.setCase_res(ary[i]);
		                court.setCourt(ary[i].substring(ary[i].indexOf("法庭")-2,ary[i].indexOf("法庭")+2));
		                court.setStartTime(ary[i].substring(ary[i].indexOf("法庭")+2,ary[i].indexOf("字第")-10));
		                court.setCase_no(ary[i].substring(ary[i].indexOf("字第")-10,ary[i].indexOf("字第")+8));
		                
						court.setDataFrom("马鞍山中级人民法院");
						array.add(court);
					}
					
					if(ary[i].contains("皖")&&ary[i].contains("号")){
						Court court = new Court();
						
						String[] item=ary[i].trim().split(" ");
						court.setCourt(item[0]);
						court.setStartTime(item[1].substring(0, 10));
						court.setCase_no(item[3]);
						court.setCase_res(item[4]);
						if(item[5].contains(";")&&item[5].contains("上诉人")){
							String[] shangsu=item[5].split(";");
							for (int j = 0; j < shangsu.length; j++) {
								if(shangsu[j].contains("被上诉人:")){
									court.setDefandant(shangsu[j].split(":")[1]);
								}else if(shangsu[j].contains("上诉人:")){
									court.setPlaintiff(shangsu[j].split(":")[1]);
								}
							}
						}
						court.setDirector(item[7]);
						court.setDataFrom("马鞍山中级人民法院");
						array.add(court);
					}
				}
			}
			if(eles.text().contains("合议庭成员")){
				for(int i =0;i<ary.length-6;i++){
				 if(ary[i].contains("字第")&&ary[i+12].contains("法庭")&&ary[i+8].contains("合议庭成员")&&(s==1)){
					Court court = new Court();
					 court.setCase_res(ary[i+2]);
					 court.setCase_no(ary[i]);
					 court.setPlaintiff(ary[i+4]);
					 court.setDefandant(ary[i+6]);
					 court.setStartTime(ary[i+10]);
					 court.setCourt(ary[i+12]);
					 court.setDataFrom("马鞍山中级人民法院");
					 array.add(court);
				 }
				}
			}
		}
		
		
		
		
		return array;
}
		

	
	/**
	 *得到page数
	 * 
	 *@return int
	 *@author lis
	 **/
	private int getPageByjsoup(String content){
		
		int p=0;
		Document doc =null;
		doc=Jsoup.parse(content);
		
		 Elements eles=doc.select("a");
         for(int i = 0;i<eles.size();i++){
	     if(eles.get(i).text().contains("尾页")){
	 		String page = eles.get(i).attr("href");
	 		String pag = page.substring(page.indexOf("/page/")+6,page.indexOf(".shtml"));
	        p=Integer.parseInt(pag);		
	      }
       }
		return p;
	}

	public static void main(String[] args){
		MaanshanCourt ls=new MaanshanCourt();
		ls.getHtml();
	}	
	
}

