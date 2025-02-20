package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Index;

import java.util.List;

/**
 * Интерфейс IndexRepository предоставляет методы для работы с сущностью
 * Index {@link Index} в базе даннных.
 * Он наследует функциональность от JpaRepository {@link JpaRepository},
 * что позволяет выполнить стандартные операции CRUD.
 */
public interface IndexRepository extends JpaRepository<Index, Long> {

    /**
     * Обновляет значение автоинкремента для таблицы indexes, устанавливая его на 1.
     */
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE indexes AUTO_INCREMENT = 1",
            nativeQuery = true)
    void updateIdentity();

    /**
     * Удаляет все записи из таблицы indexes.
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM indexes", nativeQuery = true)
    void deleteAllData();

    /**
     * Удаляет записи из таблицы indexes, связанные с указанным индефикатаром старницы.
     *
     * @param pageId индификатор страницы, по которой нужно удалить запись из таблицы
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM indexes WHERE page_id = :pageId", nativeQuery = true)
    void deleteByPage(@Param("pageId") Integer pageId);

    /**
     * Находит индефикаторы лемм, связанные с указанным индефикатором страницы.
     *
     * @param pageId индефикатор страниыцы, для которой необходимо найти леммы.
     * @return списко индификаторов лемм.
     */
    @Query(value = "SELECT lemma_id FROM indexes WHERE page_id = :pageId",
            nativeQuery = true)
    List<Integer> findLemmasIdByPage(@Param("pageId") Integer pageId);

    /**
     * Получает ранги для указанных лемм и индификатора страницы.
     *
     * @param lemmas список лемм, для которых необходимо получить ранги.
     * @param pageId индификатор страницы, для которой необходимо получить ранги.
     * @return список рангов.
     */
    @Query(value = "SELECT i.rank FROM indexes AS i JOIN lemmas AS l ON i.lemma_id = l.id " +
            "WHERE l.lemma IN (:lemmas) " +
            "AND i.page_id = :pageId",
            nativeQuery = true)
    List<Integer> getRankByLemmasAndPageId(@Param("lemmas") List<String> lemmas,
                                           @Param("pageId") int pageId);
}
