
package com.app.common.enums;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** IP추출
 * @author guney
 * @date 2024. 8. 27.
 */
@AllArgsConstructor
public enum IP {
    
    HEADER(new String[]{"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"});

    @Getter
    private String[] header;
    
    public static String get() {
        String ipAddress = null;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        for (String key : IP.HEADER.getHeader()) {
            ipAddress = request.getHeader(key);
            
            if (ObjectUtils.isNotEmpty(ipAddress)) break;
        }
        
        return ObjectUtils.isNotEmpty(ipAddress) ? ipAddress : request.getRemoteAddr();
    }
}
