package kr.co.igns.framework.api.file.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperty {
    private String uploadDir;
    private long maxSize;
    private boolean allowDelete;
    private String target;
    private Nas nas;

    @Data
    public static class Nas {
        private String ip;
        private String user;
        private String pass;
        private String shareFolder;
        private String foPath;
    }
}
