package com.sharegogo.video.data;

/**
 * ϵͳ����Ӧ��
 * @author Raymon
 *
 */
public class NotifyListResponse extends BaseResponse{
	public int count;
	public NotifyListItem[] list;
	
	static public class NotifyListItem
	{
		public long notifyId;
		public String notifyTitle;
		public String notifyContent;
		public long lastUpdateTime;
	}
}
