package com.qq.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.qq.view.util.Config;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class MainMenu extends JFrame{

	private JPanel contentPane;
	private JButton btn_head;
	private JLabel labelname;
	private JLabel labelinfo;
	boolean run = false;		//用来反馈顶号
	private Thread thread = null;
	
	public void mainUpdate() {		//更新自己主界面的上方显示
			//{"dd":"","mm":"","profession":"","yy":"","img":"无","gend":"男","phonenumber":"","back":"我有一丶丶想你",
			//"realname":"mm","relation":"单身","uid":"10002","bloodtype":"A","netname":"小龙人","email":"","info":"我是小龙人"}

		JSONObject jsonObject = JSONObject.fromObject(Config.personality_json_data);
		labelname.setText(jsonObject.getString("netname"));
		labelinfo.setText(jsonObject.getString("info"));
		if (jsonObject.getString("img").equals("无")) {
			btn_head.setIcon(new ImageIcon("resources//online//0.png"));
		}
		else {
			btn_head.setIcon(new ImageIcon("resources//online//"+jsonObject.getString("img")+".png"));
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		JFrame.setDefaultLookAndFeelDecorated(true);		//swing框架皮肤
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		setBounds(1000, 50, 303, 720);
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//北面面板
		JPanel panelNorth = new JPanel();
		panelNorth.setPreferredSize(new Dimension(300, 75));	//设置面板容器的大小 
		contentPane.add(panelNorth, BorderLayout.NORTH);
		ImageIcon head = new ImageIcon("resources//mainmenu//head_normal.jpg");
		btn_head = new JButton(head);	//设置头像
		btn_head.setBounds(10, 10, 48,48);  
		btn_head.setToolTipText("\u5B8C\u5584\u4E2A\u4EBA\u8D44\u6599");
		btn_head.setFocusPainted(false);  
//		btn_head.setRolloverIcon(new ImageIcon("resources//mainmenu//head_highlight.jpg"));   鼠标放上去
//		btn_head.setPressedIcon(new ImageIcon("resources//mainmenu//head_down.jpg"));  	鼠标按压
		btn_head.setBorderPainted(false);  
		btn_head.setContentAreaFilled(false);  
		btn_head.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				new Personality();
			}
		});
		panelNorth.setLayout(null); 
		panelNorth.add(btn_head);
		
		labelname = new JLabel("\u9EC4\u9F99\u58EB");	//设置名称
		labelname.setFont(new Font("微软雅黑", Font.BOLD, 18));
		labelname.setBounds(72, 6, 180, 35);
		panelNorth.add(labelname);
		
		labelinfo = new JLabel("\u9EC4\u9F99\u58EB\u7684qq\u7B7E\u540D");	//设置个性签名
		labelinfo.setToolTipText("\u4E2A\u6027\u7B7E\u540D\u66F4\u65B0");
		labelinfo.setFont(new Font("微软雅黑", Font.PLAIN, 11));
		labelinfo.setBounds(72, 40, 180, 20);
		panelNorth.add(labelinfo); 
		
		//南面面板
		JPanel panelSouth = new JPanel();
		panelSouth.setPreferredSize(new Dimension(300, 60));	//设置面板容器的大小 
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(null);
		
		JButton btn_set = new JButton("设置");
		btn_set.setToolTipText("\u8BBE\u7F6E");
		btn_set.setBounds(10, 20, 60, 20);
		panelSouth.add(btn_set);
		
		JButton btn_exit = new JButton("退出");
		btn_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btn_exit.setToolTipText("\u9000\u51FA");
		btn_exit.setBounds(80, 20, 60, 20);
		panelSouth.add(btn_exit);
		
		JButton btn_search = new JButton("\u67E5\u627E");
		btn_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new Select();
				
			}
		});
		btn_search.setToolTipText("\u67E5\u627E");
		btn_search.setBounds(213, 20, 60, 20);
		panelSouth.add(btn_search);
		
		//中间面板
		JPanel panelCenter = new JPanel();
		panelCenter.setPreferredSize(new Dimension(0, 0));
		contentPane.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panelCenter.add(tabbedPane);
		
		JPanel panelmessage = new JPanel();
		tabbedPane.addTab(" 消 息 ", null, panelmessage, null);
		panelmessage.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelmessage.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panelfriend = new JPanel();
		tabbedPane.addTab(" 联系人 ", null, panelfriend, null);
		panelfriend.setLayout(new BorderLayout(0, 0));
		
//		JLabel bg = new JLabel(new ImageIcon("resources//mainmenu//bg0.png"));	//这样设置背景有BUG
//		bg.setBounds(0, 0, 600, 600);	//背景覆盖全面版
//		panelfriend.add(bg,-1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panelfriend.add(scrollPane_1, BorderLayout.CENTER);
		
		JPanel panelgroup = new JPanel();
		tabbedPane.addTab(" 群 组 ", null, panelgroup, null);
		panelgroup.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panelgroup.add(scrollPane_2, BorderLayout.CENTER);
		tabbedPane.setSelectedIndex(1); //设置联系人的选项卡被选中
		scrollPane_1.setViewportView(new FriendListJPanel());	// 插入好友列表
		
		this.mainUpdate();
	}
}
