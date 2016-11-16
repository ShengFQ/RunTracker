package runtracker.android.bignerdranch.com.runtracker;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Loader;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sheng on 16/10/12.
 */
public class RunFragment extends Fragment {
    private static final String TAG="RunFragment";
    private static final String ARG_RUN_ID="RUN_ID";
    private static final int LOAD_RUN=0;//TODO 标示Loader的id
    private static final int LOAD_LOCATION=1;//TODO 标志loader的id
    private Button mStartButton,mStopButton;
    private TextView mStartText,mLatitudeText,mLongitudeText,mAltitudeText,mDurationText;
    private Runmanger mRunManager;
    private Location mLastLocation;//最后接收到的位置数据
    private Run mRun;
    //实例化接收器
    private BroadcastReceiver mLocationReceiver=new LocationReceiver(){
       //当接收到定位数据时,更新UI
        protected void onLocationReceived(Context context,Location loc){
            super.onLocationReceived(context,loc);
            if(!mRunManager.isTrackingRun()){
                return;
            }
            mLastLocation=loc;
            if(isVisible())
                updateUI();
        }

        //在GPS服务提供者启动或停止时,还会有消息显示.
        protected void onProviderEnabledChanged(boolean enabled){
            super.onProviderEnabledChanged(enabled);
            int toastText=enabled? R.string.gps_enabled:R.string.gps_disabled;
            Toast.makeText(getActivity(),toastText,Toast.LENGTH_LONG).show();
        }
    };

    /**
     * TODO
     * 给fragment传参实例化的最佳实践
     * */
    public static RunFragment newInstance(long runId){
        Bundle args=new Bundle();
        args.putLong(ARG_RUN_ID,runId);
        RunFragment rf=new RunFragment();
        rf.setArguments(args);
        return rf;
    }
    /**
     * oncreate方法的调用会有以下场景:
     * 1.启动一个新的定位服务UI
     * 2.从已经定位的历史列表中点击进入查看历史定位服务UI
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);//TODO ???
        /*
        method 1
        mRunManager=Runmanger.getInstance(getActivity());
        Bundle args=getArguments();
        if(args!=null){
            long runId=args.getLong(ARG_RUN_ID,-1);
            if(runId!=-1) {
                //从本地数据库加载旅程
                mRun = mRunManager.getRun(runId);
                //从本地数据库加载位置
                mLastLocation=mRunManager.getLocationForRun(runId);
            }
        }*/

        //method 2
        mRunManager=Runmanger.getInstance(getActivity());
        Bundle args=getArguments();
        if(args!=null) {
            long runId=args.getLong(ARG_RUN_ID,-1);
            if(runId!=-1) {
                getLoaderManager().initLoader(LOAD_RUN, args, new RunLoaderCallbacks());
                getLoaderManager().initLoader(LOAD_LOCATION,args,new LocationLoaderCallbacks());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main,container,false);
       mStartText=(TextView) v.findViewById(R.id.run_startedText);
        mAltitudeText=(TextView) v.findViewById(R.id.run_altitudeText);
        mLatitudeText=(TextView) v.findViewById(R.id.run_latitudeText);
        mLongitudeText=(TextView) v.findViewById(R.id.run_longitudeText);
        mDurationText=(TextView) v.findViewById(R.id.run_durationText);

        mStartButton=(Button) v.findViewById(R.id.run_startButton);
        mStopButton=(Button) v.findViewById(R.id.run_stopButton);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG,"start new Run");
                //method 1
               // mRunManager.startLocationUpdates();
                //run
              //  mRun = new Run();
                //method 2
               // mRun=mRunManager.startNewRun();
                //method 3
                if(mRun==null){
                    mRun=mRunManager.startNewRun();
                }else{
                    mRunManager.startTrackingRun(mRun);
                }
                updateUI();
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //  mRunManager.stopLocationUpdates();
                mRunManager.stopRun();
                updateUI();
            }
        });

        updateUI();
        return v;

    }

    public void onStart(){
        super.onStart();
        //注册接收器
        getActivity().registerReceiver(mLocationReceiver, new IntentFilter(Runmanger.ACTION_LOCATION));
    }

    public void onStop(){
        //解除接收器
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }

    /**
     * good job
     *
     * */
    private void updateUI(){
        //done well
        boolean started=mRunManager.isTrackingRun();

        //good
        boolean trackingThisRun=mRunManager.isTrackingRun();
        if(mRun!=null){
            mStartText.setText(mRun.getmStartDate().toString());

        }

        int durationSeconds=0;
        if(mRun!=null && mLastLocation!=null){
            durationSeconds= mRun.getDurationSeconds(mLastLocation.getTime());
            mLatitudeText.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeText.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeText.setText(Double.toString(mLastLocation.getAltitude()));
        }
        mDurationText.setText(Run.formatDuration(durationSeconds));
        mStartButton.setEnabled(!started);
       // mStopButton.setEnabled(started);
        mStopButton.setEnabled(started && trackingThisRun);

    }

    private class RunLoaderCallbacks implements LoaderManager.LoaderCallbacks<Run>{

        @Override
        public Loader<Run> onCreateLoader(int id, Bundle args) {
           return new Runloader(getActivity(),args.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(Loader<Run> loader, Run data) {
            mRun=data;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Run> loader) {
            //TODO run实例完全存放在内存中,并不需要onLoaderReset()
        }
    }

    private class LocationLoaderCallbacks implements LoaderManager.LoaderCallbacks<Location>{

        @Override
        public Loader<Location> onCreateLoader(int id, Bundle args) {
            return new LastLocationLoader(getActivity(),args.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(Loader<Location> loader, Location data) {
                mLastLocation=data;
                updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {
            //do nothing
        }
    }
}
