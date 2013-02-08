package  com.sharegogo.video.http.impl.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sharegogo.video.http.impl.utils.HttpConfig;

/**
 * Http任务调度器
 * 
 * @author weizhengqin
 * @date: 2012-09-25
 */
public class HttpScheduler{
	
	//线程池
	private List<HttpThread> mThreadList = new ArrayList<HttpThread>();

	
	/**
	 * 禁止外部构造
	 */
	public HttpScheduler(){
		init();
	}
	
	
	
	/**
	 * (api)根据req取消请求
	 * @param req
	 */
	public void cancel(HttpRequest req){
		cancel(req.getId());
	}
	
	/**
	 * 根据tag cancel 请求
	 * @param tag
	 */
	public void cancel(String tag){
		if(tag.length() <=0) return;
		//从正在运行的队列中停止
		synchronized(mThreadList){
			int size = mThreadList.size();
			for(int i=0; i<size; i++){
				HttpThread thread = mThreadList.get(i);
				HttpExecutor executor = thread.getexecutor();
				if(executor != null){
					if(tag.equalsIgnoreCase(executor.getCurrRequest().getTag())){
						executor.cancel();
					}
				}
			}	
		}	
	}
	
	/**
	 * (api)根据id取消请求
	 * @param id
	 */
	public void cancel(long id){
		if(id == -1) return;
		
		//从正在运行的队列中停止
		synchronized(mThreadList){
			int size = mThreadList.size();
			for(int i=0; i<size; i++){
				HttpThread thread = mThreadList.get(i);
				HttpExecutor executor = thread.getexecutor();
				if(executor != null){
					HttpRequest request =executor.getCurrRequest();
					if(request != null && id == request.getId()){
						executor.cancel();
						break;
					}
				}
			}	
		}	
	}
	
	
	/**
	 * (api)结束调度器，释放资源
	 */
	public void  release(){
		//停掉线程 
		synchronized(mThreadList){
			int size = mThreadList.size();
			for(int i =0; i<size; i++){
				HttpThread thread = mThreadList.get(i);
				HttpExecutor executor = thread.getexecutor();
				if(executor != null)
				{
					executor.cancel();
				}
				thread.release();
			}
		}
	}
	


	///////////////////////////////////////////////////////////////////////////////////
	// 内部函数
	
	
	/**
	 * 初始化调度器
	 */
	private void init(){
		
		for(int i = 0; i <HttpConfig.MAX_THREAD_COUNT; i++){
			HttpThread thread = new HttpThread();
			mThreadList.add(thread);
			thread.start();
		}	
	}
	
	
	/**
	 * 如果有请求，调度一次
	 * ugly function 
	 * @throws Exception
	 */
	public void scheduler(){
		synchronized(mThreadList){
			int size = mThreadList.size();
			for(int i = 0; i<size; i++){
				HttpThread task = mThreadList.get(i);
				//通知空闲线程有新请求到来
				if(!task.isRunning()){
					synchronized(task){
						task.notify();
						break;  //主动调度只处理一个请求
					}
				}
			}
		}
	}
	
}