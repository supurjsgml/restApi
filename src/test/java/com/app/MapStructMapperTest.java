package com.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.common.core.mapStruct.MapStructInterface;
import com.app.common.util.CommonUtil;
import com.app.junit.dto.res.OrderSeatAssignPriceResponseDto;
import com.app.junit.dto.res.SeatAssignPriceDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MapStructMapperTest {
	
	@Autowired
    private MapStructInterface mapStructInterface;

	@Test
	@Order(value = 1)
	void CommonUtilConvertTest() {
		List<SeatAssignPriceDto> sourceList = createDummyData();
		
		List<OrderSeatAssignPriceResponseDto> result1 = CommonUtil.convertList(sourceList, OrderSeatAssignPriceResponseDto.class);
		
		assertNotNull(result1);
		assertEquals(30000, result1.size());
		assertEquals("VIP", result1.get(0).getSeatGradeName());
	}
	
	@Test
	@Order(value = 2)
	void MapStructMapperConvertTest() {
	    List<SeatAssignPriceDto> sourceList = createDummyData();

	    List<OrderSeatAssignPriceResponseDto> result = mapStructInterface.toResponseList(sourceList);

	    assertNotNull(result);
	    assertEquals(30000, result.size());
	    assertEquals("VIP", result.get(0).getSeatGradeName());
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
