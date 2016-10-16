package com.upg.zx.clientDaoImpl;

import java.util.ArrayList;
import java.util.HashMap;

import com.upg.zx.dto.Court;

/**
 * 转换为开庭类，继承转换接口
 * 
 * @author 001552
 * 
 */
public class CourtTransformImp implements Transform {
	//字符串转换为法庭集合
	public ArrayList<Court> getCourt(String text) {
		ArrayList<Court> list=new ArrayList<Court>();
		// 按照"案号："拆分
		String[] textArray = text.split("案号：");
		for (int i = 1; i < textArray.length; i++) {
			// 判断包含
			if (textArray[i].contains("原告：") && textArray[i].contains("被告：")) {
				String[] courtArray = textArray[i]
						.split("案由：|原告：|被告：|开庭时间：|开庭地点：|承办人：|申请人：|被申请人：");
				if(courtArray.length==7){
					// 转换为法庭对象
					Court court = new Court();
					court.setCase_no(courtArray[0]);
					court.setCase_res(courtArray[1]);
					court.setPlaintiff(courtArray[2]);
					court.setDefandant(courtArray[3]);
					court.setStartTime(courtArray[4]);
					court.setCourt(courtArray[5]);
					court.setDirector(courtArray[6]);
					list.add(court);
				}
			} else {
				String[] courtArray = textArray[i]
						.split("案由：|被告：|开庭时间：|开庭地点：|承办人：|被告人：|申请人：|被申请人：");
				if(courtArray.length==6){
					Court court = new Court();
					court.setCase_no(courtArray[0]);
					court.setCase_res(courtArray[1]);
					court.setDefandant(courtArray[2]);
					court.setStartTime(courtArray[3]);
					court.setCourt(courtArray[4]);
					court.setDirector(courtArray[5]);
					list.add(court);
				}
			}
		}
		return list;
	}
	
	public ArrayList<Court> getCourtJianYe(String text) {
		ArrayList<Court> list = new ArrayList<Court>();
		// 按照"案号："拆分
		String[] textArray = text.split("案号：");
		for (int i = 1; i < textArray.length; i++) {
			String[] courtArray = textArray[i]
					.split("案由：|原告：|被告：|时间：|地点：|承办人：|日期：|书记员：|合议庭：|审判长：|被告人：");
			if (textArray[i].contains("原告：") && textArray[i].contains("被告：")) {
				if (textArray[i].contains("审判长：")
						&& textArray[i].contains("合议庭：")) {
					Court court = new Court();
					court.setDataFrom("南京市建邺区人民法院");
					court.setCase_no(courtArray[0]);
					court.setCase_res(courtArray[1]);
					court.setPlaintiff(courtArray[2]);
					court.setDefandant(courtArray[3]);
					court.setJudge(courtArray[4]);
					court.setMembers(courtArray[5]);
					court.setCourt(courtArray[6]);
					court.setStartTime(courtArray[7] + " " + courtArray[8]);
					if (textArray[i].contains("书记员：")) {
						court.setClerk(courtArray[9]);
					}
					list.add(court);
				} else if (textArray[i].contains("承办人：")) {
					Court court = new Court();
					court.setDataFrom("南京市建邺区人民法院");
					court.setCase_no(courtArray[0]);
					court.setCase_res(courtArray[1]);
					court.setPlaintiff(courtArray[2]);
					court.setDefandant(courtArray[3]);
					court.setDirector(courtArray[4]);
					court.setCourt(courtArray[5]);
					court.setStartTime(courtArray[6] + " " + courtArray[7]);
					if (textArray[i].contains("书记员：")) {
						court.setClerk(courtArray[8]);
					}
					list.add(court);
				} else {
					Court court = new Court();
					court.setDataFrom("南京市建邺区人民法院");
					court.setCase_no(courtArray[0]);
					court.setCase_res(courtArray[1]);
					court.setPlaintiff(courtArray[2]);
					court.setDefandant(courtArray[3]);
					court.setCourt(courtArray[4]);
					court.setStartTime(courtArray[5] + " " + courtArray[6]);
					if (textArray[i].contains("书记员：")) {
						court.setClerk(courtArray[7]);
					}
					list.add(court);
				}
			} else {
				if (textArray[i].contains("审判长：")
						&& textArray[i].contains("合议庭：")) {
					Court court = new Court();
					court.setDataFrom("南京市建邺区人民法院");
					court.setCase_no(courtArray[0]);
					court.setCase_res(courtArray[1]);
					court.setDefandant(courtArray[2]);
					court.setJudge(courtArray[3]);
					court.setMembers(courtArray[4]);
					court.setCourt(courtArray[5]);
					court.setStartTime(courtArray[6] + " " + courtArray[7]);
					if (textArray[i].contains("书记员：")) {
						court.setClerk(courtArray[8]);
					}
					list.add(court);
				} else if (textArray[i].contains("承办人：")) {
					Court court = new Court();
					court.setDataFrom("南京市建邺区人民法院");
					court.setCase_no(courtArray[0]);
					court.setCase_res(courtArray[1]);
					court.setDefandant(courtArray[2]);
					court.setDirector(courtArray[3]);
					court.setCourt(courtArray[4]);
					court.setStartTime(courtArray[5] + " " + courtArray[6]);
					if (textArray[i].contains("书记员：")) {
						court.setClerk(courtArray[7]);
					}
					list.add(court);
				} else {
					Court court = new Court();
					court.setDataFrom("南京市建邺区人民法院");
					court.setCase_no(courtArray[0]);
					court.setCase_res(courtArray[1]);
					court.setDefandant(courtArray[2]);
					court.setCourt(courtArray[3]);
					court.setStartTime(courtArray[4] + " " + courtArray[5]);
					if (textArray[i].contains("书记员：")) {
						court.setClerk(courtArray[6]);
					}
					list.add(court);
				}
			}
		}
		return list;
	}
	
