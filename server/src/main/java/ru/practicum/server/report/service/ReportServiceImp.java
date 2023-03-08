package ru.practicum.server.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.report.dto.ReportDto;
import ru.practicum.server.report.mapper.ReportMapper;
import ru.practicum.server.report.repository.ReportRepository;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportServiceImp implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper mapper;

    @Override
    @NotNull
    public ReportDto getReportByUserId(@NotNull Long userId) {
            return mapper.mapToReportDto(reportRepository.findByReportedUserId(userId)
                    .orElseThrow(() -> new NotFoundException("Отчет с сообщенным id=" + userId + " не найден")));
    }
}
