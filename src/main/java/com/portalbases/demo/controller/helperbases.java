package com.portalbases.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // Mudamos de @RestController para @Controller
public class helperbases{

    @GetMapping("/")  // Mapeia para a raiz do site
    public String showPortal() {
        return "portaldev";  // Retorna o nome do template sem extensão
    }
}
