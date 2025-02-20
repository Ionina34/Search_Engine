package searchengine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Класс, представляющий данные запроса.
 * Этот класс содержит настройки, связанные с запросом, такие как пользователь и реферер.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "request-settings")
public class RequestData {
    private String user;
    private String referrer;
}
