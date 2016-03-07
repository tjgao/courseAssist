package org.courseAssist.service;

import java.util.List;

import org.courseAssist.mapper.InformationMapper;
import org.courseAssist.model.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InformationService {
	@Autowired
	private InformationMapper iMapper;
	

	public List<Information>  listInfo(int uniid, int deptid, int start, int limit) {
		return iMapper.getInformation(uniid, deptid,  start, limit);
	}
	
	public int countInfo(int uniid, int deptid) {
		return iMapper.getInformationCount(uniid, deptid);
	}
	
	public List<Information> listInfo2(int uniid, int start, int limit ) {
		return iMapper.getInformation2(uniid, start, limit);
	}
	
	public int countInfo2( int uniid ) {
		return iMapper.getInformationCount2(uniid);
	}
	
	public void insertInfo(Information i) {
		iMapper.insertInfo(i);
	}
	
	public void updateInfo(Information i) {
		iMapper.updateInfo(i);
	}
	
	public void deleteInfo(int id) {
		iMapper.deleteInfo(id);
	}
	
	public Information getinfo(int iid ) {
		return iMapper.getinfo(iid);
	}
}
