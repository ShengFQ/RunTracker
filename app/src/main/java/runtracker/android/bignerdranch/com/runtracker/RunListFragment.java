package runtracker.android.bignerdranch.com.runtracker;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by sheng on 16/10/14.
 * 显示用户旅行位置历史记录的列表.
 * 使用Cursor配合CursorAdapter,直接将游标对象对接显示
 */
public class RunListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG="RunListFragment";
   //TODO loader的作用就是替换掉数据库加载数据的方式
    //private RunCursor mCursor;
    private static final int REQUEST_NEW_RUN=0;//requestCode

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//设置使用选项菜单
        //TODO method1
      //  mCursor=Runmanger.getInstance(getActivity()).queryRuns();

        //create an adapter to point at this cursor
     //   RunCursorAdapter adapter=new RunCursorAdapter(getActivity(),mCursor);
     //   super.setListAdapter(adapter);


        //TODO method2
        //initialize the loader to load the list of runs
      getLoaderManager().initLoader(0,null,this);//this==LoaderManager.LoaderCallbacks<T>
    }

    //@Override
  /*  public void onDestroy() {
        mCursor.close();
        super.onDestroy();
    }*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    /**
     * 实例化选项菜单
     * */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.run_list_options,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_run:
                Intent i=new Intent(getActivity(),RunActivity.class);
                startActivityForResult(i,REQUEST_NEW_RUN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"onActivityResult() start");
        if(REQUEST_NEW_RUN==requestCode){
           // method1
         //  mCursor.requery();
          // ((RunCursorAdapter)getListAdapter()).notifyDataSetChanged();

            //restart the loader to get any new run avaliable
           getLoaderManager().restartLoader(0,null,this);//this==LoaderManager.LoaderCallbacks<T>
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
       // super.onListItemClick(l, v, position, id);
        Intent i=new Intent(getActivity(),RunActivity.class);
        i.putExtra(RunActivity.EXTRA_RUN_ID,id);
        startActivity(i);
    }

    /**
     * LoaderCallbacks.onCreateLoader
     * LoaderManager创建loader时
     * */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"LoaderManager.LoaderCallbacks.onCreateLoader()");
        return new RunListCursorLoader(getActivity());
    }

    /**
     * LoaderCallbacks.onLoadFinished
     * 拉取数据完毕
     * */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(TAG,"LoaderManager.LoaderCallbacks.onLoadFinished()");
        RunCursorAdapter adapter=new RunCursorAdapter(getActivity(),(RunCursor)cursor);
        setListAdapter(adapter);
    }

    /**
     * LoaderCallbacks.onLoaderReset
     * 在没有可加载数据时
     * */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(TAG,"LoaderManager.LoaderCallbacks.onLoaderReset()");
        //stop using the cursor
        setListAdapter(null);
    }


}
