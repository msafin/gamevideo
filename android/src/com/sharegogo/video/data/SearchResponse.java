package com.sharegogo.video.data;

/**
 * ËÑË÷Ó¦´ð
 * @author Raymon
 *
 */
public class SearchResponse extends BaseResponse{
	public int status;
	public String msg;
	public int count;
	public int listType;
	public int asc;
	public SearchListItem[] list;
	
	static public class SearchListItem
	{
		public long id;
		public String name;
		public String author;
		public String from;
		public long hot;
		public String img;
		public int state;
		public long lastUpdateTime;
	}
}
