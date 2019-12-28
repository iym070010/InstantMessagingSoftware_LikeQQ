package com.ym.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

/**
 * 
 * UDP��Ϣ��ת������
 * @author ����ʿ
 *
 */

public class UDPMessageServer implements Runnable{

	private DatagramPacket packet = null;
	private static DatagramSocket datagramSocket = null; 
	
	public UDPMessageServer(DatagramPacket packet){		
		this.packet = packet;
	}
	
	@Override
	public void run() {
		try {
			
			String json_str = new String(packet.getData(),0,packet.getLength());	//����תstring
			JSONObject json = JSONObject.fromObject(json_str);	//����json
			
			if(json.getString("type").equals("reg")) {	//����������
				
				String MyUID = json.getString("myUID");
				UserOnlineList.getUserOnlineList().updateOnlineUDP(MyUID, packet.getAddress().getHostAddress()
						, packet.getPort());	//�������µ�IP�Ͷ˿ں�
				
				System.out.println("��ע����Ϣ��������"+json_str);	
				
			}else if (json.getString("type").equals("msg") || json.getString("type").equals("ack")) {	//������Ϣת��	
				
				String MyUID = json.getString("myUID");
				String toUID = json.getString("toUID");
				UserOnlineList.getUserOnlineList().updateOnlineUDP(MyUID, packet.getAddress().getHostAddress()
						, packet.getPort());	//�������µ�IP�Ͷ˿ں�
				
				//��������Ϣ
				UserInfo toUserInfo = UserOnlineList.getUserOnlineList().getOnlineUserInfo(toUID);
				
				//ת���������˵����ݰ� - ����ת��δ���ߵĺ��ѣ����з���ǰҪ�ж϶Է��Ƿ�����
				DatagramPacket datagramPacket = new DatagramPacket(packet.getData(), packet.getLength(),
						InetAddress.getByName(toUserInfo.getUdpip()),toUserInfo.getUdpport());
				
				//�������ݰ�
				datagramSocket.send(datagramPacket);
					
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	//����������
	public static void openServer() throws Exception{
		ExecutorService service = Executors.newFixedThreadPool(2000);	//�����̳߳�	- �ٶ��߳̾�Ҫ��Ⱥ��
		datagramSocket = new DatagramSocket(4004);	////ע�Ὺ��UDP:4004����˿� - ������Ϣת��ҵ�� 
		while(true) {
			
			try {
				//�ȴ����Ĵ���
				byte[] bytes = new byte[1024*32];		//UDP�������65535�ֽڡ�64KB=1024*64 - 1024*10Ӧ���㹻��
				DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
				datagramSocket.receive(datagramPacket);
				
				//�����յ��󴴽�һ���̴߳���
				service.execute(new UDPMessageServer(datagramPacket));
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		}
	}
	
}
