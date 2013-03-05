package com.sharegogo.video.controller;

public class UserManager {
	static private UserManager mInstance;
	
	private String mToken;
	
	private UserManager() 
	{
		if(mToken == null)
		{
			autoRegister();
		}
	}
	
	static public UserManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(UserManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new UserManager();
				}
			}
		}
		
		return mInstance;
	}
	
	public String getToken()
	{
		synchronized(mInstance)
		{
			if(mToken == null)
			{
				autoRegister();
			}
			
			return mToken;
		}
	}
	
	private void autoRegister()
	{
		
	}
}
