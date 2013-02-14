package com.sharegogo.video.data;

import java.sql.SQLException;

import android.content.Context;
import android.content.res.Resources;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.game.R;

/**
 * ������Ϸ��
 * @author weizhengqin
 * @date 2013-2-7
 * @version 1.0
 */
@DatabaseTable(tableName = "people")
public class GamePeople {
	@DatabaseField(generatedId = true)
	public long _id;
	
	@DatabaseField(unique = true)
	public String name;
	
	//��Ϸ����
	@DatabaseField
	public int gameType;
	
	//��˵���߱���
	@DatabaseField
	public int type;
	
	@DatabaseField
	public int hot;
	
	@DatabaseField
	public long update;
	
	static public void makePhonyData(Context context)
	{
		Resources res = context.getResources();
		
		String peopleStr[] = res.getStringArray(R.array.dota);
		
		if(peopleStr != null && peopleStr.length > 0)
		{
			MySqliteHelper helper = SharegogoVideoApplication.getApplication().getHelper();
			
			Dao<GamePeople,String> dao = null;
			
			try {
				dao = helper.getDao(GamePeople.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(String item:peopleStr)
			{
				GamePeople people = new GamePeople();
			
				people.name = item;
				people.gameType = 8;
				
				if(dao != null)
				{
					try {
						dao.createIfNotExists(people);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}