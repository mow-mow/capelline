package info.caprese.capelline.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController extends AbstractControlelr {

    @GetMapping(path = "/")
    String showMainPage(Model model) {
        writeTweetIdIfLoggedInWithTwitter();
        return "main/main";
    }
}