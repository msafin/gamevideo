package com.sharegogo.video.http;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;


import com.sharegogo.config.BuildingConfig;
import com.sharegogo.config.HttpConstants;
import com.sharegogo.video.utils.DeviceInfo;
import com.sharegogo.video.utils.HttpUtils;
import com.sharegogo.video.utils.LogUtils;

/**
 * This class creates pools of background threads for downloading
 * Picasa images from the web, based on URLs retrieved from Picasa's featured images RSS feed.
 * The class is implemented as a singleton; the only way to get an HttpManager instance is to
 * call {@link #getInstance}.
 * <p>
 * The class sets the pool size and cache size based on the particular operation it's performing.
 * The algorithm doesn't apply to all situations, so if you re-use the code to implement a pool
 * of threads for your own app, you will have to come up with your choices for pool size, cache
 * size, and so forth. In many cases, you'll have to set some numbers arbitrarily and then
 * measure the impact on performance.
 * <p>
 * This class actually uses two threadpools in order to limit the number of
 * simultaneous image decoding threads to the number of available processor
 * cores.
 * <p>
 * Finally, this class defines a handler that communicates back to the UI
 * thread to change the bitmap to reflect the state.
 */
@SuppressWarnings("unused")
public class HttpManager {
    /*
     * Status indicators
     */
    static final int DOWNLOAD_FAILED = -1;
    static final int DOWNLOAD_STARTED = 1;
    static final int DOWNLOAD_COMPLETE = 2;
    static final int DECODE_STARTED = 3;
    static final int TASK_COMPLETE = 4;

    
    // Sets the amount of time an idle thread will wait for a task before terminating
    private static final int KEEP_ALIVE_TIME = 1;

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

    // Sets the initial threadpool size to 8
    private static final int CORE_POOL_SIZE = 8;

    // Sets the maximum threadpool size to 8
    private static final int MAXIMUM_POOL_SIZE = 8;

    // A queue of Runnables for the image download pool
    private final BlockingQueue<Runnable> mHttpWorkQueue;

    // A queue of HttpManager tasks. Tasks are handed to a ThreadPool.
    private final Queue<HttpTask> mHttpTaskWorkQueue;

    // A managed pool of background download threads
    private final ThreadPoolExecutor mHttpThreadPool;

    // An object that manages Messages in a Thread
    private Handler mHandler;

    // A single instance of HttpManager, used to implement the singleton pattern
    private static HttpManager sInstance = null;

    private List<NameValuePair> mHeaders = null;
    
    // A static block that sets class fields
    static {
        
        // The time unit for "keep alive" is in seconds
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        
        // Creates a single static instance of HttpManager
        sInstance = new HttpManager();
    }
    
    static public void test()
    {
    	HttpRequest request = new BasicHttpRequest(HttpPost.METHOD_NAME, HttpConstants.URL_AUTO_REGISTER);
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	String imsi = DeviceInfo.getDeviceImsi();
    	//需要指定NO_WRAP否者会以换行结束
    	String regid = Base64.encodeToString(imsi.getBytes(), Base64.NO_WRAP);
    	LogUtils.e("http", "regid=" + regid);
    	NameValuePair regidPair = new BasicNameValuePair("regid",regid);
    	
    	String imei = DeviceInfo.getDeviceImei();
    	String regimei = Base64.encodeToString(imei.getBytes(), Base64.NO_WRAP);
    	LogUtils.e("http", "regimei=" + regimei);
    	NameValuePair imeiPair = new BasicNameValuePair("regimei",regimei);
    	
    	params.add(regidPair);
    	params.add(imeiPair);
    	
    	doRequest(request,params);
    }
    /**
     * Constructs the work queues and thread pools used to download and decode images.
     */
    private HttpManager() {

        /*
         * Creates a work queue for the pool of Thread objects used for downloading, using a linked
         * list queue that blocks when the queue is empty.
         */
        mHttpWorkQueue = new LinkedBlockingQueue<Runnable>();

        /*
         * Creates a work queue for the set of of task objects that control downloading and
         * decoding, using a linked list queue that blocks when the queue is empty.
         */
        mHttpTaskWorkQueue = new LinkedBlockingQueue<HttpTask>();

        /*
         * Creates a new pool of Thread objects for the download work queue
         */
        mHttpThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mHttpWorkQueue);

        /*
         * Instantiates a new anonymous Handler object and defines its
         * handleMessage() method. The Handler *must* run on the UI thread, because it moves photo
         * Bitmaps from the PhotoTask object to the View object.
         * To force the Handler to run on the UI thread, it's defined as part of the HttpManager
         * constructor. The constructor is invoked when the class is first referenced, and that
         * happens when the View invokes startDownload. Since the View runs on the UI Thread, so
         * does the constructor and the Handler.
         */
        mHandler = new Handler(Looper.getMainLooper()) {

            /*
             * handleMessage() defines the operations to perform when the
             * Handler receives a new Message to process.
             */
            @Override
            public void handleMessage(Message inputMessage) {
            }
        };
        
