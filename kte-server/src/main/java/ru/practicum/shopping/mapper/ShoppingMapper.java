package ru.practicum.shopping.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shopping.dto.ShoppingDto;
import ru.practicum.shopping.model.Shopping;

@Mapper(componentModel = "spring")
public interface ShoppingMapper {
    Shopping mapToShopping(ShoppingDto shoppingDto);
}
