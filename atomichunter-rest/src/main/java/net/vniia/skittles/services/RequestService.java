package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.RequestDto;
import net.vniia.skittles.entities.Request;
import net.vniia.skittles.readers.RequestReader;
import net.vniia.skittles.repositories.RequestPositionRepository;
import net.vniia.skittles.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestReader requestReader;

    private final RequestRepository requestRepository;

    private final RequestPositionRepository requestPositionRepository;

    private final MessageService messageService;


    @Transactional
    public void sendMessageOfNewRequests() {
//        List<Message> messages = messageRepository.findAll();
//        List<Message> newMessages = messages.stream().filter(e -> e.getEmailSign().equals(false)
//                && e.getType().equals(Types.newRequests)).toList();
//        String numbers = String.join(",", newMessages.stream().map(Message::getObjectName).toList());
//        List<String> emails = userService.getEmailsByRole("chief");
//        emails.forEach(email -> {
//            emailService.sendSimpleMessage(email,
//                    "Поступили новые заказы",
//                    ("Количество заказов: " + newMessages.size() + "\n"
//                            + "Номера заказов: " + numbers));
//        });
//        newMessages.forEach(e -> e.setEmailSign(true));
//        messageRepository.saveAll(newMessages);
//        System.out.println("Отправил сообщение о новых реквестах");
    }

    private void sortList(List<Request> list) {
        Comparator<Request> c = Comparator.comparing(Request::getRequestDate)
                .thenComparing(Request::getReleaseDate);
        list.sort(c);
    }

    public RequestDto createRequest(RequestDto requestDto) {
        Request request = new Request(requestDto);
        request = this.requestRepository.save(request);
        this.messageService.createMessagesForAllUsers(request.getId(),
                "Новый заказ №" + request.getNumber());
        this.messageService.createTelegramMessagesForAllUsers("Новый заказ №" + request.getNumber());
        return requestReader.getRequestById(request.getId());
    }

    @Transactional
    public RequestDto updateRequest(Long id, RequestDto requestDto) {
        Request request = this.requestRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Заказ не найден!");
                }
        );
        request.update(requestDto);
        return requestReader.getRequestById(request.getId());
    }

    @Transactional
    public void archiveRequest(Long id) {
        Request request = this.requestRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Заказ не найден!");
                }
        );
        request.archive();
    }
}
