package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.*;
import net.vniia.skittles.entities.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
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

    private static final QVacancyRespond vacancyRespond = QVacancyRespond.vacancyRespond;

    private static final QPosition position = QPosition.position;

    private static final QVacancy vacancy = QVacancy.vacancy;
    public static final QStaffUnit staffUnit = QStaffUnit.staffUnit;

    public static QBean<InterviewDto> getMappedSelectForInterviewDto() {
        return Projections.bean(
                InterviewDto.class,
                interview.id,
                interview.placeId,
                interview.dateEnd,
                interview.dateStart
        );
    }

    private final JPAQueryFactory queryFactory;

    private final EmployeeReader employeeReader;

    public static QBean<InterviewCalendarDto> getMappedSelectForInterviewCalendarDto() {
        return Projections.bean(
                InterviewCalendarDto.class,
                InterviewReader.getMappedSelectForInterviewDtoFull().as("interview"),
                VacancyReader.getMappedSelectForVacancyDto().as("vacancy")
        );
    }

    public static QBean<InterviewDto> getMappedSelectForInterviewDtoFull() {
        return Projections.bean(
                InterviewDto.class,
                interview.id,
                interview.placeId,
                interview.dateEnd,
                interview.dateStart,
                VacancyReader.getMappedSelectForVacancyRespondDto().as("vacancyRespond"),
                PlaceReader.getMappedSelectForPlaceDto().as("place")
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

    public List<InterviewCalendarDto> getAllInterviewCalendar(boolean showArchive) {
        List<InterviewCalendarDto>  calendarDtos = queryFactory.from(interview)
                .innerJoin(vacancyRespond).on(interview.vacancyRespondId.eq(vacancyRespond.id))
                .innerJoin(place).on(place.id.eq(interview.placeId))
                .innerJoin(vacancy).on(vacancy.id.eq(vacancyRespond.vacancyId))
                .leftJoin(position).on(position.id.eq(vacancy.positionId))
                .leftJoin(staffUnit).on(staffUnit.id.eq(vacancy.staffUnitId))
                .leftJoin(employee).on(employee.id.eq(vacancy.hrId))
                .select(InterviewReader.getMappedSelectForInterviewCalendarDto())
                .where(showArchive ? interview.dateStart.lt(Instant.now()) : interview.dateStart.goe(Instant.now()))
                .orderBy(interview.dateStart.desc())
                .fetch();

//        Map<Long, List<Employee>> interviewEmployees =
//                this.employeeReader.getInterviewAllEmployees();
//
//        for (InterviewCalendarDto calendarDto : calendarDtos) {
//            List<Employee> employeesForInterview = interviewEmployees.get(calendarDto.getInterview().getId());
//            if (employeesForInterview != null && !employeesForInterview.isEmpty()) {
//                calendarDto.setMembers(
//                        employeesForInterview.stream().map(e -> e.getLastName() + " " + e.getFirstName())
//                                .collect(Collectors.joining(","))
//                );
//            }
//        }

        return calendarDtos;
    }
}
