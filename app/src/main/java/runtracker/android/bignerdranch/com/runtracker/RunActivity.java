package runtracker.android.bignerdranch.com.runtracker;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RunActivity extends SingleFragmentActivity {

    public static final String EXTRA_RUN_ID="com.bingnerdranch.android.runtracker.run_id";
    @Override
    public Fragment createFragment() {
        long runId=getIntent().getLongExtra(EXTRA_RUN_ID,-1);
        if(runId!=-1){
            return RunFragment.newInstance(runId);
        }else{
            return new RunFragment();
        }
    }
}
