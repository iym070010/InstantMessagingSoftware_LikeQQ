package com.qq.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.qq.view.util.Config;

import net.sf.json.JSONObject;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class Personality extends JFrame {

	private JButton btn_head = new JButton();
	private JPanel contentPane;
	private JTextField text_netName = new JTextField();
	private JTextField text_info = new JTextField();
	private JTextField text_realname = new JTextField();
	private JTextField textemail = new JTextField();
	private JTextField textphone = new JTextField();
	private JTextArea text_back = new JTextArea();
	private JComboBox cbYear,cbMonth,cbDay,cbBlood,cbSex,cbPosition,cbHometown,cbEmotion;
	private String str0[] = new String[30];
	private String str1[] = {"1","2","3","4","5","6","7","8","9","10","11","12"};
	private String str2[] = new String[31];
	private String str3[] = {"男","女"};
	private String str4[] = {"A","B","AB","O"};

	public void personUpdate() {		//更新自己主界面的上方显示
		//{"dd":"","mm":"","profession":"","yy":"","img":"无","gend":"男","phonenumber":"","back":"我有一丶丶想你",
		//"realname":"mm","relation":"单身","uid":"10002","bloodtype":"A","netname":"小龙人","email":"","info":"我是小龙人"}

		JSONObject jsonObject = JSONObject.fromObject(Config.personality_json_data);
		text_netName.setText(jsonObject.getString("netname"));
		text_info.setText(jsonObject.getString("info"));
		if (jsonObject.getString("img").equals("无")) {
			btn_head.setIcon(new ImageIcon("resources//online//0.png"));
		}
		else {
			btn_head.setIcon(new ImageIcon("resources//online//"+jsonObject.getString("img")+".png"));
		}
		
		textemail.setText(jsonObject.getString("email"));
		textphone.setText(jsonObject.getString("phonenumber"));
		text_back.setText(jsonObject.getString("back"));
		cbSex.setSelectedItem(jsonObject.getString("gend"));
		cbBlood.setSelectedItem(jsonObject.getString("bloodtype"));
//		cbYear.setSelectedItem(jsonObject.getString("yy"));
		cbMonth.setSelectedItem(jsonObject.getString("mm"));

	}

	/**
	 * Create the frame.
	 */
	public Personality() {
		
		
		setTitle("\u4E2A\u4EBA\u8D44\u6599");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setBounds(100, 100, 750, 540);
		setLocationRelativeTo(null);	//正中间位置
		setResizable(false);	//不可改变大小
//		this.setUndecorated(true);	//消除窗体边框
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//全背景
		ImageIcon background = new ImageIcon("resources//personality//bg3.jpg");	//背景要放到JLabel里面才能显示 - //getClass().getResource("qqlogin2.jpg")放在该包下
		JLabel backgroundLabel = new JLabel(background);
		backgroundLabel.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());	//设置标签显示的位置和大小
		this.getLayeredPane().add(backgroundLabel, new Integer(Integer.MIN_VALUE));		//放在jframe的layeredpane层
		JPanel contentPanel = (JPanel)this.getContentPane(); 			//将contentPane层设为透明,jframe直接add的部件都放在这一层
		contentPanel.setOpaque(false);		//将contentPane设置成透明 - 需要将子组件中的jpanel都设成透明才可见
		
		//左边面板
		JPanel panelLeft = new JPanel();
		panelLeft.setBounds(0, 0, 375, 500);
		contentPane.add(panelLeft);
		panelLeft.setLayout(new BorderLayout(0, 0));
		
		JLabel bg1 = new JLabel(new ImageIcon("resources//personality//personality.jpg"));
		panelLeft.add(bg1, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, 100));
		panelLeft.add(panel, BorderLayout.SOUTH); 
		panel.setLayout(null);
		
		btn_head.setToolTipText("\u8BBE\u7F6E\u5934\u50CF");
		btn_head.setFocusPainted(false);  
