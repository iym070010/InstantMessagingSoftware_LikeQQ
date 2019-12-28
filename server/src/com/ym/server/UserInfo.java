package com.ym.server;


import java.net.Socket;


// 用户队列的UserInfo
public class UserInfo {

	private String uid;
	private String passward;
	private Socket socket;
	private String udpip;
	private int udpport;
	private String qqnumber;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPassward() {
		return passward;
	}
	public void setPassward(String passward) {
		this.passward = passward;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getUdpip() {
		return udpip;
	}
	public void setUdpip(String udpip) {
		this.udpip = udpip;
	}
	public int getUdpport() {
		return udpport;
	}
	public void setUdpport(int udpport) {
		this.udpport = udpport;
	}
	public String getQqnumber() {
		return qqnumber;
	}
	public void setQqnumber(String qqnumber) {
		this.qqnumber = qqnumber;
	}
	
	
	
}
