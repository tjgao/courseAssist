package org.courseAssist.utils;

import java.awt.color.CMMException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;


public class CommonUtils {
	public static String getFileNameExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		if (i > 0)
			return fileName.substring(i + 1);
		return "";
	}
	
	public static String getFileNameBase(String fileName) {
		int i = fileName.lastIndexOf(File.separator);
		int j = fileName.lastIndexOf('.');
		if( j > 0 ) return fileName.substring(i+1,j);
		return fileName.substring(i+1);
	}

	public static String md5(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void cleanDirectory(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		File[] files = dir.listFiles();
		for (File i : files) {
			if (i.isFile())
				i.delete();
			else if (i.isDirectory()) {
				cleanDirectory(i);
				i.delete();
			}
		}
	}

	public static void cleanDirectory(String dir) {
		File f = new File(dir);
		cleanDirectory(f);
	}

	public static boolean createDirs(String dirs) {
		File f = new File(dirs);
		return f.mkdirs();
	}
	
	public static boolean moveFile(String srcFile, String dstDir) {
		try {
			File src = new File(srcFile);
			File dst = new File(dstDir + File.separator + src.getName());
			return src.renameTo(dst);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getTempDir() {
		String t = System.getProperties().getProperty("java.io.tmpdir")
				+ File.separator + "_TMP_TJ";
		File f = new File(t);
		if (f.exists()) {
			if (f.isFile())
				f.delete();
		} else {
			f.mkdir();
		}
		return t;
	}
	
	public static void downloadImg(String url, String dst) throws Exception {
		URL u = new URL(url);
		BufferedImage img = ImageIO.read(u);
		ImageIO.write(img, "jpg", new File(dst));
	}

	
	public static void thumbnail(String src, String dst, int width, int height)
			throws Exception {
		File fsrc = new File(src);
		String ext = getFileNameExtension(src).toLowerCase();
		BufferedImage img = null;
		try {
			img = ImageIO.read(fsrc);
		} catch (CMMException ce) {
			ce.printStackTrace();
			return;
		}
		//crop the image
		int w = img.getWidth();
		int h = img.getHeight();
	     
		int x = ( w < h) ? 0 : ( w - h )/2;
		int y = ( h < w) ? 0 : ( h - w )/2;
		int m = (w > h) ? h : w;
		BufferedImage cropped = img.getSubimage(x, y, m, m);
		
		BufferedImage thumbnail = Scalr.resize(cropped, Method.QUALITY,
				Mode.AUTOMATIC, width, height, Scalr.OP_ANTIALIAS);
		File fdst = new File(dst);
		ImageIO.write(thumbnail, ext, fdst);
	}
	
	public static String getIp(HttpServletRequest req) {
		String ip = req.getHeader("X-Forwarded-For");
		if( ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
			int idx = ip.indexOf(',');
			if( idx != -1 ) return ip.substring(0, idx);
			else return ip;
		}
		ip = req.getHeader("X-Real-IP");
		if( ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip))
			return ip;
		return req.getRemoteAddr();
	}
	
	public static long ip2int(String ip) {
		long val = 0;
		String[] slices = ip.split("\\.");
		if( slices == null || slices.length < 4 ) return 0;
		for( String s : slices ) {
			val *= 256;
			val += Integer.parseInt(s); 
		}
		return val;
	}
}
