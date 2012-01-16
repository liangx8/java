package com.ruihui.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class SqlHelperDaoImpl implements SqlHelperDao {
	private final JdbcTemplate jdbcTemplate;
	public SqlHelperDaoImpl(JdbcTemplate template) {
		jdbcTemplate=template;
	}
	@Override
	public <T> List<T> query(String sql, Object[] param,RowMapper<T> mapper) {
		return jdbcTemplate.query(sql, param,mapper );
	}
	@Override
	public <T> List<T> query(String sql,RowMapper<T> rowMapper) {
		return jdbcTemplate.query(sql,rowMapper);
	}
	@Override
	public void executeUpdate(String sql) {
		jdbcTemplate.execute(sql);
	}

}
