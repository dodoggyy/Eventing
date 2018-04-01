package edu.ntust.embedded;

public class EventWorker {
	private String name;
	private String job;
	private String num; // phone number
	public EventWorker(String name, String job, String num) {
		super();
		this.name = name;
		this.job = job;
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return name + "\n"+ "job title: " + job;
	}
	
	
}
