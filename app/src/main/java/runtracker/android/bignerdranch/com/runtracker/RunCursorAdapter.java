package runtracker.android.bignerdranch.com.runtracker;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by sheng on 16/10/17.
 */
public class RunCursorAdapter extends CursorAdapter {
    private static final String TAG="RunCursorAdapter";
    private RunCursor mRunCursor;
    public RunCursorAdapter(Context context, RunCursor cursor){
        super(context,cursor,0);
        mRunCursor=cursor;
    }
    /**
     *TODO ???
     *先调用
     *
     * */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.i(TAG,"newView()");
        //use a layout inflater to get a row view
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(android.R.layout.simple_list_item_1,parent,false);
    }
    /**
     *TODO ???
     *当需要配置视图显示cursor中的一行数据时
     * */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.i(TAG,"bindView()");
        //get the run for the current row
        Run run=mRunCursor.getRun();

        //set up the start date text view
        TextView startDateTextView=(TextView)view;
        String cellText=context.getString(R.string.cell_text,run.getmStartDate());
        startDateTextView.setText(cellText);
    }
}
