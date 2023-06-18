package net.vniia.skittles.controllers;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.services.VacancyService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportController {

    private final VacancyService vacancyService;

    @RequestMapping("vacancy/{path}")
    @ResponseBody
    public HttpEntity<byte[]> getVacancyReportFileByPath(@PathVariable String path) throws IOException {
        return this.vacancyService.getVacancyReportFileByPath(path);
    }
}
