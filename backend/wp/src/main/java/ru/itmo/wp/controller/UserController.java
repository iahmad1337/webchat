package ru.itmo.wp.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.exception.ValidationException;
import ru.itmo.wp.form.RegisterForm;
import ru.itmo.wp.form.validator.RegisterFormRegisterValidator;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1")
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;
    private final RegisterFormRegisterValidator registerFormRegisterValidator;

    public UserController(JwtService jwtService, UserService userService, RegisterFormRegisterValidator registerFormRegisterValidator) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.registerFormRegisterValidator = registerFormRegisterValidator;
    }

    @InitBinder("registerForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(registerFormRegisterValidator);
    }

    @GetMapping("users")
    public List<User> findUsers() {
        return userService.findAll();
    }

    @PostMapping("users")
    public void register(@RequestBody @Valid RegisterForm registerForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        userService.save(registerForm);
    }

    @GetMapping("users/auth")
    public User findUserByJwt(@RequestParam String jwt) {
        return jwtService.find(jwt);
    }
}
