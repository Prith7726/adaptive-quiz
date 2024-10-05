package com.adaptive.quiz.controller;

import com.adaptive.quiz.service.DeRegisterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Optional;

import static com.adaptive.quiz.controller.RegisterController.SESSION_REGISTER;

@Controller
@RequiredArgsConstructor
public class DeRegisterController {

    private final DeRegisterService deRegisterService;

    @PostMapping(value = "/quiz/deregister", produces = {"application/json"})
    public ResponseEntity<DeRegisterResponse> deregister(HttpServletRequest request, SessionStatus status) {
        RegisterResponse register = (RegisterResponse) request.getSession().getAttribute(SESSION_REGISTER);
        String result = deRegisterService.deregister(register.uuid());
        String message = Optional.ofNullable(result)
                .orElseGet(() -> "Invalid UUID.");

        DeRegisterResponse.Status deregister = Optional.ofNullable(result)
                .map(n -> DeRegisterResponse.Status.DE_REGISTER_SUCCESS)
                .orElseGet(() -> DeRegisterResponse.Status.DE_REGISTER_FAILED);
        request.getSession().removeAttribute(SESSION_REGISTER);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DeRegisterResponse(message, deregister));
    }

}
