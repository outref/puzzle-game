package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.exception.ZipDownloadException;
import com.nikonets.puzzle.repository.PuzzleRepository;
import com.nikonets.puzzle.service.DownloadService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@RequiredArgsConstructor
@Service
public class DownloadServiceImpl implements DownloadService {
    private final PuzzleRepository puzzleRepository;

    @Override
    public void streamZipToResponse(HttpServletResponse response, String imageName) {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=download.zip");
        List<String> filesList = puzzleRepository.getPuzzleFilesByImageName(imageName);
        for (String fileName : filesList) {
            try (ZipOutputStream zipOutputStream =
                         new ZipOutputStream(response.getOutputStream())) {
                FileSystemResource fileSystemResource = new FileSystemResource(fileName);
                ZipEntry zipEntry = new ZipEntry(fileSystemResource.getFilename());
                zipEntry.setSize(fileSystemResource.contentLength());
                zipEntry.setTime(System.currentTimeMillis());
                zipOutputStream.putNextEntry(zipEntry);
                StreamUtils.copy(fileSystemResource.getInputStream(), zipOutputStream);
                zipOutputStream.closeEntry();
                zipOutputStream.finish();
            } catch (IOException e) {
                throw new ZipDownloadException("Failed to output file in ZipOutputStream: "
                        + fileName, e);
            }
        }
    }
}
