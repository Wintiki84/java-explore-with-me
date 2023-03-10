package ru.practicum.server.report.service;

import ru.practicum.server.report.dto.ReportDto;

import javax.validation.constraints.NotNull;

public interface ReportService {
    @NotNull
    ReportDto getReportByUserId(@NotNull Long userId);
}
