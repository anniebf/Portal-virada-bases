package com.portalbases.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.portalbases.demo.models.BaseAtualizacao;
import com.portalbases.demo.models.BaseAtualizacaoService;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BasesController {

    private final BaseAtualizacaoService baseService;

    public BasesController(BaseAtualizacaoService baseService) {
        this.baseService = baseService;
    }

    @GetMapping("/teste")
    @ResponseBody
    public String testarThymeleaf() {
        return "Funcionando";
    }

    @GetMapping("/bases")
    public String mostrarBases(Model model) {
        System.out.println("Endpoint /bases acessado"); // Log de debug
        List<BaseAtualizacao> bases = baseService.carregarBases();
        System.out.println("Número de bases carregadas: " + bases.size()); // Debug
        model.addAttribute("bases", bases);
        return "bases/index";
    }
}
