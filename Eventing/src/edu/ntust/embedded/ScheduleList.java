package edu.ntust.embedded;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class ScheduleList extends ListActivity {

	public static ArrayList<ScheduleItem> scheduleList = new ArrayList<ScheduleItem>();
	// private ArrayAdapter<ScheduleItem> adapter;
	private ScheduleItemAdapter adapter;
	private Dialog jobDlg;
	private EditText jobEdtTxt;
	public static ScheduleDBAdapter dbAdapter;

	/** MENU CONSTANTS **/
	private static final int MENU_SETTING = Menu.FIRST,
			MENU_ABOUT = Menu.FIRST + 1, MENU_LOGOUT = Menu.FIRST + 2,
			MENU_PRIVACY = Menu.FIRST + 3, MENU_PRIVATE_MES = Menu.FIRST + 4,
			MENU_GROUP = Menu.FIRST + 5, MENU_REVEAL = Menu.FIRST + 6,
			MENU_HELP = Menu.FIRST + 7, MENU_CONTACTS = Menu.FIRST + 8, MENU_WORKERS = Menu.FIRST+9;
	/** CONTEXT MENU CONSTANTS **/
	public final static int MODIFY_context_menu = Menu.FIRST;
	public final static int REMOVE_context_menu = Menu.FIRST + 1;
	public final static int ADD_JOB_context_menu = Menu.FIRST + 2;

	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu submenu = menu.addSubMenu(0, MENU_SETTING, 0, "設定").setIcon(
				android.R.drawable.ic_menu_preferences);

		submenu.add(0, MENU_PRIVATE_MES, 0, "私人訊息");
		submenu.add(0, MENU_GROUP, 1, "群組");
		submenu.add(0, MENU_HELP, 2, "幫助");
		submenu.add(0, MENU_REVEAL, 3, "顯示選項");
		submenu.add(0, MENU_PRIVACY, 4, "隱私選項");
		menu.add(0, MENU_ABOUT, 1, "關於").setIcon(
				android.R.drawable.ic_dialog_info);
		menu.add(0, MENU_LOGOUT, 2, "登出").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(0, MENU_CONTACTS,Menu.NONE, "Contacts");
		menu.add(0,MENU_WORKERS, Menu.NONE, "Event workers");

		return super.onCreateOptionsMenu(menu);

	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ABOUT:
				new AlertDialog.Builder(ScheduleList.this)
				.setTitle("關於此程式")
				.setMessage(R.string.about)
				.setIcon(android.R.drawable.star_big_on)
				.show();
				break;
				
			case MENU_LOGOUT:
				finish();
				break;
				
			case MENU_HELP:
				new AlertDialog.Builder(ScheduleList.this)
				.setTitle("幫助")
				.setMessage(R.string.help)
				.setIcon(android.R.drawable.star_big_on)
				.show();
				break;
			case MENU_PRIVATE_MES:
				Intent intent = new Intent();
				intent.setClass(ScheduleList.this, ScheduleMessage.class);
				startActivity(intent);
				break;
			case MENU_GROUP: 
				Intent intent2 = new Intent();
				intent2.setClass(this, ScheduleGroup.class);
				startActivity(intent2);
				break;
			case MENU_REVEAL:
				drawTest glSurfView = new drawTest(this);
		        setContentView(glSurfView);
		        break;
			case MENU_CONTACTS:
				Intent intent3 = new Intent();
				intent3.setClass(this, ContactsActivity.class);
				startActivity(intent3);
				break;
			case MENU_WORKERS:
				Intent intent4 = new Intent();
				intent4.setClass(this, EventWorkersList.class);
				startActivity(intent4);
				break;
				
		}
		return super.onOptionsItemSelected(item);
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedulelist);

		// final ArrayList<ScheduleItem> scheduleList = new
		// ArrayList<ScheduleItem>();
		adapter = new ScheduleItemAdapter(this, R.layout.scheduleitem,
				scheduleList);
		this.setListAdapter(adapter); // connects the adapter to the todoList
										// view
		final Button neweventbutton = (Button) findViewById(R.id.neweventbutton);
		neweventbutton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent();
				// intent.setClass(EventingStart.this,
				// ScheduleNewActivity.class);
				intent.setClass(ScheduleList.this, ScheduleNewActivity.class);
				startActivity(intent);
			}

		});
		registerForContextMenu(getListView());

		//dbAdapter = new ScheduleDBAdapter(this);
		dbAdapter = ((Eventing) getApplication()).getDatabaseAdapter();
		dbAdapter.open();
		populateSchedule();
	}

	void populateSchedule() {
		Cursor scheduleCursor = dbAdapter.getAllScheduleItemsCursor();
		startManagingCursor(scheduleCursor);
		scheduleCursor.requery();
		scheduleList.clear();

		if (scheduleCursor.moveToFirst()) {
			do {
				String name = scheduleCursor
						.getString(ScheduleDBAdapter.EVENT_NAME_COLUMN_INDEX);
				int start = scheduleCursor
						.getInt(ScheduleDBAdapter.START_COLUMN_INDEX);
				int end = scheduleCursor
						.getInt(ScheduleDBAdapter.END_COLUMN_INDEX);
				String location = scheduleCursor
						.getString(ScheduleDBAdapter.LOCATION_COLUMN_INDEX);
				String description = scheduleCursor
						.getString(ScheduleDBAdapter.DESCRIPTION_COLUMN_INDEX);
				Boolean imp = scheduleCursor
						.getInt(ScheduleDBAdapter.IMPORTANT_COLUMN_INDEX) > 0;
				Boolean fin = scheduleCursor
						.getInt(ScheduleDBAdapter.FIN_COLUMN_INDEX) > 0;

				ScheduleItem nextItem = new ScheduleItem(name, new SimpleTime(start),
						new SimpleTime(end), location, description, imp, fin);
				scheduleList.add(nextItem);
			} while (scheduleCursor.moveToNext());
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		saveArray();
		dbAdapter.close();
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

	void saveArray() {
		int i = 0;
		dbAdapter.removeAllEvents();
		for (ScheduleItem item : scheduleList) {
			dbAdapter.insertEvent(item, i);
			i++;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Selected To Do Item");
		menu.add(0, MODIFY_context_menu, Menu.NONE, "Modify");
		menu.add(0, REMOVE_context_menu, Menu.NONE, "Remove");
		menu.add(0, ADD_JOB_context_menu, Menu.NONE, "Add job");
		// menu.add(0, REMOVE_TODO, Menu.NONE, "Remove");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);

		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int index = menuInfo.position;

		// removeItemFromList(index);
		switch (item.getItemId()) {
		case (REMOVE_context_menu): {
			removeItemFromList(index);
			return true;

		}
		case (MODIFY_context_menu): {
			Intent intent = new Intent();
			intent.putExtra("index", index);
			intent.setClass(ScheduleList.this, ScheduleNewActivity.class);
			startActivity(intent);
			return true;
		}
		case (ADD_JOB_context_menu): {
			addJobDlg(index);
			return true;
		}
		}
		return false;
	}
	
	private void addJobDlg(final int eventIndex) {
		final ScheduleDBAdapter dbadapter = ((Eventing) getApplication()).getDatabaseAdapter();
		ArrayList<EventWorker> workerlist = dbadapter.getAllEventWorkersList();
		
		ArrayAdapter<EventWorker> workersSpnAdapter = new ArrayAdapter<EventWorker>(this, android.R.layout.simple_spinner_item, workerlist);
		
		
		jobDlg = new Dialog(ScheduleList.this);
		jobDlg.setTitle("Event Worker Job");
		jobDlg.setContentView(R.layout.newjob);
		final Spinner spnWorker = (Spinner)jobDlg.findViewById(R.id.worker_spinner);
		spnWorker.setAdapter(workersSpnAdapter);
		jobEdtTxt = (EditText)jobDlg.findViewById(R.id.job_description);
		Button btnOK = (Button)jobDlg.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				long worker_id = dbadapter.getEventWorkerId(((EventWorker)spnWorker.getSelectedItem()).getName());

				dbadapter.insertJob(eventIndex, worker_id, jobEdtTxt.getText().toString());
				adapter.notifyDataSetChanged();
				jobDlg.cancel();
			}
		});
		//loginBtnOK.setOnClickListener(loginDlgBtnOKOnClkLis);
		jobDlg.show();
	}
	
	void removeItemFromList(int index) {
		ScheduleItem item = scheduleList.remove(index);
		// dbAdapter.removeByName(item.getName());
		dbAdapter.removeEventByPos(index);
		adapter.notifyDataSetChanged();
	}

}
