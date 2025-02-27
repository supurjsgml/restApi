package com.app.common.exception;

import java.io.Serial;

import com.app.common.enums.MessageEnum;
import com.app.common.util.MessagesUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author guney
 * @date 2024. 4. 17.
 */
@Getter
@Setter
@NoArgsConstructor
public class ValidException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2891298704913159355L;

    public ValidException(String message) {
        super(message);
    }
    
    public ValidException(MessageEnum e) {
        super(MessagesUtils.getMessage(e.getCode()));
    }
    
    public ValidException(MessageEnum e, String o) {
        super(MessagesUtils.getMessage(e.getCode(), new Object[] {o}));
    }
    
    public ValidException(MessageEnum e, Object[] o) {
        super(MessagesUtils.getMessage(e.getCode(), o));
    }
}
