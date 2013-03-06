package com.sharegogo.video.controller;

/**
 * ����������
 * @author Raymon
 *
 */
public class UpdateManager {
	static private UpdateManager mInstance;
	
	private UpdateManager()
	{
	}
	
	static public UpdateManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(UpdateManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new UpdateManager();
				}
			}
		}
		
		return mInstance;
	}
}
