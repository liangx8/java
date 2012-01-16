package com.ruihui.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import com.ruihui.dao.utils.Order;

public abstract class GenericDaoImpl<T> implements GenericDao<T> {
	private final Log log=LogFactory.getLog(GenericDaoImpl.class);
	private final JdbcTemplate jdbcTemplate;
	private final SqlHelper<T> sqlHelper;
	private class InnerRowMapper implements RowMapper<T>{
		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			String msg1="可能出现的问题:Class 没有一个无参数的构造函数.";
			try {
				return sqlHelper.bindToObject(rs);
			} catch (InstantiationException e) {
				throw new SQLException(msg1,e);
			} catch (IllegalAccessException e) {
				throw new SQLException(msg1,e);
			} catch (IllegalArgumentException e) {
				throw new SQLException("可能出现的问题:set 方法不是可访问.",e);
			} catch (InvocationTargetException e) {
				throw new SQLException("可能是对象不正确",e);
			}
		}
	}
	private class InnerConverterRowMapper<C extends T> implements RowMapper<C>{
		private final Class<C> type;
		private final InnerRowMapper rm=new InnerRowMapper();
		public InnerConverterRowMapper(Class<C> t) {
			type=t;
		}
		@Override
		public C mapRow(ResultSet rs, int rowNum) throws SQLException {
			T src=rm.mapRow(rs, rowNum);
			C target=BeanUtils.instantiate(type);
			BeanUtils.copyProperties(src, target);
			return target;
		}
	}
	protected GenericDaoImpl(JdbcTemplate template){
		jdbcTemplate=template;
		sqlHelper=new SqlHelper<T>(entityClass());
	}
	protected abstract Class<T> entityClass();
	@Override
	public void insert(final T entity) {
		String sql=sqlHelper.sqlInsert(entity);
		PreparedStatementCallback<T> callback=new PreparedStatementCallback<T>() {
			@Override
			public T doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				List<Object> p=sqlHelper.getParams();
				for(int i=0;i<p.size();i++){
					ps.setObject(i+1, p.get(i));
				}
				ps.execute();
				return entity;
			}
		};
		log.info(sql);
		log.info(sqlHelper.getParams());
		jdbcTemplate.execute(sql,callback);
		sql=sqlHelper.sqlSelectLastId();
		Long lastId=jdbcTemplate.queryForObject(sql, Long.class);
		if(lastId == null){
			throw new RuntimeException(String.format("系统错误,插入一条记录以后没有id发生.(SQL:%s)",sql));
		}
		sqlHelper.populateId(entity,lastId);
	}

	@Override
	public T fetch(Long id) {
		String sql=sqlHelper.sqlSelect(id);
		RowMapper<T> rowMapper=new InnerRowMapper();
		return jdbcTemplate.queryForObject(sql, sqlHelper.getParams().toArray(), rowMapper);
	}

	@Override
	public List<T> all(Order order) {
		String sql=sqlHelper.sqlSelectAll(order);
		log.info(sql);
		return jdbcTemplate.query(sql, new InnerRowMapper());
	}

	@Override
	public List<T> list(String whereCondition) {
		String sql=sqlHelper.sqlSelectWithWhere(whereCondition);
		log.info(sql);
		return jdbcTemplate.query(sql, new InnerRowMapper());
	}
	@Override
	public List<T> list(String whereCondition, Object[] params) {
		String sql=sqlHelper.sqlSelectWithWhere(whereCondition);
		log.info(sql);
		log.info(String.format("[%s]", StringUtils.arrayToCommaDelimitedString(params)));
		return jdbcTemplate.query(sql, params, new InnerRowMapper());
	}
	@Override
	public void update(final T entity) {
		String sql=sqlHelper.sqlUpdate(entity);
		log.info(sql);
		log.info(sqlHelper.getParams());
		jdbcTemplate.execute(sql, new PreparedStatementCallback<T>(){
			@Override
			public T doInPreparedStatement(PreparedStatement pstm)
					throws SQLException, DataAccessException {
				List<Object> p=sqlHelper.getParams();
				for(int i=0;i<p.size();i++){
					pstm.setObject(i+1, p.get(i));
				}
				pstm.execute();
				return entity;
			}}
		);
		
	}
	@Override
	public T unique(String whereCondition) {
		String sql=sqlHelper.sqlSelectWithWhere(whereCondition);
		log.info(sql);
		return jdbcTemplate.queryForObject(sql, new InnerRowMapper());
	}
	@Override
	public <C extends T> List<C> list(String whereCondition, Object[] params, Class<C> type) {
		String sql=sqlHelper.sqlSelectWithWhere(whereCondition);
		log.info(sql);
		log.info(String.format("[%s]", StringUtils.arrayToCommaDelimitedString(params)));
		return jdbcTemplate.query(sql, params, new InnerConverterRowMapper<C>(type));
	}
	@Override
	public int count() {
		String sql=sqlHelper.sqlCountRecored();
		log.info(sql);
		return jdbcTemplate.queryForInt(sql);
	}
}
