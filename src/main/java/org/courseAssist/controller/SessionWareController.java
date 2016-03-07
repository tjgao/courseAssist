package org.courseAssist.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.courseAssist.AppConfig;
import org.courseAssist.model.SessionWare;
import org.courseAssist.model.User;
import org.courseAssist.service.CourseSessionService;
import org.courseAssist.service.SessionWareService;
import org.courseAssist.service.UserService;
import org.courseAssist.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SessionWareController {
	@Autowired
	SessionWareService swService;
	
	@Autowired
	CourseSessionService csService;
	
	@Autowired
	UserService uService;

	@Autowired
	ServletContext servletCtx;	

	private static final Logger logger = LoggerFactory.getLogger(SessionWareController.class);
	
	@RequestMapping(value="/api/sessionWare/{sid}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> getAllSessionWare(@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			List<SessionWare> l = swService.getAllSessionWare(sid);
			h.put("data", l);
			h.put("count", l.size());
			h.put("code", 0);
		} catch(Exception e) {
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/sessionWare/download/{id}", method=RequestMethod.GET)
	public @ResponseBody void downloadSessionWare(HttpServletResponse response, @PathVariable("id") int id) throws Exception {
		OutputStream os = null;
		try{
			SessionWare sw = swService.getSessionWare(id);
			if( sw == null ) return;
			String path = servletCtx.getRealPath("/") + AppConfig.uploadDir + File.separator;
			File f = new File(path);
			if( !f.exists() ) f.mkdirs();
			response.addHeader("Content-Disposition", "attachment; filename=\"" + sw.getFilename() + "\"");
			response.setContentType("application/octet-stream");
			path += sw.getFilename();
			os = response.getOutputStream();
			FileInputStream fis = new FileInputStream(path);
			byte[] buf = new byte[10240];
			int len = 0;
			while( ( len = fis.read(buf)) > 0 ) {
				os.write(buf, 0, len);
			}	
		} catch(Exception e) {
			logger.info(e.toString());
		} finally{
			if(os != null ) {
				os.flush();
				os.close();
			}
		}
	}
	
	@RequestMapping(value="/api/sessionWare/query/{sid}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> warelist(@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap();
		try{
			List<SessionWare> lsw = swService.getAllSessionWare(sid);
			h.put("code", 0);
			h.put("count", lsw.size());
			h.put("data", lsw);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	private boolean hasRightDel( int uid, int csId ) {
		User u = uService.getUserById(uid);
		if( u == null ) return false;
		if( u != null && u.getPriv() != 0 ) return true;
		else if( u != null ) {
			SessionWare sw = swService.getSessionWare(csId);
			if( sw == null ) return false;
			if( csService.islecturer(uid, sw.getSid()) != 0 ) return true;
		}
		return false;
	}
	
	private boolean hasRightUpload( int uid, int csId ) {
		User u = uService.getUserById(uid);
		if( u == null ) return false;
		if( u != null && u.getPriv() != 0 ) return true;
		else if( u != null ) {
			if( csService.islecturer(uid, csId) != 0 ) return true;
		}
		return false;
	}
	
	@RequestMapping(value="/api/sessionWare/delete/{csId}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> delete(HttpServletRequest req, @PathVariable("csId") int csId) {
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("code", 1);
		try {
			if( hasRightDel( uid, csId )) {
				SessionWare sw = swService.getcw(csId);
				swService.deleteSessionWare(csId);
				h.put("code", 0);
				// delete the file
				String full = servletCtx.getRealPath("/") + File.separator + AppConfig.uploadDir + File.separator + sw.getFilename();
				try {
					File f = new File(full);
					f.delete();
				} catch(Exception e) { 
					logger.debug(e.toString());
				}
			} else 
				h.put("msg", "无权限修改课件");
		} catch(Exception e) {
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	@RequestMapping(value="/api/uploadpic", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> uploadpic(HttpServletRequest req, @RequestParam("file") MultipartFile file,
			@RequestParam("ext") String ext) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("code", 1);
		if( !file.isEmpty()) {
			try{
				int uid = Integer.parseInt((String)req.getAttribute("uid"));
				User u = uService.getUserById(uid);
				if( u == null || u.getPriv() == 0 ) {
					h.put("code", 1);
					h.put("msg", "无上传权限");
					return h;
				}
				Date now = new Date();
				String fn = "pic_" + CommonUtils.md5(now.toString()) + ext;
				String full = servletCtx.getRealPath("/") + File.separator + AppConfig.uploadDir + File.separator + fn;
				file.transferTo(new File(full));
				h.put("url", AppConfig.uploadDir + File.separator + fn);
				h.put("code", 0);
			} catch(Exception e) {
				h.put("code", 1);
				h.put("msg", "上传中发生异常");
			}
		}
		return h;
	}
	
	@RequestMapping(value = "/api/sessionWare/upload/{sid}", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> upload(
			HttpServletRequest req,
			@RequestParam("file") MultipartFile file, @PathVariable("sid") int sid, 
			@RequestParam(value="description", required=false) String description, 
			@RequestParam("name") String name,
			@RequestParam("ext") String ext) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if (!file.isEmpty()) {
			try {
				int uid = Integer.parseInt((String)req.getAttribute("uid"));
				if( !hasRightUpload(uid, sid) ) {
					h.put("code", 1);
					h.put("msg", "无上传权限");
					return h;
				}
				if( ext == null || ext.isEmpty() ) ext = ".pptx";
				Date now = new Date();
				String fn = sid + "_" + CommonUtils.md5(now.toString()) + ext;
				String full = servletCtx.getRealPath("/") + File.separator + AppConfig.uploadDir + File.separator + fn;
				logger.debug(full);
				file.transferTo(new File(full));
				SessionWare s = new SessionWare();
				s.setSid(sid);
				s.setName(name);
				s.setDescription(description);
				s.setFilename(AppConfig.uploadDir + File.separator + fn);
				s.setUid(uid);
				swService.uploadSessionWare(s);
				h.put("code", 0);
			} catch (Exception e) {
				e.printStackTrace();
				h.put("code", 2);
				h.put("msg", "操作中发生异常！");
			}
		} else {
			h.put("code", 1);
			h.put("msg", "上传失败！");
		}
		return h;
	}
	
	@RequestMapping(value="/errorSize")
	public @ResponseBody HashMap<String, Object> errorSize() {
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("code", 1);
		h.put("msg", "文件尺寸超过了限制!");
		return h;
	}
	
	public @ResponseBody ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object Handler, 
			Exception exception) {
		if( exception instanceof MaxUploadSizeExceededException ) {
			ModelAndView mv = new ModelAndView("redirect:/errorSize");
			return mv;
		}
		exception.printStackTrace();
		return new ModelAndView("500");
	}	
}
