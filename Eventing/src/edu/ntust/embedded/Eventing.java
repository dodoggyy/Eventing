package edu.ntust.embedded;

import android.app.Application;

public class Eventing extends Application {

	ScheduleDBAdapter dbadapter;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dbadapter = new ScheduleDBAdapter(getApplicationContext()); // don't start too early? let super start first
	}

	public ScheduleDBAdapter getDatabaseAdapter() {
		return dbadapter;
	}
}
