package com.app.junit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.core.annotations.ApiDocumentResponse;
import com.app.common.core.mapStruct.MapStructInterface;
import com.app.common.dto.ApiBodyDTO;
import com.app.common.dto.ApiBodyDTO.Response;
import com.app.common.dto.ApiDocumentResponseDTO.Success.HeaderSuccess;
import com.app.common.enums.MessageEnum;
import com.app.common.service.CommonService;
import com.app.common.util.ApiResUtil;
import com.app.common.util.CommonUtil;
import com.app.junit.dto.res.OrderSeatAssignPriceResponseDto;
import com.app.junit.dto.res.SeatAssignPriceDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "TEST", description = "깔깔")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/test")
public class TestController {

	private final CommonService commonService;
	
	private final MapStructInterface mapStructInterface;
	
	@ApiDocumentResponse
    @Operation(summary = "쿼먼", description = "commonUtil")
	@GetMapping(value = "/commonUtil", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<OrderSeatAssignPriceResponseDto>> commonUtilConvertTest(HttpServletResponse response){
    	return ApiResUtil.success(CommonUtil.convertList(createDummyData(), OrderSeatAssignPriceResponseDto.class), HeaderSuccess.builder().build(), MessageEnum.SUCCESS.getCode());
	}
    
	@ApiDocumentResponse
    @Operation(summary = "매애", description = "mapStruct")
	@GetMapping(value = "/mapStruct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiBodyDTO.Response<List<OrderSeatAssignPriceResponseDto>> mapStructMapperConvertTest(HttpServletResponse response){
    	return ApiResUtil.success(mapStructInterface.toResponseList(createDummyData()), HeaderSuccess.builder().build(), MessageEnum.SUCCESS.getCode());
    }
	
	public List<SeatAssignPriceDto> createDummyData() {
	    List<SeatAssignPriceDto> dtoList = new ArrayList<>();

	    for (int i = 1; i <= 30000; i++) {
	    	SeatAssignPriceDto dto = new SeatAssignPriceDto();
	        dto.setProductTurnPriceSequence(100000 + i);
	        dto.setProductPriceSequence(1000 + i);
	        dto.setSeatGradeCode("00" + i);
	        dto.setSeatGradeName(i == 1 ? "VIP" : (i == 2 ? "R" : "S"));
	        dto.setProductSalePrice(10000f * i);
	        dto.setPreferredRank(i);
	        dto.setProductPriceClassificationName("분류" + i);
	        dto.setProductPriceClassificationDescription("상세설명" + i);
	        dto.setPriceTypeCode("D");
	        dto.setPriceTypeName("할인가");
	        dto.setPersonalDocumentRequirementYn("N");
	        dto.setPartnerPatternCode("1");
	        dto.setPayCode("10");
	        dto.setCode("30");
	        dto.setProductSaleCardQuantity("5");
	        
	        dtoList.add(dto);
	    }
	    return dtoList;
	}

}
