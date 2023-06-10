package creatip.oms.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
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
}
