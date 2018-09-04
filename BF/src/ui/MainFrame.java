package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.undo.UndoManager;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;

import rmi.RemoteHelper;


public class MainFrame extends JFrame {
	static JTextArea codeTextArea;
	static JTextArea paraTextArea;
	static JTextArea resultTextArea;
	static String type;
	static String code;
	static String user = "Nobody";
	static String fileName = "";
	static JMenuBar menuBar;
	static JMenu userMenu;
	static JMenuItem loginMenuItem;
	static JMenuItem registerMenuItem;
	static UndoManager undomg;

	public MainFrame() {
		// 创建窗体
		JFrame frame = new JFrame("BF Client");
		frame.setLayout(new BorderLayout());
		
		//File下拉菜单
		menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenu newMenu = new JMenu("New");
		fileMenu.add(newMenu);
		JMenuItem bfMenuItem = new JMenuItem("Bf");
		newMenu.add(bfMenuItem);
		JMenuItem ookMenuItem = new JMenuItem("Ook!");
		newMenu.add(ookMenuItem);
		
		JMenuItem openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		
		JMenu saveMenu = new JMenu("Save");
		fileMenu.add(saveMenu);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenu.add(saveMenuItem);
		JMenuItem saveAsMenuItem = new JMenuItem("Save as");
		saveMenu.add(saveAsMenuItem);
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		frame.setJMenuBar(menuBar);
		
		//Run下拉菜单
		JMenu runMenu = new JMenu("Run");
		menuBar.add(runMenu);
		JMenuItem executeMenuItem = new JMenuItem("Execute");
		runMenu.add(executeMenuItem);
		
		//version下拉菜单
		JMenu versionMenu = new JMenu("Version");
		menuBar.add(versionMenu);
		JMenuItem chooseMenuItem = new JMenuItem("Choose");
		versionMenu.add(chooseMenuItem);
		JMenuItem undoMenuItem = new JMenuItem("Undo");
		versionMenu.add(undoMenuItem);
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		versionMenu.add(redoMenuItem);
		
		//User下拉菜单
		userMenu = new JMenu("User");
		menuBar.add(userMenu);
		loginMenuItem = new JMenuItem("Login");
		userMenu.add(loginMenuItem);
		registerMenuItem = new JMenuItem("Register");
		userMenu.add(registerMenuItem);
//		JMenuItem logoutMenuItem = new JMenuItem("Logout");
//		userMenu.add(logoutMenuItem);
		
		//设置监听
		ookMenuItem.addActionListener(new NewItemActionListener());
		bfMenuItem.addActionListener(new NewItemActionListener());
		
		exitMenuItem.addActionListener(new ExitActionListener());
//		logoutMenuItem.addActionListener(new LogoutActionListener());
		openMenuItem.addActionListener(new OpenActionListener());
		chooseMenuItem.addActionListener(new ChooseActionListener());
		undoMenuItem.addActionListener(new UndoActionListener());
		redoMenuItem.addActionListener(new RedoActionListener());
		
		saveMenuItem.addActionListener(new SaveActionListener());
		saveAsMenuItem.addActionListener(new SaveAsActionListener());
		executeMenuItem.addActionListener(new ExecuteActionListener());
		loginMenuItem.addActionListener(new LoginActionListener());
		registerMenuItem.addActionListener(new RegisterActionListener());
		
		//设置代码和数据文本区域
		JPanel textPanel = new JPanel(new BorderLayout());
		codeTextArea = new JTextArea("//Add some code here!", 5, 40);
		codeTextArea.setMargin(new Insets(10, 10, 10, 10));
		codeTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		codeTextArea.setFont(new Font("宋体", Font.PLAIN, 20));
		codeTextArea.requestFocus();
//		codeTextArea.setBackground(Color.LIGHT_GRAY);
		
		paraTextArea = new JTextArea("//Input your parameter here!", 5, 40);
		paraTextArea.setMargin(new Insets(10, 10, 10, 10));
		paraTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		paraTextArea.setFont(new Font("宋体", Font.PLAIN, 20));
		paraTextArea.requestFocus();
//		paraTextArea.setBackground(Color.GREEN);
		
		//设置滚动条
		JScrollPane js1 = new JScrollPane(codeTextArea);
		JScrollPane js2 = new JScrollPane(paraTextArea);
		
//		js1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		js1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//		js2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		js2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		undomg = new UndoManager();
		codeTextArea.getDocument().addUndoableEditListener(undomg);
		paraTextArea.getDocument().addUndoableEditListener(undomg);
		
		textPanel.add(js1, BorderLayout.CENTER);
		textPanel.add(js2, BorderLayout.EAST);
		frame.getContentPane().add(textPanel, BorderLayout.CENTER);
		
		//设置结果文本区域
		resultTextArea = new JTextArea("//Your result will be shown here!", 8, 42);
		resultTextArea.setMargin(new Insets(10, 10, 10, 10));
		resultTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//		resultTextArea.setBackground(Color.LIGHT_GRAY);
		resultTextArea.setFont(new Font("宋体", Font.PLAIN, 20));
		resultTextArea.setEditable(false);  //结果不可编辑
		frame.getContentPane().add(resultTextArea, BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocation(400, 200);
		frame.setVisible(true);
	}
	
	//open按钮
	class OpenActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//改变根路径  
			FileSystemView fsv = new DirectoryRestrictedFileSystemView(new File("E:\\java\\SmallJava\\BF\\BFServer\\src\\Data\\" + user));
			JFileChooser fileChooser = new JFileChooser(fsv);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.showDialog(new JLabel(), "选择");
			//制定初始路径
			fileChooser.setCurrentDirectory(new File("E:\\java\\SmallJava\\BF\\BFServer\\src\\Data\\" + user));
			File file = fileChooser.getSelectedFile();
			if(file.isDirectory()){
				System.out.println("文件夹:"+file.getAbsolutePath());
			} else if(file.isFile()) {
				System.out.println("文件:"+file.getAbsolutePath());
			}
			System.out.println(fileChooser.getSelectedFile().getName());
			
			//写入信息
			String parentFile = file.getParent();
			parentFile = parentFile.trim();
			fileName = parentFile.substring(parentFile.lastIndexOf("\\") + 1);

			String[] newtemp = fileChooser.getSelectedFile().getName().split("\\.");
			fileName = newtemp[0];
			type = newtemp[1];
			
			//读入最新版本的文件
			String code = "";
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				while ((code = br.readLine()) != null) {
					code = code.substring(0, code.length() - 1);
					String[] version = code.split("/");
					codeTextArea.setText(version[version.length - 1]);
					paraTextArea.setText("");
					resultTextArea.setText("");
				}
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	//new按钮
	class NewItemActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			codeTextArea.setText("");
			paraTextArea.setText("");
			resultTextArea.setText("");
			String cmd = e.getActionCommand();
			if (cmd.equals("Bf")) {
				type = "Bf";
			} else if (cmd.equals("Ook!")) {
				type = "Ook";
			} 
		}
	}
	
	//save按钮
	class SaveActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(fileName.equals("")) {
				System.out.println("文件名不得为空...");
				SaveReminder sr = new SaveReminder();
			} else {
				String code = codeTextArea.getText();
				try {
					boolean isWrited = RemoteHelper.getInstance().getIOService().writeFile(code, user, fileName, type);
					if(isWrited) {
						System.out.println("File write successfully.");
					} else {
						System.out.println("File fail to write.");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	//save as按钮
	class SaveAsActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			code = codeTextArea.getText();
			Save save = new Save();
		}
	}
	
	//exit按钮
	class ExitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	//execute按钮
	class ExecuteActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			code = codeTextArea.getText();
			String para = paraTextArea.getText();
			try {
				if(type.equals("Bf")) {
					String result = RemoteHelper.getInstance().getExecuteService().bfexecute(code, para);
					resultTextArea.setText(result);
				} else if (type.equals("Ook")) {
					String result = RemoteHelper.getInstance().getExecuteService().ookexecute(code, para);
					resultTextArea.setText(result);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	//choose按钮
	static class ChooseActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Choose choose = new Choose();
		}
	}
		
	//undo按钮
	static class UndoActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(undomg.canUndo()) {
				undomg.undo();
			} else {
				JOptionPane.showMessageDialog(null,"无法撤销","警告",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	//redo按钮
	static class RedoActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(undomg.canRedo()) {
				undomg.redo();
			} else {
				JOptionPane.showMessageDialog(null,"无法撤销","警告",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	//login按钮
	static class LoginActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Login login = new Login();
		}
	}
	
	//register按钮
	static class RegisterActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Register register = new Register();
		}
	}
	
}

