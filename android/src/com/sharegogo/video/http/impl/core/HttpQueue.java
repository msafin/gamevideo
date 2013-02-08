package  com.sharegogo.video.http.impl.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.params.HttpParams;

import android.content.Context;


/**
 * Http请求队列
 * 
 * @author weizhengqin
 * @date: 2012-09-25
 */
public class HttpQueue{
	
	//等待执行的请求队列
	public List<HttpRequest> mRequestList = new LinkedList<HttpRequest>();
	
	//当前最大的请求id
	public static  long id = 0;
	
	
	//实例
	private static HttpQueue queue;
	
	private HttpQueue(){
		
	}
	
	
	/**
	 * 获得实例
	 * @return
	 */
	public static HttpQueue getInstance(){
		if(queue == null){
			synchronized(HttpQueue.class){
				if(queue == null){
					queue = new HttpQueue();
				}
			}
		}
		return queue;
	}
	
	
	
	public long push_back(HttpRequest req){
		return insert(req,-1);  //-1:追加到队尾
	}
	
	public long push_front(HttpRequest req){
		return insert(req,0);
	}
	
	public void remove(HttpRequest req){
		remove(req.getId());
	}
	
	
	public void remove(long id){

		//从等待队列中停止
		synchronized(mRequestList){
			int size =  mRequestList.size();
			for(int i = 0; i < size; i++){
				HttpRequest req = mRequestList.get(i);
				if(req != null && id == req.getId()){
					mRequestList.remove(i);
					break;
				}	
			}
		}
	}
	
	
	/**
	 * 根据tag删除请求队列
	 * @param tag
	 */
	public void remove(String tag){
		synchronized(mRequestList){
			int size =  mRequestList.size();
			for(int i = size-1; i > 0; i++){
				HttpRequest req = mRequestList.get(i);
				if(tag.equalsIgnoreCase(req.getTag())){
					mRequestList.remove(i);
				}	
			}
		}
	}
	
	/**
	 * 清空队列
	 */
	public void clear(){
		synchronized(mRequestList){
			mRequestList.clear();
		}
	}
	
	
	public HttpRequest pop_front(){
		synchronized(mRequestList){
			if(mRequestList.size()>0){
				return mRequestList.remove(0);
			}
		}
		return null;
		
	}


	
	private long insert(HttpRequest req, int pos){
		if(req == null || req.getUrl() == null) return -1;
		
		synchronized(mRequestList){
			long id = generate();
			req.setId(id);
			if(pos == 0){
				mRequestList.add(0, req);
			}else{
				mRequestList.add(req);
			}
		}
		return id;
	}
	
	/**
	 * 生成一个唯一id 
	 * @return
	 */
	private long generate(){
		id++;
		return id;
	}
	
}