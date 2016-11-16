package runtracker.android.bignerdranch.com.runtracker;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by sheng on 16/10/24.
 */
public class RunListCursorLoader extends SQLiteCursorLoader {
    private final Context context;

    public RunListCursorLoader(Context context){
        super(context);
        this.context = context;
    }

    protected Cursor loadCursor(){
        return Runmanger.getInstance(getContext()).queryRuns();
    }
}
