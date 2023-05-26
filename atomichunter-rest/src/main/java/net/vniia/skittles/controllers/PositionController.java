package net.vniia.skittles.controllers;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.readers.PositionReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionReader positionReader;

    @GetMapping("all")
    public List<PositionDto> getAllPositions() {
        return positionReader.getAllPositions();
    }

}
