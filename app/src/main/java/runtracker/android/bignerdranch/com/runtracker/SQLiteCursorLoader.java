package runtracker.android.bignerdranch.com.runtracker;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by sheng on 16/10/24.
 * TODO AsyncTaskLoader.java ??
 */
public abstract class SQLiteCursorLoader extends AsyncTaskLoader<Cursor>{
    private static final String TAG="SQLiteCursorLoader";
   private Cursor mCursor;

    public SQLiteCursorLoader(Context context){
        super(context);
    }

    protected abstract Cursor loadCursor();

    /**
     * 高效加载cursor
     *TODO 写错误了 loadInBackgroud()
     * 是override loadInBackground() 这是后台默认调用的,没有数据就是因为该方法没有实例化cursor
     * */
    @Override
    public Cursor loadInBackground(){
        Log.i(TAG,"AsyncTaskLoader.loadInBackground()");
        Cursor cursor=loadCursor();
        if(cursor!=null){
            cursor.getCount();//这种代码检测cursor一定不为空
        }
        return cursor;
    }

    /**
     * 释放资源,如果是现有的cursor,则调用父类的方法释放
     * 如果是旧的cursor,则要关闭先
     * */
    @Override
    public void deliverResult(Cursor data){
        Log.i(TAG,"AsyncTaskLoader.deliverResult()");
        Cursor oldCursor=mCursor;
        mCursor=data;
        if(isStarted()){
            super.deliverResult(data);
        }
        if(oldCursor!=null && oldCursor!=data && !oldCursor.isClosed()){
            oldCursor.close();
        }
    }
    @Override
    protected void onStartLoading(){
        Log.i(TAG,"AsyncTaskLoader.onStartLoading()");
        if(mCursor!=null){
            deliverResult(mCursor);
        }
        if(takeContentChanged()||mCursor==null){
            forceLoad();
        }
    }
    @Override
    protected void onStopLoading(){
        Log.i(TAG,"AsyncTaskLoader.onStopLoading()");
      //attempt to cancel the current load task if possible.
        cancelLoad();
    }
    @Override
    public void onCanceled(Cursor cursor){
        Log.i(TAG,"AsyncTaskLoader.onCanceled()");
        if(cursor!=null && !cursor.isClosed()){
            cursor.close();
        }
    }
    @Override
    protected void onReset(){
        Log.i(TAG,"AsyncTaskLoader.onReset()");
        super.onReset();
        //ensure the loader is stopped
        onStopLoading();
        if(mCursor!=null && !mCursor.isClosed()){
            mCursor.close();
        }
        mCursor=null;
    }

}
