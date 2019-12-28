package com.qq.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.qq.view.util.Config;

import net.sf.json.JSONObject;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class Recome extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

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
					Recome frame = new Recome();
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
	public Recome() {
		setTitle("\u627E\u56DE\u5BC6\u7801");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 421);
		setLocationRelativeTo(null);	//���м�λ��
		setVisible(true);		//����jframe����û�м�
		setResizable(false);	//���ɸı��С
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		initBG();
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 400, 421);
		contentPane.add(panel);
		panel.setLayout(null);
		panel.setOpaque(false);
		
		JLabel lblqq = new JLabel("\u8F93\u5165 Q Q");
		lblqq.setBounds(43, 65, 85, 35);
		panel.add(lblqq);
		lblqq.setHorizontalAlignment(SwingConstants.CENTER);
		lblqq.setFont(new Font("����", Font.BOLD, 14));
		lblqq.setForeground(Color.black);
		
		JLabel lblnumber = new JLabel("\u624B\u673A/Email");
		lblnumber.setBounds(43, 116, 85, 35);
		panel.add(lblnumber);
		lblnumber.setHorizontalAlignment(SwingConstants.CENTER);
		lblnumber.setFont(new Font("����", Font.BOLD, 14));
		lblnumber.setForeground(Color.black);
		
		JLabel lblcode = new JLabel("\u9A8C \u8BC1 \u7801");
		lblcode.setBounds(43, 178, 85, 35);
		panel.add(lblcode);
		lblcode.setHorizontalAlignment(SwingConstants.CENTER);
		lblcode.setFont(new Font("����", Font.BOLD, 14));
		lblcode.setForeground(Color.black);
		
		textField = new JTextField();
		textField.setBounds(140, 65, 180, 35);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(140, 116, 180, 35);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(140, 178, 87, 35);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton = new JButton("\u53D1\u9001\u9A8C\u8BC1");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String username_str = lblnumber.getText().trim();	//�õ��û���
					if(username_str.trim().equals("")) 
					{
						javax.swing.JOptionPane.showMessageDialog(Recome.this, "�ֻ�/email������д!");
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
						javax.swing.JOptionPane.showMessageDialog(Recome.this, "���ͳɹ�!");
					}else {
						javax.swing.JOptionPane.showMessageDialog(Recome.this, "����ʧ�ܣ�����ֻ�/email��дʧ��!");
					}
					
					input.close();
					output.close();
					socket.close();
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(261, 238, 87, 30);
		panel.add(btnNewButton);
		
		JButton button = new JButton("\u9000  \u51FA");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button.setBounds(41, 310, 87, 30);
		panel.add(button);
		
		JButton button_1 = new JButton("\u627E\u56DE\u5BC6\u7801");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					String username = lblnumber.getText().trim();	//�õ��û��� 
					String qqnumber = lblqq.getText().trim();
					String code = lblcode.getText().trim();
					if(username.trim().equals("")) 
					{
						javax.swing.JOptionPane.showMessageDialog(Recome.this, "�ֻ�/email������д!");
						return;
					}
					if(qqnumber.trim().equals("")) 
					{
						javax.swing.JOptionPane.showMessageDialog(Recome.this, "QQ���������д!");
						return;
					}
					if(code.trim().equals("")) 
					{
						javax.swing.JOptionPane.showMessageDialog(Recome.this, "��֤�������д!");
						return;
					}
					
					Socket socket = new Socket(Config.IP,Config.REG_PORT);
					InputStream input = socket.getInputStream();
					OutputStream output = socket.getOutputStream();
					
					output.write(("{\"type\":\"recome\",\"username\":\""+username+"\",\"qqnumber\":\""+qqnumber+"\",\"code\":\""+code+"\"}").getBytes());
					output.flush();
					
					byte[] bytes =new byte[1024];
					int len = input.read(bytes);
					String string = new String(bytes,0,len);
					JSONObject json = JSONObject.fromObject(string);
					
					if(json.getInt("state") ==0 ) {	//�һسɹ�
						javax.swing.JOptionPane.showMessageDialog(Recome.this, ("��ϲ��!�һسɹ�!�����˺�������:"+json.getString("password")));
						lblcode.setText("");
						lblnumber.setText("");
						lblqq.setText("");
					}else if(json.getInt("state") ==2 ){
						javax.swing.JOptionPane.showMessageDialog(Recome.this, "ע��ʧ�ܣ���QQ�����ڻ�����ֻ�/Email��ƥ��!");
					}else if(json.getInt("state") ==1 ){
						javax.swing.JOptionPane.showMessageDialog(Recome.this, "����ʧ�ܣ���֤�����!");
					}else if(json.getInt("state") ==3 ){
						javax.swing.JOptionPane.showMessageDialog(Recome.this, "����ʧ�ܣ�δ֪����!");
					}
						
					input.close();
					output.close();
					socket.close();
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
			}
		});
		button_1.setBounds(261, 310, 87, 30);
		panel.add(button_1);
	}
	
	private void initBG() {	
		ImageIcon background = new ImageIcon("resources//register//regbg2.png");	
		JLabel labelbg = new JLabel(background);
		labelbg.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());	//���ñ�ǩ��ʾ��λ�úʹ�С
		this.getLayeredPane().add(labelbg, new Integer(Integer.MIN_VALUE));		//����jframe��layeredpane��
		JPanel contentPanel = (JPanel)this.getContentPane(); 			//��contentPane����Ϊ͸��,jframeֱ��add�Ĳ�����������һ��
		contentPanel.setOpaque(false);		//��contentPane���ó�͸�� - ��Ȼûɶ�ã�ʵ������Ϊ�б߽粼�֣����Եý������ϵ�JPanel���͸�����ܿ�������
	}
	
}
