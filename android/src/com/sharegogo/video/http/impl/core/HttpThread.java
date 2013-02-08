package  com.sharegogo.video.http.impl.core;

/**
 * http线程
 * @author weizhengqin
 *
 */
public class HttpThread extends Thread{
	
	//线程继续运行标记
	private boolean 	retain = true; 
	//正在执行请求标记 
	private boolean 	running = false;
	//当前正在执行的请求
	private HttpExecutor 	executor = null;
	
	/**
	 * (api)当前task
	 * @return
	 */
	public HttpExecutor getexecutor(){
		return this.executor;
	}
	
	
	public void run(){
		
		while(retain){
				
			HttpRequest req = getreq();
			if(req == null){
				//没有请求，等待通知
				thread_wait();
				//等待过程中，线程被通知退出
				if(!retain) break;
				req = getreq();
			}
			
			//请求服务器，处理请求
			running = true;
			try {
				executor = new HttpExecutor();
				executor.request(req);
				executor.release();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				executor = null;
			}
			
			thread_sleep(20);
			running = false;
		}
	}
	
	
	/**
	 * (api)结束当前线程
	 */
	public void release(){
		synchronized(this)
		{
			retain = false;
			this.notify();
		}
	}
	
	/**
	 * (api)是否有任务正在执行
	 * @return
	 */
	public boolean isRunning(){
		return this.running;
	}
	
	
	/////////////////////////////////////////////////////////////////////////
	//内部函数
	
	/**
	 * 从队列头取出一个请求， 没有返回null
	 * @return
	 */
	private HttpRequest getreq(){
		HttpRequest req = null;
		
		req = HttpQueue
				.getInstance()
				.pop_front();
		
		return req;
	}	
	
	
	
	/**
	 * 封装sleep
	 * @param time
	 */
	private void thread_sleep(long time){
		try {
			this.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 封装wait
	 */
	private void thread_wait(){
		synchronized(this){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}

