package com.upg.zx.timer;

import java.util.TimerTask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.upg.zx.clientDaoImpl.BeanFactoryHelper;
import com.upg.zx.factory.CourtThreadOne;


@Component
public class SpideTimerOne extends TimerTask {
	
	
	private CourtThreadOne handl ;

	@Override
	@Scheduled(cron = "0 0 8,15 * * ?")
	public void run() {
		handl = BeanFactoryHelper.getBean("courtThreadOne");
		handl.startThread();
	}


}