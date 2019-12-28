package com.ym.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;



/**
 * ���ݿ��ѯ�߼�
 * 
 * @author ����ʿ
 *
 *@throws UsernameNotFoundException:�û�������
 *	PasswordNotFoundException �������
 *	StateException �˻�����
 *	SQLException ���ݿ�����ʧ��
 */


public class UserService {

	// ��½����
	public String login(String qqnumber,String password) 	//�����޸Ĳ�����ɶ��ַ�����ʽ��½
			throws UsernameNotFoundException,PasswordNotFoundException,StateException,SQLException{
		
		Connection conn =null;
		try {
			
			conn = DBmanage.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE qqnumber=?");	//û������sqlע��
			pst.setString(1, qqnumber);
			ResultSet rs = pst.executeQuery();	//��ѯ�õ��Ľ��
			if(rs.next()) {
				if(rs.getInt("state") == 0) {	//0����������½
					
					if(rs.getString("password").equals(password)) {
						return rs.getString(1);		//��½�ɹ�����uid
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
	
	// ��ú����б�
	public Vector<UserInfo> getFriendList(String uid) throws SQLException{
		
		Connection conn =null;
		try {
			
			conn = DBmanage.getConnection();
			PreparedStatement pst = conn.prepareStatement("select u.uid,u.img,u.netname,u.info,u.qqnumber from users u inner join friendship f on u.uid=f.friendid and f.uid=?");	//û������sqlע��
			pst.setString(1, uid);
			ResultSet rs = pst.executeQuery();	//��ѯ�õ��Ľ��
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
	
	// �������ϲ�ѯ �������ϲ�ѯ
	public UserInfo2 getPersonality(String uid) throws SQLException{

		Connection conn =null;
		try {
			
			conn = DBmanage.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM personality where uid=?;");	//û������sqlע��
			pst.setString(1, uid);
			ResultSet rs = pst.executeQuery();	//��ѯ�õ��Ľ��
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
	
	// ע���˻�
	public String regUser(String username,String password) throws UsernameException,SQLException{
		
		Connection conn =null;
		try {
			
			conn = DBmanage.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM personality where phonenumber=? or email=?;");	//û������sqlע��
			pst.setString(1, username);
			pst.setString(2, username);
			ResultSet rs = pst.executeQuery();	//��ѯ�õ��Ľ��
			if(rs.next()) {
				throw new UsernameException();	// �����ڸ��ֻ�/email �򱨴�
			}
			
			if(username.indexOf("@")>=0) {
				pst = conn.prepareStatement("insert into personality(uid,email,qqnumber) values(?,?,?)");	//���¸������� - ����
			}else if(username.trim().length()==11){
				pst = conn.prepareStatement("insert into personality(uid,phonenumber,qqnumber) values(?,?,?)");	//���¸������� - �ֻ�
			}
			String uid = System.currentTimeMillis()+"R"+(int)(Math.random()*10000);		//uid�������
			String qqnumber = (int)(Math.random()*100000)+""+System.currentTimeMillis()%10000;	//������ɾ�λqq��
			pst.setString(1, uid);
			pst.setString(2, username);
			pst.setString(3,qqnumber);
			if (pst.executeUpdate()<=0) {
				throw new SQLException();
			}
			
			pst = conn.prepareStatement("insert into users(uid,password,createtime,qqnumber) values(?,?,sysdate(),?)");	//�����û���
			pst.setString(1, uid);
			pst.setString(2, password);
			pst.setString(3, qqnumber);
			if (pst.executeUpdate()<=0) {
				throw new SQLException();
			}
			
			int logid = (int)(Math.random()*1000000);
			pst = conn.prepareStatement("insert into friendship(logid,uid,friendid,createtime) values(?,?,?,sysdate())");	//Ĭ�ϸ�10001�˺��Ǻ���
			pst.setInt(1, logid);
			pst.setString(2, uid);
			pst.setString(3, "10001");
			if (pst.executeUpdate()<=0) {
				throw new SQLException();
			}
			logid = (int)(Math.random()*1000000);
			pst = conn.prepareStatement("insert into friendship(logid,uid,friendid,createtime) values(?,?,?,sysdate())");	//Ĭ�ϸ�10001�˺��Ǻ���
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
			ResultSet rs = pst.executeQuery();	//��ѯ�õ��Ľ��
			String password = null;
			if(rs.next()) {
				pst = conn.prepareStatement("SELECT * FROM users where qqnumber=?;");
				pst.setString(1, qqnumber);
				rs = pst.executeQuery();	//��ѯ�õ��Ľ��
				rs.next();
				password = rs.getString("password");
			}else {
				throw new UsernameException();	// �������ڸ�qqnumber or qq������֤�˻���ƥ��
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
//			ResultSet rs = pst.executeQuery();	//��ѯ�õ��Ľ��
//			String username = null;
//			if(rs.next()) {
//				pst = conn.prepareStatement("SELECT * FROM users where qqnumber=?;");
//				pst.setString(1, qqnumber);
//				rs = pst.executeQuery();	//��ѯ�õ��Ľ��
//				rs.next();
//				password = rs.getString("password");
//			}else {
//				throw new UsernameException();	// �������ڸ�qqnumber or qq������֤�˻���ƥ��
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
