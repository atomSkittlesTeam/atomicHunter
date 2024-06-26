package net.vniia.skittles.controllers;


import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.PasswordChangeRequestDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.dto.UserDto;
import net.vniia.skittles.integration.OrgStructIntegrationService;
import net.vniia.skittles.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    @Value("${telegram.link}")
    private String telegramLink;

    private final UserService userService;
    private final TelegramBotController telegramBotController;

    private final OrgStructIntegrationService orgStructIntegrationService;

    @GetMapping("all")
    public List<UserDto> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("{login}/roles")
    public List<String> getUserRoles(@PathVariable String login) {
        return Collections.singletonList(userService.getUserRole(login));
    }

    @PutMapping("update")
    public UserDto updateUser(@RequestBody UserDto user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("delete/{login}")
    public void deleteUser(@PathVariable String login) {
        userService.deleteUser(login);
    }

    @PostMapping("currentUser")
    public UserDto getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth.getName());
    }

    @PostMapping("user-data-change")
    public void fullNameChange(@RequestBody UserDto userWithChanges) {
        userService.userDataChange(userWithChanges);
    }

    @PostMapping("password-change")
    public Boolean passwordChange(@RequestBody PasswordChangeRequestDto request) {
        return userService.passwordChange(request.getUser().getLogin(), request.getOldPassword(), request.getNewPassword());
    }

    @GetMapping("telegram-subscribe-status")
    public Boolean getUserTelegramSubscribeStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserTelegramBotStatus(auth.getName());
    }

    @GetMapping("telegram-link")
    public List<String> getTelegramLink() {
        return Collections.singletonList(telegramLink);
    }

    @GetMapping("telegram-enable")
    public Boolean getTelegramBotCondition() {
        return !telegramLink.equals("null");
    }

    @GetMapping("telegram-unsubscribe/{login}")
    public void telegramUnsubscribe(@PathVariable String login) {
        telegramBotController.telegramUnsubscribe(login);
    }

    @PostMapping("recover/send-recover-letter")
    public Boolean sendRecoverLetter(@RequestBody String email) {
        return true;
    }

    @PostMapping("recover/verify-recover-code")
    public Boolean verifyRecoverCOde(@RequestBody String recoverCode) {
        return true;
    }

    @PostMapping("recover/save-new-password")
    public Boolean saveNewPassword(@RequestBody String newPassword) {
        return true;
    }

    @GetMapping("staff")
    public List<StaffUnitDto> getAllStaffUnits() {
        return this.orgStructIntegrationService.getAllStaffUnits();
    }

}
