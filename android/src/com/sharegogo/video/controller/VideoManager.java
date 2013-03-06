package com.sharegogo.video.controller;

/**
 *  ”∆µπ‹¿Ì∆˜
 * @author Raymon
 *
 */
public class VideoManager {
	static private VideoManager mInstance;
	
	private VideoManager()
	{
		
	}
	
	static public VideoManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(VideoManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new VideoManager();
				}
			}
		}
		
		return mInstance;
	}
}
