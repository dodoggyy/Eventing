package edu.ntust.embedded;


public class ScheduleItem {
	
	private String name;
	private SimpleTime startTime;
	private SimpleTime endTime;
	private String location;
	private String description;
	
	private Boolean important;
	private Boolean finished;
	
	
	public ScheduleItem() {
		super();
		this.startTime=null;
		this.endTime=null;
	}
	public ScheduleItem(String name, SimpleTime startTime, SimpleTime endTime,
			String location, String description) {
		super();
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.location = location;
		this.description = description;
		this.important = false;
		this.finished = false;
	}
	public ScheduleItem(String name, SimpleTime startTime, SimpleTime endTime,
			String location, String description, Boolean important,
			Boolean finished) {
		super();
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.location = location;
		this.description = description;
		this.important = important;
		this.finished = finished;
	}
	public Boolean getImportant() {
		return important;
	}
	public void setImportant(Boolean important) {
		this.important = important;
	}
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public SimpleTime getStartTime() {
		return startTime;
	}
	public void setStartTime(SimpleTime startTime) {
		this.startTime = startTime;
	}
	public SimpleTime getEndTime() {
		return endTime;
	}
	public void setEndTime(SimpleTime endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "ScheduleItem [name=" + name + ", startTime=" + startTime
				+ ", endTime=" + endTime + "]";
	}
	
	
}