//试一下

class DirectoryRestrictedFileSystemView extends FileSystemView {
	
	private final File[] rootDirectories;
	
	DirectoryRestrictedFileSystemView(File rootDirectory) {
		this.rootDirectories = new File[] {rootDirectory};
	}
	
	DirectoryRestrictedFileSystemView(File[] rootDirectories) {
		this.rootDirectories = rootDirectories;
	}
	
	@Override
	public File createNewFolder(File arg0) throws IOException {
		throw new UnsupportedOperationException("Unable to create directory");
	}
	
	@Override
	public File[] getRoots() {
		return rootDirectories;
	}
	
	@Override
	public boolean isRoot(File file) {
		for(File root : rootDirectories) {
			if(root.equals(file)) {
				return true;
			}
		}
		return false;
	}
	
}

//登陆失败提示窗口
class LoginReminder extends JFrame {
	JLabel jl;
	JPanel jp1;
	JPanel jp2;
	
	public LoginReminder() {
		//创建组件  
		JButton jb1 = new JButton("注册");
		JButton jb2 = new JButton("重新登录");
		
		jl = new JLabel("  用户名或密码错误 =_='");
		jl.setFont(new Font("宋体", Font.PLAIN, 20));
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		
		//添加组件
		jp1.add(jl);
		jp2.add(jb1);
		jp2.add(jb2);
		this.add(jp1, BorderLayout.CENTER);
		this.add(jp2, BorderLayout.SOUTH);
		
		//设置监听
		jb1.addActionListener(new RegisterButtonListener());
		jb2.addActionListener(new ReloginButtonListener());
		
		//设置窗体属性
		this.setTitle("登陆失败");
		this.setSize(400, 150);
		this.setLocation(600, 350);
		this.setVisible(true);
		
	}
	
