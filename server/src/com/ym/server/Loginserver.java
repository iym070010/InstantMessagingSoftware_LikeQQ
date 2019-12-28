package com.ym.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ym.db.PasswordNotFoundException;
import com.ym.db.StateException;
import com.ym.db.UserInfo2;
import com.ym.db.UserService;
import com.ym.db.UsernameNotFoundException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 登陆服务器 主要负责登陆
 * 
 * @author 黄龙士
 *
 */

public class Loginserver implements Runnable{

	private Socket socket = null;
	
	public Loginserver(Socket socket) {
		// TODO Auto-generated constructor stub
		
		this.socket = socket;
	}
	
	@Override
	public void run() {		//线程方法
		// TODO Auto-generated method stub
		
		//登陆操作
		String uid = null;	//数据库登陆成功后会返回uid
		InputStream in = null;
		OutputStream out = null;
		
		try {
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//等待客户端信息 - 通用写法 其他等待消息也可以这样写	{username:,password:}
			byte[] bytes =new byte[1024];
			int len = in.read(bytes);
			String json_str = new String(bytes,0,len);
			
			//传入消息
			JSONObject json = JSONObject.fromObject(json_str);	//解析json文本	
			String username = json.getString("username");	//qqnumber
			String password = json.getString("password");	//password
			
			try {
				Long.parseLong(username);	//qqnumber全由数字组成
			} catch (NumberFormatException e) {
				// TODO: handle exception
				out.write("{\"state\":4,\"msg\":\"未知错误!\"}".getBytes());
				out.flush();
				return;
			}
			
			try {		
				
				uid = new UserService().login(username, password);	//此处username就是qqnumber
												//password passward 单词拼写有些地方写错了
			
//				System.out.print(uid);	//debug用
				
				//登陆成功后加入在线用户队列
				UserOnlineList.getUserOnlineList().regOnline(uid, password, socket, username);
				
				out.write("{\"state\":0,\"msg\":\"登陆成功!\"}".getBytes());		
				out.flush();
				
				while(true) {	//登陆之后陆陆续续接受客户端发送的请求
					
					bytes =new byte[2048];
					len = in.read(bytes);		
					String command = new String(bytes,0,len);
					
					
					if (command.equals("U0001")) {	//更新好友列表
						
						Vector<com.ym.db.UserInfo> userInfos = new UserService().getFriendList(uid);
						out.write(JSONArray.fromObject(userInfos).toString().getBytes("utf-8"));		
						out.flush();
						
					} else if (command.equals("U0002")) {	//更新好友在线状态
						
						out.write(1);	//发送好友列表更新消息
						out.flush();
						
						len = in.read(bytes);	// 获得好友id列表
						String str = new String(bytes,0,len);	//10001,10002,...
						String[] ids = str.split(",");
						StringBuffer stringBuffer = new StringBuffer();	//存放在线好友id
						for(String string:ids) {
							if (UserOnlineList.getUserOnlineList().isUserOnline(string)) {
								stringBuffer.append(string);
								stringBuffer.append(",");
							}
						}
						
						if(stringBuffer.length()==0) {	//0表示只有自己在线 (在线好友id如果显示自己的话此处就改成0)
							//没有好友在线
							out.write("notFound".getBytes("utf-8"));
							out.flush();
						}else {
							//回执好友在线列表
							out.write(stringBuffer.toString().getBytes("utf-8"));
							out.flush();
						}
						
					} else if (command.equals("U0003")) {	//更新个人资料
						
						UserInfo2 userInfo2 = new UserService().getPersonality(uid);
						out.write(JSONArray.fromObject(userInfo2).toString().getBytes("utf-8"));
						out.flush();
						
					} else if (command.equals("E0001")){	//修改个人资料

					} else if (command.equals("U0004")) {	// 查找qq号
						
						out.write(1);	//握手
						out.flush();
						
						System.out.println("查找qq号中");				/// debug
						
						len = in.read(bytes);	
						String str_find = new String(bytes,0,len);	//"username";
						
						username = new UserService().login(username, password);		//
						
						
						
						
					} else if (command.equals("EXIT")) {	//退出登陆
						UserOnlineList.getUserOnlineList().logout(uid);
						return;
					}
					
				}
				
				
				
			} catch (UsernameNotFoundException e) {
				out.write("{\"state\":2,\"msg\":\"用户名不存在!\"}".getBytes());
				out.flush();
				return;
			} catch (PasswordNotFoundException e) {
				out.write("{\"state\":1,\"msg\":\"密码错误!\"}".getBytes());
				out.flush();
				return;
			} catch (StateException e) {
				out.write("{\"state\":3,\"msg\":\"账户被锁定，请联系客服!\"}".getBytes());
				out.flush();
				return;
			} catch (SQLException e) {
				out.write("{\"state\":4,\"msg\":\"未知错误!\"}".getBytes());
				out.flush();
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {	//关闭连接
				UserOnlineList.getUserOnlineList().logout(uid);
				in.close();
				out.close();
				socket.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		
	}

	public static void openServer() throws Exception{
		ExecutorService execute = Executors.newFixedThreadPool(2000);	//创建线程池	- 再多线程就要集群了
		
		ServerSocket server = new ServerSocket(4001);	//注册开启TCP:4001这个端口 - 用于登陆业务 
		
		while(true) {	//死循环目的:可以无限服务
			
			Socket socket = server.accept();
			socket.setSoTimeout(10000);		//超过10s自动断开
			execute.execute(new Loginserver(socket));	//开启1个线程
			
			
		}
	}
	
	public static void main(String[] args) {
		try {
			openServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
