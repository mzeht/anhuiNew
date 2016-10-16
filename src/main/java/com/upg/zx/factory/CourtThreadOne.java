package com.upg.zx.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import com.upg.zx.clientDaoImpl.BeanFactoryHelper;
import com.upg.zx.service.*;

@Component
public class CourtThreadOne {
	AnHuiCourt anHuiCourt;
	AnQingShiCourt anQingShiCourt;
	ChaoFengCourt chaoFengCourt;
	CzzyCourt czzyCourt;
	HbzyCourt hbzyCourt;
	HeFeiShiCourt heFeiShiCourt;
	HuangShanZYCourt huangShanZYCourt;
	LuAnZYCourt luAnZYCourt;
	
	
	

	public void startThread() {
		anHuiCourt = BeanFactoryHelper.getBean("anHuiCourt");
		anQingShiCourt = BeanFactoryHelper.getBean("anQingShiCourt");
		chaoFengCourt = BeanFactoryHelper.getBean("chaoFengCourt");
		czzyCourt = BeanFactoryHelper.getBean("czzyCourt");
		hbzyCourt = BeanFactoryHelper.getBean("hbzyCourt");
		heFeiShiCourt = BeanFactoryHelper.getBean("heFeiShiCourt");
		huangShanZYCourt = BeanFactoryHelper.getBean("huangShanZYCourt");
		luAnZYCourt = BeanFactoryHelper.getBean("luAnZYCourt");
		
		
		ExecutorService pool = Executors.newFixedThreadPool(10);
		exceThread1 thread1 = new exceThread1(anHuiCourt);
		exceThread2 thread2 = new exceThread2(anQingShiCourt);
		exceThread3 thread3 = new exceThread3(chaoFengCourt);
		exceThread4 thread4 = new exceThread4(czzyCourt);
		exceThread5 thread5 = new exceThread5(hbzyCourt);
		exceThread6 thread6 = new exceThread6(heFeiShiCourt);
		exceThread7 thread7 = new exceThread7(huangShanZYCourt);
		exceThread8 thread8 = new exceThread8(luAnZYCourt);
		
		pool.submit(thread1);
		pool.submit(thread2);
		pool.submit(thread3);
		pool.submit(thread4);
		pool.submit(thread5);
		pool.submit(thread6);
		pool.submit(thread7);
		pool.submit(thread8);
		
		
	}

	class exceThread1 extends Thread {
		private AnHuiCourt dghandl;

		public exceThread1(AnHuiCourt dghandl) {

			this.dghandl = dghandl;
		}

		public void run() {
			dghandl.getUrl();
		}
	}
	class exceThread2 extends Thread {
		private AnQingShiCourt dghandl;
		
		public exceThread2(AnQingShiCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getUrl();
		}
	}
	class exceThread3 extends Thread {
		private ChaoFengCourt dghandl;
		
		public exceThread3(ChaoFengCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getUrl();
		}
	}
	class exceThread4 extends Thread {
		private CzzyCourt dghandl;
		
		public exceThread4(CzzyCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getInfo();
		}
	}
	class exceThread5 extends Thread {
		private HbzyCourt dghandl;
		
		public exceThread5(HbzyCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getInfo();
		}
	}
	class exceThread6 extends Thread {
		private HeFeiShiCourt dghandl;
		
		public exceThread6(HeFeiShiCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getUrl();;
		}
	}
	class exceThread7 extends Thread {
		private HuangShanZYCourt dghandl;
		
		public exceThread7(HuangShanZYCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getUrl();
		}
	}
	class exceThread8 extends Thread {
		private LuAnZYCourt dghandl;
		
		public exceThread8(LuAnZYCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getUrl();
		}
	}

	
}
