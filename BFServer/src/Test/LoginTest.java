package Test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginTest extends JFrame {
	JButton jb1;
	JButton jb2;
	JTextField jtf;
	JPasswordField jpwd;
	JLabel jl1;
	JLabel jl2;
	JPanel jp1;
	JPanel jp2;
	JPanel jp3;
	
	public static void main(String[] args) {
		//LoginTest lt = new LoginTest();
		SaveTest st = new SaveTest();
	}
	
	public LoginTest() {
		//创建组件  
		jb1 = new JButton("确认");
		jb2 = new JButton("取消");
		jtf = new JTextField(10);
		jpwd = new JPasswordField(10);
		
		jl1 = new JLabel("用户名：");
		jl2 = new JLabel("密    码：");
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		
		//设置布局管理器
		this.setLayout(new GridLayout(3, 1, 5, 5));
		
		//添加组件
		jp1.add(jl1);
		jp1.add(jtf);
		
		jp2.add(jl2);
		jp2.add(jpwd);
		
		jp3.add(jb1);
		jp3.add(jb2);
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
		
		//设置窗体属性
		this.setTitle("登录界面");
		this.setSize(360, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}

class SaveTest extends JFrame {
	JTextField jtf;
	JLabel jl;
	JPanel jp1;
	JPanel jp2;
	
	public SaveTest() {
		//创建组件  
		JButton jb1 = new JButton("确认");
		JButton jb2 = new JButton("取消");
		
		jl = new JLabel("设置文件名：");
		jtf = new JTextField(10);
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		
		//设置布局管理器
		this.setLayout(new GridLayout(3, 2, 5, 5));
		
		//添加组件
		jp1.add(jl);
		jp1.add(jtf);
		jp2.add(jb1);
		jp2.add(jb2);
		this.add(jp1);
		this.add(jp2);
		
		//设置监听
		jb1.addActionListener(new SureButtonListener());
		jb2.addActionListener(new CancelButtonListener());
		
		//设置窗体属性
		this.setTitle("注册用户");
		this.setSize(400, 260);
		this.setLocation(600, 350);
		this.setVisible(true);
	}
	
	class SureButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
	class CancelButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
}