package com.upg.zx.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import com.upg.zx.clientDaoImpl.BeanFactoryHelper;
import com.upg.zx.factory.CourtThreadOne.exceThread1;
import com.upg.zx.service.AnHuiCourt;
import com.upg.zx.service.MaanshanCourt;
import com.upg.zx.service.NingGuoShiCourt;
import com.upg.zx.service.SuZhouShiCourt;
import com.upg.zx.service.TongLingShiCourt;
import com.upg.zx.service.WuHuShiCourt;
import com.upg.zx.service.XuanChengShiCourt;
import com.upg.zx.service.YueXiCourt;


@Component
public class CourtThreadTwo {
NingGuoShiCourt ningGuoShiCourt;
SuZhouShiCourt suZhouShiCourt;
TongLingShiCourt tongLingShiCourt;
WuHuShiCourt wuHuShiCourt;
XuanChengShiCourt xuanChengShiCourt;
YueXiCourt yueXiCourt;
MaanshanCourt maanshanCourt;

	public void startThread() {
		ningGuoShiCourt = BeanFactoryHelper.getBean("ningGuoShiCourt");
		suZhouShiCourt = BeanFactoryHelper.getBean("suZhouShiCourt");
		tongLingShiCourt = BeanFactoryHelper.getBean("tongLingShiCourt");
		wuHuShiCourt = BeanFactoryHelper.getBean("wuHuShiCourt");
		xuanChengShiCourt = BeanFactoryHelper.getBean("xuanChengShiCourt");
		yueXiCourt = BeanFactoryHelper.getBean("yueXiCourt");
		maanshanCourt = BeanFactoryHelper.getBean("maanshanCourt");
		
		ExecutorService pool = Executors.newFixedThreadPool(10);
		exceThread1 thread1 = new exceThread1(ningGuoShiCourt);
		exceThread2 thread2 = new exceThread2(suZhouShiCourt);
		exceThread3 thread3 = new exceThread3(tongLingShiCourt);
		exceThread4 thread4 = new exceThread4(wuHuShiCourt);
		exceThread5 thread5 = new exceThread5(xuanChengShiCourt);
		exceThread6 thread6 = new exceThread6(yueXiCourt);
		exceThread7 thread7 = new exceThread7(maanshanCourt);
		
		pool.submit(thread1);
		pool.submit(thread2);
		pool.submit(thread3);
		pool.submit(thread4);
		pool.submit(thread5);
		pool.submit(thread6);
		pool.submit(thread7);
		
	}

	class exceThread1 extends Thread {
		private NingGuoShiCourt dghandl;

		public exceThread1(NingGuoShiCourt dghandl) {

			this.dghandl = dghandl;
		}

		public void run() {
			dghandl.getUrl();
		}
	}
	class exceThread2 extends Thread {
		private SuZhouShiCourt dghandl;
		
		public exceThread2(SuZhouShiCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getUrl();
		}
	}
	class exceThread3 extends Thread {
		private TongLingShiCourt dghandl;
		
		public exceThread3(TongLingShiCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.geturl();
		}
	}
	class exceThread4 extends Thread {
		private WuHuShiCourt dghandl;
		
		public exceThread4(WuHuShiCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getUrl();
		}
	}
	class exceThread5 extends Thread {
		private XuanChengShiCourt dghandl;
		
		public exceThread5(XuanChengShiCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.geturl();
		}
	}
	class exceThread6 extends Thread {
		private YueXiCourt dghandl;
		
		public exceThread6(YueXiCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.pageTurn();
		}
	}
	class exceThread7 extends Thread {
		private MaanshanCourt dghandl;
		
		public exceThread7(MaanshanCourt dghandl) {
			
			this.dghandl = dghandl;
		}
		
		public void run() {
			dghandl.getHtml();
		}
	}
}
