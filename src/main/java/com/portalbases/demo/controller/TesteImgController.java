package com.portalbases.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteImgController {

    private final ResourceLoader resourceLoader;

    public TesteImgController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/testeImagem")
    public ResponseEntity<Resource> testeImagem() {
        Resource resource = resourceLoader.getResource("classpath:/static/img/logo_bf.png");
        return ResponseEntity.ok(resource);
    }
}
