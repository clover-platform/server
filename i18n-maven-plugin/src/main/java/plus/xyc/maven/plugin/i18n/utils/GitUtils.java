package plus.xyc.maven.plugin.i18n.utils;

import org.apache.maven.plugin.logging.SystemStreamLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GitUtils {

    public static String getCurrentBranch() {
        SystemStreamLog log = new SystemStreamLog();
        try {
            Process process = Runtime.getRuntime().exec("git branch --show-current");
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String branch = reader.readLine();
            reader.close();
            inputStream.close();
            log.info("current branch: " + branch);
            return branch;
        } catch (IOException e) {
            log.error("get current branch error: " + e.getMessage(), e);
        }
        return null;
    }

}
