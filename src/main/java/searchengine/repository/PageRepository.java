package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для рабты с сущностью Page {@link Page} в базе данных.
 * Он наследует функциональность от JpaRepository {@link JpaRepository},
 * что позволяет выполнить стандартные операции CRUD.
 */
@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    /**
     * Обновляет значение автоинкремента для таблицы pages, устанавливая его на 1
     */
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE pages AUTO_INCREMENT = 1",
            nativeQuery = true)
    void updateIdentity();

    /**
     * Удаляет все записи из таблицы pages
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM pages", nativeQuery = true)
    void deleteAllData();

    /**
     * Находит страницу по пути и сайту.
     *
     * @param path путь сттаницы
     * @param site сайт, к которому принадлежит страница
     * @return опциональная страница {@link Page}
     */
    Optional<Page> findByPathAndSite(String path, Site site);

    /**
     * Подсчитывает кол-во сттраниц для указанного сайта.
     *
     * @param siteId индификатор сайта
     * @return кол-во страниц
     */
    @Query(value = "SELECT COUNT(*) FROM pages WHERE site_id = :siteId",
            nativeQuery = true)
    Integer countPagesBySiteId(@Param("siteId") Integer siteId);

    /**
     * Находит страницу по лемме и индификатору сайта.
     *
     * @param lemma  лемма для поиска
     * @param siteId индификатор сайта (может быть null)
     * @return список страниц {@link Page}, соответствующих критериям
     */
    @Query(value = "SELECT p.id, p.code, p.content, p.path, p.site_id FROM pages AS p " +
            "JOIN indexes AS i ON p.id = i.page_id " +
            "JOIN lemmas AS l ON l.id = i.lemma_id " +
            "WHERE l.lemma = :lemma " +
            "AND (:siteId IS NULL OR l.site_id = :siteId)",
            nativeQuery = true)
    List<Page> findPageByLemmaAndSiteId(@Param("lemma") String lemma,
                                        @Param("siteId") Integer siteId);

    /**
     * Находит страницы по лемме и списку индификаторов страниц.
     *
     * @param lemma   лемма для поиска
     * @param pagesId список индификаторов страниц
     * @return список страниц {@link Page}, соотвествующих критериям
     */
    @Query(value = "SELECT p.id, p.code, p.content, p.path, p.site_id FROM pages AS p " +
            "JOIN indexes AS i ON p.id = i.page_id " +
            "JOIN lemmas AS l ON l.id = i.lemma_id " +
            "WHERE l.lemma = :lemma AND p.id IN (:pages)",
            nativeQuery = true)
    List<Page> findPageByLemmaAndPageId(@Param("lemma") String lemma, @Param("pages") List<Integer> pagesId);
}

