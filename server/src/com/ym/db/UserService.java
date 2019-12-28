package com.ym.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;



/**
 * 数据库查询逻辑
 * 
 * @author 黄龙士
 *
 *@throws UsernameNotFoundException:用户不存在
 *	PasswordNotFoundException 密码错误
 *	StateException 账户锁定
 *	SQLException 数据库连接失败
 */


public class UserService {

	// 登陆服务
	public String login(String qqnumber,String password) 	//可以修改参数变成多种方法形式登陆
			throws UsernameNotFoundException,PasswordNotFoundException,StateException,SQLException{
		
		Connection conn =null;
		try {
			
			conn = DBmanage.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE qqnumber=?");	//没有做防sql注入
			pst.setString(1, qqnumber);
			ResultSet rs = pst.executeQuery();	//查询得到的结果
			if(rs.next()) {
				if(rs.getInt("state") == 0) {	//0就是正常登陆
					
					if(rs.getString("password").equals(password)) {
						return rs.getString(1);		//登陆成功返回uid
					}else {
						throw new PasswordNotFoundException();
					}

				}else {
					throw new StateException();
				}
			}else {
				throw new UsernameNotFoundException();
			}
			
			
		} catch (SQLException e) {
			throw e;
		} finally {
			conn.close();
		}
		
	}
	
