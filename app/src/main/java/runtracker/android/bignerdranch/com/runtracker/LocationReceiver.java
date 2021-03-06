package runtracker.android.bignerdranch.com.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by sheng on 16/10/12.
 */
public class LocationReceiver extends BroadcastReceiver {
    private static final String TAG="LocationReceiver";

    public void onReceive(Context context ,Intent intent){
        Log.i(TAG,"BroadcastReceiver.onReceive()");
        //if you got a location extra,use it
        Location loc=(Location)intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if(loc!=null){
            onLocationReceived(context,loc);
            return;
        }
        // if you get here,something else has happend
        if(intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)){
            boolean enabled=intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED,false);
            onProviderEnabledChanged(enabled);
        }
    }

    protected  void onLocationReceived(Context context,Location loc){
        Log.d(TAG,"got location from "+ loc.getProvider()+":"+loc.getLatitude()+","+loc.getLongitude());
    }

    protected  void onProviderEnabledChanged(boolean enabled){
        Log.d(TAG,"Provider "+(enabled?"enabled":"disabled"));
    }
}
