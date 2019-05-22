package PT2019.Assignment5;
/**
 * Hello world!
 *
 */
public class App 
{
	
    public static void main( String[] args )
    {
      DataManipulation.readData();
      System.out.println("Number of days of monitored data:"+DataManipulation.countDiffDays());
      DataManipulation.apparitions();
      DataManipulation.apparitionPerDay();
      DataManipulation.duration();
      DataManipulation.entireDuration();
      DataManipulation.lessDuration();
    }
}
