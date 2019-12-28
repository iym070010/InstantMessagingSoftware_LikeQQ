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
 *	 ͨѶ���� �������һֱ��������״̬
 *	1.���º�������״̬ - 5s����һ��
 *	2.��½��֤ 
 * 	3.�˳��˻�
 * 
 * @author:����ʿ
 */

public class NetService implements Runnable{

	
	private NetService() {
		// TODO Auto-generated constructor stub
	}
	
	//����
	private static NetService netService = new NetService();
	
	public static NetService getNetService() {
		return netService;
	}
	
	private Socket socket =null;
	private InputStream input = null;
	private OutputStream output = null;
	private Thread thread = null;
	private boolean run = false;
	
	//����׼������������ֳ���ͨѶ

	
	public JSONObject login() throws UnknownHostException,IOException {
		
		socket = new Socket(Config.IP,Config.LOGIN_PORT);
		
		input = socket.getInputStream();
		output = socket.getOutputStream();
		
		String json_str = "{\"username\":\"" + Config.username + "\",\"password\":\"" + Config.password + "\"}";
		
		//��ʼ�������������Ϣ
		output.write(json_str.getBytes());
		output.flush();
		
		//�ȴ���������ִ��Ϣ	{state:,msg:}		
		byte[] bytes =new byte[1024];
		int len = input.read(bytes);
		json_str = new String(bytes,0,len);
		JSONObject json = JSONObject.fromObject(json_str);	//����json�ı�
		
		//���state==0,���½�ɹ�
		if(json.getInt("state") == 0) {
			//�����������������
			
			if(thread != null) {
				//ѯ���߳��Ƿ񻹻���
				if(thread.getState() == Thread.State.RUNNABLE) {
					run = false;	//��ֹ�߳�
					try {
						thread.stop();
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}	
			}
			
			/////////////////////////////	
			output.write("U0001".getBytes());	//���ͺ����б������Ϣ
			output.flush();

			bytes = new byte[1024*10];	//�����б���Ϣ���
			len = input.read(bytes);
			String jsonstr = new String(bytes,0,len);

			Config.jiexi_friend_json_data(jsonstr); 	//���������б�
			
			System.out.println("��������:"+Config.friend_json_data);		//debug
			
			output.write("U0003".getBytes());	//���¸�������
			output.flush();
			len = input.read(bytes);
			
			String str = new String(bytes,0,len);		//�˴�����Ϊ����������Ϣǰ����� [] ���ţ���Ҫȥ������Ȼ֮�����Ҫ��д����
			Config.personality_json_data = str.substring(1,str.length()-1);	
			
			System.out.println("��������:"+Config.personality_json_data);		//debug
			/////////////////////////////
			
			//////////////////////////////����UDP������
			Config.datagramSocket_client = new DatagramSocket();
			//����������
			new MessageRegService(Config.datagramSocket_client);
			//������Ϣ��
			new MessageService(Config.datagramSocket_client);
			
			////////////////////////////
			
			//���¿��߳������������ͨѶ
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
				output.write("U0002".getBytes());	//ʵʱ���º������� 
				output.flush();
				input.read();		// ����ack
				
				output.write(Config.friend_list_data.getBytes());	//���ͺ����б������Ϣ
				output.flush();
				
				len = input.read(bytes);	
				String online = new String(bytes,0,len);
				System.out.println("�����˻�:"+online);		// notFound���Դ�
				
				try {
					if (!online.equals(Config.friend_online)) {		//ֻ�����߲����ʱ��������
						Config.friend_online = online;	
						Config.friendListJPanel.onlineUpdate();
					}
					
				} catch (Exception e) {
				}
						
				try {
					thread.sleep(2000);		//2s����һ�����ߺ���
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
			}
		} catch (StringIndexOutOfBoundsException e) {
			run = false;
			javax.swing.JOptionPane.showMessageDialog(null, "�����˻��������ط���½!");
			System.exit(0);		//ֱ���˳�����
		} catch (Exception e) {
			run = false;
			e.printStackTrace();
		}
		
	}
	
	public void searchPerson(String username) {
		try {
			 
			byte[] bytes = new byte[1024*10];
			int len = 0;
			
			output.write("U0004".getBytes());	//ʵʱ���º������� 
			output.flush();
			input.read();		// ����ack
			
			output.write(username.getBytes());	//���Ͳ���username
			output.flush();
			
			len = input.read(bytes);	
			String str_json = new String(bytes,0,len);
			System.out.println(str_json);		// ����json
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
