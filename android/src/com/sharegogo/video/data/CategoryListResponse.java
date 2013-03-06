package com.sharegogo.video.data;

/**
 * 分类列表应答
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
