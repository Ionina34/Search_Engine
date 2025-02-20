package searchengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Конфигурация локализации для приложения.
 * Этот класс настраивает {@link LocaleResolver}  для управления локализацией на основе
 * сессии, устанавливая язык по умолчанию.
 */
@Configuration
public class LocaleConfig {

    /**
     * Создает и настраивает {@link LocaleResolver}.
     * Устанавливает язык по умолчанию на русский.
     *
     * @return настроенный {@link LocaleResolver}.
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.forLanguageTag("ru"));
        return slr;
    }
}
