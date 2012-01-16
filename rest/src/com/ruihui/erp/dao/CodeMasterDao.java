package com.ruihui.erp.dao;

import org.springframework.transaction.annotation.Transactional;

import com.ruihui.dao.GenericDao;
import com.ruihui.erp.entity.CodeMaster;
@Transactional(readOnly=true)
public interface CodeMasterDao extends GenericDao<CodeMaster> {
	
}
