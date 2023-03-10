package ru.practicum.server.report.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.server.report.dto.ReportDto;
import ru.practicum.server.report.service.ReportService;
import ru.practicum.validator.AdminDetails;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/reports")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminReportController {
    private final ReportService reportService;

    @JsonView(AdminDetails.class)
    @GetMapping("users/{userId}")
    public ResponseEntity<ReportDto> getReport(@PathVariable @Positive Long userId) {
        log.info("получить отчет с userId={}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(reportService.getReportByUserId(userId));
    }
}
