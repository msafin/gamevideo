package com.sharegogo.video.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.view.Gravity;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.table.TableUtils;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.activity.FavoriteFragment.FavoriteListItem;
import com.sharegogo.video.data.Favorite;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.data.VideoList.VideoListItem;
import com.sharegogo.video.game.R;

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
	
	
	public boolean addFavorite(Favorite favorite)
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
	
	public boolean delFavorite(Favorite favorite)
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		try {
			Dao<Favorite,String> favoriteDao = null;
			
			favoriteDao = helper.getDao(Favorite.class);
			favoriteDao.delete(favorite);
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean clearFavorite()
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		try {
			TableUtils.clearTable(helper.getConnectionSource(), Favorite.class);
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Favorite getFavorite(long videoId)
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		try {
			Dao<Favorite,String> favoriteDao = null;
			
			favoriteDao = helper.getDao(Favorite.class);
			
			List<Favorite> favorites = favoriteDao.queryForEq("video_id", videoId);
			
			if(favorites != null && favorites.size() > 0)
			{
				return favorites.get(0);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<FavoriteListItem> getFavoriteList()
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		Dao<Favorite,String> favoriteDao = null;
		List<Favorite> favorite = null;
		List<FavoriteListItem> data = null;
		
		try {
			favoriteDao = helper.getDao(Favorite.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			favorite = favoriteDao.queryForAll();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(favorite != null && favorite.size() > 0)
		{
			Dao<VideoListItem,String> videoDao = null;
			
			try {
				videoDao = helper.getDao(VideoListItem.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(videoDao != null)
			{
				data = new ArrayList<FavoriteListItem>();
				
				for(Favorite item:favorite)
				{
					FavoriteListItem favoriteItem = new FavoriteListItem();
				
					favoriteItem.id = item.id;
					
					try {
						favoriteItem.video = videoDao.queryForId(String.valueOf(item.video_id));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					data.add(favoriteItem);
				}
			}
		}
		
		return data;
	}
}
