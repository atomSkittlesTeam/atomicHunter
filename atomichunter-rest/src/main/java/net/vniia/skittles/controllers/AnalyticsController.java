package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.RequestDto;
import net.vniia.skittles.dto.RequestPositionDto;
import net.vniia.skittles.entities.Request;
import net.vniia.skittles.readers.RequestReader;
import net.vniia.skittles.services.AnalyticsService;
import net.vniia.skittles.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/request-position-count-by-request-number")
    public Map<String, Long> getPositionCountByNumber() {
        return analyticsService.getPositionCountByNumber();
    }

    @GetMapping("/request-position-count-by-product-id")
    public Map<String, Integer> getPositionCountByProduct() {
        return analyticsService.getPositionCountByProduct();
    }

}
