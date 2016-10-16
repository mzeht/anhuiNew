package com.upg.zx.dto;

public class CheckSite {

	private String updateDate;
	private String province;
	private String dataFrom;
	
	public CheckSite(String province, String dataFrom) {
		super();
		this.province = province;
		this.dataFrom = dataFrom;
	}
	
	public CheckSite() {
		super();
		
	}

	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getDataFrom() {
		return dataFrom;
	}
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}
	
}
