package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.RequestPositionDto;
import net.vniia.skittles.entities.RequestPosition;
import net.vniia.skittles.readers.RequestPositionReader;
import net.vniia.skittles.repositories.RequestPositionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Deprecated
public class RequestPositionService {

    private final RequestPositionReader requestPositionReader;

    private final RequestPositionRepository requestPositionRepository;

    public RequestPositionDto createRequestPosition(RequestPositionDto requestPositionDto) {
        RequestPosition requestPosition = new RequestPosition(requestPositionDto);
        requestPosition = this.requestPositionRepository.save(requestPosition);
        return requestPositionReader.getRequestPositionById(requestPosition.getId());
    }

    public RequestPositionDto updateRequestPosition(Long id, RequestPositionDto requestPositionDto) {
        RequestPosition requestPosition = this.requestPositionRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Позиция заказа не найдена!");
                }
        );
        requestPosition.update(requestPositionDto);
        return requestPositionReader.getRequestPositionById(requestPositionDto.getId());
    }

    @Transactional
    public void archiveRequestPosition(Long id) {
        RequestPosition requestPosition = this.requestPositionRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Позиция заказа не найдена!");
                }
        );
        requestPosition.archive();
    }
}
