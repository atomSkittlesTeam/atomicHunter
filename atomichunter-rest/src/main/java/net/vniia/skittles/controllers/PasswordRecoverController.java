package net.vniia.skittles.controllers;


import lombok.RequiredArgsConstructor;
import net.vniia.skittles.services.PasswordRecoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("recover")
@RequiredArgsConstructor
public class PasswordRecoverController {

    @Autowired
    private PasswordRecoverService passwordRecoverService;

    @PostMapping("send-recover-letter")
    public Boolean sendRecoverLetter(@RequestBody String email) {
        return passwordRecoverService.sendRecoverLetter(email);
    }

    @PostMapping("verify-recover-code")
    public Boolean verifyRecoverCOde(@RequestBody List<String> recoverCodeAndEmail) {
        return passwordRecoverService.verifyRecoverCode(recoverCodeAndEmail.get(0), recoverCodeAndEmail.get(1));
    }

    @PostMapping("save-new-password")
    public Boolean saveNewPassword(@RequestBody List<String> newPasswordAndEmail ) {
        return passwordRecoverService.saveNewPassword(newPasswordAndEmail.get(0), newPasswordAndEmail.get(1));
    }
}
