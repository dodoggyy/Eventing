package edu.ntust.embedded;

public class SimpleTime implements Comparable<SimpleTime> {
	/** A simple representation of the time **/
	/** Because DateTime is annoying **/

	int hour; // 0-23
	int minute; // 0-59
	
	public SimpleTime(int minutes) {
		/* Takes the number of minutes from the start of the day til now and constructs a SimpleTime with it*/
		this.hour = minutes/60;
		this.minute = minutes%60;
		
	}
	
	public SimpleTime(int hour, int minute) {
		super();
		this.hour = hour;
		this.minute = minute;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int compareTo(SimpleTime time2) {
		if (this.hour < time2.getHour())
			return -1;
		
		if (this.hour > time2.getHour())
			return 1;
		
		if (this.minute < time2.getMinute())
			return -1;
		if (this.minute > time2.getMinute())
			return 1;
		
		return 0;
	}
	
	public int minutesUntil(SimpleTime time2) {
		/** Returns the time until time2 in minutes **/
		if (this.compareTo(time2) < 0) {
			return (time2.getHour() - this.hour)*60 - this.minute + time2.getMinute();
		}
		else {
			return time2.minutesUntil(this);
		}
	}
	
	public int asMinutes() {
		/** Representation of the time as minutes passed this day **/
		return this.hour*60 + this.minute;
	}
//	public SimpleTime timeUntil(SimpleTime time2) {
//		/** Returns the time until time2, as a SimpleTime object **/
//		return null;
//	}

	@Override
	public String toString() {
		return ((hour<10)?"0":"") +hour + ":" + ((minute<10)?"0":"") + minute;
	}
	
}
