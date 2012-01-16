package com.ruihui.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ruihui.common.excel.ExcelProcess;
import com.ruihui.common.excel.HSSFExcelProcess;
import com.ruihui.dao.annotation.Table;
import com.ruihui.erp.ErpConstant;
import com.ruihui.erp.entity.Employee;

public class AnnoTest {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		excel();
		annotation();
		//dbInit();
		
	}
	static SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");

	private static void excel() throws IOException, ClassNotFoundException, SQLException {
		FileInputStream in=new FileInputStream("e:/employee.xls");
		ExcelProcess xls=new HSSFExcelProcess(in);
		int rowIndex=1;
		Class.forName("com.mysql.jdbc.Driver");
		Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/rh_erp?characterEncoding=utf8","root","");
		Statement stm=c.createStatement();
		while(true){
			String name=xls.getStringValue(rowIndex, 1);
			if(name==null)break;
			if(name.trim().length()==0)break;
			name=name.trim();
			String code=String.format("%05d", rowIndex);
			String dept=xls.getStringValue(rowIndex, 2);
			for(int i=0;i<ErpConstant.DEPT_DESCRIPTION.length;i++){
				if(ErpConstant.DEPT_DESCRIPTION[i].equals(dept)){
					dept=ErpConstant.DEPARTMENTS[i];
					break;
				}
			}
			String personId=xls.getStringValue(rowIndex, 3);
			Date enterDate=xls.getDateValue(rowIndex, 4);
			Double re=xls.getNumberValue(rowIndex, 5);
			
			
			String remark=null;
			if(re.doubleValue()>.1){
				remark=String.format("%5.0f", re);
			}
			StringBuilder sb=new StringBuilder("insert into erp_employee (emp_code,emp_name,department");
			StringBuilder values=new StringBuilder(String.format("('%s','%s','%s'", code,name,dept));
			if(personId != null && personId.trim().length()>0){
				sb.append(",person_id");
				values.append(",'"+personId+"'");
			}
			if(remark != null && remark.trim().length()>0){
				sb.append(",remark");
				values.append(",'"+remark+"'");
			}
			sb.append(')');
			values.append(')');
			sb.append(" values ");
			sb.append(values);
			System.out.println(sb.toString());
			stm.execute(sb.toString());
			String sql = "select id from erp_employee where emp_code = '"+code+"'";
			System.out.println(sql);
			ResultSet rs=stm.executeQuery(sql);
			rs.next();
			long id=rs.getLong("id");
			// 增加历史
			sql=String.format("insert into erp_emp_transfer (emp_id,joined_date,department) values (%d,'%s','HEAD')",id,df.format(enterDate));
			stm.execute(sql);
			sql=String.format("insert into erp_emp_transfer (emp_id,joined_date,department) values (%d,'%s','%s')",id,df.format(enterDate),dept);
			stm.execute(sql);
			rowIndex++;
		}
		in.close();
	}

	private static void dbInit() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/rh_erp?characterEncoding=utf8","root","");
		Statement stm=c.createStatement();
		for(int i=0;i<ErpConstant.DEPARTMENTS.length;i++){
			String sql="insert into erp_code_master (category,abbr,full_desc) values ('DEPT','"
					+ErpConstant.DEPARTMENTS[i]+"','"
					+ErpConstant.DEPT_DESCRIPTION[i]+"')";
			System.out.println(sql);
			stm.execute(sql);
		}
	}

	private static void annotation() {
		Class<Employee> clz=Employee.class;
		Annotation a=clz.getAnnotation(Table.class);
		System.out.println(a);
	}
	
}
