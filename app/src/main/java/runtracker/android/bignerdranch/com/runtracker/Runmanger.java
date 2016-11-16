package runtracker.android.bignerdranch.com.runtracker;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by sheng on 16/10/12.
 * 管理与LocationManager的通讯
 * chapter 34 与数据库
 */
public class Runmanger {
    private static final String TAG = "Runmanager";
    public static final String ACTION_LOCATION = "com.bignerdranch.android.runtracker.ACTION_LOCATION";//TODO 看怎样定义一个ACTION的
    private static Runmanger sRunManager;//单例模式
    private Context mAppContext;//依赖实例
    private LocationManager mLocationManager;
    //CHAPTER 33 ,使用虚拟定位服务提供者
    private static final String TEST_PROVIDER="TEST_PROVIDER";

    private Runmanger(Context appContext) {
        mAppContext = appContext;
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);

        //实例化数据库操作
        mHelper=new RunDatabaseHelper(mAppContext);
        mPrefs=mAppContext.getSharedPreferences(PREFS_FILE,Context.MODE_PRIVATE);
        mCurrentRunId=mPrefs.getLong(PREF_CURRENT_RUN_ID,-1);

    }

    public static Runmanger getInstance(Context c) {
        if (sRunManager == null) {
            sRunManager = new Runmanger(c.getApplicationContext());
        }
        return sRunManager;
    }

    /**
     * TODO core code PendingIntent??
     *
     * 使用PendingIntent,位置发送变化时,会创建用于广播的intent.*/
    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;//是否应该在系统内创建新的pendingintent
        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
    }

    /**
     * TODO
     * 启动位置更新,方式:发送broadcastIntent的方式获取地理位置数据更新
     * */
    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;//系统自身的GPS提供者
        //chapter33 使用虚拟定位服务提供者
        //if you have the test provider and it's enabled,use it
        if(mLocationManager.getProvider(TEST_PROVIDER)!=null && mLocationManager.isProviderEnabled(TEST_PROVIDER)){
            provider=TEST_PROVIDER;
        }
        Log.d(TAG,"Using provider "+provider);


        Location lastKnown=mLocationManager.getLastKnownLocation(provider);
        if(lastKnown!=null){
            lastKnown.setTime(System.currentTimeMillis());
            broadcastLocation(lastKnown);
        }

        //start updates from the location manager
        PendingIntent pi = getLocationPendingIntent(true);
        if (ActivityCompat.checkSelfPermission(mAppContext.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mAppContext.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        //最小等待时间,最短移动距离可用于决定发送下一次定位数据更新要移动的距离和要等待的时间
        mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
    }

    /**
     * 获取最后一次GPS定位信息
     * */
    private void broadcastLocation(Location location){
        Log.i(TAG,"broadcastLocation"+location.toString());
        Intent broadcast=new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED,location);
        mAppContext.sendBroadcast(broadcast);
    }

    //关闭位置更新
    public void stopLocationUpdates(){
        PendingIntent pi=getLocationPendingIntent(false);
        if(pi!=null){
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    //位置跟踪是否正在进行
    public boolean isTrackingRun(){
        return getLocationPendingIntent(false)!=null;
    }

    /*************
     * 实现run数据的持久化
     *
     *
     *
     * **********/
    private static final String PREFS_FILE="runs";
    private static final String PREF_CURRENT_RUN_ID="RunManager.currentRunId";
    private RunDatabaseHelper mHelper;
    private SharedPreferences mPrefs;
    private long mCurrentRunId;

    public Run startNewRun(){
        //insert a run into the db
        Run run=insertRun();
        startTrackingRun(run);
        return run;
    }

    public void startTrackingRun(Run run){
        mCurrentRunId=run.getmId();
        //store it in shared preferences
        mPrefs.edit().putLong(PREF_CURRENT_RUN_ID,mCurrentRunId).commit();
        //start location updates
        startLocationUpdates();
    }

    public void stopRun(){
        stopLocationUpdates();
        mCurrentRunId=-1;
        mPrefs.edit().remove(PREF_CURRENT_RUN_ID).commit();
    }

    public Run insertRun(){
        Run run=new Run();
        run.setmId(mHelper.insertRun(run));
        return run;
    }

    public void insertLocation(Location loc){
        if(mCurrentRunId!=-1){
            mHelper.insertLocation(mCurrentRunId,loc);
        }else{
            Log.e(TAG,"location received with no tracking run;");
        }
    }

    /**
     * 将记录查询
     * */
    public RunCursor queryRuns(){
        return mHelper.queryRuns();
    }

    /**
     * 从cursor中取得run对象
     * */
    public Run getRun(long id){
        Run run=null;
       RunCursor cursor= mHelper.queryRun(id);
        cursor.moveToFirst();//移动到结果集的第一行
        //如果有记录,isafterlast()会返回false.
        if(!cursor.isAfterLast()){
            run=cursor.getRun();
        }
        cursor.close();
        return run;
    }

    public Location getLocationForRun(long runId){
        Location location=null;
        LocationCursor cursor=mHelper.queryLastLocationForRun(runId);
        cursor.moveToFirst();
        if(!cursor.isFirst()){
          location=  cursor.getLocation();
        }
        cursor.close();
        return location;
    }

}