	class RegisterButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			Register register = new Register();
		}
	}
	
	class ReloginButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			Login login = new Login();
		}
	}
	
}

//注册失败提示窗口
class RegisterReminder extends JFrame {
	JLabel jl;
	JPanel jp1;
	JPanel jp2;
	
	public RegisterReminder() {
		//创建组件  
		JButton jb1 = new JButton("确定");
		JButton jb2 = new JButton("重新注册");
		
		jl = new JLabel("  用户名已经存在 =_='");
		jl.setFont(new Font("宋体", Font.PLAIN, 20));
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		
		//添加组件
		jp1.add(jl);
		jp2.add(jb1);
		jp2.add(jb2);
		this.add(jp1, BorderLayout.CENTER);
		this.add(jp2, BorderLayout.SOUTH);
		
		//设置监听
		jb1.addActionListener(new SureButtonListener());
		jb2.addActionListener(new ReregisterButtonListener());
		
		//设置窗体属性
		this.setTitle("注册失败");
		this.setSize(400, 150);
		this.setLocation(600, 350);
		this.setVisible(true);
		
	}
	
	class SureButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
	class ReregisterButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			Register register = new Register();
		}
	}
	
}

//保存失败提示窗口
class SaveReminder extends JFrame {
	JLabel jl;
	JPanel jp1;
	JPanel jp2;
	
