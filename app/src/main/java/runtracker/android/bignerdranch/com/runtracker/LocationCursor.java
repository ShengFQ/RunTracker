package runtracker.android.bignerdranch.com.runtracker;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.location.Location;
import android.util.Log;

/**
 * Created by sheng on 16/10/21.
 * 地理位置的游标类
 */
public class LocationCursor extends CursorWrapper{
    private static final String TAG="LocationCursor";
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public LocationCursor(Cursor cursor) {
        super(cursor);
        Log.i(TAG, "new LocationCursor()");
    }

    /**
     * 从cursor中获取一个Location对象
     * */
    public Location getLocation(){
        Log.i(TAG,"getLocation()");
        if(isBeforeFirst()|| isAfterLast()){
            return null;
        }
        String provider=getString(getColumnIndex(RunDatabaseHelper.COLUMN_LOCATION_PROVIDER));//provider gps or lastlocation
        Location loc=new Location(provider);
        loc.setLongitude(getDouble(getColumnIndex(RunDatabaseHelper.COLUMN_LOCATION_LONGITUDE)));
        loc.setLatitude(getDouble(getColumnIndex(RunDatabaseHelper.COLUMN_LOCATION_LATITUDE)));
        loc.setAltitude(getDouble(getColumnIndex(RunDatabaseHelper.COLUMN_LOCATION_ALTITUDE)));
        loc.setTime(getLong(getColumnIndex(RunDatabaseHelper.COLUMN_LOCATION_TIMESTAMP)));
        return loc;
    }
}
