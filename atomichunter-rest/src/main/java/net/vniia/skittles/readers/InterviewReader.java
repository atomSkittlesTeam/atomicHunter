package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.EmployeeTimeMapDto;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.dto.PlaceTimeMapDto;
import net.vniia.skittles.entities.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InterviewReader {

    private static final QInterview interview = QInterview.interview;
    private static final QPlaceTimeMap placeTimeMap = QPlaceTimeMap.placeTimeMap;
    private static final QEmployeeTimeMap employeeTimeMap = QEmployeeTimeMap.employeeTimeMap;
    private static final QPlace place = QPlace.place;
    private static final QEmployee employee = QEmployee.employee;

    private final JPAQueryFactory queryFactory;

    private final EmployeeReader employeeReader;

    public static QBean<InterviewDto> getMappedSelectForInterviewDto() {
        return Projections.bean(
                InterviewDto.class,
                interview.id,
                interview.placeId,
                interview.dateEnd,
                interview.dateStart
        );
    }

    public static QBean<PlaceTimeMapDto> getMappedSelectForPlaceTimeMapDto() {
        return Projections.bean(
                PlaceTimeMapDto.class,
                placeTimeMap.id,
                placeTimeMap.placeId,
                placeTimeMap.dateEnd,
                placeTimeMap.dateStart
        );
    }

    public static QBean<EmployeeTimeMapDto> getMappedSelectForEmployeeTimeMapDto() {
        return Projections.bean(
                EmployeeTimeMapDto.class,
                employeeTimeMap.id,
                employeeTimeMap.employeeId,
                employeeTimeMap.dateEnd,
                employeeTimeMap.dateStart
        );
    }

    public InterviewDto getInterviewById(Long interviewId) {
        InterviewDto interviewDto = interViewQuery()
                .where(interview.id.eq(interviewId))
                .fetchFirst();

        List<EmployeeDto> interviewEmployees =
                this.employeeReader.getInterviewEmployees(interviewDto.getId());
        interviewDto.setEmployees(interviewEmployees);

        return interviewDto;
    }

    private JPAQuery<InterviewDto> interViewQuery() {
        return queryFactory.from(interview)
                .select(getMappedSelectForInterviewDto());
    }

    public List<PlaceTimeMapDto> getAllPlaceTimeMapById(Long placeId, Long interviewId) {
        if (interviewId != null) {
            return queryFactory.from(placeTimeMap)
                    .where(placeTimeMap.placeId.in(placeId)
                            .and(placeTimeMap.interviewId.notIn(interviewId)))
                    .select(getMappedSelectForPlaceTimeMapDto()).fetch();
        } else {
            return queryFactory.from(placeTimeMap)
                    .where(placeTimeMap.placeId.in(placeId))
                    .select(getMappedSelectForPlaceTimeMapDto()).fetch();
        }
    }

    public List<EmployeeTimeMapDto> getAllEmployeeTimeMapById(UUID employeeId, Long interviewId) {
        if (interviewId != null) {
            return queryFactory.from(employeeTimeMap)
                    .where(employeeTimeMap.employeeId.in(employeeId)
                            .and(employeeTimeMap.interviewId.notIn(interviewId)))
                    .select(getMappedSelectForEmployeeTimeMapDto()).fetch();
        } else {
            return queryFactory.from(employeeTimeMap)
                    .where(employeeTimeMap.employeeId.in(employeeId))
                    .select(getMappedSelectForEmployeeTimeMapDto()).fetch();
        }
    }
}