	public ArrayList<Court> getCourtChangZhou(String text) {
		ArrayList<Court> list = new ArrayList<Court>();
		String[] courtArray = text
				.split("主审法院：|法庭:|宣判时间：|案件名称：|案件号：|听证时间：|法庭：|主审法官：|书记员：|开庭时间：|案号：");
		if (courtArray.length == 7 && courtArray[4].contains("法庭")) {
			Court court = new Court();
			court.setDataFrom("常州市中级人民法院");
			court.setCase_res(courtArray[1]);
			court.setCase_no(courtArray[2]);
			court.setStartTime(courtArray[3]);
			court.setCourt(courtArray[4]);
			court.setJudge(courtArray[5]);
			court.setClerk(courtArray[6]);
			list.add(court);
		}else if(courtArray.length == 7 && courtArray[3].contains("法庭")){
			Court court = new Court();
			court.setDataFrom("常州市中级人民法院");
			court.setCase_res(courtArray[1]);
			court.setCase_no(courtArray[2]);
			court.setStartTime(courtArray[4]);
			court.setCourt(courtArray[3]);
			court.setJudge(courtArray[5]);
			court.setClerk(courtArray[6]);
			list.add(court);
		}else if(courtArray.length == 6 && courtArray[4].contains("法庭") && courtArray[3].contains("月|日|时")){
			Court court = new Court();
			court.setDataFrom("常州市中级人民法院");
			court.setCase_res(courtArray[1]);
			court.setCase_no(courtArray[2]);
			court.setStartTime(courtArray[3]);
			court.setCourt(courtArray[4]);
			list.add(court);
		}else if(courtArray.length == 6 && courtArray[3].contains("法庭") && courtArray[4].contains("月|日|时")){
			Court court = new Court();
			court.setDataFrom("常州市中级人民法院");
			court.setCase_res(courtArray[1]);
			court.setCase_no(courtArray[2]);
			court.setStartTime(courtArray[4]);
			court.setCourt(courtArray[3]);
			list.add(court);
		}
		return list;
	}
	public ArrayList<Court> getCourtNanTong(String text) {
		ArrayList<Court> list = new ArrayList<Court>();
		String[] courtArray = text
				.split("案由：|时间:|法庭:|审判长:|合议庭:|书记员:");
		if(courtArray.length==7){
			Court court = new Court();
			court.setDataFrom("南通市中级人民法院");
			court.setCase_res(courtArray[1]);
			court.setStartTime(courtArray[2]);
			court.setCourt(courtArray[3]);
			court.setJudge(courtArray[4]);
			court.setMembers(courtArray[5]);
			court.setClerk(courtArray[6]);
			list.add(court);
		}
		return list;
	}
	
