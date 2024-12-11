package searchengine.task;

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

public class UpdateStatusTimeTask {

    private static final SiteRepository siteRepository = SiteRepositoryInitializer.getSiteRepository();
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final List<ScheduledFuture<?>> statusUpdateFutureList = new ArrayList<>();

    public static void startingTaskToUpdateTheStatusTime(searchengine.model.Site site) {
        Runnable task = () -> {
            try {
                site.setStatusTime(Timestamp.valueOf(LocalDateTime.now()));
                siteRepository.save(site);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        statusUpdateFutureList.add(executorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS));
    }

    public static void stoppingTaskToUpdateTheStatusTime() {
        Iterator<ScheduledFuture<?>> iterator = statusUpdateFutureList.iterator();
//        for (ScheduledFuture<?> task : statusUpdateFutureList) {
//            task.cancel(false);
//        }
        while (iterator.hasNext()){
            ScheduledFuture<?> task = iterator.next();
            task.cancel(false);
            if(!iterator.hasNext()){
                updateStatusSite();
            }
        }
    }

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
