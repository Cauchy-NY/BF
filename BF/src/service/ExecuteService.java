package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ExecuteService extends Remote{
	
	public String bfexecute(String code, String param) throws RemoteException;
	
	public String ookexecute(String code, String param) throws RemoteException;
	
}