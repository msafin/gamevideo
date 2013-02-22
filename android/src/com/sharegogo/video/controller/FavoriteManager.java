package com.sharegogo.video.controller;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.data.Favorite;
import com.sharegogo.video.data.MySqliteHelper;

/**
 *  ’≤ÿπ‹¿Ì∆˜
 * @author Raymon
 * @version 1.0
 */
public class FavoriteManager {
	static private FavoriteManager mInstance;
	
	static public FavoriteManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(FavoriteManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new FavoriteManager();
				}
			}
		}
		
		return mInstance;
	}
	
	
	public boolean addFavoriteItem(Favorite favorite)
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		try {
			
			Dao<Favorite,String> dao = helper.getDao(Favorite.class);
			
			dao.createOrUpdate(favorite);
			
			return true;
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
