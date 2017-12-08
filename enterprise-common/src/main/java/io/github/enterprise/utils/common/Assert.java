package io.github.enterprise.utils.common;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Created by Sheldon on 2017/12/08
 */
public class Assert {

    /**
     * 如果参数不能通过验证，则抛出异常
     * @param errors
     */
    public static void newErrorsProcess(Errors errors) {
        if (errors.hasErrors()) {
            List<ObjectError> errorList = errors.getAllErrors();
            StringBuilder sb = new StringBuilder();
            String desc = "";
            for (ObjectError error: errorList) {
                desc = error.getDefaultMessage();
                sb.append(desc + " ");
            }
            throw new BusinessException(desc.trim());
        }
    }
}
