package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.PlaceDto;
import net.vniia.skittles.readers.PlaceReader;
import net.vniia.skittles.services.PlaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;
    private final PlaceReader placeReader;

    @GetMapping("all")
    public List<PlaceDto> getAllPlaces(@RequestParam boolean showArchive) {
        return placeReader.getAllPlaces(showArchive);
    }

    @GetMapping("{placeId}")
    @Transactional
    public PlaceDto getPlaceById(@PathVariable Long placeId) {
        return placeReader.getPlaceById(placeId);
    }

    @PostMapping("create")
    @Transactional
    public PlaceDto createPlace(@RequestBody PlaceDto placeDto) {
        return this.placeService.createPlace(placeDto);
    }

    @PutMapping("{id}/update")
    @Transactional
    public PlaceDto updatePlace(@PathVariable Long id, @RequestBody PlaceDto placeDto) {
        return this.placeService.updatePlace(id, placeDto);
    }

    @DeleteMapping("{id}/archive")
    @Transactional
    public void archivePlace(@PathVariable Long id) {
        this.placeService.archiveVacancy(id);
    }
}
