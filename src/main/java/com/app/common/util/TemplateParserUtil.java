package com.app.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.common.core.annotations.TemplateAlias;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@UtilityClass
public class TemplateParserUtil {

    private final Pattern PATTERN = Pattern.compile("\\{(.+?)\\}");

    //Method(Getter) 메모리에 캐시
    private final Map<Class<?>, Map<String, Method>> GETTER_CACHE = new ConcurrentHashMap<>();

    /**
     * 파쓰파쓰 
     * @param template
     * @param 줴네릭 dto
     * @return String
     * @user guney
     * @date 2026. 2. 15.
     */
    public <T> String parse(String template, T dto) {
        if (template == null) return "";
        if (dto == null) return template;

        Map<String, Method> getterMap = getGetterMap(dto.getClass());
        Matcher matcher = PATTERN.matcher(template);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            Method method = getterMap.get(key);
            
            String value = "";
            if (method != null) {
                try {
                    //필드 직접 접근 대신 Getter 메서드 호출 > 보안 설계 원칙 땜시 setAccessible(true)안씀 
                    Object obj = method.invoke(dto);
                    if (obj != null) {
                        value = String.valueOf(obj);
                    }
                } catch (Exception e) {
                    log.error("TemplateParserUtil parse ERROR for key '{}' : {}", key, e.getMessage());
                }
            }
            
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        
        matcher.appendTail(sb);
        
        return sb.toString();
    }

    /**
     * @TemplateAlias 커스텀 어노테이션 필드 맵핑 (사용 하려면 적용된 예시 코드를 찾아서 보고 오셔야 한다는...)
     * @param clazz
     * @return Map<String,Field>
     * @user guney
     * @date 2026. 2. 15.
     */
    private Map<String, Method> getGetterMap(Class<?> clazz) {
        return GETTER_CACHE.computeIfAbsent(clazz, c -> {
            Map<String, Method> map = new ConcurrentHashMap<>();
            Field[] fields = c.getDeclaredFields();
            
            for (Field field : fields) {
                try {
                    //Getter 메서드 이름 생성
                    String fieldName = field.getName();
                    String getterName = "get".concat(fieldName.substring(0, 1).toUpperCase()).concat(fieldName.substring(1));
                    
                    Method getter = c.getMethod(getterName);

                    //@TemplateAlias가 있으면 별칭으로 저장
                    if (field.isAnnotationPresent(TemplateAlias.class)) {
                        String aliasValue = field.getAnnotation(TemplateAlias.class).value();
                        map.put(aliasValue, getter);
                    }
                    
                    //기본 필드명도 저장 좀 너프하게 ?; 카멜케이스로 쓰고 싶을때도 있자네 ? 
                    map.putIfAbsent(fieldName, getter);
                } catch (NoSuchMethodException e) {
                    //Getter가 없는 필드는 건너뜀
                    continue;
                } catch (Exception e) {
                    log.warn("Failed to map getter for field: {}", field.getName());
                }
            }
            
            return map;
        });
    }
}