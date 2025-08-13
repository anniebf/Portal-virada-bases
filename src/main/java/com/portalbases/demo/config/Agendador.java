package com.portalbases.demo.config;

import com.portalbases.demo.utils.ManipuladorCSV;
import com.portalbases.demo.models.Retorno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class Agendador{

    private static final Logger logger = LoggerFactory.getLogger(Agendador.class);

    @Scheduled(cron = "0 0 6 * * *") // a cada todo dia às 6:00 AM
    //@Scheduled(cron = "0 * * * * *")
    public void tarefaRepetitiva() {
        logger.info("Tarefa agendada executada a cada 1 dia");
        ManipuladorCSV dadosBanco = new ManipuladorCSV();
        Retorno retorno = dadosBanco.buscarDadosNoOracle();

        List<String> listaBases = retorno.getBase();
        List<String> listaDatas = retorno.getUltimaOperacao().stream()
                .map(timestamp -> timestamp != null ? timestamp.toString() : "")
                .collect(Collectors.toList());

        ManipuladorCSV Csv = new ManipuladorCSV();
        Csv.processarCSVComVariaveis(listaBases, listaDatas);

    }
}
