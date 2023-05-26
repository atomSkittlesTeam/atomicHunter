package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.RequestDto;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String number;
    private Date requestDate;
    private String description;
    private Date releaseDate;
    private Long priority;
    private boolean archive;

    public Request(RequestDto requestDto) {
        this.update(requestDto);
    }

    public void update(RequestDto requestDto) {
        this.number = requestDto.getNumber();
        this.requestDate = requestDto.getRequestDate();
        this.description = requestDto.getDescription();
        this.releaseDate = requestDto.getReleaseDate();
        this.priority = requestDto.getPriority();
    }

    public void archive() {
        this.archive = true;
    }
}
