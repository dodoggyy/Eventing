package edu.ntust.embedded;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ScheduleDBAdapter {
	/* Database Constants */
	private static final String DATABASE_NAME = "schedule.db";
	private static final int DATABASE_VERSION = 1;
	/** Table for the individual events(activities) on the schedule **/
	private static final String DATABASE_TABLE_EVENTS = "scheduleItems";
	private static final String KEY_ID = "_id";
	private static final String KEY_EVENT_NAME = "event_name";
	// private static final String KEY_EVENT_DATE = "date";
	private static final String KEY_EVENT_START_TIME = "start";
	private static final String KEY_EVENT_END_TIME = "end";
	private static final String KEY_EVENT_DESCRIPTION = "description";
	private static final String KEY_EVENT_LOCATION = "location";
	private static final String KEY_EVENT_LISTPOS = "pos";
	private static final String KEY_EVENT_IMPORTANT = "important";
	private static final String KEY_EVENT_FINISHED = "fin";

	/**
	 * Table for your contacts - the other workers participating in the
	 * execution of this event (as a whole)
	 **/
	private static final String DATABASE_TABLE_WORKERS = "scheduleWorkers";
	// id same constant
	private static final String KEY_WORKER_NAME = "worker_name";
	private static final String KEY_WORKER_JOB = "worker_job"; // job title
	private static final String KEY_WORKER_PHONE_NUMBER = "worker_phone"; // phone
																			// number

	/** Table for activity-specific jobs **/
	private static final String DATABASE_TABLE_EVENT_JOBS = "schedule_jobs";
	private static final String FOREIGN_KEY_WORKER = "worker_id";
	private static final String FOREIGN_KEY_EVENT = "event_pos";
	private static final String KEY_JOB_DESCRIPTION = "job_description";
	
	/*Table for the event date*/
	private static final String DATABASE_TABLE_EVENT_DATE = "schedule_date";
	private static final String KEY_EVENT_DATE = "event_date";

	public static final String[] TABLE_EVENTS_COLUMNS = new String[] { KEY_ID,
			KEY_EVENT_NAME, KEY_EVENT_START_TIME, KEY_EVENT_END_TIME,
			KEY_EVENT_LOCATION, KEY_EVENT_DESCRIPTION, KEY_EVENT_LISTPOS,
			KEY_EVENT_IMPORTANT, KEY_EVENT_FINISHED };
	public static final int EVENT_NAME_COLUMN_INDEX = 1;
	public static final int START_COLUMN_INDEX = 2;
	public static final int END_COLUMN_INDEX = 3;
	public static final int LOCATION_COLUMN_INDEX = 4;
	public static final int DESCRIPTION_COLUMN_INDEX = 5;
	public static final int POS_COLUMN_INDEX = 6;
	public static final int IMPORTANT_COLUMN_INDEX = 7;
	public static final int FIN_COLUMN_INDEX = 8;

	public static final String[] TABLE_WORKERS_COLUMNS = new String[] { KEY_ID,
			KEY_WORKER_NAME, KEY_WORKER_JOB, KEY_WORKER_PHONE_NUMBER };
	public static final int WORKER_NAME_COLUMN_INDEX = 1;
	public static final int JOB_COLUMN_INDEX = 2;
	public static final int PHONE_NUMBER_COLUMN_INDEX = 3;

	public static final String[] TABLE_JOBS_COLUMNS = new String[] {
			FOREIGN_KEY_WORKER, FOREIGN_KEY_EVENT, KEY_JOB_DESCRIPTION };
	public static final int FK_WORKER_COLUMN_INDEX = 0;
	public static final int FK_EVENT_COLUMN_INDEX = 1;
	public static final int JOB_DESCRIPTION_COLUMN_INDEX = 2;

	private SQLiteDatabase db;
	private final Context context;

	private scheduleDBOpenHelper dbHelper;

	public ScheduleDBAdapter(Context _context) {
		this.context = _context;
		dbHelper = new scheduleDBOpenHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public void close() {
		db.close();
	}

	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public long insertEvent(ScheduleItem _event, int pos) {
		ContentValues newValues = new ContentValues();

		newValues.put(KEY_EVENT_NAME, _event.getName());
		newValues.put(KEY_EVENT_START_TIME, _event.getStartTime().asMinutes());
		newValues.put(KEY_EVENT_END_TIME, _event.getEndTime().asMinutes());
		newValues.put(KEY_EVENT_LOCATION, _event.getLocation());
		newValues.put(KEY_EVENT_DESCRIPTION, _event.getDescription());

		newValues.put(KEY_EVENT_LISTPOS, pos);

		newValues.put(KEY_EVENT_IMPORTANT, _event.getImportant() ? 1 : 0);
		newValues.put(KEY_EVENT_FINISHED, _event.getFinished() ? 1 : 0);

		// id is autoincremeted right?
		return db.insert(DATABASE_TABLE_EVENTS, null, newValues);
	}

	public long insertWorker(EventWorker _worker) {
		ContentValues workerVals = new ContentValues();
		workerVals.put(KEY_WORKER_NAME, _worker.getName());
		workerVals.put(KEY_WORKER_JOB, _worker.getJob());
		workerVals.put(KEY_WORKER_PHONE_NUMBER, _worker.getNum());
		/*
		 * db.execSQL("insert into table " + DATABASE_TABLE_WORKERS + " (" +
		 * KEY_WORKER_NAME + ", " + KEY_WORKER_JOB + "," +
		 * KEY_WORKER_PHONE_NUMBER + ")" + " values (" + _worker.getName() +
		 * ", " + _worker.getJob() + ", " + _worker.getNum() + ");");
		 */// you didn't put quote marks ... screw this contentvalues is better
		return db.insert(DATABASE_TABLE_WORKERS, null, workerVals);
	}

	public long insertJob(long event_pos, long worker_id, String job_description) {
		ContentValues jobVals = new ContentValues();
		jobVals.put(FOREIGN_KEY_EVENT, event_pos);
		jobVals.put(FOREIGN_KEY_WORKER, worker_id);
		jobVals.put(KEY_JOB_DESCRIPTION, job_description);

		return db.insert(DATABASE_TABLE_EVENT_JOBS, null, jobVals);

	}
	
	public long insertEventDate(long dateAsLong) {
		ContentValues dateVals = new ContentValues();
		dateVals.put(KEY_EVENT_DATE, dateAsLong);
		
		return db.insert(DATABASE_TABLE_EVENT_DATE, null, dateVals);
	}

	/*
	 * public boolean removeTask(long _rowIndex) { return
	 * db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null)>0; }
	 */

	public boolean removeByName(String name) {
		return db.delete(DATABASE_TABLE_EVENTS, KEY_EVENT_NAME + "=" + "\""
				+ name + "\"", null) > 0;
	}

	public boolean removeEventByPos(int pos) {
		return db.delete(DATABASE_TABLE_EVENTS, KEY_EVENT_LISTPOS + "=" + pos,
				null) > 0;
	}

	public boolean removeAllEvents() {
		return db.delete(DATABASE_TABLE_EVENTS, null, null) > 0;
	}
	
	public boolean removeAllJobs() {
		return db.delete(DATABASE_TABLE_EVENT_JOBS, null, null)>0;
	}
	
	public boolean removeAllEventDates() {
		return db.delete(DATABASE_TABLE_EVENT_DATE, null, null)>0;
	}
	
	/*public boolean removeAllEventJobs(int pos) {
		return db.delete(DATABASE_TABLE_EVENT_JOBS, "WHERE "+FOREIGN_KEY_EVENT+"="+pos, null)>0;
	}*/
	/*
	 * public boolean updateTask(long _rowIndex, String _task) { ContentValues
	 * newValue = new ContentValues(); newValue.put(KEY_TASK, _task); return
	 * db.update(DATABASE_TABLE, newValue, KEY_ID + "=" + _rowIndex, null)>0; }
	 */

	

	public Cursor getAllScheduleItemsCursor() {
		/* in ascending order */
		return db.query(DATABASE_TABLE_EVENTS, TABLE_EVENTS_COLUMNS, null,
				null, null, null, KEY_EVENT_LISTPOS + " ASC", null);

	}

	public Cursor getAllEventWorkersCursor() {
		return db.query(DATABASE_TABLE_WORKERS, TABLE_WORKERS_COLUMNS, null,
				null, null, null, null);
	}

	public ArrayList<EventWorker> getAllEventWorkersList() {
		Cursor workerCsr = getAllEventWorkersCursor();
		ArrayList<EventWorker> workerlist = new ArrayList<EventWorker>();
		if (workerCsr != null) {
			while (workerCsr.moveToNext()) {
				EventWorker ew = new EventWorker(
						workerCsr
								.getString(ScheduleDBAdapter.WORKER_NAME_COLUMN_INDEX),
						workerCsr.getString(ScheduleDBAdapter.JOB_COLUMN_INDEX),
						workerCsr
								.getString(ScheduleDBAdapter.PHONE_NUMBER_COLUMN_INDEX));
				workerlist.add(ew);
			}
		}
		return workerlist;
	}

	public Cursor setCursorToScheduleItem(long _rowIndex) throws SQLException {
		Cursor result = db.query(true, DATABASE_TABLE_EVENTS,
				TABLE_EVENTS_COLUMNS, KEY_ID + "=" + _rowIndex, null, null,
				null, null, null);
		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("No to do items found for row: " + _rowIndex);
		}
		return result;

	}

	public ScheduleItem getScheduleItem(long _rowIndex) throws SQLException {
		Cursor cursor = setCursorToScheduleItem(_rowIndex);

		String name = cursor.getString(EVENT_NAME_COLUMN_INDEX);
		int start = cursor.getInt(START_COLUMN_INDEX);
		int end = cursor.getInt(END_COLUMN_INDEX);
		String location = cursor.getString(LOCATION_COLUMN_INDEX);
		String description = cursor.getString(DESCRIPTION_COLUMN_INDEX);

		ScheduleItem result = new ScheduleItem(name, new SimpleTime(start), new SimpleTime(
				end), location, description);

		return result;
	}

	public EventWorker getEventWorkerByName(String name) {
		Cursor workerCsr = db.query(DATABASE_TABLE_WORKERS,
				TABLE_WORKERS_COLUMNS, KEY_WORKER_NAME + "=" + name, null,
				null, null, null);// csr as short for cursor ... works right??
		if ((workerCsr.getCount() == 0) || !workerCsr.moveToFirst()) {
			throw new SQLException("No to do items found for name: " + name);
		}

		return new EventWorker(workerCsr.getString(WORKER_NAME_COLUMN_INDEX),
				workerCsr.getString(JOB_COLUMN_INDEX),
				workerCsr.getString(PHONE_NUMBER_COLUMN_INDEX));
	}

	public long getEventWorkerId(String name) {
		Cursor workerCsr = db.query(DATABASE_TABLE_WORKERS,
				TABLE_WORKERS_COLUMNS, KEY_WORKER_NAME + "=" + "\"" + name
						+ "\"", null, null, null, null);// csr as short for
														// cursor ... works
														// right??
		if ((workerCsr.getCount() == 0) || !workerCsr.moveToFirst()) {
			throw new SQLException("No to do items found for name: " + name);
		}

		return workerCsr.getLong(0);
	}

	public ArrayList<WorkerJob> getEventJobs(int pos) {
		/* get jobs specific to an event activity */
		String MY_QUERY = "SELECT " + KEY_WORKER_NAME + ","
				+ KEY_JOB_DESCRIPTION + " FROM " + DATABASE_TABLE_EVENT_JOBS
				+ " INNER JOIN "
				+ DATABASE_TABLE_WORKERS 
				+" ON " + FOREIGN_KEY_WORKER
				+"=" + KEY_ID
				+  " WHERE "
				+ FOREIGN_KEY_EVENT+ "="
				+pos;
		Log.d("MY_QUERY", MY_QUERY);
		Cursor jobsCsr = db.rawQuery(MY_QUERY, null);
		ArrayList<WorkerJob> jobs = new ArrayList<WorkerJob>();
		while (jobsCsr.moveToNext()) {
			WorkerJob job = new WorkerJob(jobsCsr.getString(0),jobsCsr.getString(1));
			jobs.add(job);
		}
		return jobs;
	}

	/*public boolean removeAllEventJobs(int pos) {
		return db.delete(DATABASE_TABLE_EVENT_JOBS, "WHERE "+FOREIGN_KEY_EVENT+"="+pos, null)>0;
	}*/
	/*
	 * public boolean updateTask(long _rowIndex, String _task) { ContentValues
	 * newValue = new ContentValues(); newValue.put(KEY_TASK, _task); return
	 * db.update(DATABASE_TABLE, newValue, KEY_ID + "=" + _rowIndex, null)>0; }
	 */
	
	
	
	private static class scheduleDBOpenHelper extends SQLiteOpenHelper {
	
		public scheduleDBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
	
		/* SQL statement to create a new database */
		private static final String DATABASE_TABLE_EVENTS_CREATE = "create table "
				+ DATABASE_TABLE_EVENTS
				+ " ("
				+ KEY_ID
				+ " integer, "
				+ KEY_EVENT_NAME
				+ " text not null, "
				+ KEY_EVENT_START_TIME
				+ " integer, "
				+ KEY_EVENT_END_TIME
				+ " integer, "
				+ KEY_EVENT_LOCATION
				+ " text, "
				+ KEY_EVENT_DESCRIPTION
				+ " text, "
				+ KEY_EVENT_LISTPOS
				+ " integer primary key, "
				+ KEY_EVENT_IMPORTANT
				+ " integer, "
				+ KEY_EVENT_FINISHED
				+ " integer);";
	
		private static final String DATABASE_TABLE_WORKERS_CREATE = "create table "
				+ DATABASE_TABLE_WORKERS
				+ " ("
				+ KEY_ID
				+ " integer primary key autoincrement, "
				+ KEY_WORKER_NAME
				+ " text, "
				+ KEY_WORKER_JOB
				+ " text, "
				+ KEY_WORKER_PHONE_NUMBER + " text);";
	
		private static final String DATABASE_TABLE_WORKERS_JOBS_CREATE = "create table "
				+ DATABASE_TABLE_EVENT_JOBS
				+ " ("
				+ KEY_JOB_DESCRIPTION
				+ " text, "
				+ FOREIGN_KEY_EVENT
				+ " integer not null, "
				+ FOREIGN_KEY_WORKER
				+ " integer not null, "
				+ "foreign key "
				+ "("
				+ FOREIGN_KEY_EVENT
				+ ") "
				+ "references "
				+ DATABASE_TABLE_EVENTS
				+ " ("
				+ KEY_ID
				+ "), "
				+ "foreign key "
				+ "("
				+ FOREIGN_KEY_WORKER
				+ ") "
				+ "references "
				+ DATABASE_TABLE_WORKERS
				+ " ("
				+ KEY_ID
				+ "), "
				+ "primary key ("
				+ FOREIGN_KEY_EVENT
				+ ", "
				+ FOREIGN_KEY_WORKER + ") )";
		private static final String DATABASE_TABLE_EVENT_DATE_CREATE = "create table "
				+ DATABASE_TABLE_EVENT_DATE + " "
				+ " ("
				+ KEY_ID + " "
				+ "integer primary key autoincrement, "
				+ KEY_EVENT_DATE + " "
				+ "integer);";
	
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_TABLE_EVENTS_CREATE);
			_db.execSQL(DATABASE_TABLE_WORKERS_CREATE);
			_db.execSQL(DATABASE_TABLE_WORKERS_JOBS_CREATE);
			_db.execSQL(DATABASE_TABLE_EVENT_DATE_CREATE);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			Log.w("scheduleDBAdapter", "Upgrading from version " + _oldVersion
					+ " to" + _newVersion + ", which will destroy all old data");
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_EVENTS);
			onCreate(_db);
		}
	
	}

	public long getEventPos(int pos) {
		Cursor evtCsr = db.query(DATABASE_TABLE_EVENTS, TABLE_EVENTS_COLUMNS,
				KEY_EVENT_LISTPOS + "=" + pos, null, null, null, null);
		return evtCsr.getLong(POS_COLUMN_INDEX);
	}



	public SimpleDate getEventDate() {
		Cursor dateCsr = db.query(DATABASE_TABLE_EVENT_DATE, new String[] {KEY_EVENT_DATE}, null, null, null, null, null);
		if(dateCsr.moveToFirst())
			return new SimpleDate(dateCsr.getLong(0));
		else
			return null;
	}
}
