package runtracker.android.bignerdranch.com.runtracker;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import java.util.Date;

/**
 * Created by sheng on 16/10/14.
 * Cursor游标类,实现传入一个游标对象能获得一个实体对象
 * 注意:取完数据后要关闭游标.
 */
public  class RunCursor extends CursorWrapper {
    private static final String TAG="RunCursor";

    private Cursor mCursor;
    public RunCursor(Cursor cursor){
        super(cursor);
        Log.i(TAG,"new RunCursor()");
    }

    /**
     *
     * 调用者可遍历结果集里的行数,并对每一行调用getRun()方法.
     * */
    public Run getRun(){
        Log.i(TAG,"getRun()");
        if(isBeforeFirst() || isAfterLast()){
            return null;
        }
        Run run=new Run();
        long runId=getLong(getColumnIndex(RunDatabaseHelper.COLUMN_RUN_ID));
        run.setmId(runId);
        long startDate=getLong(getColumnIndex(RunDatabaseHelper.COLUMN_RUN_START_DATE));
        run.setmStartDate(new Date(startDate));
        //mCursor.close(); nullpointerException
        return run;
    }
}
