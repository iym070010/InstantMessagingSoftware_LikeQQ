package com.ym.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;

/**
 * 
 * �����������ݿ����� - ����+JDBC���ݿ����ӳ� 
 * @author 62473
 *
 */


public class DBmanage {

	public static final String driverName="com.mysql.jdbc.Driver";
	public static final String userName="root";
	public static final String userPwd="MySQL@123";
	public static final String dbName = "qq";
	public static final String url = "jdbc:mysql://localhost:3306/" + dbName;
	public static DataSource datasource = null;
	
	
	// ׼����������ԴC3P0
	static {
		try {
			
			ComboPooledDataSource pool = new ComboPooledDataSource();
			pool.setDriverClass(driverName);
			pool.setUser(userName);
			pool.setPassword(userPwd);
			pool.setJdbcUrl(url);
			pool.setMaxPoolSize(30);	//�����С���ӳ���
			pool.setMinPoolSize(5);
			datasource = pool;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.print("���ݿ����ӳؼ���ʧ��!");
		}
	}
	
	// ͨ������������Connection����
	public static Connection getConnection() throws SQLException{
		
		return datasource.getConnection();
		
	}
		
	
}
