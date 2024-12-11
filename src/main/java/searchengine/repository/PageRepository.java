package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE pages AUTO_INCREMENT = 1",
            nativeQuery = true)
    void updateIdentity();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM pages", nativeQuery = true)
    void deleteAllData();

    Optional<Page> findByPathAndSite(String path, Site site);

    @Query(value = "SELECT COUNT(*) FROM pages WHERE site_id = :siteId",
            nativeQuery = true)
    Integer countPagesBySiteId(@Param("siteId") Integer siteId);

    @Query(value = "SELECT p.id, p.code, p.content, p.path, p.site_id FROM pages AS p " +
            "JOIN indexes AS i ON p.id = i.page_id " +
            "JOIN lemmas AS l ON l.id = i.lemma_id " +
            "WHERE l.lemma = :lemma",
            nativeQuery = true)
    List<Page> findPageByLemma(@Param("lemma") String lemma);

    @Query(value = "SELECT p.id, p.code, p.content, p.path, p.site_id FROM pages AS p " +
            "JOIN indexes AS i ON p.id = i.page_id " +
            "JOIN lemmas AS l ON l.id = i.lemma_id " +
            "WHERE l.lemma = :lemma AND p.id IN (:pages)",
            nativeQuery = true)
    List<Page> findPageByLemmaAndPageId(@Param("lemma") String lemma, @Param("pages") List<Integer> pagesId);
}

