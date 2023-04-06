package ru.practicum.reviews.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserControllerAdmin {

}
