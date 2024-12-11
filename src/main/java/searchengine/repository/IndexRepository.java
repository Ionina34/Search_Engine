package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Index;

import java.util.List;

public interface IndexRepository extends JpaRepository<Index, Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE indexes AUTO_INCREMENT = 1",
            nativeQuery = true)
    void updateIdentity();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM indexes", nativeQuery = true)
    void deleteAllData();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM indexes WHERE page_id = :pageId", nativeQuery = true)
    void deleteByPage(@Param("pageId") Integer pageId);

    @Query(value = "SELECT lemma_id FROM indexes WHERE page_id = :pageId",
            nativeQuery = true)
    List<Integer> findLemmasIdByPage(@Param("pageId") Integer pageId);

    @Query(value = "SELECT i.rank FROM indexes AS i JOIN lemmas AS l ON i.lemma_id = l.id " +
            "WHERE l.lemma IN (:lemmas) " +
            "AND i.page_id = :pageId " +
            "AND (:siteId IS NULL OR l.site_id = :siteId)",
            nativeQuery = true)
    List<Integer> getRankByLemmasAndPageIdAndSiteId(@Param("lemmas") List<String> lemmas,
                                                  @Param("pageId") int pageId,
                                                  @Param("siteId") Integer siteId);
}
