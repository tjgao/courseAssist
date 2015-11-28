package org.courseAssist.service;

import java.util.List;

import org.courseAssist.mapper.QRAuthMapper;
import org.courseAssist.model.QRAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QRAuthService {
	@Autowired
	QRAuthMapper mapper;
	public QRAuth getQRAuthByCode(String code) {
		return mapper.getQRAuthByCode(code);
	}
	
	public void updateQRAuthByCode(QRAuth q) {
		mapper.updateQRAuth(q);
	}
	
	public void insertQRAuth(QRAuth q) {
		mapper.insertQRAuthByCode(q);
	}
	
	public void cleanQRAuth(String code) {
		mapper.cleanQRAuth(code);
	}
	
	public QRAuth getQRAuthBySid(int sid, int seconds) {
		return mapper.getQRAuthBySid(sid, seconds);
	}
}
