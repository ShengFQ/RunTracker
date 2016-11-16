package runtracker.android.bignerdranch.com.runtracker;

import android.content.Context;

/**
 * Created by sheng on 16/10/25.
 */
public class Runloader extends DataLoader<Run> {
    private long mRunid;
    public Runloader(Context context,long runId){
        super(context);
        mRunid=runId;
    }

    public Run loadInBackground(){
        return Runmanger.getInstance(getContext()).getRun(mRunid);
    }
}
