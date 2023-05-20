package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.ProductDto;
import net.vniia.skittles.dto.RequestPositionDto;

import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long requestId;

    private String note;

    private Long productId;

    private boolean archive;

    public RequestPosition(RequestPositionDto requestPositionDto) {
        this.update(requestPositionDto);
    }

    public void update(RequestPositionDto requestPositionDto) {
        this.requestId = requestPositionDto.getRequestId();
        this.note = requestPositionDto.getNote();
        this.productId = Optional.ofNullable(requestPositionDto.getProduct())
                .map(ProductDto::getId).orElse(null);
    }

    public void archive() {
        this.archive = true;
    }
}
