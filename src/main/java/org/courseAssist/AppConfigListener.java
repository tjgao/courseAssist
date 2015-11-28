package org.courseAssist;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.courseAssist.utils.CommonUtils;


public class AppConfigListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		ServletContext sc = arg0.getServletContext();
		AppConfig cfg = AppConfig.getConfig();
		try{
			Integer seconds = Integer.parseInt(sc.getInitParameter(AppConfig.bookingTime));
			cfg.put(AppConfig.bookingTime, seconds);
		} catch(Exception e) {
			cfg.put(AppConfig.bookingTime, 10800);
		}
		
		// create directories if not exist
		try{
			CommonUtils.createDirs(AppConfig.uploadDir);
			CommonUtils.createDirs(AppConfig.headDir);
		} catch(Exception e) {
		}
	}

}
