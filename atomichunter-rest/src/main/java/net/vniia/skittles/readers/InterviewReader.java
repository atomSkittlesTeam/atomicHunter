package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.entities.QInterview;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InterviewReader {

    private static final QInterview interview = QInterview.interview;

    private final JPAQueryFactory queryFactory;

    private final EmployeeReader employeeReader;

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

    public static QBean<InterviewDto> getMappedSelectForInterviewDto() {
        return Projections.bean(
                InterviewDto.class,
                interview.id,
                interview.meeting,
                interview.dateEnd,
                interview.dateStart
        );
    }
}
