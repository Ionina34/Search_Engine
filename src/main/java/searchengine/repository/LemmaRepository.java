package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Lemma;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для рабты с сущностью Lemma {@link Lemma} в базе данных.
 * Он наследует функциональность от JpaRepository {@link JpaRepository},
 * что позволяет выполнить стандартные операции CRUD.
 */
public interface LemmaRepository extends JpaRepository<Lemma, Long> {

    /**
     * Обновляет значение автоинкремента для таблицы indexes, устанавливая его на 1.
     */
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE lemmas AUTO_INCREMENT = 1",
            nativeQuery = true)
    void updateIdentity();

    /**
     * Удаляет все записи из таблицы lemmas.
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM lemmas", nativeQuery = true)
    void deleteAllData();

    /**
     * Уменьшает частоту лемм на 1 для заданногго списка индификаторов.
     *
     * @param id списко индефикаторов лемм, частота которых будет уменьшаться
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE lemmas SET frequency = frequency - 1 WHERE id IN(:id)",
            nativeQuery = true)
    void minusFrequencyByListId(@Param("id") List<Integer> id);

    /**
     * Находит лемму поо её значению и индефикатору сайта.
     *
     * @param lemma  значение леммы
     * @param siteId индефикатор сайта
     * @return опциональная лемма {@link Lemma}, если найдена
     */
    Optional<Lemma> findByLemmaAndSiteId(String lemma, Integer siteId);

    /**
     * Подсчитывает кол-во лемм для заданного индефикатора сайта.
     *
     * @param siteId индификатор сайта
     * @return кол-во лемм для данного сайта
     */
    @Query(value = "SELECT COUNT(*) FROM lemmas WHERE site_id = :siteId"
            , nativeQuery = true)
    Integer countLemmasBySiteId(@Param("siteId") Integer siteId);

    /**
     * Находит частоту леммы для заданного индефикатора сайта.
     *
     * @param lemma  значение леммы
     * @param siteId индификатор сайта (может быть null)
     * @return опциональная частота леммы, если найдена
     */
    @Query(value = "SELECT sum(frequency) FROM lemmas WHERE lemma = :lemma " +
            "AND (:siteId IS NULL OR site_id = :siteId)",
            nativeQuery = true)
    Optional<Integer> findFrequencyByLemmaAndSiteId(@Param("lemma") String lemma,
                                                    @Param("siteId") Integer siteId);
}
