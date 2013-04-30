package com.sharegogo.video.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.activity.HistoryFragment.HistoryListItem;
import com.sharegogo.video.data.History;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.data.VideoDetail;
import com.sharegogo.video.data.VideoList.VideoListItem;

/**
 * 播放历史管理器
 * @author Raymon
 * @version 1.0
 */
public class HistoryManager {
	static private HistoryManager mInstance;
	
	static public HistoryManager getInstance()
	{
		if(mInstance == null)
		{
			synchronized(HistoryManager.class)
			{
				if(mInstance == null)
				{
					mInstance = new HistoryManager();
				}
			}
		}
		
		return mInstance;
	}
	
	public boolean addHistory(long videoId,long progress)
	{
//		Log.i("jardge", "video id="+videoId+" progress="+progress);
		History history = new History();
		
		history.video_id = videoId;
		history.update = System.currentTimeMillis();
		history.pos = progress;
		
		return addHistory(history);
	}
	
	public boolean addHistory(History history)
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		try {
			
//			Log.i("jardge", "addHistory vid="+history.video_id+" pos ="+history.pos);
			
			Dao<History,String> dao = helper.getDao(History.class);
			
			List<History> historys = dao.queryForEq("video_id", history.video_id);
			
			History oldhistory = null; 
			
			if(historys != null && historys.size() > 0)
			{
				oldhistory = historys.get(0);
			}
			
			if(oldhistory != null && oldhistory.video_id == history.video_id)
			{
				oldhistory.update = history.update;
				oldhistory.pos = history.pos;
//				dao.updateId(oldhistory, ""+history.pos);
				dao.update(oldhistory);
//				Log.i("jardge", "dao.update(history) vid="+history.video_id+" pos ="+history.pos);
			}else{
				dao.createIfNotExists(history);
//				Log.i("jardge", "return ");
			}		
			
			return true;
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean delHistory(History history)
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		try {
			Dao<History,String> historyDao = null;
			
			historyDao = helper.getDao(History.class);
			historyDao.delete(history);
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean clearHistory()
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		try {
			TableUtils.clearTable(helper.getConnectionSource(), History.class);
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public History getHistory(long videoId)
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		try {
			Dao<History,String> historyDao = null;
			
			historyDao = helper.getDao(History.class);
			
			List<History> historys = historyDao.queryForEq("video_id", videoId);
			
			if(historys != null && historys.size() > 0)
			{
				return historys.get(0);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<HistoryListItem> getHistoryList()
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
		
		Dao<History,String> historyDao = null;
		List<History> historys = null;
		List<HistoryListItem> data = null;
		
		try {
			historyDao = helper.getDao(History.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			historys = historyDao.queryForAll();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(historys != null && historys.size() > 0)
		{
			Dao<VideoDetail,String> videoDao = null;
			
			try {
				videoDao = helper.getDao(VideoDetail.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(videoDao != null)
			{
				data = new ArrayList<HistoryListItem>();
				
				for(History item:historys)
				{
					HistoryListItem historyItem = new HistoryListItem();
				
					historyItem.id = item.id;
					historyItem.time = item.update;
//					Log.i("jardge", "history add id="+item.video_id+" pos="+item.pos);
					historyItem.progress = item.pos;					
					try {
						historyItem.video = videoDao.queryForId(String.valueOf(item.video_id));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					data.add(historyItem);
				}
			}
		}
		
		return data;
	}
}
