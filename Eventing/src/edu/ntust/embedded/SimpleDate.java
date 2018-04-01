package edu.ntust.embedded;

public class SimpleDate implements Comparable<SimpleDate> {
	private int year; // e.g. 2012
	private int month; //0-11
	private int day; //0-31
	
	
	public SimpleDate(String rep) {
		/** construct SimpleDate object from a string **/
		this.year = Integer.parseInt(rep.substring(0, 4));
		this.month = Integer.parseInt(rep.substring(4,6));
		this.day = Integer.parseInt(rep.substring(6,8));
	}
	
	public SimpleDate(long rep) {
		/** construct SimpleDate object from a long **/
		/*this.year = (int) rep/10000;
		this.month = (int) ((rep/100)%100);
		this.day = (int) (rep%100);*/
		this(""+rep);
	}
	
	public SimpleDate(int year, int month, int day) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	private String padDigit(int x) {
		return (x<10)?"0"+x:""+x;
	}
	
	public String asString() {
		/** pack into a string representation for storage **/
		return year + padDigit(month)+padDigit(day);
		
	}
	public long asLong() {
		/** pack into a long type for storage**/
		/** year, month, day**/
		/** 0000 - 00 - 00 **/
		//return (long) day + (long) month*100 + (long) year * 10000;
		return Long.parseLong(asString());
	}
	
	@Override
	public String toString() {
		return year + "/" + (month+1) + "/" + day;
	}

	public int compareTo(SimpleDate date2) {
		if (year < date2.getYear())
			return -1;
		if (year > date2.getYear())
			return 1;
		
		if (month < date2.getMonth())
			return -1;
		if (month > date2.getMonth())
			return 1;
		
		return day - date2.getDay();
		
	}
	
	public int daysUntil(SimpleDate date2) {
		/*Returns number of days until date2*/
		/* Leap years not accounted for yet*/
		//TODO
		return 0;
	}

//	public static void main(String args[]) {
//		System.out.println(new SimpleDate(20120301));
//		System.out.println(new SimpleDate(20120301).asLong());
//		System.out.println(new SimpleDate(20111010));
//		System.out.println(new SimpleDate(20111010).asLong());
//	}
}
