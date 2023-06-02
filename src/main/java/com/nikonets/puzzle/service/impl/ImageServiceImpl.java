package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.dto.ImageInfoDto;
import com.nikonets.puzzle.service.ImageService;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${images.dir}")
    private String imagesDir;

    @Override
    public List<ImageInfoDto> getImageInfosList() {
        File dir = new File(imagesDir);
        return Arrays.stream(dir.list())
                .map(s -> new ImageInfoDto(s,
                        "/game?imageName=" + s,
                        "/download?imageName=" + s))
                .collect(Collectors.toList());
    }
}
