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
	

	public List<Information>  getInformation(int uniid, int deptid, int type, int start, int limit) {
		return iMapper.getInformation(uniid, deptid, type, start, limit);
	}
}
