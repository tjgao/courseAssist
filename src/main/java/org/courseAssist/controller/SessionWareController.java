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
import org.courseAssist.service.SessionWareService;
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
	
	@RequestMapping(value = "/api/sessionWare/upload/{sid}/{uid}", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> upload(
			@RequestParam("file") MultipartFile file, @PathVariable("sid") int sid, 
			@PathVariable("uid") int uid, @RequestParam(value="description", required=false) String description, 
			@RequestParam("filename") String filename, @RequestParam("name") String name) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if (!file.isEmpty()) {
			try {
				StringBuffer sb = new StringBuffer();
				String fn = file.getName();
				String ext = CommonUtils.getFileNameExtension(fn);
				String base = CommonUtils.getFileNameBase(fn);
				Date now = new Date();
				sb.append(base);
				sb.append("-");
				sb.append(CommonUtils.md5(now.toString()));
				sb.append(".");
				sb.append(ext);
				file.transferTo(new File(AppConfig.uploadDir + File.separator + sb.toString()));
				SessionWare s = new SessionWare();
				s.setSid(sid);
				s.setName(name);
				s.setDescription(description);
				s.setFilename(filename);
				swService.uploadSessionWare(s);
				h.put("code", 0);
			} catch (Exception e) {
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
