package runtracker.android.bignerdranch.com.runtracker;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by sheng on 16/10/24.
 */
public abstract class DataLoader<D> extends AsyncTaskLoader<D> {
    private D mData;
    public DataLoader(Context context){
        super(context);
    }

    protected void onStartLoading(){
        if(mData!=null){
            deliverResult(mData);//立即发送
        }else{
            forceLoad();//加载数据
        }
    }

    public void deliverResult(D data){
        mData=data;
        if(isStarted()){
            super.deliverResult(data);
        }
    }
}
