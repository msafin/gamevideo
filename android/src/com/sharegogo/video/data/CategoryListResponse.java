package com.sharegogo.video.data;

/**
 * �����б�Ӧ��
 * @author Raymon
 *
 */
public class CategoryListResponse extends BaseResponse{
	public CategoryListItem[] list;
	
	static public class CategoryListItem
	{
		public long id;
		public String name;
		public String img;
		public String desc;
	}
}
