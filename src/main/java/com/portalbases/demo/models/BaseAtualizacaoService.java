package com.portalbases.demo.models;
import com.portalbases.demo.utils.CsvUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class BaseAtualizacaoService {

    //private static final String CSV_PATH = "bases/saida.csv";

    public List<BaseAtualizacao> carregarBases() {
        List<BaseAtualizacao> bases = new ArrayList<>();
        Path caminho = Paths.get("S:/Portal_Virada_Bases/PortalViradaBases/src/main/resources/bases/saida.csv");
        File file = caminho.toFile();

        System.out.println("Tentando acessar: " + file.getAbsolutePath());
        System.out.println("Existe? " + file.exists());
        System.out.println("Pode ler? " + file.canRead());
        System.out.println("É arquivo? " + file.isFile());

        if (!Files.exists(caminho)) {
            System.err.println("ARQUIVO NÃO ENCONTRADO!");
            return bases;
        }

        try (BufferedReader br = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            System.out.println("Arquivo aberto com sucesso. Iniciando leitura...");

            // Pular cabeçalho
            String header = br.readLine();
            System.out.println("Cabeçalho: " + header);

            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                System.out.printf("Linha %d: %s%n", lineNumber, line);

                try {
                    // Separador por ponto e vírgula
                    String[] values = line.split(";", -1);

                    if (values.length < 2) {
                        System.out.println("Linha ignorada - formato inválido");
                        continue;
                    }

                    BaseAtualizacao base = new BaseAtualizacao();
                    base.setNomeBase(CsvUtils.cleanValue(values[0]));
                    base.setUltimaAtualizacao(CsvUtils.parseDate(CsvUtils.cleanValue(values[1])));

                    if (values.length > 2) {
                        base.setProximaAtualizacao(CsvUtils.parseDate(CsvUtils.cleanValue(values[2])));
                    }

                    bases.add(base);
                } catch (Exception e) {
                    System.err.printf("Erro na linha %d: %s%n", lineNumber, e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("\nERRO GRAVE AO LER ARQUIVO CSV:");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("Tipo: " + e.getClass().getName());

            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }

        System.out.println("\n===== RESUMO FINAL =====");
        System.out.println("Total de bases carregadas: " + bases.size());
        System.out.println("========================\n");

        return bases;
    }

}
