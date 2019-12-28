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
	
	//��Ҫ����Ӧ�������ö�����ȫ��
	private JButton btn_send,btn_exit,btn_register;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);		//swing���Ƥ��
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
		setLocationRelativeTo(null);	//���м�λ��
		setVisible(true);		//����jframe����û�м�
		setResizable(false);	//���ɸı��С
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		initBG();	//���ñ���
		
		JPanel panelC = new JPanel();
		contentPane.add(panelC, BorderLayout.CENTER);
		panelC.setLayout(null);
		panelC.setOpaque(false);
		
		JLabel lab_username = new JLabel("\u624B\u673A/Email");
		lab_username.setFont(new Font("����", Font.BOLD, 14));
		lab_username.setBounds(20, 30, 85, 35);
//		lab_username.setOpaque(true);  //�˾����ص㣬���ñ�����ɫ�����Ƚ�������Ϊ��͸���ģ���ΪĬ����͸���ġ�����
//		lab_username.setBackground(Color.white);
		panelC.add(lab_username);
		
		text_username = new JTextField();
		text_username.setBounds(105, 30, 175, 35);
		panelC.add(text_username);
		text_username.setColumns(10);
		
		JLabel lab_password = new JLabel(" \u8F93\u5165\u5BC6\u7801");
		lab_password.setFont(new Font("����", Font.BOLD, 14));
		lab_password.setBounds(20, 100, 85, 35);
		panelC.add(lab_password);
		
		text_password = new JTextField();
		text_password.setColumns(10);
		text_password.setBounds(105, 100, 175, 35);
		panelC.add(text_password);
		
		JLabel lab_certain = new JLabel(" \u786E\u5B9A\u5BC6\u7801");
		lab_certain.setFont(new Font("����", Font.BOLD, 14));
		lab_certain.setBounds(20, 170, 85, 35);
		panelC.add(lab_certain);
		
		text_certain = new JTextField();
		text_certain.setColumns(10);
		text_certain.setBounds(105, 170, 175, 35);
		panelC.add(text_certain);
		
		JLabel lab_code = new JLabel("\u9A8C \u8BC1 \u7801");
		lab_code.setHorizontalAlignment(SwingConstants.CENTER);
		lab_code.setFont(new Font("����", Font.BOLD, 14));
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
				
				String username_str = text_username.getText().trim();	//�õ��û���
				String password = text_password.getText().trim();
				String certain = text_certain.getText().trim();
				String code = text_code.getText().trim();
				if(username_str.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "�ֻ�/email������д!");
					return;
				}
				if(password.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "���������д!");
					return;
				}
				if(certain.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "����ȷ�ϱ�����д!");
					return;
				}
				if(code.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "��֤�������д!");
					return;
				}
				if(!password.trim().equals(certain)) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "����ȷ�ϱ���������һ��!");
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
				
				if(json.getInt("state") == 0 ) {	//ע��ɹ�
					javax.swing.JOptionPane.showMessageDialog(register.this, ("��ϲ��!ע��ɹ�!����qq������:"+json.getString("qqnumber")));
					text_certain.setText("");
					text_code.setText("");
					text_password.setText("");
					text_username.setText("");
				}else if(json.getInt("state") ==2 ){
					javax.swing.JOptionPane.showMessageDialog(register.this, "ע��ʧ�ܣ��û����Ѵ���!");
				}else if(json.getInt("state") ==1 ){
					javax.swing.JOptionPane.showMessageDialog(register.this, "����ʧ�ܣ���֤�����!");
				}else if(json.getInt("state") ==3 ){
					javax.swing.JOptionPane.showMessageDialog(register.this, "����ʧ�ܣ�δ֪����!");
				}
					
				input.close();
				output.close();
				socket.close();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}else if (e.getSource() == btn_send) { 
			//������֤��
			try {
				String username_str = text_username.getText().trim();	//�õ��û���
				if(username_str.trim().equals("")) 
				{
					javax.swing.JOptionPane.showMessageDialog(register.this, "�ֻ�/email������д!");
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
				if(json.getInt("state") ==0 ) {	//���ͳɹ�
					javax.swing.JOptionPane.showMessageDialog(register.this, "���ͳɹ�!");
				}else {
					javax.swing.JOptionPane.showMessageDialog(register.this, "����ʧ�ܣ�����ֻ�/email��дʧ��!");
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
		labelbg.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());	//���ñ�ǩ��ʾ��λ�úʹ�С
		this.getLayeredPane().add(labelbg, new Integer(Integer.MIN_VALUE));		//����jframe��layeredpane��
		JPanel contentPanel = (JPanel)this.getContentPane(); 			//��contentPane����Ϊ͸��,jframeֱ��add�Ĳ�����������һ��
		contentPanel.setOpaque(false);		//��contentPane���ó�͸�� - ��Ȼûɶ�ã�ʵ������Ϊ�б߽粼�֣����Եý������ϵ�JPanel���͸�����ܿ�������
	}
	
}
