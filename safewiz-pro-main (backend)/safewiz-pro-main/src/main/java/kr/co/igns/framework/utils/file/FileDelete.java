package kr.co.igns.framework.utils.file;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileDelete extends Thread {
    private String pathName = "";
    private File f = null;
    private static final Logger log = LogManager.getLogger("com.project");

    public FileDelete(String path) {
        pathName = path;
    }

    public FileDelete(File file) {
        f = file;
    }

    public synchronized void run() {
        try {
            if (f == null) {
                f = new File(pathName);
            }
            if (f.exists()) {
                if (f.delete()) {
                    log.info("file delete success");
                } else {
                    log.info("file delete fail");
                }
            } else {
                log.info("file not found");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
