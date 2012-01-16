package com.ruihui.erp.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ruihui.dao.GenericDaoImpl;
import com.ruihui.erp.entity.CodeMaster;

public class CodeMasterDaoImpl extends GenericDaoImpl<CodeMaster> implements CodeMasterDao {

	protected CodeMasterDaoImpl(JdbcTemplate template) {
		super(template);
	}

	@Override
	protected Class<CodeMaster> entityClass() {
		return CodeMaster.class;
	}

}
