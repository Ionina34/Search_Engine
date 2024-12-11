package searchengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sites")
@Data
@NoArgsConstructor
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

    @OneToMany(mappedBy = "site",cascade = CascadeType.PERSIST)
    private Set<Page> pages=new HashSet<>();

    @OneToMany(mappedBy = "site", cascade = CascadeType.PERSIST)
    private Set<Lemma> lemmas = new HashSet<>();
}
