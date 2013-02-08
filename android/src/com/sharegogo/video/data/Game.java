package com.sharegogo.video.data;

import java.sql.SQLException;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.game.R;

/**
 * 描述游戏类别
 * @author weizhengqin
 * @date 2013-2-7
 * @version 1.0
 */
@DatabaseTable(tableName = "game")
public class Game {
	@DatabaseField(generatedId = true)
	public long _id;
	
	@DatabaseField
	public int type;
	
	@DatabaseField(unique = true)
	public String name;
	
	@DatabaseField
	public int status;
	
	@DatabaseField
	public int hot;
	
	@DatabaseField
	public long update;
	
	
	static public void makePhonyData(Context context)
	{
		Resources res = context.getResources();
		
		String gameStr[] = res.getStringArray(R.array.game);
		
		if(gameStr != null && gameStr.length > 0)
		{
			int index = 0;
			MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
			
			Dao<Game,String> dao = null;
			
			try {
				dao = helper.getDao(Game.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(String item:gameStr)
			{
				//Log.e("test",item);
				//这里不能共用一个game
				Game game = new Game();
				game.type = index;
				game.name = item;
				game.status = 0;
				game.hot = 0;
				game.update = System.currentTimeMillis();
				
				index++;
				
				if(dao != null)
				{
					try {
						dao.createIfNotExists(game);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
