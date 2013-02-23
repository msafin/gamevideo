package com.sharegogo.video.data;

import java.sql.SQLException;

import android.content.Context;
import android.content.res.Resources;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.game.R;

@DatabaseTable(tableName="favotie")
public class Favorite {
	@DatabaseField(generatedId = true)
	public long id;
	
	@DatabaseField(unique = true)
	public long video_id;
	
	@DatabaseField
	public long update;
	
	static public void makePhonyData(Context context)
	{
		MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
	
		Dao<Favorite,String> dao = null;
		
		try {
			dao = helper.getDao(Favorite.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < 3; i ++)
		{
			Favorite item = new Favorite();
			
			item.video_id = i+1;
			item.update = System.currentTimeMillis();
			
			if(dao != null)
			{
				try {
					dao.createIfNotExists(item);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
