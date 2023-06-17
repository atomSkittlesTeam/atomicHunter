package net.vniia.skittles.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final MessageRepository messageRepository;

    private final EmailService emailService;

    private final UserService userService;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JPAQueryFactory queryFactory;


    public Map<String, Long> getPositionCountByNumber() {
        Map<String, Long> result = new HashMap<>();
//        List<RequestPosition> requestPositionList = requestPositionRepository.findAll();
//        queryFactory.from(request)
//                .leftJoin(requestPosition).on(requestPosition.requestId.eq(request.id))
//                .where(request.archive.isFalse())
//                .groupBy(request.number)
//                .select(request.number, requestPosition.count())
//                .fetch().forEach(tuple -> result.put(tuple.get(0, String.class), tuple.get(1, Long.class)));
        return result;
    }


    public Map<String, Integer> getPositionCountByProduct() {
        //ждем переноса продукта в позиции заявки
        Map<String, Integer> result = new HashMap<>();
        return result;
    }


}
