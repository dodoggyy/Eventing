package edu.ntust.embedded;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class ScheduleNewActivity extends Activity {
	private DatePicker mDate;
	private TimePicker mTime;
	private TimePicker mTime2;
	private TextView mTxtResult2;
	private Dialog mDateDlg;
	//private Button mBtnEdtDate;
	private Dialog mTimeDlg;
	private Button mBtnEdtTime1;
	private Button mBtnEdtTime2;
	private EditText mActivity;
	private CheckBox mCheckImportant;
	private CheckBox mCheckFinish;

	private ScheduleItem item = new ScheduleItem();
	private Date datetime = new Date();
	private Calendar cal = Calendar.getInstance();
	private Button mBtnOK;
	private EditText mEdtTxtName;
	private EditText mEdtTxtDsc;
	private EditText mEdtTxtLoc;
	private int eventIndex;

	private SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eventIndex = this.getIntent().getIntExtra("index", -1);

		setContentView(R.layout.newevent);
		setupViewComponent();
	}

	private void setupViewComponent() {
		mCheckFinish = (CheckBox) findViewById(R.id.checkFinish);
		mCheckImportant = (CheckBox) findViewById(R.id.checkImportant);

		mActivity = (EditText) findViewById(R.id.editActivityName);
		mTime = (TimePicker) findViewById(R.id.PicTime);
		//mBtnEdtDate = (Button) findViewById(R.id.btnEdtDate);
		//mBtnEdtDate.setOnClickListener(setDateBtnOKOnClkLis);
		mBtnEdtTime1 = (Button) findViewById(R.id.btnEdtTime1);
		mBtnEdtTime1.setOnClickListener(setTimeBtnOKOnClkLis1);
		mBtnEdtTime2 = (Button) findViewById(R.id.btnEdtTime2);
		mBtnEdtTime2.setOnClickListener(setTimeBtnOKOnClkLis2);

		mEdtTxtName = (EditText) findViewById(R.id.editActivityName);
		mEdtTxtDsc = (EditText) findViewById(R.id.editDescription);
		mEdtTxtLoc = (EditText) findViewById(R.id.editLocation);

		mBtnOK = (Button) findViewById(R.id.OK);
		mBtnOK.setOnClickListener(okBtnOnClkLis);

		if (eventIndex != -1) { /* modifying, load old choices */
			item = ScheduleExpandableList.scheduleList.get(eventIndex);

			mEdtTxtName.setText(item.getName());
			mEdtTxtDsc.setText(item.getDescription());
			mEdtTxtLoc.setText(item.getLocation());
			mBtnEdtTime1.setText(item.getStartTime().toString()); //WTF is going on
			mBtnEdtTime2.setText(item.getEndTime().toString());
			//mBtnEdtDate.setText(item.getStartTime().toString());

			mCheckFinish.setChecked(item.getFinished());
			mCheckImportant.setChecked(item.getImportant());
		}
	}

	/* opens the dialog to choose a date */
	/*
	private Button.OnClickListener setDateBtnOKOnClkLis = new Button.OnClickListener() {
		public void onClick(View v) {

			mDateDlg = new Dialog(ScheduleNewActivity.this);
			mDateDlg.setContentView(R.layout.date);
			mDate = (DatePicker) mDateDlg.findViewById(R.id.PicDate);
			mDateDlg.setTitle("選擇日期");
			mDateDlg.setCancelable(false);
			Button btnDateSelect = (Button) mDateDlg
					.findViewById(R.id.btnDateSelect);
			btnDateSelect.setOnClickListener(setDateDlgOKOnClkLis);
			mDateDlg.show();

		}
	};
	*/
	/* confirms choice of date */
	/*
	private Button.OnClickListener setDateDlgOKOnClkLis = new Button.OnClickListener() {
		public void onClick(View v) {
			String date_text = mDate.getYear() + " / " + mDate.getMonth()
					+ " / " + mDate.getDayOfMonth();
			cal.set(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth());
			mBtnEdtDate.setText(date_text);
			mDateDlg.cancel();
		}

	};
	*/
	/* opens a dialog to choose a time */
	/* start time */
	private Button.OnClickListener setTimeBtnOKOnClkLis1 = new Button.OnClickListener() {
		public void onClick(View v) {
			mTimeDlg = new Dialog(ScheduleNewActivity.this);
			mTimeDlg.setContentView(R.layout.time);
			mTime = (TimePicker) mTimeDlg.findViewById(R.id.PicTime);
			mTime.setIs24HourView(true);
			// mTime.setOnTimeChangedListener(timeEndBounder);
			Log.d("current start time - OpenDlg", (item.getStartTime()) != null?item.getStartTime().toString():"nothing");
			if (item.getStartTime() != null) {
				mTime.setCurrentHour(item.getStartTime().getHour());
				mTime.setCurrentMinute(item.getStartTime().getMinute());
				Log.d("current start hour", new Integer(item.getStartTime().getHour()).toString());
			}
			
			mTimeDlg.setTitle("選擇時間");
			Button btnTimeSelect = (Button) mTimeDlg
					.findViewById(R.id.btnTimeSelect);
			btnTimeSelect.setOnClickListener(setTimeDlgOKOnClkLis1);
			mTimeDlg.show();
		}
	};
	/* end time */
	private Button.OnClickListener setTimeBtnOKOnClkLis2 = new Button.OnClickListener() {
		public void onClick(View v) {
			mTimeDlg = new Dialog(ScheduleNewActivity.this);
			mTimeDlg.setContentView(R.layout.time);
			mTime = (TimePicker) mTimeDlg.findViewById(R.id.PicTime);
			mTime.setIs24HourView(true);
			// mTime.setOnTimeChangedListener(timeStartBounder);
			if (item.getEndTime() != null) {
				mTime.setCurrentHour(item.getEndTime().getHour());
				mTime.setCurrentMinute(item.getEndTime().getMinute());
				Log.d("current end hour", new Integer(item.getEndTime().getHour()).toString());

				
			}
			
			mTimeDlg.setTitle("選擇時間");
			Button btnTimeSelect = (Button) mTimeDlg
					.findViewById(R.id.btnTimeSelect);
			btnTimeSelect.setOnClickListener(setTimeDlgOKOnClkLis2);
			mTimeDlg.show();
		}
	};
	/* confirms choice of time */

	/* start time */
	private Button.OnClickListener setTimeDlgOKOnClkLis1 = new Button.OnClickListener() {
		public void onClick(View v) {
			SimpleTime chosenTime = new SimpleTime(mTime.getCurrentHour(), mTime.getCurrentMinute());
			item.setStartTime(chosenTime);
			
			String time_text = chosenTime.toString();
			mBtnEdtTime1.setText(time_text);
			mTimeDlg.cancel();
			
		}
	};
	/* end time */
	private Button.OnClickListener setTimeDlgOKOnClkLis2 = new Button.OnClickListener() {
		public void onClick(View v) {
			SimpleTime chosenTime = new SimpleTime(mTime.getCurrentHour(), mTime.getCurrentMinute());
			item.setEndTime(chosenTime);
			String time_text = chosenTime.toString();
			
			mBtnEdtTime2.setText(time_text);
			mTimeDlg.cancel();
			
		}
	};

	/* Create the new scheduled event. */
	private Button.OnClickListener okBtnOnClkLis = new Button.OnClickListener() {

		public void onClick(View v) {
			item.setName(mEdtTxtName.getText().toString());
			item.setDescription(mEdtTxtDsc.getText().toString());
			item.setLocation(mEdtTxtLoc.getText().toString());
			item.setImportant(mCheckImportant.isChecked());
			item.setFinished(mCheckFinish.isChecked());

			Log.d("index", new Integer(eventIndex).toString());
			
			/**error checking**/
			if (item.getStartTime().compareTo(item.getEndTime()) >= 0) { /* don't exit dialog if selected times are bad*/
				
				Toast.makeText(ScheduleNewActivity.this, R.string.bad_start_time, Toast.LENGTH_SHORT).show();
				return;
			}
			
			/**selected times ok, update information using user selected data**/
			
			if (eventIndex != -1) { /* modifying an event activity*/
				ScheduleExpandableList.scheduleList.set(eventIndex, item);
				// ScheduleList.dbAdapter.removeByName(old.getName());
				ScheduleExpandableList.dbAdapter.removeEventByPos(eventIndex);
				ScheduleExpandableList.dbAdapter.insertEvent(item, eventIndex);
			} 
			else { /*inserting new event activity*/
				int i;
				for (i = 0; i < ScheduleExpandableList.scheduleList.size(); i++) {
					int cmpStart = ScheduleExpandableList.scheduleList.get(i)
							.getStartTime().compareTo(item.getStartTime());
					int cmpEnd = ScheduleExpandableList.scheduleList.get(i).getEndTime()
							.compareTo(item.getEndTime());
					
					if (cmpStart > 0 || (cmpStart==0 && cmpEnd < 0)) { // want longer events listed first if same start time
						ScheduleExpandableList.scheduleList.add(i, item);
						ScheduleExpandableList.scheduleJobs.add(i, new ArrayList<WorkerJob>());
						break;
					} 
					
				}
				/* add at end if that needs to be the case */
				if (i == ScheduleExpandableList.scheduleList.size()) {
					ScheduleExpandableList.scheduleList.add(item);
					ScheduleExpandableList.scheduleJobs.add(new ArrayList<WorkerJob>());
				}
				// ScheduleList.dbAdapter.insertEvent(item,
				// ScheduleList.scheduleList.size());
			}
			finish();
		}

	};
	private Button.OnClickListener setGroupOKOnCli = new Button.OnClickListener() {
		public void onClick(View v) {
			setContentView(R.layout.newevent);
		}
	};

	private Button.OnClickListener setMesOKOnCli = new Button.OnClickListener() {
		public void onClick(View v) {
			setContentView(R.layout.newevent);
		}
	};

	/*
	 * private TimePicker.OnTimeChangedListener timeEndBounder = new
	 * TimePicker.OnTimeChangedListener() {
	 * 
	 * public void onTimeChanged(TimePicker view, int selHour, int selMin) {
	 * //selected hour, min int endHour =
	 * (item.getEndTime()!=null)?item.getEndTime().getHours():24; int endMin =
	 * (item.getEndTime()!=null)?item.getEndTime().getMinutes():60; if (
	 * !(selHour<endHour || (selHour==endHour && selMin<endMin)) ) {
	 * view.setCurrentHour(endHour); view.setCurrentMinute(endMin); } } };
	 * 
	 * private TimePicker.OnTimeChangedListener timeStartBounder = new
	 * TimePicker.OnTimeChangedListener() {
	 * 
	 * public void onTimeChanged(TimePicker view, int selHour, int selMin) {
	 * //selected hour, min int startHour =
	 * (item.getEndTime()!=null)?item.getStartTime().getHours():0; int startMin
	 * = (item.getEndTime()!=null)?item.getStartTime().getMinutes():0; if (
	 * !(selHour<startHour || (selHour==startHour && selMin<startMin)) ) {
	 * view.setCurrentHour(startHour); view.setCurrentMinute(startMin); } } };
	 */
	/*
	boolean checkEndTime(int selHour, int selMin) {
		Date currEndTime = item.getEndTime();
		if (currEndTime == null) {
			return true; // ok
		}

		int endHour = currEndTime.getHours();
		int endMin = currEndTime.getMinutes();

		return (selHour < endHour || (selHour == endHour && selMin < endMin));

	}
	boolean checkStartTime(int selHour, int selMin) {
		Date currStartTime = item.getStartTime();
		if (currStartTime == null) {
			return true; // ok
		}

		int startHour = currStartTime.getHours();
		int startMin = currStartTime.getMinutes();

		return (selHour > startHour || (selHour == startHour && selMin > startMin));

	}*/
	/*
	private boolean checkSelectedTimes() {
		Date selStartTime = item.getStartTime();
		Date selEndTime = item.getEndTime();
		if (selStartTime==null || selEndTime==null)
			return true;
		return selStartTime.compareTo(selEndTime) < 0;
	}
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub

	}*/
}