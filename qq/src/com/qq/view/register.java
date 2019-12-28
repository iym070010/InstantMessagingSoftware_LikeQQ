package com.qq.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.qq.view.server.NetService;
import com.qq.view.util.Config;

import net.sf.json.JSONObject;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class register extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField text_username;
	private JTextField text_password;
	private JTextField text_certain;
	private JTextField text_code;
	
	//需要做响应的组件最好都放在全局
	private JButton btn_send,btn_exit,btn_register;

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
					register frame = new register();
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
	public register() {
		setTitle("\u6CE8\u518C\u8D26\u6237");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 425);
		setLocationRelativeTo(null);	//正中间位置
		setVisible(true);		//所有jframe这里没有加
		setResizable(false);	//不可改变大小
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		initBG();	//设置背景
		
		JPanel panelC = new JPanel();
		contentPane.add(panelC, BorderLayout.CENTER);
		panelC.setLayout(null);
		panelC.setOpaque(false);
		
		JLabel lab_username = new JLabel("\u624B\u673A/Email");
		lab_username.setFont(new Font("宋体", Font.BOLD, 14));
		lab_username.setBounds(20, 30, 85, 35);
//		lab_username.setOpaque(true);  //此句是重点，设置背景颜色必须先将它设置为不透明的，因为默认是透明的。。。
//		lab_username.setBackground(Color.white);
		panelC.add(lab_username);
		
		text_username = new JTextField();
		text_username.setBounds(105, 30, 175, 35);
		panelC.add(text_username);
		text_username.setColumns(10);
		
		JLabel lab_password = new JLabel(" \u8F93\u5165\u5BC6\u7801");
		lab_password.setFont(new Font("宋体", Font.BOLD, 14));
		lab_password.setBounds(20, 100, 85, 35);
		panelC.add(lab_password);
		
		text_password = new JTextField();
		text_password.setColumns(10);
		text_password.setBounds(105, 100, 175, 35);
		panelC.add(text_password);
		
		JLabel lab_certain = new JLabel(" \u786E\u5B9A\u5BC6\u7801");
		lab_certain.setFont(new Font("宋体", Font.BOLD, 14));
		lab_certain.setBounds(20, 170, 85, 35);
		panelC.add(lab_certain);
		
		text_certain = new JTextField();
		text_certain.setColumns(10);
		text_certain.setBounds(105, 170, 175, 35);
		panelC.add(text_certain);
		
		JLabel lab_code = new JLabel("\u9A8C \u8BC1 \u7801");
		lab_code.setHorizontalAlignment(SwingConstants.CENTER);
		lab_code.setFont(new Font("宋体", Font.BOLD, 14));
		lab_code.setBounds(20, 239, 85, 35);
		panelC.add(lab_code);
		
		text_code = new JTextField();
		text_code.setColumns(10);
		text_code.setBounds(105, 239, 77, 35);
		panelC.add(text_code);
		
		btn_send = new JButton("\u53D1\u9001\u9A8C\u8BC1");
		btn_send.setBounds(183, 296, 97, 23);
		panelC.add(btn_send);
		
		JPanel panelS = new JPanel();
		panelS.setPreferredSize(new Dimension(0, 50));
		contentPane.add(panelS, BorderLayout.SOUTH);
		panelS.setLayout(null);
		panelS.setOpaque(false);
		
		btn_exit = new JButton("\u9000\u51FA");
		btn_exit.setBounds(15, 15, 100, 25);
		panelS.add(btn_exit);
		
		btn_register = new JButton("\u6CE8\u518C\u8D26\u6237");
		btn_register.setBounds(210, 15, 100, 25);
		panelS.add(btn_register);
		
		addBtnListener();
	}

	private void addBtnListener() {
		
		btn_exit.addActionListener(this);
		btn_register.addActionListener(this);
		btn_send.addActionListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btn_exit) {
			this.dispose();
		}else if (e.getSource() == btn_register) {
			try {
				
				String username_str = text_username.getText().trim();	//得到用户名
				String password = text_password.getText().trim();
				String certain = text_certain.getText().trim();
				String code = text_code.getText().trim();
				if(username_str.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "手机/email必须填写!");
					return;
				}
				if(password.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "密码必须填写!");
					return;
				}
				if(certain.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "密码确认必须填写!");
					return;
				}
				if(code.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "验证码必须填写!");
					return;
				}
				if(!password.trim().equals(certain)) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "密码确认必须与密码一致!");
					return;
				}
				
				Socket socket = new Socket(Config.IP,Config.REG_PORT);
				InputStream input = socket.getInputStream();
				OutputStream output = socket.getOutputStream();
				
				output.write(("{\"type\":\"reg\",\"username\":\""+username_str+"\",\"password\":\""+password+"\",\"code\":\""+code+"\"}").getBytes());
				output.flush();
				
				byte[] bytes =new byte[1024];
				int len = input.read(bytes);
				String string = new String(bytes,0,len);
				JSONObject json = JSONObject.fromObject(string);
				
				if(json.getInt("state") == 0 ) {	//注册成功
					javax.swing.JOptionPane.showMessageDialog(register.this, ("恭喜您!注册成功!您的qq号码是:"+json.getString("qqnumber")));
					text_certain.setText("");
					text_code.setText("");
					text_password.setText("");
					text_username.setText("");
				}else if(json.getInt("state") ==2 ){
					javax.swing.JOptionPane.showMessageDialog(register.this, "注册失败，用户名已存在!");
				}else if(json.getInt("state") ==1 ){
					javax.swing.JOptionPane.showMessageDialog(register.this, "发送失败，验证码错误!");
				}else if(json.getInt("state") ==3 ){
					javax.swing.JOptionPane.showMessageDialog(register.this, "发送失败，未知错误!");
				}
					
				input.close();
				output.close();
				socket.close();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}else if (e.getSource() == btn_send) { 
			//发送验证码
			try {
				String username_str = text_username.getText().trim();	//得到用户名
				if(username_str.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "手机/email必须填写!");
					return;
				}
				
				Socket socket = new Socket(Config.IP,Config.REG_PORT);
				InputStream input = socket.getInputStream();
				OutputStream output = socket.getOutputStream();
				
				output.write(("{\"type\":\"code\",\"username\":\""+username_str+"\"}").getBytes());
				output.flush();
				
				byte[] bytes =new byte[1024];
				int len = input.read(bytes);
				String string = new String(bytes,0,len);
				JSONObject json = JSONObject.fromObject(string);
				if(json.getInt("state") ==0 ) {	//发送成功
					javax.swing.JOptionPane.showMessageDialog(register.this, "发送成功!");
				}else {
					javax.swing.JOptionPane.showMessageDialog(register.this, "发送失败，你的手机/email填写失败!");
				}
				
				input.close();
				output.close();
				socket.close();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
	}
	
	private void initBG() {	

		ImageIcon background = new ImageIcon("resources//register//regbg.png");	
		JLabel labelbg = new JLabel(background);
		labelbg.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());	//设置标签显示的位置和大小
		this.getLayeredPane().add(labelbg, new Integer(Integer.MIN_VALUE));		//放在jframe的layeredpane层
		JPanel contentPanel = (JPanel)this.getContentPane(); 			//将contentPane层设为透明,jframe直接add的部件都放在这一层
		contentPanel.setOpaque(false);		//将contentPane设置成透明 - 虽然没啥用，实际上因为有边界布局，所以得将布局上的JPanel设成透明才能看到背景
	}
	
}
