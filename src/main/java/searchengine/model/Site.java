package searchengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс представляющий сайт в базе данных.
 * Сожержит информацию о связи между леммами {@link Lemma} и страницами {@link Page},
 * статусе индексации {@link StatusIndexing}, ошибке,
 * последнем изменением, URL и название сайта.
 */
@Entity
@Table(name = "sites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')",
            nullable = false)
    private StatusIndexing status;

    @Column(name = "status_time", nullable = false)
    private Timestamp statusTime;

    @Column(name = "last_error",
            columnDefinition = "TEXT")
    private String lastError;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String url;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @OneToMany(mappedBy = "site", cascade = CascadeType.PERSIST)
    private Set<Page> pages = new HashSet<>();

    @OneToMany(mappedBy = "site", cascade = CascadeType.PERSIST)
    private Set<Lemma> lemmas = new HashSet<>();

    public Site(Integer id, StatusIndexing status, Timestamp statusTime, String lastError, String url, String name) {
        this.id = id;
        this.status = status;
        this.statusTime = statusTime;
        this.lastError = lastError;
        this.url = url;
        this.name = name;
    }
}
