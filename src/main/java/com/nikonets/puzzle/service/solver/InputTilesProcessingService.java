package com.nikonets.puzzle.service.solver;

import com.nikonets.puzzle.model.SolverTile;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface InputTilesProcessingService {
    List<SolverTile> createSolverTilesFromInput(MultipartFile[] files);
}
