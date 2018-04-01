package edu.ntust.embedded;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ContactsActivity extends ListActivity {

	private ArrayList<_Contact> contactsArray;
	private _Contact person;
	private class _Contact {
		private String name;
		private String number;
		public _Contact(String name, String number) {
			super();
			this.name = name;
			this.number = number;
		}
		public String getName() {
			return name;
		}
		public String getNumber() {
			return number;
		}
		@Override
		public String toString() {
		return name + "        " + number;
		}
		
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactslist);
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query (ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
		
		int indexName = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		int indexNumber = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		
		
		contactsArray = new ArrayList<_Contact>();
		while (cur.moveToNext()){
			contactsArray.add(new _Contact(cur.getString(indexName), cur.getString(indexNumber)));
		}
		this.setListAdapter(new ArrayAdapter<_Contact>(this, android.R.layout.simple_list_item_1,contactsArray));
		registerForContextMenu(getListView());
	}

	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
				//add contact to event select cuz it's the only option right now
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		person = (_Contact) getListView().getItemAtPosition(menuInfo.position);
		Intent addWorker = new Intent(this, AddEventWorkerActivity.class);

		addWorker.putExtra("name", person.getName());
		addWorker.putExtra("number", person.getNumber());
		
		startActivityForResult(addWorker, 0);
		

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add("Add this contact to the event.");
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		EventWorker worker = new EventWorker(person.getName(), data.getStringExtra("job"), person.getNumber());
		
		ScheduleDBAdapter dbadapter = ((Eventing) getApplication()).getDatabaseAdapter();
		dbadapter.insertWorker(worker);
	}
	

}
