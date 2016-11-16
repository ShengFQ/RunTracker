package runtracker.android.bignerdranch.com.runtracker;

import android.content.Context;
import android.location.Location;

/**
 * Created by sheng on 16/10/28.
 */
public class LastLocationLoader extends DataLoader<Location> {
    private long mRunId;
    public LastLocationLoader(Context context,long runId){
        super(context);
        mRunId=runId;
    }
    @Override
    public Location loadInBackground() {
        return Runmanger.getInstance(getContext()).getLocationForRun(mRunId);
    }
}
