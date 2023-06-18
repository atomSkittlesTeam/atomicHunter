package net.vniia.skittles.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long interviewId;
    private UUID employeeId;

    public InterviewEmployee(Long interviewId, UUID employeeId) {
        this.interviewId = interviewId;
        this.employeeId = employeeId;
    }
}
