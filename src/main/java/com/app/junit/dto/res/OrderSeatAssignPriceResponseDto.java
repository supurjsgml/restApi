package com.app.junit.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "등급별 가격분류 응답 DTO")
public class OrderSeatAssignPriceResponseDto {

    @Schema(description = "상품회차 가격 순번", example = "000000")
    private Integer productTurnPriceSequence;

    @Schema(description = "상품 가격 순번", example = "0000")
    private Integer productPriceSequence;

    @Schema(description = "좌석 등급 코드", example = "001")
    private String seatGradeCode;

    @Schema(description = "좌석 등급명", example = "VIP")
    private String seatGradeName;

    @Schema(description = "상품 판매가", example = "1000")
    private Float productSalePrice;

    @Schema(description = "우선순위", example = "1")
    private Integer preferredRank;

    @Schema(description = "상품 가격 분류명", example = "VIP")
    private String productPriceClassificationName;

    @Schema(description = "상품 가격 분류 상세 내용", example = "VIP 전용")
    private String productPriceClassificationDescription;

    @Schema(description = "가격타입 코드", example = "D")
    private String priceTypeCode;

    @Schema(description = "가격타입 코드명", example = "할인가")
    private String priceTypeName;

    @Schema(description = "개인 확인서류 지참 필수여부", example = "N")
    private String personalDocumentRequirementYn;

    @JsonIgnore
    @Schema(description = "거래처 유형 코드", example = "1")
    private String partnerPatternCode;

    @Schema(description = "결제혜택 payCode(카드일때만 10)", example = "10")
    private String payCode;

    @Schema(description = "결제혜택 결제수단 코드", example = "30")
    private String code;

    @Schema(description = "결제혜택 수량제한", example = "5")
    private String productSaleCardQuantity;

}


