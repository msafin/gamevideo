package com.sharegogo.video.http;

import java.net.URL;
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
    // The image's URL
    private URL mImageURL;
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
    private static HttpManager sPhotoManager;

    /**
     * Creates an HttpTask containing a download object and a decoder object.
     */
    HttpTask() {
        // Create the runnables
        mHttpRunnable = new HttpRunnable(this);
        sPhotoManager = HttpManager.getInstance();
    }
    
    /**
     * Initializes the Task
     *
     * @param photoManager A ThreadPool object
     * @param photoView An ImageView instance that shows the downloaded image
     * @param cacheFlag Whether caching is enabled
     */
    void initializeDownloaderTask(HttpManager photoManager)
    {
        // Sets this object's ThreadPool field to be the input argument
        sPhotoManager = photoManager;
    }
    
    // Implements HTTPDownloaderRunnable.getByteBuffer
    @Override
    public byte[] getByteBuffer() {
        
        // Returns the global field
        return null;
    }
    
    /**
     * Recycles an HttpTask object before it's put back into the pool. One reason to do
     * this is to avoid memory leaks.
     */
    void recycle() {
        
    }

    // Implements HttpRunnable.getImageURL. Returns the global Image URL.
    @Override
    public URL getImageURL() {
        return mImageURL;
    }

    // Implements HttpRunnable.setByteBuffer. Sets the image buffer to a buffer object.
    @Override
    public void setByteBuffer(byte[] imageBuffer) {
    	
    }
    
    // Delegates handling the current state of the task to the HttpManager object
    void handleState(int state) {
        sPhotoManager.handleState(this, state);
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
        synchronized(sPhotoManager) {
            return mCurrentThread;
        }
    }

    /*
     * Sets the identifier for the current Thread. This must be a synchronized operation; see the
     * notes for getCurrentThread()
     */
    public void setCurrentThread(Thread thread) {
        synchronized(sPhotoManager) {
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