//		btn_head.setRolloverIcon(new ImageIcon("resources//online//head_highlight.jpg"));   
//		btn_head.setPressedIcon(new ImageIcon("resources//online//head_down.jpg"));  
		btn_head.setBorderPainted(false);  
		btn_head.setContentAreaFilled(false);  
		btn_head.setBounds(21, 25, 48, 48);
		panel.add(btn_head);
		
		text_netName.setText("\u9EC4\u9F99\u58EB");
		text_netName.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		text_netName.setBounds(95, 25, 179, 26);
		panel.add(text_netName);
		text_netName.setColumns(10);
		
		text_info.setText("\u9EC4\u9F99\u58EB\u7684\u4E2A\u6027\u7B7E\u540D");
		text_info.setFont(new Font("宋体", Font.PLAIN, 10));
		text_info.setBounds(95, 58, 179, 21);
		panel.add(text_info);
		text_info.setColumns(10);
		
		JLabel bg2 = new JLabel(new ImageIcon("resources//personality//bg2.jpg"));
		bg2.setBounds(0, 0, 375, 100);
		panel.add(bg2,-1);
		
		
		//右边面板
		JPanel panelRight = new JPanel(); 
		panelRight.setBounds(375, 0, 370, 500);
		contentPane.add(panelRight);
		panelRight.setLayout(new BorderLayout(0, 0));
		panelRight.setOpaque(false);
		
		JPanel panel_control = new JPanel();
		panel_control.setPreferredSize(new Dimension(0, 25));
		panelRight.add(panel_control, BorderLayout.NORTH);
		panel_control.setLayout(null);
		panel_control.setOpaque(false);
		
		JButton btn_close = new JButton(new ImageIcon("resources//login//btn_close_normal.png"));
		btn_close.setFocusPainted(false);  
		btn_close.setRolloverIcon(new ImageIcon("resources//login//btn_close_highlight.png"));   
		btn_close.setPressedIcon(new ImageIcon("resources//login//btn_close_down.png"));  
		btn_close.setBorderPainted(false);  
		btn_close.setContentAreaFilled(false);  
		btn_close.setBounds(330, -10, 36, 36);
		panel_control.add(btn_close);
		
		JPanel panel_1 = new JPanel();
		panelRight.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		panel_1.setOpaque(false);
		
		JLabel lblNewLabel = new JLabel("\u771F\u5B9E\u59D3\u540D");
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 12));
		lblNewLabel.setBounds(20, 60, 56, 25);
		panel_1.add(lblNewLabel);
		
		text_realname = new JTextField();
		text_realname.setBounds(84, 60, 89, 25);
		panel_1.add(text_realname);
		text_realname.setColumns(10);
		
		JLabel label = new JLabel("\u6027  \u522B");
		label.setFont(new Font("宋体", Font.BOLD, 12));
		label.setBounds(185, 60, 56, 25);
		panel_1.add(label);
		
		JLabel label_1 = new JLabel("\u51FA\u751F\u5E74\u6708");
		label_1.setFont(new Font("宋体", Font.BOLD, 12));
		label_1.setBounds(20, 115, 56, 25);
		panel_1.add(label_1);

		cbYear = new JComboBox();
		cbYear.setBounds(84, 115, 56, 25);
		panel_1.add(cbYear);
		
		cbMonth = new JComboBox(str1);
		cbMonth.setBounds(171, 115, 56, 25);
		panel_1.add(cbMonth);
		
		cbDay = new JComboBox();
		cbDay.setBounds(253, 115, 56, 25);
		panel_1.add(cbDay);
		
		JLabel label_2 = new JLabel("\u8840   \u578B");
		label_2.setFont(new Font("宋体", Font.BOLD, 12));
		label_2.setBounds(20, 170, 56, 25);
		panel_1.add(label_2);
		
		cbBlood = new JComboBox(str3);
		cbBlood.setBounds(84, 170, 56, 25);
		panel_1.add(cbBlood);
		
		cbSex = new JComboBox(str4);
		cbSex.setBounds(251, 60, 56, 23);
		panel_1.add(cbSex);
		
		JLabel lblNewLabel_1 = new JLabel("\u804C   \u4E1A");
		lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 12));
		lblNewLabel_1.setBounds(185, 170, 56, 25);
		panel_1.add(lblNewLabel_1);
		
		cbPosition = new JComboBox();
		cbPosition.setBounds(253, 170, 56, 25);
		panel_1.add(cbPosition);
		
		JLabel label_3 = new JLabel("\u624B   \u673A");
		label_3.setFont(new Font("宋体", Font.BOLD, 12));
		label_3.setBounds(20, 220, 56, 25);
		panel_1.add(label_3);
		
		JLabel label_4 = new JLabel("\u6545   \u4E61");
		label_4.setFont(new Font("宋体", Font.BOLD, 12));
		label_4.setBounds(185, 220, 56, 25);
		panel_1.add(label_4);
		
		JLabel label_5 = new JLabel("\u90AE   \u7BB1");
		label_5.setFont(new Font("宋体", Font.BOLD, 12));
		label_5.setBounds(20, 265, 56, 25);
		panel_1.add(label_5);
		
		JLabel label_6 = new JLabel("\u60C5\u611F\u5173\u7CFB");
		label_6.setFont(new Font("宋体", Font.BOLD, 12));
		label_6.setBounds(20, 310, 56, 25);
		panel_1.add(label_6);
		
		JLabel label_7 = new JLabel("\u4E2A\u4EBA\u8BF4\u660E");
		label_7.setFont(new Font("宋体", Font.BOLD, 12));
		label_7.setBounds(20, 350, 56, 25);
		panel_1.add(label_7);
		
		cbHometown = new JComboBox();
		cbHometown.setBounds(253, 220, 56, 25);
		panel_1.add(cbHometown);
		
		textemail.setColumns(10);
		textemail.setBounds(84, 267, 222, 25);
		panel_1.add(textemail);
		
		cbEmotion = new JComboBox();
		cbEmotion.setBounds(84, 310, 56, 25);
		panel_1.add(cbEmotion);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(84, 350, 258, 80);
		panel_1.add(scrollPane);
		
		scrollPane.setViewportView(text_back);
		
		JLabel lblQq = new JLabel("\u4E2A\u4EBA\u8D44\u6599");
		lblQq.setFont(new Font("宋体", Font.BOLD, 14));
		lblQq.setBounds(20, 20, 68, 25);
		panel_1.add(lblQq);
		
		textphone.setColumns(10);
		textphone.setBounds(84, 220, 89, 25);
		panel_1.add(textphone);
		
		// 呈现个人资料
		JSONObject json = JSONObject.fromObject(Config.personality_json_data);
		text_realname.setText(json.getString("realname"));
		
		JButton btn_save = new JButton("\u4FDD\u5B58");
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn_save.setBounds(245, 442, 97, 25);
		panel_1.add(btn_save);
		
		this.personUpdate();
	}
}
