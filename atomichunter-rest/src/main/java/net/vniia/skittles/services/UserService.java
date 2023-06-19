package net.vniia.skittles.services;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.UserDto;
import net.vniia.skittles.entities.QTelegramSubscriber;
import net.vniia.skittles.entities.TelegramSubscriber;
import net.vniia.skittles.entities.User;
import net.vniia.skittles.repositories.TelegramSubscriberRepository;
import net.vniia.skittles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final TelegramSubscriberRepository telegramSubscriberRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    private QTelegramSubscriber telegramSubscriber =
            new QTelegramSubscriber("telegramSubscriber");

    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<TelegramSubscriber> subscribers = queryFactory.select(telegramSubscriber).from(telegramSubscriber)
                        .where(telegramSubscriber.login.in(users.stream().map(User::getLogin).toList()))
                        .fetch();
        Map<String, TelegramSubscriber> subscriberMap = subscribers.stream().collect(Collectors.toMap(TelegramSubscriber::getLogin, Function.identity()));
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> {
            userDtos.add(new UserDto(user, subscriberMap.get(user.getLogin())));
        });
        return userDtos;
    }

    public String getUserRole(String login) {
        User user = userRepository.findByLogin(login);
        String userRole = user.getRole();
        return userRole;
    }

    public UserDto updateUser(UserDto userDto) {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().equals("admin")) {
            throw new RuntimeException("Необходима роль администратора!");
        }
        User user = userRepository.findByLogin(userDto.login);
        if (user == null) {
            throw new RuntimeException("Не найден пользователь");
        }
        user.update(userDto);
        if (!isValidatedOfChiefCreate(user)) {
            throw new RuntimeException("Нельзя создавать второго шефа!");
        }
        userRepository.save(user);
        return userDto;
    }

    public void deleteUser(String login) {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().equals("admin")) {
            throw new RuntimeException("Необходима роль администратора!");
        }
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new RuntimeException("Не найден пользователь");
        }
        userRepository.delete(user);
    }

    public boolean isValidatedOfChiefCreate(User userForCreateOrUpdate) {
        Boolean validated = false;
        User chief = userRepository.findByRole("chief");
        //если есть шеф, и новый пользователь не совпадает с ним по логину, запрещаем
        if (chief != null && !chief.getLogin().equals(userForCreateOrUpdate.getLogin())
                && userForCreateOrUpdate.getRole().equals("chief")) {
            throw new RuntimeException("Нельзя создать нового начальника!");
        }
        validated = true;
        return validated;
    }


    public boolean isValidatedOfDublicateCreate(User userForCreate) {
        Boolean validated = false;
        User dublicate = userRepository.findByLogin(userForCreate.login);
        //если есть дубликат, запрещаем
        if (dublicate != null) {
            throw new RuntimeException("Логин не уникален!");
        }
        validated = true;
        return validated;
    }

    public UserDto getUser(String login) {
        TelegramSubscriber telegramSubscriberStatus = new TelegramSubscriber();
        User user = userRepository.findByLogin(login);
        telegramSubscriberStatus = queryFactory.select(telegramSubscriber).from(telegramSubscriber)
                .where(telegramSubscriber.login.eq(login))
                .fetchOne();
        String userRoles = user.getRole();
        UserDto currentUser = new UserDto(user, telegramSubscriberStatus);

        return currentUser;
    }

    public void userDataChange(UserDto userWithChanges) {
        User user = this.userRepository.findByLogin(userWithChanges.getLogin());
        user.setFullName(userWithChanges.getFullName());
        user.setEmail(userWithChanges.getEmail());
        userRepository.saveAndFlush(user);
    }

    public Boolean passwordChange(String login, String oldPassword, String newPassword) {
        User user = this.userRepository.findByLogin(login);
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.saveAndFlush(user);
            return true;
        } else {
            return false;
        }

    }

    public Boolean getUserTelegramBotStatus(String login) {
        return this.telegramSubscriberRepository.findByLogin(login) != null;
    }

    public void telegramUnsubscribe(String login) {
        TelegramSubscriber subscriber = this.telegramSubscriberRepository.findByLogin(login);
        if (subscriber == null) {
            new RuntimeException("Пользователя не существует! Невозможно отписаться от телеграм-бота");
        }
        this.telegramSubscriberRepository.delete(subscriber);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLogin(auth.getName());
    }

    public List<String> getEmailsByRole(String role) {
        List<User> users = userRepository.findAll().stream().filter(e -> e.getRole().equals(role)).toList();
        if (users == null || users.isEmpty()) {
            new RuntimeException("Не найдены пользователи по роли");
        }
        return users.stream().map(User::getEmail).toList();
    }

    public String getEmailByLogin(String login) {
        //user обязательно один
        List<User> users = userRepository.findAll().stream().filter(e -> e.getRole().equals(login)).toList();
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("Не найден пользователь по логину");
        }
        return users.get(0).getEmail();
    }

    public TelegramSubscriber getTelegramSubscriberByLogin(String login) {
        return telegramSubscriberRepository.findByLogin(login);
    }

    public TelegramSubscriber getTelegramSubscriberById(Long telegramId) {
        return telegramSubscriberRepository.findByTelegramId(telegramId);
    }

    public List<TelegramSubscriber> getTelegramSubscribersByRole(String role) {
        return telegramSubscriberRepository.findAllByRole(role);
    }

    public String loginToTelegramBotOrShowError(String login, String password,
                                                String telegramUserName, Long telegramId) {
        if (telegramSubscriberRepository.findByLogin(login) != null) {
            return "Пользователь уже существует!";
        }
        User user = userRepository.findByLogin(login);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            TelegramSubscriber telegramSubscriber = new TelegramSubscriber();
            telegramSubscriber.setLogin(user.getLogin());
            telegramSubscriber.setFullName(user.getFullName());
            telegramSubscriber.setRole(user.getRole());
            telegramSubscriber.setTelegramId(telegramId);
            telegramSubscriber.setTelegramName(telegramUserName);
            telegramSubscriberRepository.saveAndFlush(telegramSubscriber);
            return "Вы успешно залогинены, здравствуйте, " + user.getFullName() +
                    ".\nДанный канал предназначен только для оповещений. " +
                    "При необходимости, здесь появится сообщение о заведении новой вакансии.";
        } else {
            return "Вы не прошли аутентификацию! Попробуйте снова!";
        }
    }


    //////////////init admin////////////////////
    @Value("${default-user.login}")
    private String adminLogin;
    @Value("${default-user.password}")
    private String adminPassword;
    @Value("${default-user.email}")
    private String adminEmail;
    @Value("${default-user.full-name}")
    private String adminFullName;

    @PostConstruct
    public void initializeAdmin() {
        User adminRole = userRepository.findByLogin("admin");
        if (adminRole == null) {
            this.generateAdminUser();
        }
    }

    @PostConstruct
    public void initializeAllOtherRoles() {
        User userRole = userRepository.findByLogin("user");
        if (userRole == null) generateUserUser();
        User chiefRole = userRepository.findByLogin("chief");
        if (chiefRole == null) generateChiefUser();
        User contragentRole = userRepository.findByLogin("contragent");
        if (contragentRole == null) generateContragentUser();
    }

    private void generateAdminUser() {
        User admin = new User();
        admin.setLogin(adminLogin);
        admin.setPassword(adminPassword);
        admin.setEmail(adminEmail);
        admin.setFullName(adminFullName);
        User user = User.builder()
                .login(admin.getLogin())
                .password(passwordEncoder.encode(admin.getPassword()))
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role("admin")
                .build();
        userRepository.save(user);
    }

    private void generateUserUser() {
        User admin = new User();
        admin.setLogin("user");
        admin.setPassword("user");
        admin.setEmail("user-42-user@mail.com");
        admin.setFullName("Юзер Юзерович Юзеров");
        User user = User.builder()
                .login(admin.getLogin())
                .password(passwordEncoder.encode(admin.getPassword()))
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role("user")
                .build();
        userRepository.save(user);
    }

    private void generateChiefUser() {
        User admin = new User();
        admin.setLogin("chief");
        admin.setPassword("chief");
        admin.setEmail("chief-42-chief@mail.com");
        admin.setFullName("Шеф Шефович Шефов");
        User user = User.builder()
                .login(admin.getLogin())
                .password(passwordEncoder.encode(admin.getPassword()))
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role("chief")
                .build();
        userRepository.save(user);
    }

    private void generateContragentUser() {
        User admin = new User();
        admin.setLogin("contragent");
        admin.setPassword("contragent");
        admin.setEmail("contragent-42-contragent@mail.com");
        admin.setFullName("Контрагент Контрагентович Контрагентов");
        User user = User.builder()
                .login(admin.getLogin())
                .password(passwordEncoder.encode(admin.getPassword()))
                .fullName(admin.getFullName())
                .email(admin.getEmail())
                .role("contragent")
                .build();
        userRepository.save(user);
    }


}
