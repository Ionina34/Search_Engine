package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import searchengine.model.Site;

import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE sites AUTO_INCREMENT = 1",
            nativeQuery = true)
    void updateIdentity();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sites", nativeQuery = true)
    void deleteAllData();

    Optional<Site> findByUrl(String url);

    Optional<Site> findByName(@Param("name") String name);

    @Query(value = "SELECT COUNT(*) FROM sites", nativeQuery = true)
    Integer getNumSites();

    @Query(value = "SELECT * FROM sites WHERE url = :url",
    nativeQuery = true)
    Site getSiteByPath(@Param("url") String url);
}
