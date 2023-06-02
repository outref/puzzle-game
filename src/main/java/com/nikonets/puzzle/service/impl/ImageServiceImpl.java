package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.repository.ImageRepository;
import com.nikonets.puzzle.service.ImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public List<String> getImagesList() {
        return imageRepository.getAllImages();
    }
}
