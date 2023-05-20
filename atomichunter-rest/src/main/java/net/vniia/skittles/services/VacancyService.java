package net.vniia.skittles.services;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.VacancyDto;
import net.vniia.skittles.entities.Vacancy;
import net.vniia.skittles.readers.VacancyReader;
import net.vniia.skittles.repositories.VacancyRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyService {
    private final VacancyReader vacancyReader;

    private final VacancyRepository vacancyRepository;

    private final MessageService messageService;

    private void sortList(List<Vacancy> list) {
        // just a sort algo
//        Comparator<Vacancy> c = Comparator.comparing(Request::getRequestDate)
//                .thenComparing(Request::getReleaseDate);
//        list.sort(c);
    }

    public VacancyDto createVacancy(VacancyDto vacancyDto) {
        Vacancy vacancy = new Vacancy(vacancyDto);
        vacancy = this.vacancyRepository.save(vacancy);
        this.messageService.createMessagesForAllUsers(vacancy.getId(),
                "Новая вакансия №" + vacancy.getId());
        this.messageService.createTelegramMessagesForAllUsers("Новая вакансия №" + vacancy.getId());
        return vacancyReader.getVacancyById(vacancy.getId());
    }

    @Transactional
    public VacancyDto updateVacancy(Long id, VacancyDto vacancyDto) {
        Vacancy vacancy = this.vacancyRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Вакансия не найдена!");
                }
        );
        vacancy.update(vacancyDto);
        return vacancyReader.getVacancyById(vacancy.getId());
    }

    @Transactional
    public void archiveVacancy(Long id) {
        Vacancy vacancy = this.vacancyRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Вакансия не найдена!");
                }
        );
        vacancy.archive();
    }
}
