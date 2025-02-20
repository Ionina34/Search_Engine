package searchengine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер по умолчанию для обработки запросов по корневому URL.
 * <p>
 *     Этот контроллер отвечает за отображение главной страницы приложения,
 *     загружая HTML-файл index.html из папки resources/templates с помощью библеотеки
 *     Thymeleaf
 * </p>
 */
@Controller
public class DefaultController {

    /**
     * Метод формирует страницу из HTML-файла index.html,
     * который находится в папке resources/templates.
     * Это делает библиотека Thymeleaf.
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