	public SaveReminder() {
		//创建组件  
		JButton jb1 = new JButton("确定");
		JButton jb2 = new JButton("另存为");
		
		jl = new JLabel("   文件名不得为空 =_='");
		jl.setFont(new Font("宋体", Font.PLAIN, 20));
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		
		//添加组件
		jp1.add(jl);
		jp2.add(jb1);
		jp2.add(jb2);
		this.add(jp1, BorderLayout.CENTER);
		this.add(jp2, BorderLayout.SOUTH);
		
		//设置监听
		jb1.addActionListener(new SureButtonListener());
		jb2.addActionListener(new ResaveButtonListener());
		
		//设置窗体属性
		this.setTitle("保存失败");
		this.setSize(400, 150);
		this.setLocation(600, 350);
		this.setVisible(true);
		
	}
	
	class SureButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
	class ResaveButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			Save save = new Save();
		}
	}
	
}

//保存文件窗口
class Save extends JFrame {
	JTextField jtf;
	JLabel jl;
	JPanel jp1;
	JPanel jp2;
	
	public Save() {
		//创建组件  
		JButton jb1 = new JButton("确认");
		JButton jb2 = new JButton("取消");
		
		jtf = new JTextField(10);
		jl = new JLabel("设置文件名：");
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		
		//设置布局管理器
		this.setLayout(new GridLayout(3, 1, 5, 5));
		
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
			MainFrame.fileName = jtf.getText();
			
//			//获取已经登陆的用户的文件列表
//			ArrayList<String> fileList = new ArrayList<String>();
//			try {
//				String str = RemoteHelper.getInstance().getIOService().readFileList(MainFrame.user);
//				str = str.substring(0, str.length() - 1);
//				String[] files = str.split(",");
//				for(int i = 0; i < files.length; i++) {
//					if(!fileList.contains(files[i])) {
//						fileList.add(files[i]);
//					}
//				}
//			} catch (RemoteException e1) {
//				e1.printStackTrace();
//			}
			
			
			try {
				boolean isWrited = RemoteHelper.getInstance().getIOService().writeFile(MainFrame.code, MainFrame.user, MainFrame.fileName, MainFrame.type);
				if(isWrited) {
					System.out.println("File write successfully.");
				} else {
					System.out.println("File fail to write.");
				}
			} catch (RemoteException e2) {
				e2.printStackTrace();
			}
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

//选择版本窗口
class Choose extends JFrame {
	JTextField jtf;
	JPasswordField jpwd;
	JLabel jl;
	JPanel jp1;
	JPanel jp2;
	JPanel jp3;
	
	public Choose() {
		//确定文件有多少个版本
		int number = 0;
		String code = "";
		String filePath = "E:/java/SmallJava/BF/BFServer/src/Data/" + MainFrame.user + "/" + MainFrame.fileName + "." + MainFrame.type + ".txt";;
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			while ((code = br.readLine()) != null) {
				code = code.substring(0, code.length() - 1);
				String[] version = code.split("/");
				number = version.length;
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//创建组件  
		JButton jb1 = new JButton("确认");
		JButton jb2 = new JButton("取消");
		
		jl = new JLabel("请选择历史版本，现在共有" + number + "版本 ( 请输入1-" + number + "进行选择 )");
		jtf = new JTextField(10);
		
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		
		//设置布局管理器
		this.setLayout(new GridLayout(3, 1, 5, 5));
		
		//添加组件
		jp1.add(jl);
		jp2.add(jtf);
		jp3.add(jb1);
		jp3.add(jb2);
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
		
		//设置监听
		jb1.addActionListener(new SureButtonListener());
		jb2.addActionListener(new CancelButtonListener());
		
		//设置窗体属性
		this.setTitle("选择版本");
		this.setSize(400, 260);
		this.setLocation(600, 350);
		this.setVisible(true);
	}
	
	class SureButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int versionNumber = Integer.parseInt(jtf.getText());
			try {
				String code = RemoteHelper.getInstance().getIOService().readFile(MainFrame.user, MainFrame.fileName, MainFrame.type, versionNumber);
				MainFrame.codeTextArea.setText(code);
				MainFrame.paraTextArea.setText("");
				MainFrame.resultTextArea.setText("");
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
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

//注册用户窗口
class Register extends JFrame {
	JTextField jtf;
	JPasswordField jpwd;
	JLabel jl1;
	JLabel jl2;
	JPanel jp1;
	JPanel jp2;
	JPanel jp3;
	
	public Register() {
		//创建组件  
		JButton jb1 = new JButton("确认");
		JButton jb2 = new JButton("取消");
		jtf = new JTextField(10);
		jpwd = new JPasswordField(10);
		
		jl1 = new JLabel("设置用户名：");
		jl2 = new JLabel("设置密码：");
		
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
			String username = jtf.getText();
			@SuppressWarnings("deprecation")
			String password = jpwd.getText();
			try {
				boolean isRegister = RemoteHelper.getInstance().getUserService().register(username, password);
				if(isRegister) {
					dispose();
					Login login = new Login();
				} else {
					dispose();
					RegisterReminder registerReminder = new RegisterReminder(); 
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	class CancelButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
		
}

//登陆窗口
class Login extends JFrame {
	JTextField jtf;
	JPasswordField jpwd;
	JLabel jl1;
	JLabel jl2;
	JPanel jp1;
	JPanel jp2;
	JPanel jp3;
	static JLabel jl;
	static JMenuItem logout;
	static boolean isLogin;
	
	public Login() {
		//创建组件  
		JButton jb1 = new JButton("确认");
		JButton jb2 = new JButton("取消");
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
		
		//设置监听
		jb1.addActionListener(new SureButtonListener());
		jb2.addActionListener(new CancelButtonListener());
		
		//设置窗体属性
		this.setTitle("用户登陆");
		this.setSize(400, 260);
		this.setLocation(600, 350);
		this.setVisible(true);
	}
	
	class SureButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = jtf.getText();
			@SuppressWarnings("deprecation")
			String password = jpwd.getText();
			try {
				isLogin = RemoteHelper.getInstance().getUserService().login(username, password);
				if(isLogin) {
					System.out.println("Login Success!");
					MainFrame.user = username;
//					MainFrame.codeTextArea.setText("");
//					MainFrame.paraTextArea.setText("");
//					MainFrame.resultTextArea.setText("");
					
					jl = new JLabel("welcome, " + username);
					MainFrame.menuBar.add(jl);
					
					MainFrame.userMenu.removeAll();
					logout = new JMenuItem("Logout");
					MainFrame.userMenu.add(logout);
					logout.addActionListener(new LogoutActionListener());
				} else {
					LoginReminder r = new LoginReminder();
					System.out.println("Login Fail...");
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			dispose();
		}
	}
	
	//logout按钮
	class LogoutActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				boolean isLogout = RemoteHelper.getInstance().getUserService().logout(MainFrame.user);
				if(isLogout) {
					MainFrame.user = "Nobody";
					MainFrame.fileName = "";
					MainFrame.menuBar.remove(jl);;
					MainFrame.userMenu.removeAll();
					MainFrame.loginMenuItem = new JMenuItem("Login");
					MainFrame.userMenu.add(MainFrame.loginMenuItem);
					MainFrame.registerMenuItem = new JMenuItem("Register");
					MainFrame.userMenu.add(MainFrame.registerMenuItem);
					
					MainFrame.loginMenuItem.addActionListener(new MainFrame.LoginActionListener());
					MainFrame.registerMenuItem.addActionListener(new MainFrame.RegisterActionListener());
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	class CancelButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
		
}
