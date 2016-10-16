package com.upg.zx.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.upg.zx.dto.CheckSite;
import com.upg.zx.dto.Court;



public interface CourtDao {
	public void insertCourt(List<Court> list);
	public void insertCourt(Court c);
	public  int courtForUpdate(final ArrayList<Court> list);
	public int courtForUpdate(Court obj);
	public void insertUpdateDate(CheckSite obj);//插入日期
	public String queryForUpdateDate(CheckSite obj);//查询日期
	public void insertTime(CheckSite obj); //插入更新时间   李韬涛
	
}
