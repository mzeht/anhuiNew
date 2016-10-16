package com.upg.zx.timer;

import java.util.TimerTask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.upg.zx.clientDaoImpl.BeanFactoryHelper;
import com.upg.zx.factory.CourtThreadTwo;

@Component
public class SpideTimerTwo extends TimerTask {
	
	
	private CourtThreadTwo handl ;

	@Override
	@Scheduled(cron = "0 0 9,16 * * ?")
	public void run() {
		handl = BeanFactoryHelper.getBean("courtThreadTwo");
		handl.startThread();
	}
	
}

