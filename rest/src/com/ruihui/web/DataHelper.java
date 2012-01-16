package com.ruihui.web;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import com.ruihui.common.text.TextObject;

public class DataHelper {
	final private JdbcTemplate jdbcTemplate;
	final private Log log;
	public DataHelper(JdbcTemplate t) {
		jdbcTemplate=t;
		log=LogFactory.getLog(DataHelper.class);
	}
	public TextObject getAll(){
		PreparedStatementCallback<TextObject> callBack;
		String sql="select * from erp_item";
		log.info(sql);
		callBack = new PreparedStatementCallback<TextObject>() {
			private TextObject tob=new TextObject("class").addAttribute("title", "Ä¿±ê"); 

			@Override
			public TextObject doInPreparedStatement(PreparedStatement stm)
					throws SQLException, DataAccessException {
				if(stm.execute()){
					ResultSet rs=stm.getResultSet();
					while(rs.next()){
						TextObject obj=new TextObject("item")
							.addAttribute("id", rs.getLong("id"))
							.addAttribute("code", rs.getString("code"))
							.addAttribute("name", rs.getString("name"))
							.addAttribute("type", rs.getString("type"))
							.addAttribute("created_date", rs.getTimestamp("created_date").getTime());
						tob.appendChild(obj);
					}
					return tob;
				}
				throw new SQLException("ResultSet was expected as result but it's not actually!");
			}
		};
		return jdbcTemplate.execute(sql,callBack);
	}
}
