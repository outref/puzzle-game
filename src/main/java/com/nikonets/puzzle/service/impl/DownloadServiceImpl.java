package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.service.DownloadService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
public class DownloadServiceImpl implements DownloadService {
    @Value("${images.dir}")
    private String imagesDir;

    @Override
    public void streamZipToResponse(HttpServletResponse response, String imageName) {
        File dir = new File(imagesDir + imageName);
        List<String> imagesList = new ArrayList<>();
        for (int i = 0; i < dir.list().length; i++) {
            imagesList.add(imagesDir + imageName + "/" + i + ".jpg");
        }

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=download.zip");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            for (String fileName : imagesList) {
                FileSystemResource fileSystemResource = new FileSystemResource(fileName);
                ZipEntry zipEntry = new ZipEntry(fileSystemResource.getFilename());
                zipEntry.setSize(fileSystemResource.contentLength());
                zipEntry.setTime(System.currentTimeMillis());

                zipOutputStream.putNextEntry(zipEntry);

                StreamUtils.copy(fileSystemResource.getInputStream(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
