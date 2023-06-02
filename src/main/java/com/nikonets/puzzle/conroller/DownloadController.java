package com.nikonets.puzzle.conroller;

import com.nikonets.puzzle.service.DownloadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class DownloadController {
    private final DownloadService downloadService;

    @GetMapping("/download")
    public void download(HttpServletResponse response,
                         @RequestParam String imageName) {
        downloadService.streamZipToResponse(response, imageName);
    }
}
