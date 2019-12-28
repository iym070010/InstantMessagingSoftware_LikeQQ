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
 * ��½������ ��Ҫ�����½
 * 
 * @author ����ʿ
 *
 */

public class Loginserver implements Runnable{

	private Socket socket = null;
	
	public Loginserver(Socket socket) {
		// TODO Auto-generated constructor stub
		
		this.socket = socket;
	}
	
	@Override
	public void run() {		//�̷߳���
		// TODO Auto-generated method stub
		
		//��½����
		String uid = null;	//���ݿ��½�ɹ���᷵��uid
		InputStream in = null;
		OutputStream out = null;
		
		try {
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//�ȴ��ͻ�����Ϣ - ͨ��д�� �����ȴ���ϢҲ��������д	{username:,password:}
			byte[] bytes =new byte[1024];
			int len = in.read(bytes);
			String json_str = new String(bytes,0,len);
			
			//������Ϣ
			JSONObject json = JSONObject.fromObject(json_str);	//����json�ı�	
			String username = json.getString("username");	//qqnumber
			String password = json.getString("password");	//password
			
			try {
				Long.parseLong(username);	//qqnumberȫ���������
			} catch (NumberFormatException e) {
				// TODO: handle exception
				out.write("{\"state\":4,\"msg\":\"δ֪����!\"}".getBytes());
				out.flush();
				return;
			}
			
			try {		
				
				uid = new UserService().login(username, password);	//�˴�username����qqnumber
												//password passward ����ƴд��Щ�ط�д����
			
//				System.out.print(uid);	//debug��
				
				//��½�ɹ�����������û�����
				UserOnlineList.getUserOnlineList().regOnline(uid, password, socket, username);
				
				out.write("{\"state\":0,\"msg\":\"��½�ɹ�!\"}".getBytes());		
				out.flush();
				
				while(true) {	//��½֮��½½�������ܿͻ��˷��͵�����
					
					bytes =new byte[2048];
					len = in.read(bytes);		
					String command = new String(bytes,0,len);
					
					
					if (command.equals("U0001")) {	//���º����б�
						
						Vector<com.ym.db.UserInfo> userInfos = new UserService().getFriendList(uid);
						out.write(JSONArray.fromObject(userInfos).toString().getBytes("utf-8"));		
						out.flush();
						
					} else if (command.equals("U0002")) {	//���º�������״̬
						
						out.write(1);	//���ͺ����б������Ϣ
						out.flush();
						
						len = in.read(bytes);	// ��ú���id�б�
						String str = new String(bytes,0,len);	//10001,10002,...
						String[] ids = str.split(",");
						StringBuffer stringBuffer = new StringBuffer();	//������ߺ���id
						for(String string:ids) {
							if (UserOnlineList.getUserOnlineList().isUserOnline(string)) {
								stringBuffer.append(string);
								stringBuffer.append(",");
							}
						}
						
						if(stringBuffer.length()==0) {	//0��ʾֻ���Լ����� (���ߺ���id�����ʾ�Լ��Ļ��˴��͸ĳ�0)
							//û�к�������
							out.write("notFound".getBytes("utf-8"));
							out.flush();
						}else {
							//��ִ���������б�
							out.write(stringBuffer.toString().getBytes("utf-8"));
							out.flush();
						}
						
					} else if (command.equals("U0003")) {	//���¸�������
						
						UserInfo2 userInfo2 = new UserService().getPersonality(uid);
						out.write(JSONArray.fromObject(userInfo2).toString().getBytes("utf-8"));
						out.flush();
						
					} else if (command.equals("E0001")){	//�޸ĸ�������

					} else if (command.equals("U0004")) {	// ����qq��
						
						out.write(1);	//����
						out.flush();
						
						System.out.println("����qq����");				/// debug
						
						len = in.read(bytes);	
						String str_find = new String(bytes,0,len);	//"username";
						
						username = new UserService().login(username, password);		//
						
						
						
						
					} else if (command.equals("EXIT")) {	//�˳���½
						UserOnlineList.getUserOnlineList().logout(uid);
						return;
					}
					
				}
				
				
				
			} catch (UsernameNotFoundException e) {
				out.write("{\"state\":2,\"msg\":\"�û���������!\"}".getBytes());
				out.flush();
				return;
			} catch (PasswordNotFoundException e) {
				out.write("{\"state\":1,\"msg\":\"�������!\"}".getBytes());
				out.flush();
				return;
			} catch (StateException e) {
				out.write("{\"state\":3,\"msg\":\"�˻�������������ϵ�ͷ�!\"}".getBytes());
				out.flush();
				return;
			} catch (SQLException e) {
				out.write("{\"state\":4,\"msg\":\"δ֪����!\"}".getBytes());
				out.flush();
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {	//�ر�����
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
		ExecutorService execute = Executors.newFixedThreadPool(2000);	//�����̳߳�	- �ٶ��߳̾�Ҫ��Ⱥ��
		
		ServerSocket server = new ServerSocket(4001);	//ע�Ὺ��TCP:4001����˿� - ���ڵ�½ҵ�� 
		
		while(true) {	//��ѭ��Ŀ��:�������޷���
			
			Socket socket = server.accept();
			socket.setSoTimeout(10000);		//����10s�Զ��Ͽ�
			execute.execute(new Loginserver(socket));	//����1���߳�
			
			
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
