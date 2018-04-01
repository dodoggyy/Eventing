package edu.ntust.embedded;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleItemAdapter extends ArrayAdapter<ScheduleItem> {
	int resource;
	private Context context;
	public ScheduleItemAdapter(Context _context, int _resource,
			List<ScheduleItem> _items) {
		super(_context, _resource, _items);
		resource = _resource;
		context = _context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout theView;

		ScheduleItem item = getItem(position);

		String eventNameString = item.getName();
		SimpleTime eventStart = item.getStartTime();
		SimpleTime eventEnd = item.getEndTime();
		String eventDescriptionString = item.getDescription();
		String eventLocationString = item.getLocation();

		SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

		String startTimeString = timeFormatter.format(eventStart);
		String endTimeString = timeFormatter.format(eventEnd);
		String eventTimeString = startTimeString + " to " + endTimeString;
		/*
		 * eventNameString eventDescriptionString eventLocationString
		 * eventTimeString
		 */

		if (convertView == null) {
			theView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(resource, theView, true);
		} else {
			theView = (LinearLayout) convertView;
		}

		/* views for name description location and time */
		TextView nameView = (TextView) theView.findViewById(R.id.name);
		TextView descriptionView = (TextView) theView
				.findViewById(R.id.description);
		TextView locationView = (TextView) theView.findViewById(R.id.location);
		TextView timeView = (TextView) theView.findViewById(R.id.time);

		nameView.setText(eventNameString);
		nameView.setTextSize(21);
		nameView.setTextColor(Color.WHITE);
		if (item.getImportant())
			nameView.setTextColor(Color.RED);
		if (item.getFinished())
			nameView.setTextColor(Color.YELLOW);

		timeView.setText(eventTimeString);

		if (eventDescriptionString.equals("")) {
			descriptionView.setVisibility(View.GONE);
		} else
			descriptionView.setText(eventDescriptionString);

		if (eventLocationString.equals(""))
			locationView.setVisibility(View.GONE);
		else
			locationView.setText(eventLocationString);
		ScheduleDBAdapter dbadapter = ((Eventing)context.getApplicationContext()).getDatabaseAdapter();
		
		
		ArrayList<WorkerJob> jobs = dbadapter.getEventJobs(position);
		ArrayAdapter<WorkerJob> jobsAdapter = new ArrayAdapter<WorkerJob>(context, android.R.layout.simple_list_item_1, jobs);
		
		
		
		return theView;
	}
}
