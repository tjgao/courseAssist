package org.courseAssist;
import java.io.File;
import java.util.HashMap;

public class AppConfig extends HashMap<String, Object> {
	public static final String bookingTime = "bookintTime";
	public static final String uploadDir = "resources" + File.separator + "sessionWare";
	private static final AppConfig m = new AppConfig();
	private AppConfig(){}
	
	public static AppConfig getConfig() { return m; }
	
	public Object put(String k, Object v) {
		if( k == null || v == null ) return null;
		if( super.get(k) != null ) return null; //once put, exists and does not change forever
		return super.put(k, v);
	}
}
