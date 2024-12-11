package searchengine.config.Init;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import searchengine.config.RequestData;

@Component
public class RequestDataInitializer {

    @Getter
    private static RequestData data;

    @Autowired
    public RequestDataInitializer(ApplicationContext context){
        data = context.getBean(RequestData.class);
    }

}
