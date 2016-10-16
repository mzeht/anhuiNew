package com.upg.zx.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.upg.zx.service.AnHuiCourt;
import com.upg.zx.service.AnQingShiCourt;
import com.upg.zx.service.ChaoFengCourt;
import com.upg.zx.service.CzzyCourt;
import com.upg.zx.service.HbzyCourt;
import com.upg.zx.service.HeFeiShiCourt;
import com.upg.zx.service.HuangShanZYCourt;
import com.upg.zx.service.LuAnZYCourt;
import com.upg.zx.service.NingGuoShiCourt;
import com.upg.zx.service.SuZhouShiCourt;

@Controller
public class ExecuteController {

	// @Resource(name = "dongGuanZYCourt")
	@Autowired
	AnHuiCourt anHuiCourt;

	@RequestMapping("/AnHuiCourt")
	public void start() {
		// anHuiCourt = BeanFactoryHelper.getBean("anHuiCourt");
		anHuiCourt.getUrl();
	}

	@Autowired
	AnQingShiCourt anQingShiCourt;

	@RequestMapping("/AnQingShiCourt")
	void AnQingShiCourt() {
		anQingShiCourt.getUrl();
	}

	@Autowired
	ChaoFengCourt chaoFengCourt;

	@RequestMapping("/ChaoFengCourt")
	void ChaoFengCourt() {
		chaoFengCourt.getUrl();
	}

	@Autowired
	CzzyCourt czzyCourt;

	@RequestMapping("/CzzyCourt")
	void CzzyCourt() {
		czzyCourt.getInfo();
	}

	@Autowired
	HbzyCourt hbzyCourt;

	@RequestMapping("/HbzyCourt")
	void HbzyCourt() {
		hbzyCourt.getInfo();
	}

	@Autowired
	HeFeiShiCourt heFeiShiCourt;

	@RequestMapping("/HeFeiShiCourt")
	void HeFeiShiCourt() {
		heFeiShiCourt.getUrl();
	}

	@Autowired
	HuangShanZYCourt huangShanZYCourt;

	@RequestMapping("/HuangShanZYCourt")
	void HuangShanZYCourt() {
		huangShanZYCourt.getUrl();
	}

	@Autowired
	LuAnZYCourt luAnZYCourt;

	@RequestMapping("/LuAnZYCourt")
	void LuAnZYCourt() {
		luAnZYCourt.getUrl();
	}
	
	@Autowired
	NingGuoShiCourt ningGuoShiCourt;
	
	@RequestMapping("/NingGuoShiCourt")
	void NingGuoShiCourt(){
		ningGuoShiCourt.getUrl();
	}
	
	@Autowired
	SuZhouShiCourt suZhouShiCourt;
	
	@RequestMapping("/SuZhouShiCourt")
	void SuZhouShiCourt(){
		suZhouShiCourt.getUrl();
	}

}