	public ArrayList<Court> getCourtLianYuGang1(String text) {
		ArrayList<Court> list = new ArrayList<Court>();
		Court court = new Court();
		court.setDataFrom("连云港市中级人民法院");
		court.setCase_res(text);
		list.add(court);
		return list;
	}

	public ArrayList<Court> getCourtLianYuGang2(String text) {
		ArrayList<Court> list = new ArrayList<Court>();
		String[] textArray = text.split("案件名称：");
		for (int i = 0; i < textArray.length; i++) {
			//System.out.println(textArray[i]);
			String[] courtArray = textArray[i].split("案号：|合议庭成员：|开庭时间：|开庭地点：|案 号：");
			System.out.println(courtArray.length);
			if(courtArray.length==5){
				Court court = new Court();
				court.setDataFrom("连云港市中级人民法院");
				court.setCase_res(courtArray[0]);
				court.setCase_no(courtArray[1]);
				court.setMembers(courtArray[2]);
				court.setStartTime(courtArray[3]);
				court.setCourt(courtArray[4]);
				list.add(court);
			}
		}
		return list;
	}
	public ArrayList<Court> getCourtRuDongXian(String text) {
		ArrayList<Court> list = new ArrayList<Court>();
		String[] textArray = text.split("<NEXT>");
		for(int i=0;i<textArray.length;i++){
			String courtArray[]=textArray[i].split("<next>");
			Court court = new Court();
			court.setDataFrom("如东县人民法院");
			for(int j=0;j<courtArray.length;j++){
				if(courtArray[j].contains("案由：")){
					court.setCase_res(courtArray[j].split("：")[1]);
				}
				if(courtArray[j].contains("案号：")){
					court.setCase_no(courtArray[j].split("：")[1]);
				}
				if(courtArray[j].contains("当事人：")){
					court.setPerson(courtArray[j].split("：")[1]);
				}
				if(courtArray[j].contains("承办人：")){
					court.setDirector(courtArray[j].split("：")[1]);
				}
				if(courtArray[j].contains("地点：")){
					court.setCourt(courtArray[j].split("：")[1]);
				}
				if(courtArray[j].contains("日期：")){
					court.setStartTime(courtArray[j].split("：")[1]);
				}
				if(courtArray[j].contains("书记员：")){
					court.setClerk(courtArray[j].split("：")[1]);
				}
			}
			list.add(court);
		}
		return list;
	}
	public ArrayList<Court> getCourtQiDong(String text,
			HashMap<String, String> map, String url) {
		ArrayList<Court> list = new ArrayList<Court>();
		String[] textArray = text.split("<NEXT>");
		int num = 0;
		for (int i = 0; i < textArray.length; i++) {
			String[] courtArray = textArray[i].split("<next>");
			Court court = new Court();
			num = num + 1;
			court.setDataFrom("启东市人民法院");
			for (int j = 0; j < courtArray.length; j++) {
				if (courtArray[j].contains("时间：")) {
					if (courtArray[j].split("：")[1].contains("2014|")
							|| courtArray[j].split("：")[1].contains("2015")
							|| courtArray[j].split("：")[1].contains("2016")
							|| courtArray[j].split("：")[1].contains("2017")
							|| courtArray[j].split("：")[1].contains("2018")
							|| courtArray[j].split("：")[1].contains("2019")
							|| courtArray[j].split("：")[1].contains("2020")
							|| courtArray[j].split("：")[1].contains("2021")
							|| courtArray[j].split("：")[1].contains("2022")
							|| courtArray[j].split("：")[1].contains("2023")
							|| courtArray[j].split("：")[1].contains("2024")
							|| courtArray[j].split("：")[1].contains("2025")) {
						court.setStartTime(courtArray[j].split("：")[1]);
					} else {
						court.setStartTime(map.get(url) + " "
								+ courtArray[j].split("：")[1]);
					}
				}
				if (courtArray[j].contains("地点：")) {
					court.setCourt(courtArray[j].split("：")[1]);
				}
				if (courtArray[j].contains("案由：")) {
					court.setCase_res(courtArray[j].split("：")[1]);
				}
				if (courtArray[j].contains("当事人：")) {
					court.setPerson(courtArray[j].split("：")[1]);
				}
				if (courtArray[j].contains("审判员:")) {
					court.setJudge(courtArray[j].split(":")[1]);
				}
			}
			if (court.getPerson() != null) {
				list.add(court);
			}
		}
		return list;
	}
}
