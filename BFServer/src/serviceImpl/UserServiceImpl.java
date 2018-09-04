package serviceImpl;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

import service.UserService;

public class UserServiceImpl implements UserService{
	
	String filePath = "E:/java/SmallJava/BF/BFServer/src/Data";
	String filePath_UsersList = "E:/java/SmallJava/BF/BFServer/src/Data/UsersList.txt";

	@Override
	public boolean login(String username, String password) throws RemoteException {
		//请求登陆的用户的用户名和密码
		String request = username + "_" + password;
		//获取已存在的用户的姓名和密码
		ArrayList<String> users = getUsers();
		if(users.contains(request)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		//请求登陆的用户的用户名和密码
		String request = username;
		//获取已存在的用户的姓名和密码
		ArrayList<String> users = getUsers();
		//获取用户名
		ArrayList<String> usernames = new ArrayList<String>();
		for(int i = 0; i < users.size(); i++) {
			String temp = users.get(i);
			String[] split = temp.split("_");
			usernames.add(split[0]);
		}
		if(usernames.contains(request)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean register(String username, String password) throws RemoteException {
		String str = "";
		//请求注册的用户的用户名和密码
		String request = username + "_" + password;
		
		//获取用户和密码
		ArrayList<String> users = getUsers();
		
		//获取用户名,防止重复注册
		ArrayList<String> usernames = new ArrayList<String>();
		for(int i = 0; i < users.size(); i++) {
			String temp = users.get(i);
			String[] split = temp.split("_");
			usernames.add(split[0]);
		}
		
		//判断用户是否已经注册
		if(!usernames.contains(username)) {
			users.add(request);
			for(int i = 0; i < users.size() - 1; i++) {
				str += users.get(i) + ",";
			}
			str += users.get(users.size() - 1);
			//更新用户列表
			File f = new File(filePath_UsersList);
			try {
				FileWriter writer = new FileWriter(f, false);
				writer.write(str);
				writer.flush();
				writer.close();
				System.out.println("Register Success!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			String newFilePath = filePath + "/" + username;
			String fileName = "FileList.txt";
			File file = new File(newFilePath, fileName);
			if(file.exists()) {
				//文件已经存在，输出文件的相关信息
				System.out.println(file.getAbsolutePath());
				System.out.println(file.getName());
				System.out.println(file.length());
			} else {
				//先创建文件所在的目录
//				file.mkdirs();
				file.getParentFile().mkdirs();
				try {
					//创建文件
					file.createNewFile();
				} catch (IOException e) {
					System.out.println("File create fail...");
					e.printStackTrace();
				}
			}
		
			return true;
		} else {
			return false;
		}
		
	}
	
	//获取已存在的用户的姓名和密码
	public ArrayList<String> getUsers() {
		ArrayList<String> users = new ArrayList<String>();
		try{
			FileInputStream fileIs = new FileInputStream(filePath_UsersList);
			BufferedReader bf = new BufferedReader(new InputStreamReader(fileIs));
			String str = "";
			while((str = bf.readLine()) != null) {
				String[] nameAndPassword = str.split(",");
				for(int i = 0; i < nameAndPassword.length; i++) {
					if(!users.contains(nameAndPassword[i])) {
						users.add(nameAndPassword[i]);
					}
				}
			}
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return users;
	}

}
