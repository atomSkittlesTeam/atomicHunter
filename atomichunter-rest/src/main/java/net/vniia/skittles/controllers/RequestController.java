package net.vniia.skittles.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.RequestDto;
import net.vniia.skittles.entities.Request;
import net.vniia.skittles.readers.RequestReader;
import net.vniia.skittles.services.RequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    private final RequestReader requestReader;

    @GetMapping("debug")
    public List<RequestDto> helloWorld() {
        return requestReader.getAllRequest(false);
    }

    @GetMapping("all")
    public List<RequestDto> getAllRequests(@RequestParam boolean showArchive) {
        return requestReader.getAllRequest(showArchive);
    }

    @PostMapping("create")
    @Transactional
    public RequestDto createRequest(@RequestBody RequestDto requestDto) {
        return this.requestService.createRequest(requestDto);
    }

    @PutMapping("{id}/update")
    @Transactional
    public RequestDto updateRequest(@PathVariable Long id, @RequestBody RequestDto requestDto) {
        return this.requestService.updateRequest(id, requestDto);
    }

    @DeleteMapping("{id}/archive")
    @Transactional
    public void archiveRequest(@PathVariable Long id) {
        this.requestService.archiveRequest(id);
    }
}
