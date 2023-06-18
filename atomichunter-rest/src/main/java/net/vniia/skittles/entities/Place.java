package net.vniia.skittles.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // наименование места (например "404 комната")
    private String name;

    private boolean archive;

    public Place(PlaceDto placeDto) {
        this.name = placeDto.getName();
        this.archive = placeDto.isArchive();
    }

    public void update(PlaceDto placeDto) {
        this.name = placeDto.getName();
        this.archive = placeDto.isArchive();
    }

    public void archive() {
        this.archive = true;
    }
}
