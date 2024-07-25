package com.educaguard.api.controller;


import com.educaguard.api.dto.others.Message;
import com.educaguard.api.dto.password.NewPasswordInputDTO;
import com.educaguard.domain.service.EmailSenderService;
import com.educaguard.domain.service.UserService;
import com.educaguard.utils.Feedback;
import com.educaguard.utils.StrongPassword;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recover")
public class RecoverAccountController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserService userService;

    @GetMapping("/recover-account/{email}")
    public ResponseEntity<Message> recoverAccount(@PathVariable("email") @Email String email) {
        emailSenderService.recoverAccount(email);
        return new ResponseEntity<Message>(new Message(Feedback.ACCOUNT_RECOVER_SENT), HttpStatus.OK);
    }

    @PostMapping("/new-password")
    public ResponseEntity<Message> newPassword(@RequestBody NewPasswordInputDTO newPasswordInputDTO) {
        StrongPassword.isStrong(newPasswordInputDTO.getNewpassword());
        userService.updatePassword(newPasswordInputDTO);
        return new ResponseEntity<Message>(new Message(Feedback.OK_PASSWORD_CHANGE), HttpStatus.OK);
    }


}
