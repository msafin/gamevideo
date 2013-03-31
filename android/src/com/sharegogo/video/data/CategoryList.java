package com.sharegogo.video.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * �����б�Ӧ��
 * @author Raymon
 *
 */
public class CategoryList extends BaseResponse{
	public CategoryListItem[] categories;
	
	@DatabaseTable(tableName = "category")
	static public class CategoryListItem
	{
		@DatabaseField(id = true)
		public long id;
		
		@DatabaseField
		public long parentId;
		
		@DatabaseField
		public String name;
		
		@DatabaseField
		public String img;
		
		@DatabaseField
		public String desc;
	}
}
