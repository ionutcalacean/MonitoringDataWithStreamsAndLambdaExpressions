package PT2019.Assignment5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataManipulation {
	private static ArrayList<MonitoredData> activities=new ArrayList<MonitoredData>();
	private static ArrayList<Integer> days=new ArrayList<Integer>();
	public static void readData() {
		  
	        List<String> activitiesList=new ArrayList<String>();
	        String filename="Activities.txt";
	        Stream<String> stream;
			try {
				stream = Files.lines(Paths.get(filename));
				activitiesList=stream.collect(Collectors.toList());
			    
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	        
			for(String s:activitiesList) {
				//System.out.println(s);
				ArrayList<String> row=new ArrayList<String>();
				Pattern p=Pattern.compile("(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})");
				Matcher m=p.matcher(s);
				Date date1=new Date();
				Date date2=new Date();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				while(m.find())
				{
					row.add(m.group(1));
					
				}
				try {
					date1=df.parse(row.get(0));
					date2=df.parse(row.get(1));
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
				
				String activity=s.replaceAll("[^A-Za-z]","");
				//System.out.println(date1+"-"+date2+""+activity);
				MonitoredData readedAct=new MonitoredData(date1,date2,activity);
				activities.add(readedAct);
				row.clear();
			}
			for(MonitoredData md:activities)
				System.out.println(md);
	}
	
	public static int countDiffDays()
	{
		
		List<Integer> days=new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		activities.stream().forEach(d->{cal.setTime(d.getStartTime());int searchedDay=cal.get(Calendar.DAY_OF_MONTH);if(!days.contains(searchedDay)) days.add(searchedDay);});
		
		
		return days.size();
	}
	
	public static Map<String, Integer> apparitions(){
		Map<String, Integer> apparitions=new HashMap<String,Integer>();
		
		Map<Object, Long> apparitionsMap =new HashMap<Object, Long>();
		apparitionsMap=activities.stream().collect(Collectors.groupingBy(x -> x.getActivity(), Collectors.counting()));
		
		for (Map.Entry< Object,Long> entry : apparitionsMap.entrySet())
		{
			apparitions.put(entry.getKey().toString(), Math.toIntExact(entry.getValue()));
		}
		
		//System.out.println(apparitions);
		PrintWriter writer;
		try {
			writer = new PrintWriter("ActionsOccurrences.txt", "UTF-8");
			writer.print(apparitions);
			writer.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
	
			e.printStackTrace();
		}
		
		return apparitions;
	}
	
	public static void apparitionPerDay()
	{
		
		
		/*Map<String,Integer> daysAndActivities=new HashMap<String,Integer>();
		Calendar cal = Calendar.getInstance();
		for(Integer currDay:days)
		{
		   System.out.println("Date:"+currDay+" activities:");
		  System.out.println(activities.stream().filter(d->{ cal.setTime(d.getStartTime()); return cal.get(Calendar.DAY_OF_MONTH)==currDay;}).collect(Collectors.groupingBy(p->p.getActivity(),Collectors.counting())));
		}*/
		Map<Object, Map<Object, Long>> daysAndActivities=new HashMap<Object,Map<Object,Long>>();
		daysAndActivities=activities.stream().collect(Collectors.groupingBy(d->d.getStartDay(),Collectors.groupingBy(d-> d.getActivity(),Collectors.counting())));
		
		Map<Integer,Map<String,Integer>> daysActivities=new HashMap<Integer,Map<String,Integer>>();
		for (Map.Entry<Object, Map<Object, Long>> entry : daysAndActivities.entrySet())
		{
			Map<String, Integer> currApparitions=new HashMap<String,Integer>();
			for (Map.Entry< Object,Long> entry1 : entry.getValue().entrySet())
			{
				currApparitions.put(entry1.getKey().toString(), Math.toIntExact(entry1.getValue()));
			}
			daysActivities.put(Math.toIntExact(Long.parseLong(entry.getKey().toString())), currApparitions);
			
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter("ActionsOccurrencesPerDays.txt", "UTF-8");
			for (Map.Entry<Integer, Map<String, Integer>> entry : daysActivities.entrySet())
			{
				writer.print("DayOfMonth:");
				writer.print(entry.getKey()+" "+entry.getValue()+"\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
	
			e.printStackTrace();
		}
	}
	
	public static void duration()
	{ 
		
		Map<String,Long> duration= new HashMap<String,Long>();
		activities.stream().forEach(d-> { long diffMS=d.getEndTime().getTime()-d.getStartTime().getTime();String key=Integer.toString(activities.indexOf(d))+" "+d.getActivity();duration.put(key,  TimeUnit.SECONDS.convert(diffMS, TimeUnit.MILLISECONDS));});

       for(int i=0;i<activities.size();i++)
       {
    	   System.out.println("Line: "+i+" Activity: "+activities.get(i).getActivity()+" Duration in sec:"+duration.get(Integer.toString(i)+" "+activities.get(i).getActivity()));
       }
     
	}
	
	public static void entireDuration()
	{
		Map<String,Long> entireDuration= new HashMap<String,Long>();
	    activities.stream().forEach(d->{long diffMS=d.getEndTime().getTime()-d.getStartTime().getTime();if(entireDuration.get(d.getActivity())!=null ) entireDuration.put(d.getActivity(),entireDuration.get(d.getActivity())+TimeUnit.HOURS.convert(diffMS, TimeUnit.MILLISECONDS));else entireDuration.put(d.getActivity(),TimeUnit.HOURS.convert(diffMS, TimeUnit.MILLISECONDS));});
	    System.out.println(entireDuration);
	    Map<String,Long> entireDuration10= new HashMap<String,Long>();
	    entireDuration10=entireDuration.entrySet().stream().filter(d->d.getValue()>10).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));;
	    System.out.println(entireDuration10);
	    PrintWriter writer;
		try {
			writer = new PrintWriter("ActivitiesDuration10.txt", "UTF-8");
			writer.print("Activities duration greater than 10 hours:");
			writer.println(entireDuration10);
			writer.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
	
			e.printStackTrace();
		}
	}

	public static void lessDuration()
	{
	    Map<String,Long> lessThan5=new HashMap<String,Long>();
	    activities.stream().forEach(d->{long diffMS=d.getEndTime().getTime()-d.getStartTime().getTime();long sec=TimeUnit.SECONDS.convert(diffMS, TimeUnit.MILLISECONDS);long min=TimeUnit.MINUTES.convert(diffMS, TimeUnit.MILLISECONDS);if(lessThan5.get(d.getActivity())!=null && min<5) lessThan5.put(d.getActivity(), lessThan5.get(d.getActivity())+sec); else if(min<5) lessThan5.put(d.getActivity(),sec); });
		System.out.println(lessThan5);
		Map<String,Long> entireDuration= new HashMap<String,Long>();
	    activities.stream().forEach(d->{long diffMS=d.getEndTime().getTime()-d.getStartTime().getTime();if(entireDuration.get(d.getActivity())!=null ) entireDuration.put(d.getActivity(),entireDuration.get(d.getActivity())+TimeUnit.SECONDS.convert(diffMS, TimeUnit.MILLISECONDS));else entireDuration.put(d.getActivity(),TimeUnit.SECONDS.convert(diffMS, TimeUnit.MILLISECONDS));});
	  
	    List<String> lessThan5Activities=new ArrayList<String>();
	    System.out.println("Activities that have 90% of the monitoring records with duration less than 5 minutes:");
	    Iterator it=entireDuration.entrySet().iterator();
	    while(it.hasNext())
	    {
	    	Map.Entry<String, Long> pair=(Map.Entry<String, Long>)it.next();
	    	if(lessThan5.get(pair.getKey())!= null) {
	    	    float raport=((float)lessThan5.get(pair.getKey())/pair.getValue())*100;
	    	    if(raport>90)
	    		lessThan5Activities.add(pair.getKey());
	    	}
	    }
	    PrintWriter writer;
	 		try {
	 			writer = new PrintWriter("AverageLess5.txt", "UTF-8");
	 			writer.println("activities that have 90% of the monitoring samples with duration less than 5 minutes:");
	 			writer.println(lessThan5Activities);
	 			writer.close();
	 		} catch (FileNotFoundException e) {

	 			e.printStackTrace();
	 		} catch (UnsupportedEncodingException e) {
	 	
	 			e.printStackTrace();
	 		}
	 	
	}
}
