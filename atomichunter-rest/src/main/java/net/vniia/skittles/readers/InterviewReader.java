package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.entities.QInterview;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InterviewReader {

    private static final QInterview interview = QInterview.interview;

    private final JPAQueryFactory queryFactory;

    public InterviewDto getInterviewById(Long interviewId) {
        return interViewQuery()
                .where(interview.id.eq(interviewId))
                .fetchFirst();
    }

    private JPAQuery<InterviewDto> interViewQuery() {
        return queryFactory.from(interview)
                .select(getMappedSelectForInterviewDto());
    }

    public static QBean<InterviewDto> getMappedSelectForInterviewDto() {
        return Projections.bean(
                InterviewDto.class,
                interview.id,
                interview.placeId,
                interview.dateEnd,
                interview.dateStart
        );
    }
}
