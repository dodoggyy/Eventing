package edu.ntust.embedded;

import java.util.ArrayList;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EventWorkersList extends ListActivity {
	public static ArrayList<EventWorker> workerList = new ArrayList<EventWorker>();
	private ArrayAdapter<EventWorker> adapter;
	public ScheduleDBAdapter dbAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workerslist);

		adapter = new ArrayAdapter<EventWorker>(this, android.R.layout.simple_list_item_1,
				workerList);
		this.setListAdapter(adapter); // connects the adapter to the todoList
										// view
		registerForContextMenu(getListView());

		//dbAdapter = new ScheduleDBAdapter(this);
		populateList();
	}

	private void populateList() {
		workerList.clear(); //so inefficient :(
		
		dbAdapter = ((Eventing) getApplication()).getDatabaseAdapter();
		Cursor workerCsr = dbAdapter.getAllEventWorkersCursor();
		if (workerCsr!=null) {
			while(workerCsr.moveToNext()) {
				EventWorker ew = new EventWorker(workerCsr.getString(ScheduleDBAdapter.WORKER_NAME_COLUMN_INDEX),
						workerCsr.getString(ScheduleDBAdapter.JOB_COLUMN_INDEX),
						workerCsr.getString(ScheduleDBAdapter.PHONE_NUMBER_COLUMN_INDEX));
				workerList.add(ew);
			}
			adapter.notifyDataSetChanged();
		}
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		EventWorker c = (EventWorker) l.getItemAtPosition(position);
		Intent callContactIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+c.getNum()));
		
		startActivity(callContactIntent);
	}

}
