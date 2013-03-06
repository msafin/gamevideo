package com.sharegogo.video.controller;

public class NotifyManager {
	static private NotifyManager mInstance;

	private NotifyManager() 
	{
	}
	
	static public NotifyManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(NotifyManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new NotifyManager();
				}
			}
		}
		
		return mInstance;
	}
}
