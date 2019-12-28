package com.ym.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ym.db.UserService;
import com.ym.db.UsernameException;

import net.sf.json.JSONObject;

/**
 * 	注册服务器	
 * 
 * @author 黄龙士
 *
 */


public class ResServer implements Runnable{

	private Socket socket = null;
	
	public ResServer(Socket socket) {
		// TODO Auto-generated constructor stub
		
		this.socket = socket;
	}
	
	private static HashMap<String, String> hashMap = new HashMap<String, String>();
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		InputStream in = null;
		OutputStream out = null;
		
		try {
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//等待客户端信息 - 通用写法 其他等待消息也可以这样写	
			byte[] bytes =new byte[1024];
			int len = in.read(bytes);
			String json_str = new String(bytes,0,len);
			
			//传入消息
			JSONObject json = JSONObject.fromObject(json_str);	//解析json文本
			String type = json.getString("type");
			
			if (type.equals("code")) {	//请求验证码
				String username = json.getString("username");	//用户名 - 手机/email
				
				//随机生成验证码
				StringBuffer code = new StringBuffer();
				Random random = new Random();
				for (int i = 0; i < 6; i++) {		//验证码随机生成
					code.append(random.nextInt(10));
				}

				hashMap.put(username, code.toString());		//保存用户-验证码键值对
				
				if (username.trim().length()==11 && username.indexOf("@")<=-1) {	//手机号
					try {
						Long.parseLong(username);	//手机号全由数字组成
						
						SendCode.send(username, code.toString());
						
						out.write("{\"state\":0,\"msg\":\"验证码发送至手机成功!\" }".getBytes());
						out.flush();
						
					} catch (Exception e) {
						out.write("{\"state\":1,\"msg\":\"验证码发送手机失败!\" }".getBytes());
						out.flush();
					}
				}else {
					if(username.indexOf("@")>=0) {	//邮箱号
						SendCode.sendEmail(username, code.toString());
						
						out.write("{\"state\":0,\"msg\":\"验证码发送至邮箱成功!\" }".getBytes());
						out.flush();
						
					}else {
						out.write("{\"state\":1,\"msg\":\"验证码发送邮箱失败!\" }".getBytes());
						out.flush();
					}
				}
			}else if (type.equals("reg")) {	//请求注册
				
				String username = json.getString("username");
				String password = json.getString("password");
				String code = json.getString("code");
				String code_right = hashMap.get(username);
				
				if(code_right!=null) {
					
					hashMap.remove(username);		//正确与否都要删掉
				}
				
				if (code_right.equals(code)) {
					
					try {
						String qqnumber = new UserService().regUser(username, password);
						out.write(("{\"state\":0,\"msg\":\"恭喜您!注册成功!\" ,\"qqnumber\":\""+qqnumber+"\"}").getBytes());
						out.flush();
					} catch (SQLException e) {
						out.write("{\"state\":3,\"msg\":\"未知错误!\" }".getBytes());
						out.flush();
						return;
					} catch (UsernameException e) {
						out.write("{\"state\":2,\"msg\":\"用户名已存在!\" }".getBytes());
						out.flush();
						return;
					}
					
				} else {
					out.write("{\"state\":1,\"msg\":\"验证码错误，请重新获取!\" }".getBytes());
					out.flush();
				}
			}else if (type.equals("recome")) {
				String username = json.getString("username");
				String qqnumber = json.getString("qqnumber");
				String code = json.getString("code");
				String code_right = hashMap.get(username);
				if(code_right!=null) {
					hashMap.remove(username);		//正确与否都要删掉
				}
				
				if (code_right.equals(code)) {
					try {
						String password = new UserService().recomePassword(qqnumber,username);
						out.write(("{\"state\":0,\"msg\":\"找回密码成功!\",\"password\":\""+password+"\"}").getBytes());
						out.flush();
					} catch (SQLException e) {
						out.write("{\"state\":3,\"msg\":\"未知错误!\" }".getBytes());
						out.flush();
						return;
					} catch (UsernameException e) {
						out.write("{\"state\":2,\"msg\":\"需要找回的用户名不存在或者与QQ号不匹配!\" }".getBytes());
						out.flush();
						return;
					}
				} else {
					out.write("{\"state\":1,\"msg\":\"验证码错误，请重新获取!\" }".getBytes());
					out.flush();
				}
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void openServer() throws Exception{
		ExecutorService service = Executors.newFixedThreadPool(2000);	//创建线程池	- 再多线程就要集群了
		
		ServerSocket server = new ServerSocket(4002);	//注册开启TCP:4002这个端口 - 用于注册业务 
		
		while(true) {	//死循环目的:可以无限服务
			
			Socket socket = server.accept();
			socket.setSoTimeout(10000);		//超过10s自动断开
			service.execute(new ResServer(socket));	//开启1个线程
			
			
		}
	}

}
