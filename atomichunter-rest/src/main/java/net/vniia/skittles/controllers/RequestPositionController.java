package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.RequestPositionDto;
import net.vniia.skittles.readers.RequestPositionReader;
import net.vniia.skittles.services.RequestPositionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("requests")
@RequiredArgsConstructor
public class RequestPositionController {

    private final RequestPositionService requestPositionService;

    private final RequestPositionReader requestPositionReader;

    @GetMapping("{requestId}/positions/all")
    public List<RequestPositionDto> getAllRequestPositions(@PathVariable Long requestId, @RequestParam boolean showArchive) {
        return requestPositionReader.getRequestPositionsByRequestId(requestId, showArchive);
    }

    @PostMapping("positions/create")
    @Transactional
    public RequestPositionDto createRequestPosition(@RequestBody RequestPositionDto requestPositionDto) {
        return this.requestPositionService.createRequestPosition(requestPositionDto);
    }

    @PutMapping("positions/{id}/update")
    @Transactional
    public RequestPositionDto updateRequestPosition(@PathVariable Long id, @RequestBody RequestPositionDto requestPositionDto) {
        return this.requestPositionService.updateRequestPosition(id, requestPositionDto);
    }

    @DeleteMapping("positions/{id}/archive")
    @Transactional
    public void archiveRequestPosition(@PathVariable Long id) {
        this.requestPositionService.archiveRequestPosition(id);
    }
}
