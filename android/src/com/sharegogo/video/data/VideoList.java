package com.sharegogo.video.data;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 视频列表应答
 * @author Raymon
 *
 */
public class VideoList extends BaseResponse{
	static final public int TYPE_LIST_HOT = 1;
	static final public int TYPE_LIST_LATEST = 2;
	static final public int TYPE_LIST_RECOMMEND = 3;
	
	public int count;
	public VideoListItem[] list;
	
	static public class VideoListItem
	{
		public long id;
		
		public String name;
		
		public String author;
		
		public String from;
		
		public long hot;
		
		public String img;
		
		public int state;
		
		public long lastUpdateTime;
		
		public VideoDetail toVideoDetail()
		{
			VideoDetail detail = new VideoDetail();
			detail.id  = id;
			detail.name = name;
			detail.author = author;
			detail.from = from;
			detail.hot = hot;
			detail.img = img;
			detail.state = state;
			detail.lastUpdateTime = lastUpdateTime;
			
			return detail;
		}
	}
	
	public List<VideoDetail> toVideoDetailList()
	{
		if(list != null && list.length > 0)
		{
			List<VideoDetail> detailList = new ArrayList<VideoDetail>();
			
			for(VideoListItem item : list)
			{
				detailList.add(item.toVideoDetail());
			}
			
			return detailList;
		}
		
		return null;
	}
}
