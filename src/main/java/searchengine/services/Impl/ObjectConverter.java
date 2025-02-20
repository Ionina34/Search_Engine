package searchengine.services.Impl;

import searchengine.model.Site;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Класс продствляет методы для преобразования конффигурации
 * сайта {@link searchengine.config.Site} в модель сайта {@link Site}.
 */
class ObjectConverter {
    /**
     * Преобразует объект конфигурации сайта в модель сайта.
     *
     * @param siteConfig объект конфигурации сайта, который нужно преобразовать
     * @return объект модели сайта, созданный на основе конфигурации
     */
    public static Site convertSiteConfigIntoSiteModel(searchengine.config.Site siteConfig) {
        Site site = new Site();
        site.setUrl(siteConfig.getUrl());
        site.setName(siteConfig.getName());
        site.setStatusTime(Timestamp.valueOf(LocalDateTime.now()));
        return site;
    }
}
