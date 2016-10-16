package com.upg.zx.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.google.gson.Gson;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.upg.zx.clientDaoImpl.Logger;
import com.upg.zx.dao.CourtDao;
import com.upg.zx.dto.CheckSite;
import com.upg.zx.dto.Court;
import com.upg.zx.dto.LoggerType;
import com.upg.zx.dto.Proxy;
import com.upg.zx.service.Util;

@SuppressWarnings("deprecation")
public class CourtDaoImpl extends SqlMapClientDaoSupport implements
		CourtDao {

	List<Court> caseNoList = new ArrayList<Court>();
	List<Court> OtherList = new ArrayList<Court>();
	final String PROVINCE = "安徽";

	public int courtForUpdate(Court obj) {
		int count = 0;
		getSqlMapClientTemplate().insert("courtForUpdate", obj);
		return count;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int courtForUpdate(final ArrayList<Court> list) {
		int count = 0;
		count = (Integer) this.getSqlMapClientTemplate().execute(
				new SqlMapClientCallback() {

					public Object doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						int batch = 0;
						for (Court court : list) {
							executor.insert("courtForUpdate", court);
							batch++;
							if (batch == 200) {
								executor.executeBatch();
								batch = 0;
							}
						}
						executor.executeBatch();
						return batch;
					}
				});
		return count;
	}

	public void insertUpdateDate(CheckSite obj) {
		getSqlMapClientTemplate().insert("insertSite", obj);
	}

	@SuppressWarnings({ "unchecked" })
	public String queryForUpdateDate(CheckSite obj) {
		String updateDate = "0";
		ArrayList<CheckSite> list = (ArrayList<CheckSite>) getSqlMapClientTemplate()
				.queryForList("queryUpdateSite", obj);
		if (list.size() > 0) {
			updateDate = list.get(0).getUpdateDate();
		}
		return updateDate;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int insertArrayCourt() {
		int count = 0;
		count = (Integer) this.getSqlMapClientTemplate().execute(
				new SqlMapClientCallback() {
					public Object doInSqlMapClient(SqlMapExecutor executor)
							throws SQLException {
						executor.startBatch();
						int batch = 0;
						if (caseNoList.size() > 0) {
							for (Court court : caseNoList) {
								executor.insert("hasCaseNo", court);
								batch++;
								if (batch == 200) {
									executor.executeBatch();
									batch = 0;
								}
							}
						}
						if (OtherList.size() > 0) {
							for (Court court : OtherList) {
								executor.insert("noCaseNo", court);
								batch++;
								if (batch == 200) {
									executor.executeBatch();
									batch = 0;
								}
							}
						}
						executor.executeBatch();
						return batch;
					}
				});
		return count;
	}

	private String getRealTime(String time) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("time", time);
		getSqlMapClientTemplate().insert("transformTime", map);
		return (String) map.get("realTime");
	}
//court.datafrom 设置 负责人和机器ip信息
	public void insertCourt(List<Court> list) {
		for (Court court : list) {
			court=setDataFrom(court);
			judge(court);
		}
		insertArrayCourt();
		setLogger();
	}

	

	private Court setDataFrom(Court court) {
		if(!court.getDataFrom().contains(DataFrom.SEPARATOR)){
			court.setDataFrom(court.getDataFrom()+DataFrom.SEPARATOR+DataFrom.AUTHOR+DataFrom.SEPARATOR+DataFrom.IP);
		}
		return court;
	}

	public void insertCourt(Court court) {
		court=setDataFrom(court);
		judge(court);
		insertArrayCourt();
		setLogger();
	}

	private void judge(Court court) {
		if (judgeCourt(court)) {
			court.setProvince(PROVINCE);
			String time = court.getStartTime().replaceAll("\\pZ", "");
			if (StringUtils.isNotEmpty(time)) {
				if (Util.getMatcherStr(time,
						"\\d{4}[.\\-/年]\\d{1,2}[.\\-/月]\\d{1,2}") != null) {
					time = Util.getMatcherStr(time,
							"\\d{4}[.\\-/年]\\d{1,2}[.\\-/月]\\d{1,2}")
							.replaceAll("\\D", "-");
					time = Util.cleanTime(time);
				} else
					time = getRealTime(court.getStartTime()); // 调用数据库的函数进行日期转化
				if (Util.getMatcherStr(time, "[\u4e00-\u9fa5]") != null)
					time = "0000-00-00";
				court.setStartTime(time);
				myPrint(court);
				if (StringUtils.isNotEmpty(court.getCase_no()))
					caseNoList.add(court);
				else
					OtherList.add(court);
			}
		}
	}

	private boolean judgeCourt(Court court) {
		if (StringUtils.isNotEmpty(court.getCase_no())) {
			if (court.getCase_no().length() > 100)
				return false;
		}
		if (StringUtils.isNotEmpty(court.getCase_res())) {
			if (court.getCase_res().length() > 200)
				return false;
		}
		return true;
	}

	private void myPrint(Court court) {
		System.out.println("案由:" + court.getCase_res());
		System.out.println("时间:" + court.getStartTime());
		if (!"".equals(court.getPerson()))
			System.out.println("当事人:" + court.getPerson());
		if (!"".equals(court.getDefandant()))
			System.out.println("被告:" + court.getDefandant());
		if (!"".equals(court.getPlaintiff()))
			System.out.println("原告:" + court.getPlaintiff());
		System.out.println("案号:" + court.getCase_no());
		System.out.println("法庭:" + court.getCourt());
		System.out.println("........................");
	}

	public void insertTime(CheckSite obj) {
		getSqlMapClientTemplate().insert("insertTime", obj);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Proxy> queryForProxy() {
		ArrayList<Proxy> list = (ArrayList<Proxy>) getSqlMapClientTemplate()
				.queryForList("queryForProxy");
		return list;
	}

	private void setLogger() {
		ArrayList<Court> total = new ArrayList<Court>();
		if (caseNoList.size() > 0) {
			total.addAll(caseNoList);
		}
		if (OtherList.size() > 0) {
			total.addAll(OtherList);
		}
		caseNoList.clear();
		OtherList.clear();
		for (Court c : total) {
			LoggerType log = new LoggerType();
			log.set开庭时间(c.getStartTime());
			log.set数据产生时间(Util.getDate());
			log.set案号(c.getCase_no());
			log.set案由(c.getCase_res());
			log.set法院(c.getJustice());
			log.set省份(PROVINCE);
			log.set网站来源(StringUtils.substringBefore(c.getDataFrom(), "@"));
			log.set风控数据类型("开庭");
			log.set作者("wp");
			log.setIp(Util.getServerLocalIPAddr());
			Gson gson = new Gson();
			String json = gson.toJson(log);
			Logger.debug("SYSTEM:LOG:YYYYMMDD", json);
		}
	}
	 class DataFrom {

			
			public static final String AUTHOR="wp";
			public static final String IP="172.16.2.60";
			public static final String SEPARATOR="@";
			
		}
}
