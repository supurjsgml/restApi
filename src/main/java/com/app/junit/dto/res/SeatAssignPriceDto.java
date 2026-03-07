package com.app.junit.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatAssignPriceDto {
    @Schema(description = "상품 회차 가격 순번", example = "116153")
    private Integer productTurnPriceSequence;

    @Schema(description = "상품 가격 순번", example = "2821")
    private Integer productPriceSequence;

    @Schema(description = "좌석 등급 코드", example = "002")
    private String seatGradeCode;

    @Schema(description = "좌석 등급명", example = "VIP")
    private String seatGradeName;

    @Schema(description = "상품 판매가", example = "150.0")
    private Float productSalePrice;

    @Schema(description = "우선순위", example = "2")
    private Integer preferredRank;

    @Schema(description = "상품 가격 분류명", example = "VIP")
    private String productPriceClassificationName;

    @Schema(description = "상품 가격 분류 설명", example = "VIP")
    private String productPriceClassificationDescription;

    @Schema(description = "거래처 유형 코드", example = "2")
    private String partnerPatternCode;

    @Schema(description = "가격 유형 코드", example = "")
    private String priceTypeCode;

    @Schema(description = "가격 유형명", example = "")
    private String priceTypeName;

    @Schema(description = "개인 확인서류 지참 필수여부", example = "N")
    private String personalDocumentRequirementYn;

    @Schema(description = "결제혜택 payCode(카드일때만 10)", example = "10")
    private String payCode;

    @Schema(description = "결제혜택 결제수단 코드", example = "30")
    private String code;

    @Schema(description = "결제혜택 수량제한", example = "5")
    private String productSaleCardQuantity;

}


