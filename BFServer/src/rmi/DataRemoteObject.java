package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import service.*;
import serviceImpl.*;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService, ExecuteService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private IOService iOService;
	private UserService userService;
	private ExecuteService executeService;
	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeService = new ExecuteServiceImpl();
	}

	@Override
	public boolean writeFile(String file, String userId, String fileName, String type) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.writeFile(file, userId, fileName, type);
	}

	@Override
	public String readFile(String userId, String fileName, String type, int versionNumber) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFile(userId, fileName, type, versionNumber);
	}

	@Override
	public String readFileList(String userId) throws RemoteException{
		// TODO Auto-generated method stub
		return iOService.readFileList(userId);
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.logout(username);
	}

	@Override
	public boolean register(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.register(username, password);
	}
	
	@Override
	public String bfexecute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.bfexecute(code, param);
	}
	
	@Override
	public String ookexecute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.ookexecute(code, param);
	}

}
