package creatip.oms.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.springframework.util.ResourceUtils;

public class SharedUtils {

    /**
     * Get the project root path
     *
     * @return
     */
    public static String getResourceBasePath() {
        // Get the directory
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("jewelsys").getPath());
        } catch (FileNotFoundException e) {
            // nothing to do
        }
        if (path == null || !path.exists()) {
            path = new File("");
        }

        String pathStr = path.getAbsolutePath();
        // If it is running in eclipse, it will be in the same level as the target. If the jar is deployed to the server, the default is the same as the jar package.
        // pathStr = pathStr.replace("\\target\\classes", "");

        return pathStr;
    }

    public static String generateFileName(String prefix) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        String fileName = prefix + "_" + sdf.format(new Date());
        return fileName;
    }

    public static boolean isDateStringValid(String dateStr) {
        String format = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // Disable lenient parsing to enforce strict date validation

        try {
            Date date = sdf.parse(dateStr);
            // If parsing succeeds without exceptions, the date is valid
            return true;
        } catch (ParseException e) {
            // Parsing failed; the date is not valid
            return false;
        }
    }
    //For reference
    /*
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());
	log.debug("Zone ID {}",ZoneId.systemDefault());
	log.debug("Current Time: toString {} , with formatter{} ",currentDateTime.toString(),formatter.format(currentDateTime));
	log.debug("End Time:toString {}, with formatter {} ",meetingEndTime.toString(),formatter.format(meetingEndTime));
	
	ZoneId myanmarTimeZone = ZoneId.of("Asia/Yangon");
	DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(myanmarTimeZone);
	log.debug("Myanmar Zone ID {}",myanmarTimeZone);
	log.debug("Current Time: toString {} , with formatter{} ",currentDateTime.toString(),formatter2.format(currentDateTime));
	log.debug("End Time:toString {}, with formatter {} ",meetingEndTime.toString(),formatter2.format(meetingEndTime));
	*/
}
