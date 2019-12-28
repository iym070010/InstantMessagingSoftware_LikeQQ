package com.qq.view.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.qq.view.util.Config;

import net.sf.json.JSONObject;

/**
 * 
 * 	向服务器发送心跳包
 * @author 黄龙士
 *
 */

public class MessageRegService extends Thread{

	private DatagramSocket client = null;
	
	//每10s向服务器发送心跳包
	public void run() {
		
		String uid = JSONObject.fromObject(Config.personality_json_data).getString("uid");
		String jsonstr = "{\"type\":\"reg\",\"myUID\":\""+uid+"\"}";
		byte[] bytes = jsonstr.getBytes();
		while(true) {

			try {
				DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
						InetAddress.getByName(Config.IP),Config.Msg_PORT);
				
				//将心跳包发送给服务器
				client.send(datagramPacket);
				Thread.sleep(9999); 	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public MessageRegService(DatagramSocket client) {
		this.client = client;
		this.start();
	}
	
}
