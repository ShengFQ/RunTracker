package runtracker.android.bignerdranch.com.runtracker;

import android.app.Fragment;
import android.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RunListActivity extends SingleFragmentActivity {


    @Override
    public ListFragment createFragment() {
        return new RunListFragment();
    }
}
