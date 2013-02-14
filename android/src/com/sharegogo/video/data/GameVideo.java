package com.sharegogo.video.data;

import java.sql.SQLException;

import android.content.Context;
import android.content.res.Resources;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.game.R;

@DatabaseTable(tableName="video")
public class GameVideo {
	@DatabaseField(generatedId = true)
	public long _id;
	@DatabaseField(unique = true)
	public long id;
	@DatabaseField
	public int gameType;
	@DatabaseField
	public int type;
	@DatabaseField
	public int people;
	@DatabaseField
	public long time;
	@DatabaseField
	public int status;
	@DatabaseField
	public int hot;
	@DatabaseField
	public String desc;
	@DatabaseField
	public String url;
	
	static public void makePhonyData(Context context)
	{
		Resources res = context.getResources();
		
		String games[] = res.getStringArray(R.array.dota_game);
		
		if(games != null && games.length > 0)
		{
			int index = 0;
			MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
			
			Dao<GameVideo,String> dao = null;
			
			try {
				dao = helper.getDao(GameVideo.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(String item:games)
			{
				GameVideo video = new GameVideo();
			
				video.id = index;
				video.url = item;
				index++;
				if(dao != null)
				{
					try {
						dao.createIfNotExists(video);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
