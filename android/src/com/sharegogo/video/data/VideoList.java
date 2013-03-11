package com.sharegogo.video.data;

/**
 * 视频列表应答
 * @author Raymon
 *
 */
public class VideoList extends BaseResponse{
	public int count;
	public VideoListItem[] list;
	
	static public class VideoListItem
	{
		public long id;
		public String author;
		public String from;
		public long hot;
		public String img;
		public int state;
		public long lastUpdateTime;
	}
}
