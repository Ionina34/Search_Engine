package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Site;

import java.util.Optional;

/**
 * Интерфейс репозитория для рабты с сущностью Site {@link Site} в базе данных.
 * Он наследует функциональность от JpaRepository {@link JpaRepository},
 * что позволяет выполнить стандартные операции CRUD.
 */
@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
    /**
     * Обовляет значение автоинкремента для таблицы sites, устанавливая его на 1
     */
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE sites AUTO_INCREMENT = 1",
            nativeQuery = true)
    void updateIdentity();

    /**
     * Удаляет все записи таблицы sites
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sites", nativeQuery = true)
    void deleteAllData();

    /**
     * Находит сайт по его URL.
     *
     * @param url URL сайта
     * @return опциональный сайт {@link Site}
     */
    Optional<Site> findByUrl(String url);

    /**
     * Находит сайт по его имени.
     *
     * @param name имя сайта
     * @return опциональный сайт {@link Site}
     */
    Optional<Site> findByName(@Param("name") String name);

    /**
     * Получает общее кол-во сайтов в таблице sites.
     *
     * @return кол-во сайтов
     */
    @Query(value = "SELECT COUNT(*) FROM sites", nativeQuery = true)
    Integer getNumSites();
}
