package com.nikonets.puzzle.service.solver.impl;

import com.nikonets.puzzle.model.SolverTile;
import com.nikonets.puzzle.model.TileEdge;
import com.nikonets.puzzle.repository.PuzzleRepository;
import com.nikonets.puzzle.service.FileToImageService;
import com.nikonets.puzzle.service.solver.PuzzleSolverService;
import com.nikonets.puzzle.service.solver.SolutionDrawingService;
import com.nikonets.puzzle.service.solver.SolverTileService;
import com.nikonets.puzzle.service.solver.TilesCompatabilityService;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class PuzzleSolverServiceImpl implements PuzzleSolverService {
    private final PuzzleRepository repository;
    private final SolverTileService solverTileService;
    private final TilesCompatabilityService tilesCompatabilityService;
    private final SolutionDrawingService solutionDrawer;
    private final FileToImageService fileToImageReader;

    @Override
    public String solvePuzzle(MultipartFile[] files) {
        List<SolverTile> tilesList = Arrays.stream(files)
                .map(fileToImageReader::fileToImage)
                .map(solverTileService::createSolverTile)
                .collect(Collectors.toList());
        tilesCompatabilityService.setTilesCompatabilityBySides(tilesList,
                TileEdge.Side.TOP, TileEdge.Side.BOTTOM);
        tilesCompatabilityService.setTilesCompatabilityBySides(tilesList,
                TileEdge.Side.RIGHT, TileEdge.Side.LEFT);
        //create table(canvas) for solution
        SolverTile[][] solutionTable = new SolverTile[tilesList.size() * 2][tilesList.size() * 2];
        placeInitialTileOnTable(tilesList, solutionTable);
        for (int i = 0; i < tilesList.size() - 1; i++) {
            placeMostCompatibleOnTable(tilesList, solutionTable);
        }
        BufferedImage solutionImage = solutionDrawer.drawSolutionFromTable(solutionTable);
        return repository.saveSolution(solutionImage);
    }

    //putting tile with the best compatability in the center of the table
    private void placeInitialTileOnTable(List<SolverTile> tilesList, SolverTile[][] table) {
        TileEdge tileEdge = tilesList.stream()
                .flatMap(t -> t.getTileEdges().stream())
                .max(Comparator.comparingDouble(e ->
                        e.getCompatabilityList().values().stream()
                                .max(Double::compareTo)
                                .get()))
                .get();
        SolverTile tile1 = tileEdge.getTile();
        table[table.length / 2][table.length / 2] = tile1;
        tile1.setTableX(table.length / 2);
        tile1.setTableY(table.length / 2);
        tile1.setPlaced(true);
    }

    private void placeMostCompatibleOnTable(List<SolverTile> tilesList, SolverTile[][] table) {
        //find tile placed on table that has the highest compatability with unplaced tile
        TileEdge tileEdge1 = tilesList.stream()
                .filter(SolverTile::isPlaced)
                .flatMap(t -> t.getTileEdges().stream())
                .filter(TileEdge::isAvailable)
                .max(Comparator.comparingDouble(edge ->
                        edge.getCompatabilityList().entrySet().stream()
                                .filter(e -> !e.getKey().getTile().isPlaced())
                                .map(Map.Entry::getValue)
                                .max(Double::compareTo)
                                .get()))
                .get();
        //find opposite edge
        TileEdge tileEdge2 = tileEdge1.getCompatabilityList().entrySet().stream()
                .filter(e -> !e.getKey().getTile().isPlaced())
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .get()
                .getKey();

        placeTileOnTable(table, tileEdge1, tileEdge2);
    }

    private void placeTileOnTable(SolverTile[][] table,
                                  TileEdge tileEdgePlaced,
                                  TileEdge tileEdgeNew) {
        tileEdgePlaced.setAvailable(false);
        tileEdgeNew.setAvailable(false);
        SolverTile tilePlaced = tileEdgePlaced.getTile();
        SolverTile tileNew = tileEdgeNew.getTile();
        tileNew.setPlaced(true);
        switch (tileEdgeNew.getSide()) {
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
            default -> {
            }
        }
        table[tileNew.getTableY()][tileNew.getTableX()] = tileNew;
        closeAdjacentEdges(table, tileNew.getTableX(), tileNew.getTableY());
    }

    //make common edges of given tile and all neighbouring tiles unavailable(used)
    private void closeAdjacentEdges(SolverTile[][] table, int x, int y) {
        if (table[y - 1][x] != null) {
            table[y][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.TOP)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
            table[y - 1][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.BOTTOM)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
        }
        if (table[y + 1][x] != null) {
            table[y][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.BOTTOM)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
            table[y + 1][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.TOP)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
        }
        if (table[y][x - 1] != null) {
            table[y][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.LEFT)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
            table[y][x - 1].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.RIGHT)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
        }
        if (table[y][x + 1] != null) {
            table[y][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.RIGHT)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
            table[y][x + 1].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.LEFT)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
        }
    }
}
