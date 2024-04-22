package com.app.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;

@SuppressWarnings("unchecked")
public class CommonUtil {
    
    private static final ObjectMapper om = new ObjectMapper(); 
    
    private static final ModelMapper mapper = new ModelMapper();

    /**
     * mybatis paging 문자열 생성
     * 
     * @param 
     * @return String
     * @author guney
     * @date 2024. 3. 7.
     */
    public static String strPaging(int curPage, int pageSize) {
        String rtnValue = "";

        int perPage = (pageSize == -1 || pageSize >= 500) ? 500 : pageSize;
        int startNum = curPage == 0 ? 0 : (curPage - 1) * pageSize;

        if (startNum >= 0 && perPage > 0) {
            rtnValue = String.format("LIMIT %s, %s", startNum, perPage);
        }

        return rtnValue;
    }
    
    /**
     * Camel Case > Snake Case 변환
     * 
     * @param String str : 변환문자
     * @return String
     * @author guney
     * @date 2024. 3. 20.
     */
    public static String convertCamelToSnake(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, str);
    }
    
    /**
     * Snake Case > Camel Case 변환
     * 
     * @param String str : 변환문자
     * @return String
     * @author guney
     * @date 2024. 3. 20.
     */
    public static String convertSnakeToCamel(String str) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }
    
    /**
     * List모델 type 변환
     * 
     * @param List<S> : 변환할 list 객체
     * @return List<D> : 변경할 타입 class
     * @author guney
     * @date 2024. 3. 25.
     */
    public static <S, D> List<D> convertList(List<S> sourceDataList, Class<D> destinationClass) {
        if (sourceDataList == null || sourceDataList.isEmpty()) {
            return new ArrayList<>();
        }

        return sourceDataList.stream().map(sourceData -> mapper.map(sourceData, destinationClass)).toList();
    }
    
    /**
     * 모델 type 변환
     * 
     * @param S : 변환할 객체
     *        D : 변경할 타입 class
     * @return D
     * @author guney
     * @date 2024. 3. 25.
     */
    public static <S, D> D convertObject(S sourceData, Class<D> destinationClass) {
        if (ObjectUtils.isEmpty(sourceData) || sourceData == null) return null;
        return om.convertValue(sourceData, destinationClass);
    }
    
    /**
     * 객체복사
     * 
     * @param S : sourceData
     *        D : target
     * @return D
     * @author guney
     * @date 2024. 4. 8.
     */
    public static <S, D> D copyBean(S sourceData, D target) {
        BeanUtils.copyProperties(sourceData, target);
        return target;
    }
    
    /**
     * 객체 > map 변환
     * 
     * @param 
     * @return HashMap<String,Object>
     * @author guney
     * @date 2024. 4. 9.
     */
    public static <T> Map<String, Object> convertMap(T o) {
        HashMap<String, Object> returnMap = null;
        
        if (o instanceof Map) {
            returnMap = new HashMap<>();
            Map<String, Object> readMap = om.convertValue(o, Map.class);
            
            for (Entry<String, Object> r : readMap.entrySet()) {
                returnMap.put(CommonUtil.convertCamelToSnake(r.getKey()), r.getValue());
            }
        }
        
        return returnMap;
    }
}
