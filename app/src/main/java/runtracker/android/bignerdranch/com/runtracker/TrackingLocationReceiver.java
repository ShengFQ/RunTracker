package runtracker.android.bignerdranch.com.runtracker;

import android.content.Context;
import android.location.Location;
import android.util.Log;

/**
 * Created by sheng on 16/10/14.
 * 使用BroadcastReceiver ,可以保证location intent 能够得到及时的处理,不用担心担心应用
 * 的其余部分是否正在运行.
 */
public class TrackingLocationReceiver extends LocationReceiver {
    private static final String TAG="TrackingLocation";
    @Override
    protected void onLocationReceived(Context context, Location loc) {
        //super.onLocationReceived(context, loc);
        Runmanger.getInstance(context).insertLocation(loc);
        Log.d(TAG,"got location from "+ loc.getProvider()+":"+loc.getLatitude()+","+loc.getLongitude());
    }

}
