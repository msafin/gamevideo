package com.sharegogo.video.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 视频列表应答
 * @author Raymon
 *
 */
public class VideoList extends BaseResponse{
	static final public int TYPE_LIST_HOT = 0;
	static final public int TYPE_LIST_LATEST = 1;
	static final public int TYPE_LIST_RECOMMEND = 2;
	
	public int count;
	public VideoListItem[] list;
	
	@DatabaseTable(tableName = "video")
	static public class VideoListItem
	{
		@DatabaseField(id = true)
		public long id;
		
		@DatabaseField
		public String name;
		
		@DatabaseField
		public String author;
		
		@DatabaseField
		public String from;
		
		@DatabaseField
		public long hot;
		
		@DatabaseField
		public String img;
		
		@DatabaseField
		public int state;
		
		@DatabaseField
		public long lastUpdateTime;
		
		@DatabaseField
		public String categoryName;
	}
}
