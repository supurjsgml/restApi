package com.app.common.core.mapStruct;

import java.util.List;

import org.mapstruct.Mapper;

import com.app.junit.dto.res.OrderSeatAssignPriceResponseDto;
import com.app.junit.dto.res.SeatAssignPriceDto;

@Mapper(componentModel = "spring")
public interface MapStructInterface {
	
	List<OrderSeatAssignPriceResponseDto> toResponseList(List<SeatAssignPriceDto> seatAssignPriceDtoList);

}
