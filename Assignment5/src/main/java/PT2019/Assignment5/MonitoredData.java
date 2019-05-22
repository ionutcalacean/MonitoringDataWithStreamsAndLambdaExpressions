package PT2019.Assignment5;

import java.util.Calendar;
import java.util.Date;

public class MonitoredData {
      private Date startTime;
      private Date endTime;
      private String activity;
	
      public MonitoredData(Date startTime, Date endTime, String activity) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.activity = activity;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
	public int getStartDay()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(getStartTime());
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public String toString() {
		return "MonitoredData [startTime=" + startTime + ", endTime=" + endTime + ", activity=" + activity + "]";
	}
      
      
      
}