        mHeaders = getHeaders();
    }

    private List<NameValuePair> getHeaders()
    {
    	List<NameValuePair> headers = new ArrayList<NameValuePair>();
    	
    	NameValuePair acceptEncoding = new BasicNameValuePair("Accept-Encoding","gzip,deflate,sdch");
    	NameValuePair clientVersion = new BasicNameValuePair("ClientVersion",BuildingConfig.client_version);
    	NameValuePair userAgent = new BasicNameValuePair("User-Agent",HttpUtils.getUserAgent());
    	
    	headers.add(acceptEncoding);
    	headers.add(clientVersion);
    	headers.add(userAgent);
    	
    	return headers;
    }
    /**
     * Returns the HttpManager object
     * @return The global HttpManager object
     */
    public static HttpManager getInstance() {

        return sInstance;
    }
    
    /**
     * Handles state messages for a particular task object
     * @param photoTask A task object
     * @param state The state of the task
     */
    @SuppressLint("HandlerLeak")
    public void handleState(HttpTask httpTask, int state) {
        switch (state) {
            
            // The task finished downloading and decoding the image
            case TASK_COMPLETE:
                // Gets a Message object, stores the state in it, and sends it to the Handler
                Message completeMessage = mHandler.obtainMessage(state, httpTask);
                completeMessage.sendToTarget();
                break;
            
            // The task finished downloading the image
            case DOWNLOAD_COMPLETE:
            
            // In all other cases, pass along the message without any other action.
            default:
                mHandler.obtainMessage(state, httpTask).sendToTarget();
                break;
        }

    }

    /**
     * Cancels all Threads in the ThreadPool
     */
    public static void cancelAll() {

        /*
         * Creates an array of tasks that's the same size as the task work queue
         */
        HttpTask[] taskArray = new HttpTask[sInstance.mHttpWorkQueue.size()];

        // Populates the array with the task objects in the queue
        sInstance.mHttpWorkQueue.toArray(taskArray);

        // Stores the array length in order to iterate over the array
        int taskArraylen = taskArray.length;

        /*
         * Locks on the singleton to ensure that other processes aren't mutating Threads, then
         * iterates over the array of tasks and interrupts the task's current Thread.
         */
        synchronized (sInstance) {
            
            // Iterates over the array of tasks
            for (int taskArrayIndex = 0; taskArrayIndex < taskArraylen; taskArrayIndex++) {
                
                // Gets the task's current thread
                Thread thread = taskArray[taskArrayIndex].mThreadThis;
                
                // if the Thread exists, post an interrupt to it
                if (null != thread) {
                    thread.interrupt();
                }
            }
        }
    }

    /**
     * Stops a download Thread and removes it from the threadpool
     *
     * @param downloaderTask The download task associated with the Thread
     * @param pictureURL The URL being downloaded
     */
    static public void removeDownload(HttpTask httpTask, HttpRequest request) {

        // If the Thread object still exists and the download matches the specified URL
        if (httpTask != null && httpTask.getHttpRequest() == request) {

            /*
             * Locks on this class to ensure that other processes aren't mutating Threads.
             */
            synchronized (sInstance) {
                
                // Gets the Thread that the downloader task is running on
                Thread thread = httpTask.getCurrentThread();

                // If the Thread exists, posts an interrupt to it
                if (null != thread)
                    thread.interrupt();
            }
            /*
             * Removes the download Runnable from the ThreadPool. This opens a Thread in the
             * ThreadPool's work queue, allowing a task in the queue to start.
             */
            sInstance.mHttpThreadPool.remove(httpTask.getHttpRunnable());
        }
    }

    /**
     * Starts an image download and decode
     *
     * @param imageView The ImageView that will get the resulting Bitmap
     * @param cacheFlag Determines if caching should be used
     * @return The task instance that will handle the work
     */
    static public HttpTask doRequest(HttpRequest request,List<NameValuePair> params) {
        /*
         * Gets a task from the pool of tasks, returning null if the pool is empty
         */
        HttpTask httpTask = sInstance.mHttpTaskWorkQueue.poll();

        // If the queue was empty, create a new task instead.
        if (null == httpTask) {
        	httpTask = new HttpTask();
        }

        // Initializes the task
        httpTask.initializeHttpTask(HttpManager.sInstance,request,sInstance.mHeaders,params);
        
        /*
         * "Executes" the tasks' download Runnable in order to download the image. If no
         * Threads are available in the thread pool, the Runnable waits in the queue.
         */
        sInstance.mHttpThreadPool.execute(httpTask.getHttpRunnable());
        
        return  httpTask;
    }

    /**
     * Recycles tasks by calling their internal recycle() method and then putting them back into
     * the task queue.
     * @param downloadTask The task to recycle
     */
    void recycleTask(HttpTask httpTask) {
        
        // Frees up memory in the task
    	httpTask.recycle();
        
        // Puts the task object back into the queue for re-use.
        mHttpTaskWorkQueue.offer(httpTask);
    }
}
