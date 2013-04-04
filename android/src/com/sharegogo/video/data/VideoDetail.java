package com.sharegogo.video.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * ÊÓÆµÏêÇéÓ¦´ğ
 * @author Raymon
 *
 */
@DatabaseTable(tableName = "video")
public class VideoDetail extends BaseResponse{
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
	public long playnum;
	
	@DatabaseField
	public long download;
	
	@DatabaseField
	public String img;
	
	@DatabaseField
	public int state;
	
	@DatabaseField
	public String desc;
	
	@DatabaseField
	public String playurl;
	
	@DatabaseField
	public String flashUrl;
	
	@DatabaseField
	public int length;
	
	@DatabaseField
	public long lastUpdateTime;
	
	@DatabaseField
	public String type;
	
	@DatabaseField
	public long cid;
}
