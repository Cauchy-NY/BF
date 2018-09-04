package serviceImpl;

import java.io.*;
import java.util.ArrayList;

import service.IOService;

public class IOServiceImpl implements IOService{
	
	String filePath_UsersList = "E:/java/SmallJava/BF/BFServer/src/Data/UsersList.txt";
	
	@Override
	public boolean writeFile(String file, String userId, String fileName, String type) {
		String site_1 = "E:/java/SmallJava/BF/BFServer/src/Data/" + userId + "/" + fileName + "." + type + ".txt";
		String site_2 = "E:/java/SmallJava/BF/BFServer/src/Data/" + userId + "/" +  "FileList.txt";
		String tempFileName = fileName + "." + type + ".txt" + ",";
		file += "/";
		File f1 = new File(site_1);
		File f2 = new File(site_2);
		try {
			//保存代码文件
			FileWriter fw1 = new FileWriter(f1, true);
			fw1.write(file);
			fw1.flush();
			fw1.close();
			//更新文件列表
			FileWriter fw2 = new FileWriter(f2, true);
			fw2.write(tempFileName);
			fw2.flush();
			fw2.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String readFile(String userId, String fileName, String type, int versionNumber) {
		//确定文件有多少个版本
		String code = "";
		String filePath = "E:/java/SmallJava/BF/BFServer/src/Data/" + userId + "/" + fileName + "." + type + ".txt";
		ArrayList<String> codes = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			while ((code = br.readLine()) != null) {
				code = code.substring(0, code.length() - 1);
				String[] version = code.split("/");
				for(int i = 0; i < version.length; i++) {
					codes.add(version[i]);
				}
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return codes.get(versionNumber - 1);
	}

	@Override
	public String readFileList(String userId) {
		String str = "";
		String site = "E:/java/SmallJava/BF/BFServer/src/Data/" + userId + "/" +  "FileList.txt";
		File f = new File(site);
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			while (br.readLine() != null) {
				str += br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
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
