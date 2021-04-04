package info.caprese.capelline.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class CapellineErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping
    public ModelAndView error(HttpServletRequest req, ModelAndView mav) {

        // どのエラーでも 404 Not Found にする
        // 必要に応じてステータコードや出力内容をカスタマイズ可能
        mav.setStatus(HttpStatus.NOT_FOUND);

        // ビュー名を指定する
        // Thymeleaf テンプレート src/main/resources/templates/error.html を使用
        mav.setViewName("error");

        return mav;
    }

}
