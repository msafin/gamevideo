package com.sharegogo.video;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class MySqliteHelper extends OrmLiteSqliteOpenHelper{

	public MySqliteHelper(Context context)
	{
		this(context, "ormlite_test", null, 1);
	}
	
	public MySqliteHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		
		super(context, databaseName, factory, databaseVersion);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub
		Log.e("database", "create");
		
		try
		{
			TableUtils.createTable(connectionSource, Account.class);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
}
