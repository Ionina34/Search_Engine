package searchengine.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий сайт с его URL и именем
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Site {
    private String url;
    private String name;
}
