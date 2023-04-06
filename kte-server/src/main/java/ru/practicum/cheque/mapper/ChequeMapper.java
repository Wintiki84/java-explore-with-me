package ru.practicum.cheque.mapper;

import org.mapstruct.Mapper;
import ru.practicum.cheque.dto.ChequeDto;
import ru.practicum.cheque.model.Cheque;

@Mapper(componentModel = "spring")
public interface ChequeMapper {
    Cheque mapToCheque(ChequeDto chequeDto);
}
