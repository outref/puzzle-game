package com.nikonets.puzzle;

import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StorageInitializr {
    @Value("${images.storage.dir}")
    private String imagesDir;
    @Value("${solutions.storage.dir}")
    private String solutionsDir;

    @EventListener(ApplicationReadyEvent.class)
    public void createStorageDirectory() {
        File imagesDirFile = new File(imagesDir);
        if (!imagesDirFile.exists()) {
            imagesDirFile.mkdirs();
        }

        File solutionsDirFile = new File(solutionsDir);
        if (!solutionsDirFile.exists()) {
            solutionsDirFile.mkdirs();
        }
    }
}
