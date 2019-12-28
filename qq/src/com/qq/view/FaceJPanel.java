package com.qq.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.qq.view.util.Config;

public class FaceJPanel extends JPanel implements Comparable<FaceJPanel>,MouseListener,Runnable{

	private String uid;
	private String netName;
	private String info;
	private String image;
	private String qqnumber;
	private JLabel lblhead = new JLabel();
	private JLabel lblname = new JLabel();
	private JLabel lblinfo = new JLabel();
	private JLabel bg = new JLabel();
	private boolean isOnline = false;
	int x=0,y=0;
	
	public FaceJPanel(String uid,String netName,String info,String image,String qqnumber) {	//560*60
	
		this.uid = uid;
		this.netName = netName;
		this.info = info;
		this.image = image;
		this.qqnumber = qqnumber;
		this.setLayout(null);
		
		lblhead.setBounds(5, 5, 48, 48);
		add(lblhead);
		setImage(image);
		
		lblname.setFont(new Font("微软雅黑", Font.BOLD, 13));
		lblname.setBounds(70, 10, 369, 15);
		lblname.setText(netName);
		add(lblname);
		
		lblinfo.setFont(new Font("微软雅黑", Font.PLAIN, 11));
		lblinfo.setBounds(70, 35, 369, 15);
		lblinfo.setText(info);
		add(lblinfo);
		
		bg.setBounds(0, 0, 560, 60);	//背景覆盖全面版
		add(bg,-1);
		
		this.addMouseListener(this);
	}
	
	
	public void flashImage() {
		if (isOnline) {
			lblhead.setIcon(new ImageIcon("resources//online//"+this.image+".png"));
		} else {
			lblhead.setIcon(new ImageIcon("resources//offline//"+this.image+".png"));
		}
	}
	
	public void setImage(String image) {	//改头像
		if(image.equals("无")) {
			image = "0";		// 默认头像
		}
		this.image = image;
		
		if (isOnline) {
			lblhead.setIcon(new ImageIcon("resources//online//"+image+".png"));
		} else {
			lblhead.setIcon(new ImageIcon("resources//offline//"+image+".png"));
		}
	}
	
	public void setNetname(String netName) {
		lblname.setText(netName);
		this.netName = netName;
	}
	
	public void setInfo(String info) {
		lblinfo.setText(info);
		this.info = info;
	}
	
	public void setOnline(boolean isOnline) {		//在线离线切换
		this.isOnline = isOnline;
	}
	
	//存放所有未显示在chat的消息
	private Vector<Msg> msgs = new Vector<Msg>();
	boolean run = true;		//控制来消息是否产生效果
	private Thread thread = null;
	
	@Override
	public void run() {
		int x = lblhead.getX();
		int y = lblhead.getY();
				
		run = true;
		while(run) {					//头像抖动效果
			
			lblhead.setLocation(x-2, y-2);
			try {
				thread.sleep(300);
			} catch (Exception e) {
			}
			lblhead.setLocation(x+2, y+2);
			try {
				thread.sleep(300);
			} catch (Exception e) {
			}
		}
		
		lblhead.setLocation(x, y);
	}
	
	//寄存消息
	public void addMessage(Msg msg) {
		msgs.add(msg);
	
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}else if(thread.getState() == Thread.State.TERMINATED){
			thread = new Thread(this);
			thread.start();
		}else if(run == false){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	@Override
	public int compareTo(FaceJPanel o) {

		if (o.isOnline) {
			return 1;	//他在你上面
		}else if (this.isOnline) {
			return -1;	//你在它上面
		}else {
			return 0;	//你们两平等
		}

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {	//就不做按压改变颜色了，因为这样还得监听其他的facejpanel
		bg.setIcon(new ImageIcon("resources//mainmenu//bg1.png"));
		if (e.getClickCount() == 2) {
			Config.showChat(uid, netName, info, image, isOnline,qqnumber,msgs);
			run = false;		//停止效果
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		bg.setIcon(new ImageIcon("resources//mainmenu//bg2.png"));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		bg.setIcon(new ImageIcon(""));
	}


	
}
