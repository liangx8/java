package com.ruihui.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public interface SqlHelperDao {
	<T> List<T> query(String sql,RowMapper<T> rowMapper);
	void executeUpdate(String sql);
	<T> List<T> query(String sql, Object[] param, RowMapper<T> rowMapper);
}
