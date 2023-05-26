package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class RequestPositionDto {
    private Long id;

    private Long requestId;

    private String note;

    private ProductDto product;

    private boolean archive;
}
