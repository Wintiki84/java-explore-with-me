package ru.practicum.server.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.report.dto.ReportDto;
import ru.practicum.server.report.mapper.ReportMapper;
import ru.practicum.server.report.repository.ReportRepository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class ReportServiceImp implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper mapper;

    @Override
    @NotNull
    @Transactional
    public ReportDto getReportByUserId(@NotNull @Positive Long userId) {
            return mapper.mapToReportDto(reportRepository.findByReportedUserId(userId)
                    .orElseThrow(() -> new NotFoundException("Отчет пользователя с id=" + userId + " не найден")));
    }
}
