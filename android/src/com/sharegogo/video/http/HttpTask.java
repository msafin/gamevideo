package com.sharegogo.video.http;

import java.net.URL;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;

import com.sharegogo.video.http.HttpRunnable.TaskRunnableHttpMethods;

/**
 * This class manages HttpRunnable and HttpRunnable objects.  It does't perform
 * the download or decode; instead, it manages persistent storage for the tasks that do the work.
 * It does this by implementing the interfaces that the download and decode classes define, and
 * then passing itself as an argument to the constructor of a download or decode object. In effect,
 * this allows HttpTask to start on a Thread, run a download in a delegate object, then
 * run a decode, and then start over again. This class can be pooled and reused as necessary.
 */
public class HttpTask implements TaskRunnableHttpMethods{
    /*
     * request
     */
    private HttpRequest mHttpRequest;
    
    /*
     * headers
     */
    private List<NameValuePair> mHeaders;
    
    /*
     *params 
     */
    private List<NameValuePair> mParams;
    
    /*
     * UI层的处理接口
     */
    ResponseHandler mResponseHandler;
    
    /*
     * 读取的数据
     */
    private byte[] mBuffer;
    
    /*
     * 需要解析的数据类
     */
    Class mCls;
    
    /*
     * 和mCls对应的数据
     */
    Object mData;
    /*
     * Field containing the Thread this task is running on.
     */
    Thread mThreadThis;

    /*
     * Fields containing references to the two runnable objects that handle downloading and
     * decoding of the image.
     */
    private Runnable mHttpRunnable;
    
    // The Thread on which this task is currently running.
    private Thread mCurrentThread;
    
    /*
     * An object that contains the ThreadPool singleton.
     */
    private static HttpManager sHttpManager;

    /**
     * Creates an HttpTask containing a download object and a decoder object.
     */
    HttpTask() {
        // Create the runnables
        mHttpRunnable = new HttpRunnable(this);
        sHttpManager = HttpManager.getInstance();
    }
    
    /**
     * Initializes the Task
     *
     * @param photoManager A ThreadPool object
     * @param photoView An ImageView instance that shows the downloaded image
     * @param cacheFlag Whether caching is enabled
     */
    void initializeHttpTask(HttpManager httpManager,HttpRequest httpRequest,List<NameValuePair> headers,List<NameValuePair> params,ResponseHandler handler,Class cls)
    {
        // Sets this object's ThreadPool field to be the input argument
    	sHttpManager = httpManager;
    	mHttpRequest = httpRequest;
    	mHeaders = headers;
    	mParams = params;
    	mResponseHandler = handler;
    	mCls = cls;
    	
    }
    
    
    /**
     * Recycles an HttpTask object before it's put back into the pool. One reason to do
     * this is to avoid memory leaks.
     */
    void recycle() {
        mHttpRequest = null;
        mHeaders = null;
        mParams = null;
        mResponseHandler = null;
        mBuffer = null;
        mCls = null;
    }

    @Override
	public HttpRequest getHttpRequest() {
		// TODO Auto-generated method stub
		return mHttpRequest;
	}
    
    @Override
	public List<NameValuePair> getHeaders() {
		// TODO Auto-generated method stub
		return mHeaders;
	}

	@Override
	public List<NameValuePair> getParams() {
		// TODO Auto-generated method stub
		return mParams;
	}
	
	public byte[] getByteBuffer()
	{
		return mBuffer;
	}
	
    // Implements HttpRunnable.setByteBuffer. Sets the image buffer to a buffer object.
    @Override
    public void setByteBuffer(byte[] buffer) {
    	mBuffer = buffer;
    }
    
    // Delegates handling the current state of the task to the HttpManager object
    void handleState(int state) {
    	sHttpManager.handleState(this, state);
    }

    // Returns the instance that downloaded the image
    Runnable getHttpRunnable() {
        return mHttpRunnable;
    }
    
    /*
     * Returns the Thread that this Task is running on. The method must first get a lock on a
     * static field, in this case the ThreadPool singleton. The lock is needed because the
     * Thread object reference is stored in the Thread object itself, and that object can be
     * changed by processes outside of this app.
     */
    public Thread getCurrentThread() {
        synchronized(sHttpManager) {
            return mCurrentThread;
        }
    }

    /*
     * Sets the identifier for the current Thread. This must be a synchronized operation; see the
     * notes for getCurrentThread()
     */
    public void setCurrentThread(Thread thread) {
        synchronized(sHttpManager) {
            mCurrentThread = thread;
        }
    }

    // Implements HttpRunnable.setHTTPDownloadThread(). Calls setCurrentThread().
    @Override
    public void setHttpThread(Thread currentThread) {
        setCurrentThread(currentThread);
    }

    /*
     * Implements HttpRunnable.handleHTTPState(). Passes the download state to the
     * ThreadPool object.
     * 这里运行在http线程
     */
    
    @Override
    public void handleHttpState(int state) {
        int outState;
        
        // Converts the download state to the overall state
        switch(state) {
            case HttpRunnable.HTTP_STATE_COMPLETED:
                outState = HttpManager.DOWNLOAD_COMPLETE;
                break;
            case HttpRunnable.HTTP_STATE_FAILED:
                outState = HttpManager.DOWNLOAD_FAILED;
                break;
            default:
                outState = HttpManager.DOWNLOAD_STARTED;
                break;
        }
        // Passes the state to the ThreadPool object.
        handleState(outState);
    }
}
