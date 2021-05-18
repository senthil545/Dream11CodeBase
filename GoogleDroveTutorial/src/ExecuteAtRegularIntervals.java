
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecuteAtRegularIntervals {

	static long mins = 1;
	static String currResult = "";

	public static void main(String args[]) {

		Date dateobj = new Date();
		final DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		System.out.println("ScheduledExecutorService started : " + formatter.format(dateobj));

		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					String prevResult = currResult;
					currResult = DreamXi.executeDream11();
					if (!prevResult.equals(currResult)) {
						System.out.println("Points Updated @ " + formatter.format(new Date()));
						Process p = Runtime.getRuntime().exec("C:/Users/bommu/Desktop/IPL21Dream11/TEst/trigger.bat");
						p.waitFor();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0, mins, TimeUnit.MINUTES);
	}
}
