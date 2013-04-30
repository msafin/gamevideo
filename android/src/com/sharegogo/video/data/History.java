package com.sharegogo.video.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="history")
public class History {
	@DatabaseField(generatedId = true)
	public long id;
	
	@DatabaseField(unique = true)
	public long video_id;
	
	@DatabaseField
	public long update;
	
	@DatabaseField
	public long pos;
}
