package com.ruihui.erp;

public class ErpConstant {
	final public static String ROOT_DEPARTMMENT = "HEAD";
	/** 马达QC  */ final public static String DEPARTMENT_MOTOR_QC = "D_MTRQC";
	/** 厂务  */ final public static String DEPARTMENT_LGS   = "D_LGS";
	/** 出纳  */ final public static String DEPARTMENT_EPS   = "D_EPS";
	/** 清洁  */ final public static String DEPARTMENT_CLN   = "D_CLN";
	/** 保卫  */ final public static String DEPARTMENT_SEC   = "D_SEC";
	/** 业务  */ final public static String DEPARTMENT_BUSINESS   = "D_BUS";
	/** 测试  */ final public static String DEPARTMENT_TEST   = "D_TST";
	/** 包装  */ final public static String DEPARTMENT_PACKING   = "D_PKG";
	/** 贴片  */ final public static String DEPARTMENT_ATTACHE   = "D_ATH";
	/** 后焊  */ final public static String DEPARTMENT_HITTER   = "D_HIT";
	/** 杂工  */ final public static String DEPARTMENT_MISC   = "D_MISC";
	/** 质捡  */ final public static String DEPARTMENT_QC   = "D_QC";
	/** 技术  */ final public static String DEPARTMENT_TECH   = "D_TECH";
	/** 主管  */ final public static String DEPARTMENT_ADMIN   = "D_ADMIN";
	
	final public static String DEPARTMENTS[] = {
		ROOT_DEPARTMMENT,DEPARTMENT_MOTOR_QC,DEPARTMENT_LGS,DEPARTMENT_EPS,DEPARTMENT_CLN
		,DEPARTMENT_SEC,DEPARTMENT_BUSINESS,DEPARTMENT_TEST,DEPARTMENT_PACKING,DEPARTMENT_ATTACHE
		,DEPARTMENT_HITTER,DEPARTMENT_MISC,DEPARTMENT_QC,DEPARTMENT_TECH,DEPARTMENT_ADMIN
	};
	final public static String DEPT_DESCRIPTION[]={
		"公司","马达QC","厂务","出纳 ","清洁","保卫","业务","测试","包装","贴片","后焊","杂工","质捡","技术","主管"
	};
	
	/** 离职 */final public static int EMPLOYEE_STATUS_LEFT = 0;
	/** 在职 */final public static int EMPLOYEE_STATUS_DUTY = 1;
	/** 午餐 */final public static int MEAT_LUNCH = 1;
	/** 晚餐 */final public static int MEAT_SUPPER = 2;
	/** 男 */final public static Integer GENDER_MALE = 1;
	/** 女 */final public static Integer GENDER_FEMALE = 2;
}
