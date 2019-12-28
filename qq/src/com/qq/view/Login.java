package com.qq.view;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.qq.view.server.NetService;
import com.qq.view.util.Config;

import net.sf.json.JSONObject;

public class Login extends JFrame implements ActionListener,ItemListener{

	//边框布局
	private BorderLayout bLayout;
	
	//南北中西面板容器
	private JPanel PanelNorth;
	private JPanel PanelWest;
	private JPanel PanelSouth;
	private JPanel PanelCenter;
	
	//南面变量
	private JButton jble,loginButton,jbri;		//多账号登陆、登陆、二维码登陆
	
	//北面变量
	private JButton jbnc,jbnm,jbnn;		//右上角三个按钮
	
	//中面变量
	private JButton jbu,jl1,jl2;		//小键盘、注册账号、找回密码
	private JComboBox username;		//账号栏
	private JPasswordField password;	//密码栏
	private JCheckBox jch1,jch2;		//记住密码、自动登陆
	
	//西面变量
	private JButton jcoc;	//登陆状态	//到时候要改成JComboBox形式
	private JPanel jpin;	//重绘需要
	private Image qqhead;

	
	public Login() {		//构造器
		
		initBG();
		initUI();
		addBtnListener();
		initListener();
		
		this.setTitle("qq登陆界面");
		this.setSize(380,294);
		this.setLocationRelativeTo(null);	//位置于正中间
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		//退出只退出当前窗口
		this.setResizable(false);	//设置不能调整窗口大小
		this.setUndecorated(true);	//消除窗体边框
		this.setVisible(true);	//设置窗体可见
		
		Graphics g=jpin.getGraphics(); 	//重绘，窗体可见之后取画布
	}
	 
	
//	private void saveFile() {		// 登陆成功后保存下次登陆信息
//
//	    try{
//
//	        String username_str = username.getEditor().getItem().toString().trim();	//获取下拉列表编辑的值(包括选择值)
//			String password_str = password.getText().trim();
//			String remamber = jch1.isSelected()+"";		// 保存设置
//			String auto = jch2.isSelected()+"";
//			JSONObject jsonObject = JSONObject.fromObject(Config.personality_json_data);	// 保存img
//			String img_str = new String();
//			if (jsonObject.getString("img").equals("无")) {
//				img_str = "resources//online//0.png";
//			}
//			else {
//				img_str = "resources//online//"+jsonObject.getString("img")+".png";
//			}
//			String content = username_str+","+password_str+","+remamber+","+auto+","+img_str;
//	        File file =new File("resources//login.txt");
//
//	        if(!file.exists()){
//	        	file.createNewFile();
//	        }
//
//	        FileWriter fileWritter = new FileWriter(file.getName(),true);
//	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//	        bufferWritter.write(content);
//	        bufferWritter.close();
//	        fileWritter.close();
//
//	    }catch(IOException e){
//	        e.printStackTrace();
//	    }
//	}

