package com.qq.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.qq.view.util.Config;

import net.sf.json.JSONObject;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.TextArea;

import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.FlowLayout;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chat extends JFrame implements ActionListener,WindowListener {

	private JPanel contentPane;

	private JButton btn_head = new JButton();
	private JLabel labelname = new JLabel();
	private JLabel labelinfo = new JLabel();
	private String uid,netname,info,img,qqnumber;	//uid是对方的id
	private JTextArea sendview = new JTextArea();
	private JTextArea mainview = new JTextArea();
	private boolean isOnline =false;
	/**
	 * Launch the application.
	 */
	
	public void onlineUpdate(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	public void addMyMessage(Msg msg) {		//添加自己的消息
		String str = "\n" + this.netname + "\t"+ new Date().toLocaleString() + "\n" +msg.getMsg() + "\n";
		
		mainview.setText(mainview.getText()+str);
		mainview.setSelectionStart(mainview.getText().toString().length());
		mainview.setSelectionEnd(mainview.getText().toString().length());
		
		sendview.requestFocus();	//输入框光标闪动
	}
	
	public void addMessage(Msg msg) {		//添加别人的消息
		String str = "\n" + JSONObject.fromObject(Config.personality_json_data).getString("netname")+"\t"
				+ new Date().toLocaleString() + "\n" +msg.getMsg() + "\n";
		
		mainview.setText(mainview.getText()+str);
		mainview.setSelectionStart(mainview.getText().toString().length());
		mainview.setSelectionEnd(mainview.getText().toString().length());
		
		sendview.requestFocus();	//输入框光标闪动
	}
	
	/**
	 * Create the frame.
	 */
	public Chat(String uid,String netname,String info,String img,boolean isOnline,String qqnumber,Vector<Msg> msgs) {
		this.uid = uid;
		this.netname = netname;
		this.info = info;
		this.img = img;
		this.isOnline = isOnline;
		this.qqnumber = qqnumber;
		ImageIcon imageIcon = null;
		if (isOnline) {
			imageIcon = new ImageIcon("resources//online//"+img+".png");
		}else {
			imageIcon = new ImageIcon("resources//offline//"+img+".png");
		}
		this.setIconImage(imageIcon.getImage());	// 设置小图标
		btn_head.setIcon(imageIcon);
		labelname.setText(" "+netname+" ("+qqnumber+")");
		labelinfo.setText("  "+info);
		
		
		setTitle(netname);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 875, 650);
		setLocationRelativeTo(null);	//正中间位置
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//北面面板
		JPanel panelNorth = new JPanel();
		panelNorth.setPreferredSize(new Dimension(0, 48));
		contentPane.add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new BorderLayout(0, 0));
		
		btn_head.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn_head.setPreferredSize(new Dimension(48, 48));
		btn_head.setToolTipText("\u5B8C\u5584\u4E2A\u4EBA\u8D44\u6599");
		btn_head.setFocusPainted(false);  
		btn_head.setRolloverIcon(new ImageIcon("resources//mainmenu//head_highlight.jpg"));   
		btn_head.setPressedIcon(new ImageIcon("resources//mainmenu//head_down.jpg"));  
		btn_head.setBorderPainted(false);  
		btn_head.setContentAreaFilled(false);  
		panelNorth.add(btn_head, BorderLayout.WEST); 
		
		JPanel panel = new JPanel();
		panelNorth.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		labelname.setFont(new Font("微软雅黑", Font.BOLD, 18));
		panel.add(labelname, BorderLayout.CENTER);	
		
		labelinfo.setFont(new Font("微软雅黑", Font.PLAIN, 10));
		labelinfo.setPreferredSize(new Dimension(0, 15));
		panel.add(labelinfo, BorderLayout.SOUTH);
		
		
		//中心面板
		JSplitPane splitPane = new JSplitPane();		//分割面板
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(400);		//上面板的大小
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JPanel xia = new JPanel();
		splitPane.setRightComponent(xia);
		xia.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		xia.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(null);
		panel_1.setPreferredSize(new Dimension(0, 30));
		
		JButton btn_font = new JButton("\u5B57\u4F53");
		btn_font.setToolTipText("\u5B57\u4F53");
		btn_font.setLocation(3, 4);
		btn_font.setSize(67, 23);
		panel_1.add(btn_font); 
		
		JButton btn_zhendong = new JButton("\u6296\u52A8");
		btn_zhendong.setToolTipText("\u5411\u597D\u53CB\u53D1\u9001\u7A97\u53E3\u6296\u52A8");
		btn_zhendong.setBounds(80, 4, 67, 23);
		panel_1.add(btn_zhendong);
		
		JPanel panel_2 = new JPanel();
		xia.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(null);
		panel_2.setPreferredSize(new Dimension(0, 30));
		
		JButton btn_close = new JButton("\u5173\u95ED"); 
		btn_close.setToolTipText("\u5173\u95ED");
		btn_close.setBounds(671, 5, 67, 23);	//738
		panel_2.add(btn_close);
		
		JButton btn_send = new JButton("\u53D1\u9001");
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!Chat.this.isOnline) {		//如果对方没上线，则不让发送
					javax.swing.JOptionPane.showMessageDialog(Chat.this, "对方未上线，发送消息失败!");
					sendview.setText(""); 			//发送之后清空区域
					return;
				}
				
				try {
					
					Msg msg = new Msg();
					msg.setCode(System.currentTimeMillis()+"");
					msg.setMsg(sendview.getText());
					msg.setMyUID(JSONObject.fromObject(Config.personality_json_data).getString("uid"));
					msg.setToUID(uid);
					msg.setType("msg");
					String json = JSONObject.fromObject(msg).toString();
					
					// json = {"msg":"123456","code":"1574426820174","toUID":"10002","myUID":"10001","type":"msg"}
					
					byte[] bytes = json.getBytes();
					
					DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,
							InetAddress.getByName(Config.IP),Config.Msg_PORT);
					Config.datagramSocket_client.send(datagramPacket);
					sendview.setText(""); 			//发送之后清空区域
					
					addMyMessage(msg);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			
			}
		});
		btn_send.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_send.setToolTipText("Enter\u952E\u53D1\u9001\u6D88\u606F");
		btn_send.setBounds(748, 5, 97, 23);
		panel_2.add(btn_send);
		
		JScrollPane scrollPane = new JScrollPane();
		xia.add(scrollPane, BorderLayout.CENTER);
		
		scrollPane.setViewportView(sendview);
		
		JScrollPane mainwindow = new JScrollPane();
		splitPane.setLeftComponent(mainwindow);
		
		mainwindow.setViewportView(mainview);
//		mainwindow.setEnabled(false);	//不可编辑
		
		for (Msg msg : msgs) {
			this.addMessage(msg);
		}
		msgs.clear();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		
		Config.closeChat(uid);
		this.dispose();
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
