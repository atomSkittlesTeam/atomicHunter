package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.*;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.readers.PlaceReader;
import net.vniia.skittles.readers.VacancyReader;
import net.vniia.skittles.repositories.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.vniia.skittles.services.ReportService.VACANCY_REPORT_PATH;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceReader placeReader;
    private final PlaceRepository placeRepository;

    @Transactional
    public PlaceDto createPlace(PlaceDto placeDto) {
        Place place = new Place(placeDto);
        place = this.placeRepository.save(place);
        return placeReader.getPlaceById(place.getId());
    }

    @Transactional
    public PlaceDto updatePlace(Long id, PlaceDto placeDto) {
        Place place = this.placeRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Место проведения собеседований не найдено!");
                }
        );
        place.update(placeDto);

        return placeReader.getPlaceById(place.getId());
    }

    @Transactional
    public void archiveVacancy(Long id) {
        Place place = this.placeRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Место проведения собеседований не найдено!");
                }
        );
        place.archive();
    }
}
