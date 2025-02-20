package searchengine.task;

import org.junit.jupiter.api.Test;
import searchengine.model.Site;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateStatusTimeTaskTest {
    @Test
    public void testStartingTaskToUpdateTheStatusTime_StartTask() throws InterruptedException {
        Site site = new Site();
        UpdateStatusTimeTask.startingTaskToUpdateTheStatusTime(site);

        Thread.sleep(2000);
        assertNotNull(site.getStatusTime());
    }

    @Test
    public void testStoppingTaskToUpdateTheStatusTime_StopTask(){
        UpdateStatusTimeTask.stoppingTaskToUpdateTheStatusTime();

        assertEquals(2, Thread.activeCount());
    }
}
