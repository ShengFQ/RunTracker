package runtracker.android.bignerdranch.com.runtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

/**
 * Created by sheng on 16/10/14.
    Run数据持久化帮助类.
 数据库经常要升级,但是数据不能被清空,所以要预留更新能力,更新的方法就是调用onUpgrade
 */
public class RunDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="runs.sqlite";
    private static final int VERSION=1;
    private static final String TABLE_RUN="run";
    public static final String COLUMN_RUN_START_DATE="start_date";
    public static final String TABLE_LOCATION="location";
    public static final String COLUMN_LOCATION_LATITUDE="latitude";
    public static final String COLUMN_LOCATION_LONGITUDE="longitude";
    public static final String COLUMN_LOCATION_ALTITUDE="altitude";
    public static final String COLUMN_LOCATION_TIMESTAMP="timestamp";
    public static final String COLUMN_LOCATION_PROVIDER="provider";
    public static final String COLUMN_LOCATION_RUN_ID="run_id";

    public static final String COLUMN_RUN_ID="_id";

    private Context mContext;
    public RunDatabaseHelper(Context context){
        //this.mContext=context;
        super(context,DB_NAME,null,VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table run ("+
        "_id integer primary key autoincrement , start_date integer)");
        //create location table
        db.execSQL("create table location(" +
                " timestamp integer,latitude real,longitude real,altitude real," +
                " provider varchar(100), run_id integer references run(_id))");
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //implement schema changes and data message here when upgrading
    }

    //CRUD
    public long insertRun(Run run){
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_RUN_START_DATE,run.getmStartDate().getTime());
        return getWritableDatabase().insert(TABLE_RUN,null,cv);
    }

    public long insertLocation(long runId,Location location){
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_LOCATION_LATITUDE,location.getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE,location.getLongitude());
        cv.put(COLUMN_LOCATION_ALTITUDE,location.getAltitude());
        cv.put(COLUMN_LOCATION_TIMESTAMP,location.getTime());
        cv.put(COLUMN_LOCATION_PROVIDER,location.getProvider());
        cv.put(COLUMN_LOCATION_RUN_ID,runId);
        return getWritableDatabase().insert(TABLE_LOCATION,null,cv);
    }

    public RunCursor queryRuns(){
        Cursor wrapped=getReadableDatabase().query(TABLE_RUN,null,null,null,null,null,COLUMN_RUN_START_DATE+" asc");
        return new RunCursor(wrapped);
    }

    public RunCursor queryRun(long id){
        Cursor wrapped=getReadableDatabase().query(TABLE_RUN,null,
                COLUMN_RUN_ID+" =?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                "1");
        return new RunCursor(wrapped);
    }

    public LocationCursor queryLastLocationForRun(long runId){
        Cursor wrapped=getReadableDatabase().query(TABLE_LOCATION,
                null,//all columns
                COLUMN_LOCATION_RUN_ID+" =?",//limit to the given run
                new String[]{String.valueOf(runId)},
                null,//group by
                null,//having
                COLUMN_LOCATION_TIMESTAMP+" desc",//order by lastest time
                "1" );
        return new LocationCursor(wrapped);
    }
}
