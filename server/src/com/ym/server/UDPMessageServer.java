package com.ym.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

/**
 * 
 * UDP消息中转服务器
 * @author 黄龙士
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
			
			String json_str = new String(packet.getData(),0,packet.getLength());	//报文转string
			JSONObject json = JSONObject.fromObject(json_str);	//解析json
			
			if(json.getString("type").equals("reg")) {	//处理心跳包
				
				String MyUID = json.getString("myUID");
				UserOnlineList.getUserOnlineList().updateOnlineUDP(MyUID, packet.getAddress().getHostAddress()
						, packet.getPort());	//更新最新的IP和端口号
				
				System.out.println("有注册消息发过来："+json_str);	
				
			}else if (json.getString("type").equals("msg") || json.getString("type").equals("ack")) {	//处理信息转发	
				
				String MyUID = json.getString("myUID");
				String toUID = json.getString("toUID");
				UserOnlineList.getUserOnlineList().updateOnlineUDP(MyUID, packet.getAddress().getHostAddress()
						, packet.getPort());	//更新最新的IP和端口号
				
				//接受人信息
				UserInfo toUserInfo = UserOnlineList.getUserOnlineList().getOnlineUserInfo(toUID);
				
				//转发给接收人的数据包 - 不能转发未上线的好友，所有发送前要判断对方是否在线
				DatagramPacket datagramPacket = new DatagramPacket(packet.getData(), packet.getLength(),
						InetAddress.getByName(toUserInfo.getUdpip()),toUserInfo.getUdpport());
				
				//发送数据包
				datagramSocket.send(datagramPacket);
					
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	//启动服务器
	public static void openServer() throws Exception{
		ExecutorService service = Executors.newFixedThreadPool(2000);	//创建线程池	- 再多线程就要集群了
		datagramSocket = new DatagramSocket(4004);	////注册开启UDP:4004这个端口 - 用于消息转发业务 
		while(true) {
			
			try {
				//等待报文传输
				byte[] bytes = new byte[1024*32];		//UDP报文最大65535字节≈64KB=1024*64 - 1024*10应该足够了
				DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
				datagramSocket.receive(datagramPacket);
				
				//报文收到后创建一个线程处理
				service.execute(new UDPMessageServer(datagramPacket));
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		}
	}
	
}
