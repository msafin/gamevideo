package com.sharegogo.video.data;

/**
 * ϵͳ����Ӧ��
 * @author Raymon
 *
 */
public class NotifyList extends BaseResponse{
	public int count;
	public NotifyListItem[] list;
	
	static public class NotifyListItem
	{
		public long notifyId;
		public String notifyTitleString;
		public String notifyContentString;
		public long lastUpdateTime;
	}
}
