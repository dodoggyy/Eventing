package edu.ntust.embedded;

public class WorkerJob {
	private String name;
	private String job_description;

	public WorkerJob(String name, String job_description) {
		super();
		this.name = name;
		this.job_description = job_description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob_description() {
		return job_description;
	}

	public void setJob_description(String job_description) {
		this.job_description = job_description;
	}

	@Override
	public String toString() {
		return name + ": " + job_description;
	}

}
