package com.nikonets.puzzle.conroller;

import com.nikonets.puzzle.dto.ImageInfoDto;
import com.nikonets.puzzle.service.ImageService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final ImageService imageService;

    @GetMapping("/home")
    public String displayHomePage(Model model) {
        List<ImageInfoDto> imageInfosList = imageService.getImagesList().stream()
                .map(s -> new ImageInfoDto(s,
                "/game?imageName=" + s,
                "/download?imageName=" + s))
                .collect(Collectors.toList());
        model.addAttribute("imageInfosList", imageInfosList);
        return "home";
    }
}
