package com.ym.db;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;

/**
 * 
 * 用于连接数据库配置 - 驱动+JDBC数据库连接池 
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
	
	
	// 准备连接数据源C3P0
	static {
		try {
			
			ComboPooledDataSource pool = new ComboPooledDataSource();
			pool.setDriverClass(driverName);
			pool.setUser(userName);
			pool.setPassword(userPwd);
			pool.setJdbcUrl(url);
			pool.setMaxPoolSize(30);	//最大最小连接池数
			pool.setMinPoolSize(5);
			datasource = pool;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.print("数据库连接池加载失败!");
		}
	}
	
	// 通过这个方法获得Connection对象
	public static Connection getConnection() throws SQLException{
		
		return datasource.getConnection();
		
	}
		
	
}
