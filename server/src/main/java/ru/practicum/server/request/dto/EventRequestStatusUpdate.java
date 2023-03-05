package ru.practicum.server.request.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdate {
    private List<Long> requestIds;
    private String status;
}
