package searchengine.config.Init;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import searchengine.config.RequestData;

/**
 * Инициализатор для данных запроса.
 * <p>
 *      Этот класс отвечает за получение экземпляра {@link RequestData}
 *      из контекста приложения.
 *</p>
 */
@Component
public class RequestDataInitializer {

    @Getter
    private static RequestData data;

    /**
     * Конструктор, который инициализирует {@link ApplicationContext}
     * из передагнного контекста приложения.
     *
     * @param context контекст приложения, используемый для получения бина {@link RequestData}
     */
    @Autowired
    public RequestDataInitializer(ApplicationContext context){
        data = context.getBean(RequestData.class);
    }

}
