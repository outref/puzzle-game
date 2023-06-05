package com.nikonets.puzzle.service.impl;

import com.nikonets.puzzle.repository.PuzzleRepository;
import com.nikonets.puzzle.service.PuzzleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PuzzleServiceImpl implements PuzzleService {
    private final PuzzleRepository puzzleRepository;

    @Override
    public List<String> getAllPuzzles() {
        return puzzleRepository.getAllPuzzles();
    }
}
