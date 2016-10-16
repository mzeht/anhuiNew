package com.upg.zx.dto;

public class Court {

	@Override
	public String toString() {
		return "Court [justice=" + justice + ", court=" + court + ", startTime=" + startTime + ", case_no=" + case_no
				+ ", case_res=" + case_res + ", judge=" + judge + ", plaintiff=" + plaintiff + ", defandant="
				+ defandant + ", person=" + person + ", director=" + director + ", clerk=" + clerk + ", procss="
				+ procss + ", members=" + members + ", dataFrom=" + dataFrom + ", province=" + province + "]";
	}

	// 法院
	private String justice = "";
	// 法庭
	private String court = "";
	// 开庭时间
	private String startTime = "";
	// 案号
	private String case_no = null;
	// 案由
	private String case_res = "";
	// 审判长
	private String judge = "";
	// 原告
	private String plaintiff = "";
	// 被告
	private String defandant = "";
	// 当事人
	private String person = "";
	// 承办人
	private String director;
	// 书记员
	private String clerk = "";
	// 适用程序
	private String procss;
	// 合议庭成员
	private String members;
	// 20150709 add by lis
	// 数据来源
	private String dataFrom;
	// 省份
	private String province = "";

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		if(province==null){
			province="";
		}
		this.province = province;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public String getProcss() {
		return procss;
	}

	public void setProcss(String procss) {
		this.procss = procss;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getClerk() {
		return clerk;
	}

	public void setClerk(String clerk) {
		this.clerk = clerk;
	}

	public String getJustice() {
		return justice;
	}

	public void setJustice(String justice) {
		if (justice == null) {
			justice = "";
		}
		this.justice = justice;
	}

	public String getCourt() {
		return court;
	}

	public void setCourt(String court) {
		if (court == null) {
			court = "";
		}
		this.court = court;

	}

	public String getCase_no() {
		return case_no;
	}

	public void setCase_no(String case_no) {
		this.case_no = case_no;
	}

	public String getCase_res() {
		return case_res;
	}

	public void setCase_res(String case_res) {
		if(case_res==null){
			case_res="";
		}
		this.case_res = case_res;
	}

	public String getJudge() {
		return judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	public String getPlaintiff() {
		return plaintiff;
	}

	public void setPlaintiff(String plaintiff) {
		if(plaintiff==null){
			
			plaintiff="";
		}
			
		this.plaintiff = plaintiff;
	}

	public String getDefandant() {
		return defandant;
	}

	public void setDefandant(String defandant) {
		if(defandant==null){
			defandant="";
		}
		this.defandant = defandant;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		if(person==null){
			person="";
		}
		this.person = person;
	}

	public String getStartTime() {
		
		return startTime;
	}

	public void setStartTime(String startTime) {
		if(startTime==null){
			startTime="";
		}
		this.startTime = startTime;
	}
}
