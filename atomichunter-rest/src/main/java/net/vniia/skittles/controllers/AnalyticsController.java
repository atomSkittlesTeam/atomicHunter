package net.vniia.skittles.controllers;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
