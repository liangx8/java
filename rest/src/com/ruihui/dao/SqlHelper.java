package com.ruihui.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Blob;
import com.ruihui.common.utils.Builders;
import com.ruihui.common.utils.CollectionUtils;
import com.ruihui.common.utils.Pair;
import com.ruihui.dao.annotation.Field;
import com.ruihui.dao.annotation.Field.FieldType;
import com.ruihui.dao.annotation.Id;
import com.ruihui.dao.annotation.Table;
import com.ruihui.dao.utils.Order;

public class SqlHelper<T> {
	private final Class<T> clz;
	private final String dbTable;
	private final String idName;
	private final List<Object> params;
	public SqlHelper(Class<T> value) {
		clz=value;
		Annotation ann=clz.getAnnotation(Table.class);
		if(ann==null){
			throw new RuntimeException("Class is declared as a mapping object");
		}
		dbTable=((Table)ann).name();
		Method m[]=clz.getMethods();
		Id id=null;
		for(int i=0;i<m.length;i++){
			id=m[i].getAnnotation(Id.class);
			if(id != null)break;
		}
		if(id==null)
			throw new RuntimeException("");
		idName=id.name();
		params=CollectionUtils.emptyList();
	}
	public String sqlInsert(T o){
		StringBuilder sb=new StringBuilder("insert into ");
		sb.append(dbTable);
		sb.append(' ');
		List<Pair<String, Object>> pairs;
		try {
			pairs = parseField(o);
		} catch (Exception e) {
			throw new RuntimeException("发生错误的可能:1. Annotation 'Field' 必须定义在无参数,有返回的方法前面,2. 代理对象不对, 3. 无权访问方法",e);
		}
		StringBuilder sbField=new StringBuilder("(");
		StringBuilder sbValue=new StringBuilder("(");
		if(pairs.size()==0){
			throw new RuntimeException("object is empty!");
		}
		params.clear();
		for(Pair<String,Object> pair:pairs){
			sbField.append(pair.first());
			sbField.append(',');
			sbValue.append("?,");
			params.add(pair.second());
		}
		sbField.setLength(sbField.length()-1);
		sbValue.setLength(sbValue.length()-1);
		sbField.append(')');
		sbValue.append(')');
		sb.append(sbField);
		sb.append(" values ");
		sb.append(sbValue);
		return sb.toString();
	}
	public String sqlSelect(Long id) {
		StringBuilder sb=new StringBuilder("select * from ");
		sb.append(dbTable);
		if(id!=null){
			sb.append(" where ");
			sb.append(idName );
			sb.append(" = ?");
			params.clear();
			params.add(id);
		}
		return sb.toString();
	}
	public String sqlSelectAll(Order order) {
		StringBuilder sb=new StringBuilder("select * from ");
		sb.append(dbTable);
		if(order!=null){
			sb.append(' ');
			sb.append(order.toString());
		}
		return sb.toString();
	}
	public List<Object> getParams(){
		return params;
	}
	public T bindToObject(ResultSet rs) throws InstantiationException, IllegalAccessException, SQLException, IllegalArgumentException, InvocationTargetException {
		T inst=clz.newInstance();
		ResultSetMetaData metaData=rs.getMetaData();
		Method ms[]=clz.getMethods();
		for(int i=0;i<metaData.getColumnCount();i++){
			String fieldName=metaData.getColumnName(i+1);
			for(int j=0;j<ms.length;j++){
				Field f=ms[j].getAnnotation(Field.class);
				if(f==null) continue;
				if(f.type()==FieldType.GET)continue;
				if(fieldName.equals(f.name())){
					Class<?> types[]=ms[j].getParameterTypes();
					if(types.length!=1){
						throw new RuntimeException("set方法的参数不对");
					}
					ms[j].invoke(inst, fetchValue(types[0],i,rs));
				}
			}
		}
		return inst;
	}
	private List<Pair<String,Object>> parseField(T o) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method methods[]=clz.getMethods();
		List<Pair<String,Object>> pairs=CollectionUtils.emptyList();
		for(int i=0;i<methods.length;i++){
			Field f=methods[i].getAnnotation(Field.class);
			if(f==null) continue;
			if(f.type()==FieldType.SET)continue;
			Object val=methods[i].invoke(o);
			if(val!=null){
				Pair<String,Object> p=Builders.makePair(f.name(), val);
				pairs.add(p);
			}
		}
		return pairs;
	}

	/**
	 * 根据type转换rs中第i项的值
	 * @param type
	 * @param i
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static Object fetchValue(Class<?> type, int i, ResultSet rs) throws SQLException {
		if(type==String.class){
			return rs.getString(i+1);
		}
		if(type==Date.class){
			return rs.getTimestamp(i+1);
		}
		if(type==Clob.class){
			return rs.getClob(i+1);
		}
		if(type==Blob.class){
			return rs.getBlob(i+1);
		}
		if(Number.class.isAssignableFrom(type)){
			Object val=rs.getObject(i+1);
			if(val==null)return null;
			if(type==val.getClass()){
				return val;
			}
			Number n=(Number) val;
			if(type==Short.class){
				return n.shortValue();
			}
			if(type==Integer.class){
				return n.intValue();
			}
			if(type==Long.class){
				return n.longValue();
			}
			if(type==Float.class){
				return n.floatValue();
			}
			if(type==Double.class){
				return n.doubleValue();
			}
			if(type==Byte.class){
				return n.byteValue();
			}
			throw new RuntimeException("unsuported type "+type.getName());
		}
		return null;
	}
	public String sqlSelectWithWhere(String whereCondition) {
		StringBuilder sb=new StringBuilder("select * from ");
		sb.append(dbTable);
		sb.append(' ');
		sb.append(whereCondition);
		return sb.toString();
	}
	public String sqlSelectLastId() {
		return "select max("+idName+") from "+dbTable;
	}
	public void populateId(T entity,Long id){
		Method ms[]=clz.getMethods();
		for(int i=0;i<ms.length;i++){
			Field f=ms[i].getAnnotation(Field.class);
			if(f==null)continue;
			if(f.name().equals(idName) && f.type()==FieldType.SET){
				try {
					ms[i].invoke(entity, id);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	public String sqlUpdate(T entity){
		
		StringBuilder sb=new StringBuilder("update ");
		sb.append(dbTable);
		sb.append(" set ");
		Method ms[]=clz.getMethods();
		Long entityId=null;
		params.clear();
		try {
			
			for(int i=0;i<ms.length;i++){
				Id id=ms[i].getAnnotation(Id.class);
				if(id!=null){
					entityId=(Long) ms[i].invoke(entity);
					continue;
				}
				Field f=ms[i].getAnnotation(Field.class);
				if(f==null)continue;
				if(f.type()==FieldType.SET)continue;
				Object retVal=ms[i].invoke(entity);
				if(retVal==null)continue;
				sb.append(f.name());
				sb.append("=?,");
				params.add(retVal);
			}
		} catch (IllegalArgumentException e) {
			// wrong bean definition occurred this exception  
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// wrong bean definition occurred this exception  
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("对象与类定义不相符",e);
		}
		if(entityId==null){
			throw new RuntimeException("a id should be specify");
		}
		sb.setLength(sb.length()-1);
		sb.append(" where ");
		sb.append(idName);
		sb.append("= ?");
		params.add(entityId);
		return sb.toString();
	}
	public String sqlCountRecored() {
		StringBuilder sb=new StringBuilder("select count(1) from ");
		sb.append(dbTable);
		return sb.toString();
	}
	
}
