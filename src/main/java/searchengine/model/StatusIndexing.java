package searchengine.model;

/**
 * Перечисление, представляющее возможные статусы индексации.
 * <p>
 *     Статусы индексации включают:
 *     <b>INDEXING</b> - процесс индексации в данный момент выполняется.
 *     <b>INDEXED</b> - индексация завершена успешно.
 *     <b>FAILED</b> - индексация завершилоась с ошибкой.
 * </p>
 */
public enum StatusIndexing {
    INDEXING,
    INDEXED,
    FAILED
}