	// 获得好友列表
	public Vector<UserInfo> getFriendList(String uid) throws SQLException{
		
		Connection conn =null;
		try {
			
			conn = DBmanage.getConnection();
			PreparedStatement pst = conn.prepareStatement("select u.uid,u.img,u.netname,u.info,u.qqnumber from users u inner join friendship f on u.uid=f.friendid and f.uid=?");	//没有做防sql注入
			pst.setString(1, uid);
			ResultSet rs = pst.executeQuery();	//查询得到的结果
			Vector<UserInfo> vector = new Vector<>();
			while(rs.next()) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUid(rs.getString(1));
				userInfo.setImg(rs.getString(2));
				userInfo.setNetname(rs.getString(3));
				userInfo.setInfo(rs.getString(4));
				userInfo.setQqnumber(rs.getString(5));
				vector.add(userInfo);
			}
			return vector;
		} catch (SQLException e) {
			// TODO: handle exception
			throw e;
		} finally {
			conn.close();
		}
		
	}
	
	// 个人资料查询 好友资料查询
	public UserInfo2 getPersonality(String uid) throws SQLException{

		Connection conn =null;
		try {
			
			conn = DBmanage.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM personality where uid=?;");	//没有做防sql注入
			pst.setString(1, uid);
			ResultSet rs = pst.executeQuery();	//查询得到的结果
			UserInfo2 userInfo = new UserInfo2();
			if(rs.next()) {
				userInfo.setUid(rs.getString("uid"));
				userInfo.setNetname(rs.getString("netname"));
				userInfo.setInfo(rs.getString("info"));
				userInfo.setImg(rs.getString("img"));
				userInfo.setBack(rs.getString("back"));
				userInfo.setBloodtype(rs.getString("bloodtype"));
				userInfo.setEmail(rs.getString("email"));
				userInfo.setDd(rs.getString("dd"));
				userInfo.setMm(rs.getString("mm"));
				userInfo.setYy(rs.getString("yy"));
				userInfo.setGend(rs.getString("gend"));
				userInfo.setRelation(rs.getString("relation"));
				userInfo.setPhonenumber(rs.getString("phonenumber"));
				userInfo.setProfession(rs.getString("profession"));
				userInfo.setRealname(rs.getString("realname"));
				userInfo.setQqnumber(rs.getString("qqnumber"));
			}
			return userInfo;
		} catch (SQLException e) {
			// TODO: handle exception
			throw e;
		} finally {
			conn.close();
		}
		
	}
	
	// 注册账户
	public String regUser(String username,String password) throws UsernameException,SQLException{
		
		Connection conn =null;
		try {
			
			conn = DBmanage.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM personality where phonenumber=? or email=?;");	//没有做防sql注入
			pst.setString(1, username);
			pst.setString(2, username);
			ResultSet rs = pst.executeQuery();	//查询得到的结果
			if(rs.next()) {
				throw new UsernameException();	// 若存在该手机/email 则报错
			}
			
			if(username.indexOf("@")>=0) {
				pst = conn.prepareStatement("insert into personality(uid,email,qqnumber) values(?,?,?)");	//更新个人资料 - 邮箱
			}else if(username.trim().length()==11){
				pst = conn.prepareStatement("insert into personality(uid,phonenumber,qqnumber) values(?,?,?)");	//更新个人资料 - 手机
			}
			String uid = System.currentTimeMillis()+"R"+(int)(Math.random()*10000);		//uid随机生成
			String qqnumber = (int)(Math.random()*100000)+""+System.currentTimeMillis()%10000;	//随机生成九位qq号
			pst.setString(1, uid);
			pst.setString(2, username);
			pst.setString(3,qqnumber);
			if (pst.executeUpdate()<=0) {
				throw new SQLException();
			}
			
			pst = conn.prepareStatement("insert into users(uid,password,createtime,qqnumber) values(?,?,sysdate(),?)");	//更新用户表
			pst.setString(1, uid);
			pst.setString(2, password);
			pst.setString(3, qqnumber);
			if (pst.executeUpdate()<=0) {
				throw new SQLException();
			}
			
			int logid = (int)(Math.random()*1000000);
			pst = conn.prepareStatement("insert into friendship(logid,uid,friendid,createtime) values(?,?,?,sysdate())");	//默认跟10001账号是好友
			pst.setInt(1, logid);
			pst.setString(2, uid);
			pst.setString(3, "10001");
			if (pst.executeUpdate()<=0) {
				throw new SQLException();
			}
			logid = (int)(Math.random()*1000000);
			pst = conn.prepareStatement("insert into friendship(logid,uid,friendid,createtime) values(?,?,?,sysdate())");	//默认跟10001账号是好友
			pst.setInt(1, logid);
			pst.setString(2, "10001");
			pst.setString(3, uid);
			if (pst.executeUpdate()<=0) {
				throw new SQLException();
			}
			
			return qqnumber;
		} catch (SQLException e) {
			throw e;
		} finally {
			conn.close();
		}
		
	}
	
	public String recomePassword(String qqnumber,String username) throws UsernameException,SQLException{		
		Connection conn =null;
		try {
			conn = DBmanage.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM personality where (phonenumber=? or email=?) and qqnumber=?;");
			pst.setString(1, username);
			pst.setString(2, username);
			pst.setString(3, qqnumber);
			ResultSet rs = pst.executeQuery();	//查询得到的结果
			String password = null;
			if(rs.next()) {
				pst = conn.prepareStatement("SELECT * FROM users where qqnumber=?;");
				pst.setString(1, qqnumber);
				rs = pst.executeQuery();	//查询得到的结果
				rs.next();
				password = rs.getString("password");
			}else {
				throw new UsernameException();	// 若不存在该qqnumber or qq号与验证账户不匹配
			}
			return password;	
		} catch (SQLException e) {
			throw e;
		} finally {
			conn.close();
		}
	}
	
//	public String searchFriend(String qqnumber) {
//		
//		Connection conn =null;
//		try {
//			conn = DBmanage.getConnection();
//			PreparedStatement pst = conn.prepareStatement("SELECT * FROM personality where (phonenumber=? or email=?) and qqnumber=?;");
//			pst.setString(1, username);
//			pst.setString(2, username);
//			pst.setString(3, qqnumber);
//			ResultSet rs = pst.executeQuery();	//查询得到的结果
//			String username = null;
//			if(rs.next()) {
//				pst = conn.prepareStatement("SELECT * FROM users where qqnumber=?;");
//				pst.setString(1, qqnumber);
//				rs = pst.executeQuery();	//查询得到的结果
//				rs.next();
//				password = rs.getString("password");
//			}else {
//				throw new UsernameException();	// 若不存在该qqnumber or qq号与验证账户不匹配
//			}
//			return username;	
//		} catch (SQLException e) {
//			throw e;
//		} finally {
//			conn.close();
//		}
//		
//	}
	
	public static void main(String[] args) {
		try {
			String test = new UserService().recomePassword("624730725","17792593092");
			System.out.print(test);
		} catch (UsernameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
