package com.app.common.util;

import java.util.List;
import java.util.Random;

import com.app.common.exception.ValidException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessUtil {

    private static final Random random =  new Random();
    
    /**
     * 무작위 sizs 선정
     * 
     * @param maxSize : 최대 사이즈
     * @return int
     * @author guney
     * @date 2024. 5. 7.
     */
    public static int randomSelection(int maxSize) {
        return 0 < maxSize ? random.nextInt(maxSize) : 0;
    }
    
    /**
     * 무작위 list 선정
     * 
     * @param 
     * @return T
     * @author guney
     * @date 2024. 5. 7.
     */
    public static <T> T randomSelectionList(List<T> list) {
        return list.get(randomSelection(list.size()));
    }
    
    /**
     * 확률 list 선정
     * 
     * @param List<T> items        : 리스트 객체 
     *        List<Double> weights : 확률 리스트 (items 파라미터의 인덱스 개수, 순서가 맞아야함)
     * @return T                   : items.get(랜덤 인덱스)
     * @author guney
     * @throws ValidException 
     * @date 2024. 5. 10.
     */
    public static <T> T weightedRandomSelection(List<T> items, List<Double> weights) throws ValidException {
        if (items.size() != weights.size()) {
            throw new ValidException("확률 계산에 실패하였습니다. 다시 시도해 주세요.");
        }

        double totalWeight = weights.stream().mapToDouble(Double::doubleValue).sum();
        double randomNumber = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0.0;
        
        for (int i = 0; i < items.size(); i++) {
            
            cumulativeWeight += weights.get(i);
            
            log.info("randomNumber : {}, cumulativeWeight : {}, weight : {}", randomNumber, cumulativeWeight, weights.get(i));
            
            if (randomNumber < cumulativeWeight) {
                return items.get(i);
            }
        }

        return null;
    }
    
    /**
     * 백분율 수로 확률생성
     * 
     * @param int qty    : 수량
     *        int maxQty : 총 수량
     * @return double
     * @author guney
     * @date 2024. 5. 10.
     */
    public static double probabilityCalculation(int qty, int maxQty) {
        if (0 == maxQty) return 0.0;
        return (((double) qty / maxQty) * 100);
    }
}
