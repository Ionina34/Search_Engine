package searchengine.task;

import lombok.extern.slf4j.Slf4j;
import searchengine.config.Init.SiteRepositoryInitializer;
import searchengine.model.Site;
import searchengine.model.StatusIndexing;
import searchengine.repository.SiteRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Класс отвечает за обновление врмени статуса сайтов в фоновом режиме.
 * Он использует планировщик задач для периодического
 * обновления времени статуса сайта.
 */
@Slf4j
public class UpdateStatusTimeTask {

    private static final SiteRepository siteRepository = SiteRepositoryInitializer.getSiteRepository();
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final List<ScheduledFuture<?>> statusUpdateFutureList = new ArrayList<>();

    /**
     * Запускает задачу для обновления времени статуса указанного сайта.
     * Задача будет выполнятся с заданным интервалом.
     *
     * @param site сайт, для которого необходимо обновить время статуса.
     */
    public static void startingTaskToUpdateTheStatusTime(searchengine.model.Site site) {
        Runnable task = () -> {
            try {
                site.setStatusTime(Timestamp.valueOf(LocalDateTime.now()));
                siteRepository.save(site);
            } catch (Exception e) {
                log.error("Error", e);
            }
        };

        statusUpdateFutureList.add(executorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS));
    }

    /**
     * Останавливает все задачи, обновляющие времени статуса сайтов.
     * После остановки задач обновляет стайтус всех сайтов, который находятся в
     * состоянии индексации.
     */
    public static void stoppingTaskToUpdateTheStatusTime() {
        Iterator<ScheduledFuture<?>> iterator = statusUpdateFutureList.iterator();
        while (iterator.hasNext()){
            ScheduledFuture<?> task = iterator.next();
            task.cancel(false);
            if(!iterator.hasNext()){
                updateStatusSite();
            }
        }
    }

    /**
     * Обновялет статус всех сайтов, которые находятся в сотоянии индексации.
     */
    private static void updateStatusSite() {
        List<Site> sites = siteRepository.findAll();
        for (Site site : sites) {
            if (site.getStatus().equals(StatusIndexing.INDEXING)) {
                site.setStatus(StatusIndexing.INDEXED);
                siteRepository.save(site);
            }
        }
    }
}
