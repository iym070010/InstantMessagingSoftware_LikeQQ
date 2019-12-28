package com.qq.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.qq.view.server.NetService;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class Select extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;

	Vector cols = new Vector();		//结合tab表一起使用
	Vector rows = new Vector();
	

	/**
	 * Create the frame.
	 */
	public Select() {
		setTitle("\u67E5\u8BE2\u597D\u53CB");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 489, 351);
		setLocationRelativeTo(null);	//正中间位置
		setResizable(false);	//不可改变大小
		setVisible(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u6635\u79F0");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 22, 78, 22);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(89, 23, 248, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("\u67E5\u8BE2");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String username = textField.getText();		// 输入qq号
				
				NetService.getNetService().searchPerson(username);
				
			}
		});
		btnNewButton.setBounds(365, 22, 97, 23);
		contentPane.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(19, 54, 443, 243);
		contentPane.add(scrollPane);
		
		cols.add("昵称");
		cols.add("在线");
		
		table = new JTable(rows,cols);
		scrollPane.setViewportView(table);

		
	}
}
