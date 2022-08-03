package com.example.demo;

import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.servlet.api.DeploymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * File upload path protection and recovery
 * Create temp file from MultiPartUploadHandler in {@link MultiPartParserDefinition} at multipart-request
 */
@Slf4j
@Component
public class MultipartCustomizer implements UndertowDeploymentInfoCustomizer {

    private final MultipartConfigElement multipartConfigElement;

    public MultipartCustomizer(MultipartConfigElement multipartConfigElement) {
        this.multipartConfigElement = multipartConfigElement;
    }

    private Path tempPath = Paths.get(System.getProperty("java.io.tmpdir"));
    private boolean useSystemTempDir = true;

    @Override
    public void customize(DeploymentInfo deploymentInfo) {
        String location = multipartConfigElement.getLocation();
        if (location != null && !"".equals(location)) {
            tempPath = Paths.get(location);
            useSystemTempDir = false;
        } else {
            tempPath = deploymentInfo.getTempPath();
        }
    }

    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS)
    public void createTempFile() throws IOException {
        if (useSystemTempDir) {
            log.info("Current temp dir: {}", tempPath);
            if (!Files.exists(tempPath)) {
                Files.createDirectory(tempPath);
                log.warn("Restore the file upload path because it was deleted.");
            } else {
                // NOTE: Prevent the system from deleting file upload paths in temporary directories when multipart uploads are not requested for a period of time
                Path tempFile = Files.createTempFile(tempPath.normalize(), "undertow", "upload");
                Files.delete(tempFile);
            }
        }
    }
}
