//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;
import java.util.ArrayList;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {

	/**
	 * 请实现该方法
	 */
	@Override
	public String bfexecute(String code, String param) throws RemoteException {
		char[] operaters = code.toCharArray();  //BF代码转换为一组运算符并装在char数组中
		char[] data = param.toCharArray();  //BF数据转换为char数组
		ArrayList<Integer> counter = new ArrayList<Integer>();  //运行计数器
		ArrayList<Integer> CodeMark_1 = new ArrayList<Integer>();  //循环标记，'['的下一位
		ArrayList<Integer> CodeMark_2 = new ArrayList<Integer>();  //循环标记，']'的下一位
		counter.add(0);  //初始化计数器第一位
		String result = "";  //输出的结果
		
		int illegalCode = 0;  //非法字符统计
		int CodePointer = 0;  //代码指针
//		int CodeMark_1 = 0;  //循环标记，'['的下一位
//		int CodeMark_2 = 0;  //循环标记，']'的下一位
		int CountPointer = 0;  //计数器指针
		int DataPointer = 0;  //数据指针
		
		//遍历bf代码，标记所有的循环跳转括号
		for(int i = 0; i < operaters.length; i++) {
			if(operaters[i] == '[') {
				CodeMark_1.add(i);
				CodeMark_2.add(-1);
			}
			else if(operaters[i] == ']') {
				//想了好久的标记方法=_='
				//从高位往下检查，发现空位即进行标记，这样下标一致的就是对应的左右括号
				for(int p = CodeMark_1.size() - 1; p >= 0; p--) {
					if(CodeMark_2.get(p) == -1) {
						CodeMark_2.set(p, i);
						break;
					}
				}
			}
		}
		
		while(CodePointer != operaters.length) {
			//循环跳转
			if(operaters[CodePointer] == '[') {
				if(counter.get(CountPointer) == 0) {
					CodePointer = CodeMark_2.get(CodeMark_1.indexOf(CodePointer));
				}
			}
			else if(operaters[CodePointer] == ']') {
				if(counter.get(CountPointer) != 0) {
					CodePointer = CodeMark_1.get(CodeMark_2.indexOf(CodePointer));
				}
			} 
			else {
				//指针指向的字节值加减
				if(operaters[CodePointer] == '+') {
					counter.set(CountPointer, counter.get(CountPointer) + 1);
				}
				else if(operaters[CodePointer] == '-') {
					counter.set(CountPointer, counter.get(CountPointer) - 1);
				}
				//指针移位
				else if(operaters[CodePointer] == '>'){
					counter.add(0);
					CountPointer++;
				}
				else if(operaters[CodePointer] == '<'){
					CountPointer--;
				}
				//结果输出
				else if(operaters[CodePointer] == '.'){
				result += (char)((int)(counter.get(CountPointer))); 
				}
				//数据输入
				else if(operaters[CodePointer] == ','){
					if(DataPointer != data.length) {
						counter.set(CountPointer, (int)data[DataPointer]);
						DataPointer++;
					}
				}
				//统计非法字符
				else {
					illegalCode++;
				}
			}
			//程序计数指针后移
				CodePointer++;
			}
			
			if(illegalCode > 0) {
				result = "文件不能通过编译，因为其中含有" + illegalCode + "个非法字符!";
			}
			
			return result;
	}

	public String ookexecute(String code, String param) throws RemoteException {
		String[] split = code.split(" ");
		String bfCode = "";
		String result = "";
		int illegalCode = 0;  //非法字符统计
		
		for(int i = 0; i < split.length; i += 2) {
			if(!(split[i].equals("Ook.") || split[i].equals("Ook?") || split[i].equals("Ook!"))) {
				illegalCode++;
			}
		}
		
		if(illegalCode > 0) {
			result = "文件不能通过编译，因为其中含有非法字符!";
		} else {
			for(int i = 0; i < split.length; i += 2) {
				String str = split[i] + split[i + 1];
				if(str.equals("Ook.Ook?")) {
					str = ">";
				}
				else if(str.equals("Ook?Ook.")) {
					str = "<";
				}
				else if(str.equals("Ook.Ook.")) {
					str = "+";
				}
				else if(str.equals("Ook!Ook!")) {
					str = "-";
				}
				else if(str.equals("Ook.Ook!")) {
					str = ",";
				}
				else if(str.equals("Ook!Ook.")) {
					str = ".";
				}
				else if(str.equals("Ook!Ook?")) {
					str = "[";
				}
				else if(str.equals("Ook?Ook!")) {
					str = "]";
				}
				bfCode += str;
			}
			result = bfexecute(bfCode, param);
		}
		
		return result;
	}
}
