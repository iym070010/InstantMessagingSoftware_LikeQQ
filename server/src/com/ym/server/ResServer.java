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
 * 	ע�������	
 * 
 * @author ����ʿ
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
			
			//�ȴ��ͻ�����Ϣ - ͨ��д�� �����ȴ���ϢҲ��������д	
			byte[] bytes =new byte[1024];
			int len = in.read(bytes);
			String json_str = new String(bytes,0,len);
			
			//������Ϣ
			JSONObject json = JSONObject.fromObject(json_str);	//����json�ı�
			String type = json.getString("type");
			
			if (type.equals("code")) {	//������֤��
				String username = json.getString("username");	//�û��� - �ֻ�/email
				
				//���������֤��
				StringBuffer code = new StringBuffer();
				Random random = new Random();
				for (int i = 0; i < 6; i++) {		//��֤���������
					code.append(random.nextInt(10));
				}

				hashMap.put(username, code.toString());		//�����û�-��֤���ֵ��
				
				if (username.trim().length()==11 && username.indexOf("@")<=-1) {	//�ֻ���
					try {
						Long.parseLong(username);	//�ֻ���ȫ���������
						
						SendCode.send(username, code.toString());
						
						out.write("{\"state\":0,\"msg\":\"��֤�뷢�����ֻ��ɹ�!\" }".getBytes());
						out.flush();
						
					} catch (Exception e) {
						out.write("{\"state\":1,\"msg\":\"��֤�뷢���ֻ�ʧ��!\" }".getBytes());
						out.flush();
					}
				}else {
					if(username.indexOf("@")>=0) {	//�����
						SendCode.sendEmail(username, code.toString());
						
						out.write("{\"state\":0,\"msg\":\"��֤�뷢��������ɹ�!\" }".getBytes());
						out.flush();
						
					}else {
						out.write("{\"state\":1,\"msg\":\"��֤�뷢������ʧ��!\" }".getBytes());
						out.flush();
					}
				}
			}else if (type.equals("reg")) {	//����ע��
				
				String username = json.getString("username");
				String password = json.getString("password");
				String code = json.getString("code");
				String code_right = hashMap.get(username);
				
				if(code_right!=null) {
					
					hashMap.remove(username);		//��ȷ���Ҫɾ��
				}
				
				if (code_right.equals(code)) {
					
					try {
						String qqnumber = new UserService().regUser(username, password);
						out.write(("{\"state\":0,\"msg\":\"��ϲ��!ע��ɹ�!\" ,\"qqnumber\":\""+qqnumber+"\"}").getBytes());
						out.flush();
					} catch (SQLException e) {
						out.write("{\"state\":3,\"msg\":\"δ֪����!\" }".getBytes());
						out.flush();
						return;
					} catch (UsernameException e) {
						out.write("{\"state\":2,\"msg\":\"�û����Ѵ���!\" }".getBytes());
						out.flush();
						return;
					}
					
				} else {
					out.write("{\"state\":1,\"msg\":\"��֤����������»�ȡ!\" }".getBytes());
					out.flush();
				}
			}else if (type.equals("recome")) {
				String username = json.getString("username");
				String qqnumber = json.getString("qqnumber");
				String code = json.getString("code");
				String code_right = hashMap.get(username);
				if(code_right!=null) {
					hashMap.remove(username);		//��ȷ���Ҫɾ��
				}
				
				if (code_right.equals(code)) {
					try {
						String password = new UserService().recomePassword(qqnumber,username);
						out.write(("{\"state\":0,\"msg\":\"�һ�����ɹ�!\",\"password\":\""+password+"\"}").getBytes());
						out.flush();
					} catch (SQLException e) {
						out.write("{\"state\":3,\"msg\":\"δ֪����!\" }".getBytes());
						out.flush();
						return;
					} catch (UsernameException e) {
						out.write("{\"state\":2,\"msg\":\"��Ҫ�һص��û��������ڻ�����QQ�Ų�ƥ��!\" }".getBytes());
						out.flush();
						return;
					}
				} else {
					out.write("{\"state\":1,\"msg\":\"��֤����������»�ȡ!\" }".getBytes());
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
		ExecutorService service = Executors.newFixedThreadPool(2000);	//�����̳߳�	- �ٶ��߳̾�Ҫ��Ⱥ��
		
		ServerSocket server = new ServerSocket(4002);	//ע�Ὺ��TCP:4002����˿� - ����ע��ҵ�� 
		
		while(true) {	//��ѭ��Ŀ��:�������޷���
			
			Socket socket = server.accept();
			socket.setSoTimeout(10000);		//����10s�Զ��Ͽ�
			service.execute(new ResServer(socket));	//����1���߳�
			
			
		}
	}

}
