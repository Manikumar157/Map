package com.eot.kms;

import gnu.cajo.utils.extra.TransparentItemProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;

import com.thinkways.kms.KMS;
import com.thinkways.kms.security.KMSSecurityException;

public class RemoteBean implements InvocationHandler {

	private Object remoteStub;
	
	private Object localProxy;
	
	private String connectionUrl;
	
	private Class<?> interfaceClass;
	
	public RemoteBean(String ip, String port, String className, String beanName) throws ClassNotFoundException {
	
		connectionUrl = "//" + ip + ":" + port + "/" + beanName;
		interfaceClass = Class.forName(className);
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		try {
			
			if(remoteStub == null) {
				
				initializeRemoteObject();
			}
			return method.invoke(remoteStub, args);
		}
		catch (Exception e) {

			try {
				
				throw getInnermostException(e);
			}
			catch (NoSuchObjectException e1) {
				
				initializeRemoteObject();
				return invoke(proxy, method, args);
			}
			catch (ConnectException e2) {

				throw new KMSSecurityException("Could not connect to remote KMS");
			}
		}
	}
	
	private void initializeRemoteObject() throws Exception {

		System.out.println("Initializing remote object. connectionUrl = " + connectionUrl);
		try {
			
			System.out.println("On lookup = " + Naming.lookup(connectionUrl));
		}
		catch (Exception e) {
			
			System.out.println("Unable to lookup. e = " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Remote reference obtained");
		remoteStub = TransparentItemProxy.getItem(connectionUrl, new Class[]{interfaceClass});
		System.out.println("Remote stub configured. Stub: " + remoteStub );
	}
	
	public Object getRemoteInstance() throws Exception {

		localProxy =  localProxy == null ? 
				localProxy = Proxy.newProxyInstance(interfaceClass.getClassLoader(), 
						new Class[]{interfaceClass}, this) : 
							localProxy;
				
				return localProxy;
	}
	
	private Throwable getInnermostException(Throwable allExceptions) {

		Throwable lastException = allExceptions;
		
		while(lastException.getCause() != null) {
			
			lastException = lastException.getCause();
		}
		
		System.out.println("lastException = " + lastException );
		return lastException;
	}
	
	public static void main(String[] args) {
		
		try {
			RemoteBean connection = new RemoteBean("122.166.51.195", "1198", "com.eot.kms.KMS", "kms");
			KMS kms = (KMS)connection.getRemoteInstance();
			System.out.println("After getting proxy instance");
			kms.exportRMKPvtKey();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
