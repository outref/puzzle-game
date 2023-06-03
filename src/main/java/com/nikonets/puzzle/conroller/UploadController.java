package com.nikonets.puzzle.conroller;

import com.nikonets.puzzle.service.UploadService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/upload")
public class UploadController {
    private final UploadService uploadService;

    @GetMapping
    public String displayUploadPage() {
        return "upload";
    }

    @PostMapping
    public String uploadImage(Model model,
                              @RequestParam String imageName,
                              @RequestParam Integer sideLength,
                              @RequestParam("image") MultipartFile image) throws IOException {
        String imageUrl = uploadService.uploadImage(imageName, sideLength, image);
        model.addAttribute("message", "Image uploaded successfully!"
                + " Click 'Home' to see all images...");
        model.addAttribute("image", imageUrl);
        return "upload";
    }
}