	private void initUI() {
		// TODO Auto-generated method stub
		
		bLayout = new BorderLayout();
		this.setLayout(bLayout);
		
		PanelNorth = new JPanel();
		PanelSouth = new JPanel();
		PanelWest = new JPanel();
		PanelCenter = new JPanel();
		
		//北面面板设计
		PanelNorth.setLayout(null);
		PanelNorth.setPreferredSize(new Dimension(0,140));	//设置面板大小
		PanelNorth.setOpaque(false);
			//退出按钮
		jbnc = new JButton(new ImageIcon("resources//login//btn_close_normal.png"));  
		jbnc.setBounds(342,-1,39,20);  //x,y,width,high
		jbnc.setRolloverIcon(new ImageIcon("resources//login//btn_close_highlight.png"));  //鼠标放上去
		jbnc.setPressedIcon(new ImageIcon("resources//login//btn_close_down.png"));  	//鼠标按压
		jbnc.setBorderPainted(false);  	//消除边框
		jbnc.setFocusPainted(false);  	//取消文本强调
		jbnc.setContentAreaFilled(false);	//想要按钮透明必须还得这样去除默认填充
		jbnc.setToolTipText("关闭");  	//鼠标放上去的提示语
		PanelNorth.add(jbnc); 
			//最小化按钮
		jbnm = new JButton(new ImageIcon("resources//login//btn_mini_normal.png"));
		jbnm.setBounds(315,-1,28,20);
		jbnm.setRolloverIcon(new ImageIcon("resources//login//btn_mini_highlight.png"));
		jbnm.setPressedIcon(new ImageIcon("resources//login//btn_mini_down.png"));
		jbnm.setBorderPainted(false);
		jbnm.setFocusPainted(false);
		jbnm.setContentAreaFilled(false);	
		jbnm.setToolTipText("最小化");
		PanelNorth.add(jbnm);
			//设置
		jbnn = new JButton(new ImageIcon("resources//login//btn_set_normal.png"));  
		jbnn.setBounds(288,-1,28,20);  
		jbnn.setRolloverIcon(new ImageIcon("resources//login//btn_set_hover.png"));  
		jbnn.setPressedIcon(new ImageIcon("resources//login//btn_set_press.png"));  
		jbnn.setBorderPainted(false);  
		jbnn.setFocusPainted(false);  
		jbnn.setContentAreaFilled(false);
		jbnn.setToolTipText("设置");  
		PanelNorth.add(jbnn);  
		
		//南面面板设计
		PanelSouth.setOpaque(false);		//将面板透明，里面内容不透明，这样可以把背景显现 
		PanelSouth.setPreferredSize(new Dimension(0,51));  
        PanelSouth.setBorder(null); 
        PanelSouth.setLayout(null); 
        	//设置多账号登陆
        jble = new JButton(new ImageIcon("resources//login//corner_left.png"));  
        jble.setPreferredSize(new Dimension(40,40));  
        jble.setFocusPainted(false);  
        jble.setRolloverIcon(new ImageIcon("resources//login//corner_left_hover.png"));  
        jble.setPressedIcon(new ImageIcon("resources//login//corner_left_press.png"));  
        jble.setBorderPainted(false);  
        jble.setContentAreaFilled(false);  
        jble.setBounds(0,11,40,40);  
//        JToolTip jtl = new JToolTip();  ???
//        jtl.setOpaque(false);  
//        jtl.setBackground(Color.WHITE);  
        jble.setToolTipText("多账号登录");  
        	//设置登陆按钮  
        ImageIcon logimg = new ImageIcon("resources//login//button_login_normal.png");  
        loginButton = new JButton("登 录",logimg);  
        loginButton.setFont(new Font("宋体",Font.BOLD,14));  	//粗体
        loginButton.setForeground(Color.white);
        loginButton.setHorizontalTextPosition(SwingConstants.CENTER);//将文字放在图片中间  
        loginButton.setFocusPainted(false);//设置点击不出现边框  
        loginButton.setContentAreaFilled(false); //设置透明
        loginButton.setRolloverIcon(new ImageIcon("resources//login//button_login_hover.png"));  
        loginButton.setPressedIcon(new ImageIcon("resources//login//button_login_down.png"));  //设置选择图标  
        loginButton.setBorderPainted(false);  //是否画边框  
        loginButton.setBounds(113,8,162,38);  	//设置位置很关键
//        	//设置右边按钮  
        jbri = new JButton(new ImageIcon("resources//login//corner_right_normal.png"));  
        jbri.setFocusPainted(false);  
        jbri.setRolloverIcon(new ImageIcon("resources//login//corner_right_hover.png"));  
        jbri.setPressedIcon(new ImageIcon("resources//login//corner_right_normal_down.png"));  
        jbri.setBorderPainted(false);  
        jbri.setContentAreaFilled(false);  
        jbri.setBounds(330,4,45,38);  
        jbri.setToolTipText("二维码登录");  
        loginButton.setBorder(BorderFactory.createLoweredBevelBorder());  	//设置下凹  
        PanelSouth.add(jble);  	//将按钮对象添加到面板上  
        PanelSouth.add(loginButton);  
        PanelSouth.add(jbri);         
        
		//西面面板设计
		PanelWest.setOpaque(false);
	    PanelWest.setPreferredSize(new Dimension(102,0));  	//设置西边面板容器的大小 
	    PanelWest.setLayout(new FlowLayout(FlowLayout.RIGHT)); 	//设置西边面板的布局方式为流式布局  
	    	//西面面板内容
	    ImageIcon imageWest = new ImageIcon("resources//login//qq.jpg");  	//
	    qqhead = imageWest.getImage(); 
	    jpin = new JPanel(){    		//qq头像 - 后续可以换成用户自己的头像
            public void paintComponent(Graphics g){    
               g.drawImage(qqhead, 0,0,this.getWidth(), this.getHeight(),null);  
            }    
        };
        jpin.setPreferredSize(new Dimension(82,82));
        jpin.setLayout(null);    
        jpin.setOpaque(false); 
        jcoc = new JButton(new ImageIcon("resources//login//Qme.png"));  
        jcoc.setBounds(64, 64, 18, 18);    
        jcoc.setFocusPainted(false);   
        jcoc.setOpaque(false);
        jcoc.setContentAreaFilled(false);
        jpin.add(jcoc); 
        PanelWest.add(jpin);  
		 
		//中面面板设计
		PanelCenter.setOpaque(false);
		PanelCenter.setLayout(null);  
        	//JcomboBox实现下拉框 
        String str []= {"624730725","251227228"};  
        username = new JComboBox(str);  		//用户名
        username.setEditable(true);  //设置下拉框可编辑  
        username.setBounds(7, 4, 185, 25);  
        username.setFont(new Font("Calibri ",0,13));  //设置默认字体
        PanelCenter.add(username);  
        	//实例化一个标签类的对象  
        jl1 = new JButton("注册账号");  		//到时候改button或者添加超链接 
        jl1.setFont(new Font("宋体",0,13));   
        jl1.setBounds(195,4,90,25);
        jl1.setForeground(Color.black);
        jl1.setFocusPainted(false); 
        PanelCenter.add(jl1);  
        	//实例化一个JLabel类的对象  				//到时候改button或者添加超链接
        jl2 = new JButton("找回密码");  
        jl2.setFont(new Font("宋体",0,12));  
        jl2.setForeground(Color.black);  
        jl2.setBounds(195, 38, 90, 25); 
        jl2.setFocusPainted(false); 
        PanelCenter.add(jl2);  
        	//实例化小键盘图标对象  
        ImageIcon keyboard = new ImageIcon("resources//login//keyboard.png");
        jbu = new JButton(keyboard);  
        jbu.setPreferredSize(new Dimension(22,20));  
        jbu.setBorderPainted(false);      
        	//实例化一个JPasswordField类的对象  
        password = new JPasswordField();  	//密码
        password.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));   
        LineBorder lin = new LineBorder(Color.WHITE,3,true);  
        password.setBorder(lin);  
        password.setBounds(7,38,185,23);  
        password.add(jbu);  
        password.setPreferredSize(new Dimension(185,25));  //设置大小  
        PanelCenter.add(password); 
        	//实例化两个JCheckBox类的对象  
        jch1 = new JCheckBox("记住密码");  
        jch1.setFocusPainted(false); //选中时没有边框  
        jch1.setFont(new Font("宋体",0,13));//字体  
        jch1.setBounds(2, 70, 78, 15);  
        PanelCenter.add(jch1);  
        jch2 = new JCheckBox("自动登录");  
        jch2.setFocusPainted(false);  
        jch2.setFont(new Font("宋体",0,12));  
        jch2.setBounds(80, 70, 78, 15);  
        PanelCenter.add(jch2);  
        	//设置复选框透明  
        jch1.setOpaque(false);  
        jch2.setOpaque(false);  
		
		this.add(PanelNorth,bLayout.NORTH);
		this.add(PanelSouth,bLayout.SOUTH);
		this.add(PanelWest,bLayout.WEST); 
		this.add(PanelCenter,bLayout.CENTER);
		
	}

	private void initBG() {	

		ImageIcon background = new ImageIcon("resources//login//qqlogin2.jpg");	//背景要放到JLabel里面才能显示 - //getClass().getResource("qqlogin2.jpg")放在该包下
		JLabel backgroundLabel = new JLabel(background);
		backgroundLabel.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());	//设置标签显示的位置和大小
		this.getLayeredPane().add(backgroundLabel, new Integer(Integer.MIN_VALUE));		//放在jframe的layeredpane层
		JPanel contentPanel = (JPanel)this.getContentPane(); 			//将contentPane层设为透明,jframe直接add的部件都放在这一层
		contentPanel.setOpaque(false);		//将contentPane设置成透明 - 虽然没啥用，实际上因为有边界布局，所以得将布局上的JPanel设成透明才能看到背景
	}	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JFrame.setDefaultLookAndFeelDecorated(true);		//swing框架皮肤
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		Login login = new Login();
		
	}

	private void addBtnListener() {		//jble,jb,jbri;jbnc,jbnm,jbnn;jbu,jl1,jl2;
		jble.addActionListener(this);	//登陆
		loginButton.addActionListener(this);
		jbri.addActionListener(this);
		jbnc.addActionListener(this);	//右上三
		jbnm.addActionListener(this);
		jbnn.addActionListener(this);
		jbu.addActionListener(this);	//中间
		jl1.addActionListener(this);	//注册
		jl2.addActionListener(this);
	}
	
	private void initListener() {	//jcoCenter,jch1,jch2,jcoc
		
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jbnc) {		//退出
			this.dispose();//销毁当前窗口
		}else if (e.getSource() == jbnm) { 
			this.setExtendedState(this.ICONIFIED);	//最小化
		}else if (e.getSource() == jbnn) {
			//设置界面
		}else if (e.getSource() == jbu) {
			//添加小键盘KeyListener
		}else if (e.getSource() == jl1) {		//弹出注册
			new register();
		}else if (e.getSource() == jl2) {
//			try {		//方法一
//			Runtime.getRuntime().exec( "cmd.exe /c start "+"www.baidu.com");	//执行cmd打开默认浏览器+跳转页面
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//			if (Desktop.isDesktopSupported()) {
//			      try {
//			        Desktop.getDesktop().browse(new URI("https://aq.qq.com/v2/uv_aq/html/reset_pwd/pc_reset_pwd_input_account.html?v=3.0&old_ver_account="));
//			      }
//			      catch (URISyntaxException | IOException ex) {	//首先检测是否有GUI桌面系统的存在，这样就不会在只有命令行的服务器系统下运行时也企图打开网页了
//			        ex.printStackTrace();
//			      }
//			    }
			new Recome();
		}else if (e.getSource() == loginButton) {		//登陆
			//用户名和密码
			String username_str = username.getEditor().getItem().toString().trim();	//获取下拉列表编辑的值(包括选择值)
			String password_str = password.getText().trim();
			if(username_str.trim().equals("")||password_str.trim().equals(""))
			{
				javax.swing.JOptionPane.showMessageDialog(Login.this, "用户名和密码必须填写!");
				return;
			}
			Config.username = username_str;
			Config.password = password_str;
			
			try {
				JSONObject json = NetService.getNetService().login();
				if(json.getInt("state") == 0) {
//					javax.swing.JOptionPane.showMessageDialog(Login.this, "登陆成功");
					new MainMenu();
					//记录登陆信息写入本地
//					this.saveFile();
					
					this.dispose();
									
				}else {
					javax.swing.JOptionPane.showMessageDialog(Login.this, json.getString("msg"));
				} 
				
			} catch (Exception e1) {
				e1.printStackTrace();
				javax.swing.JOptionPane.showMessageDialog(Login.this, "网络连接失败!");
			}
		}
	
	}

}
