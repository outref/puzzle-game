package com.nikonets.puzzle.service.solver.impl;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.repository.ImageRepository;
import com.nikonets.puzzle.service.solver.InputTilesProcessingService;
import com.nikonets.puzzle.service.solver.PuzzleSolverService;
import com.nikonets.puzzle.service.solver.SolutionDrawingService;
import com.nikonets.puzzle.service.solver.TilesCompatabilityService;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class PuzzleSolverServiceImpl implements PuzzleSolverService {
    private final ImageRepository repository;
    private final InputTilesProcessingService inputProcessingService;
    private final TilesCompatabilityService tilesCompatabilityService;
    private final SolutionDrawingService solutionDrawingService;

    @Override
    public String solvePuzzle(MultipartFile[] files) {
        List<SolverTile> tilesList = inputProcessingService.createSolverTilesFromInput(files);
        tilesCompatabilityService.setAllTilesCompatability(tilesList);
        SolverTile[][] table = new SolverTile[tilesList.size() * 2][tilesList.size() * 2];
        placeInitialTwoTilesOnTable(tilesList, table);
        for (int i = 0; i < tilesList.size() - 2; i++) {
            placeNextTileOnTable(tilesList, table);
        }
        BufferedImage solutionImage = solutionDrawingService.drawSolutionFromTable(table);
        return repository.saveSolution(solutionImage);
    }

    private void placeNextTileOnTable(List<SolverTile> tilesList, SolverTile[][] table) {
        SolverTile.Edge edge1 = tilesList.stream()
                .filter(SolverTile::isPlaced)
                .flatMap(t -> t.getEdges().stream())
                .filter(SolverTile.Edge::isAvailable)
                .max(Comparator.comparingDouble(edge ->
                        edge.getCompatabilityList().entrySet().stream()
                        .filter(e -> !e.getKey().getTile().isPlaced())
                        .map(Map.Entry::getValue)
                        .max(Double::compareTo)
                        .get()))
                .get();
        SolverTile.Edge edge2 = edge1.getCompatabilityList().entrySet().stream()
                .filter(e -> !e.getKey().getTile().isPlaced())
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .get()
                .getKey();

        placeAdjacentOnTable(table, edge1, edge2);
    }

    private void placeInitialTwoTilesOnTable(List<SolverTile> tilesList, SolverTile[][] table) {
        SolverTile.Edge edge1 = tilesList.stream()
                .flatMap(t -> t.getEdges().stream())
                .max(Comparator.comparingDouble(edge ->
                        edge.getCompatabilityList().values().stream()
                        .max(Double::compareTo)
                        .get()))
                .get();
        SolverTile tile1 = edge1.getTile();
        table[table.length / 2][table.length / 2] = tile1;
        tile1.setTableX(table.length / 2);
        tile1.setTableY(table.length / 2);
        tile1.setPlaced(true);
        SolverTile.Edge edge2 = edge1.getCompatabilityList().entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .get()
                .getKey();
        placeAdjacentOnTable(table, edge1, edge2);
    }

    private void placeAdjacentOnTable(SolverTile[][] table,
                                      SolverTile.Edge edgePlaced,
                                      SolverTile.Edge edgeNew) {
        edgePlaced.setAvailable(false);
        edgeNew.setAvailable(false);
        SolverTile tilePlaced = edgePlaced.getTile();
        SolverTile tileNew = edgeNew.getTile();
        tileNew.setPlaced(true);
        switch (edgeNew.getSide()) {
            case TOP -> {
                tileNew.setTableX(tilePlaced.getTableX());
                tileNew.setTableY(tilePlaced.getTableY() + 1);
            }
            case BOTTOM -> {
                tileNew.setTableX(tilePlaced.getTableX());
                tileNew.setTableY(tilePlaced.getTableY() - 1);
            }
            case RIGHT -> {
                tileNew.setTableX(tilePlaced.getTableX() - 1);
                tileNew.setTableY(tilePlaced.getTableY());
            }
            case LEFT -> {
                tileNew.setTableX(tilePlaced.getTableX() + 1);
                tileNew.setTableY(tilePlaced.getTableY());
            }
            default -> { }
        }
        closeAdjacentEdges(table, tileNew.getTableX(), tileNew.getTableY());
        table[tileNew.getTableY()][tileNew.getTableX()] = tileNew;
    }

    private void closeAdjacentEdges(SolverTile[][] table, int x, int y) {
        if (table[y - 1][x] != null) {
            table[y - 1][x].getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.BOTTOM)
                    .findFirst()
                    .get()
                    .setAvailable(false);
        }
        if (table[y + 1][x] != null) {
            table[y + 1][x].getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.TOP)
                    .findFirst()
                    .get()
                    .setAvailable(false);
        }
        if (table[y][x - 1] != null) {
            table[y][x - 1].getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.RIGHT)
                    .findFirst()
                    .get()
                    .setAvailable(false);
        }
        if (table[y][x + 1] != null) {
            table[y][x + 1].getEdges().stream()
                    .filter(e -> e.getSide() == SolverTile.Edge.Side.LEFT)
                    .findFirst()
                    .get()
                    .setAvailable(false);
        }
    }
}









