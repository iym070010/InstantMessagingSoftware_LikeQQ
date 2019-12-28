package com.qq.view.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.qq.view.Login;
import com.qq.view.util.Config;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *	 通讯服务 与服务器一直保持连接状态
 *	1.更新好友在线状态 - 5s更新一次
 *	2.登陆验证 
 * 	3.退出账户
 * 
 * @author:黄龙士
 */

public class NetService implements Runnable{

	
	private NetService() {
		// TODO Auto-generated constructor stub
	}
	
	//单例
	private static NetService netService = new NetService();
	
	public static NetService getNetService() {
		return netService;
	}
	
	private Socket socket =null;
	private InputStream input = null;
	private OutputStream output = null;
	private Thread thread = null;
	private boolean run = false;
	
	//这里准备与服务器保持长期通讯

	
	public JSONObject login() throws UnknownHostException,IOException {
		
		socket = new Socket(Config.IP,Config.LOGIN_PORT);
		
		input = socket.getInputStream();
		output = socket.getOutputStream();
		
		String json_str = "{\"username\":\"" + Config.username + "\",\"password\":\"" + Config.password + "\"}";
		
		//开始与服务器传递消息
		output.write(json_str.getBytes());
		output.flush();
		
		//等待服务器回执消息	{state:,msg:}		
		byte[] bytes =new byte[1024];
		int len = input.read(bytes);
		json_str = new String(bytes,0,len);
		JSONObject json = JSONObject.fromObject(json_str);	//解析json文本
		
		//如果state==0,则登陆成功
		if(json.getInt("state") == 0) {
			//开启持续的网络服务
			
			if(thread != null) {
				//询问线程是否还活着
				if(thread.getState() == Thread.State.RUNNABLE) {
					run = false;	//终止线程
					try {
						thread.stop();
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}	
			}
			
			/////////////////////////////	
			output.write("U0001".getBytes());	//发送好友列表更新消息
			output.flush();

			bytes = new byte[1024*10];	//好友列表信息获得
			len = input.read(bytes);
			String jsonstr = new String(bytes,0,len);

			Config.jiexi_friend_json_data(jsonstr); 	//解析好友列表
			
			System.out.println("好友资料:"+Config.friend_json_data);		//debug
			
			output.write("U0003".getBytes());	//更新个人资料
			output.flush();
			len = input.read(bytes);
			
			String str = new String(bytes,0,len);		//此处是因为传过来的消息前后多了 [] 符号，需要去除，不然之后解析要多写代码
			Config.personality_json_data = str.substring(1,str.length()-1);	
			
			System.out.println("个人资料:"+Config.personality_json_data);		//debug
			/////////////////////////////
			
			//////////////////////////////启动UDP服务器
			Config.datagramSocket_client = new DatagramSocket();
			//启动心跳包
			new MessageRegService(Config.datagramSocket_client);
			//启动消息端
			new MessageService(Config.datagramSocket_client);
			
			////////////////////////////
			
			//重新开线程与服务器保持通讯
			thread = new Thread(this);
			run = true;
			thread.start();	
		}
		
		return json;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		try {
					 
			byte[] bytes = new byte[1024*10];
			int len = 0;
			
			while(run) {
				output.write("U0002".getBytes());	//实时更新好友在线 
				output.flush();
				input.read();		// 读个ack
				
				output.write(Config.friend_list_data.getBytes());	//发送好友列表更新消息
				output.flush();
				
				len = input.read(bytes);	
				String online = new String(bytes,0,len);
				System.out.println("在线账户:"+online);		// notFound来自此
				
				try {
					if (!online.equals(Config.friend_online)) {		//只有两者不相等时才做更新
						Config.friend_online = online;	
						Config.friendListJPanel.onlineUpdate();
					}
					
				} catch (Exception e) {
				}
						
				try {
					thread.sleep(2000);		//2s更新一次在线好友
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
			}
		} catch (StringIndexOutOfBoundsException e) {
			run = false;
			javax.swing.JOptionPane.showMessageDialog(null, "您的账户在其他地方登陆!");
			System.exit(0);		//直接退出进程
		} catch (Exception e) {
			run = false;
			e.printStackTrace();
		}
		
	}
	
	public void searchPerson(String username) {
		try {
			 
			byte[] bytes = new byte[1024*10];
			int len = 0;
			
			output.write("U0004".getBytes());	//实时更新好友在线 
			output.flush();
			input.read();		// 读个ack
			
			output.write(username.getBytes());	//发送查找username
			output.flush();
			
			len = input.read(bytes);	
			String str_json = new String(bytes,0,len);
			System.out.println(str_json);		// 接受json
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
