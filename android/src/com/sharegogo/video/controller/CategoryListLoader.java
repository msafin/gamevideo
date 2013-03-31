package com.sharegogo.video.controller;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.j256.ormlite.dao.Dao;
import com.sharegogo.video.SharegogoVideoApplication;
import com.sharegogo.video.data.MySqliteHelper;
import com.sharegogo.video.data.CategoryList.CategoryListItem;

public class CategoryListLoader extends AsyncTaskLoader<List<CategoryListItem>>
{
	private long mParentId = -1;
	
	public CategoryListLoader(Context context,long parentId) {
		super(context);
		// TODO Auto-generated constructor stub
		mParentId = parentId;
	}

	@Override
	public List<CategoryListItem> loadInBackground() {
		// TODO Auto-generated method stub
		MySqliteHelper dbHelper = SharegogoVideoApplication.getApplication().getHelper();
		List<CategoryListItem> categoryList = null;
		
		try {
			Dao<CategoryListItem,String> dao = dbHelper.getDao(CategoryListItem.class);
			
			categoryList = dao.queryForEq("parentId", String.valueOf(mParentId));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return categoryList;
	}

	@Override
	protected void onStartLoading() {
		// TODO Auto-generated method stub
		super.onStartLoading();
		forceLoad();
	}
}
