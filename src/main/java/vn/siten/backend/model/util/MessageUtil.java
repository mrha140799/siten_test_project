package vn.siten.backend.model.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:message.properties")
public class MessageUtil {
    @Autowired
    private Environment environment;

    public String getMessage(String key) {
        return environment.getProperty(key, key);
    }
    public String getMessageDefaultEmpty(String key) {
        return environment.getProperty(key, "");
    }

}

