package com.sharegogo.video.data;

public class FavoriteListItem {
	static final public int MODE_NORMAL = 0;
	static final public int MODE_EDIT = 1;
	
	public long id;
	public GameVideo video;
	public int mode;
	
	public FavoriteListItem()
	{
		mode = MODE_NORMAL;
	}
}
