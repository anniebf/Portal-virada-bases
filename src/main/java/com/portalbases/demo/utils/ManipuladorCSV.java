package com.portalbases.demo.utils;

import java.io.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.portalbases.demo.models.Retorno;
import org.springframework.stereotype.Component;

@Component
public class ManipuladorCSV {


    public void processarCSVComVariaveis(List<String> lista1, List<String> lista2) {
        String CSV_PATH = "src/main/resources/bat/viradaBase.csv";
        String CSV_SAIDA = "src/main/resources/bases/saida.csv";

        Map<String, String> aliases = new HashMap<>();
        aliases.put("HOMOLOGACAO", "HOMOLLNX");
        aliases.put("DEBUG", "DEBUGLNX");
        aliases.put("AMB1", "AMB1LNX");
        aliases.put("AMB2", "AMB2LNX");
        aliases.put("AMB3", "AMB3LNX");
        aliases.put("AMB4", "AMB4LNX");
        aliases.put("AMB5", "AMB5LNX");
        aliases.put("DEVBF", "DEVBF2LNX");
        aliases.put("DEVBF2", "DEVBF2LNX");
        aliases.put("RMFOLHA", "RMFOLHA");

        try (
                BufferedReader br = new BufferedReader(new FileReader(CSV_PATH));
                BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_SAIDA))) {

            // Escrever novo cabeçalho
            bw.write("\"Base\";\"ultima Execucao\";\"proxima Execucao\"");
            bw.newLine();

            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] colunas = linha.split(",");
                if (colunas.length < 2) continue;

                String primeiraColuna = colunas[0].replace("\"", "").trim();
                String segundaColuna = colunas[1].replace("\"", "").trim();
                boolean encontrou = false;

                // Extrair a parte relevante do nome da base
                String nomeBase = primeiraColuna.substring(primeiraColuna.lastIndexOf("_") + 1)
                        .replace("COPIA_", "")
                        .toUpperCase();

                if (nomeBase.equalsIgnoreCase("PROJETO") || nomeBase.equalsIgnoreCase("RELBD")) {
                    continue; // pula esta iteração do while
                }
                String nomeComparacao = aliases.getOrDefault(nomeBase, nomeBase + "LNX");

                for (int i = 0; i < lista1.size(); i++) {
                    String var1 = lista1.get(i);
                    String var2 = lista2.get(i);

                    System.out.println("Comparando var1=" + var1 + " com nomeComparacao=" + nomeComparacao);
                    if (var1.toUpperCase().equals(nomeComparacao)) {
                        // Formatar datas
                        String dataFormatada = formatarDataParaYYYYMMDD(var2);
                        String proximaExecFormatada = "N/A";

                        if (!segundaColuna.equalsIgnoreCase("N/A")) {
                            proximaExecFormatada = formatarDataParaYYYYMMDD(parseOutroFormatoData(segundaColuna));
                        }

                        String novaLinha = String.format("\"%s\";\"%s\";\"%s\"",
                                var1, dataFormatada, proximaExecFormatada);
                        System.out.println("Adicionando: " + novaLinha);
                        bw.write(novaLinha);
                        bw.newLine();
                        encontrou = true;
                        break;
                    }
                }

                if (!encontrou) {
                    String proximaExecFormatada = "N/A";
                    if (!segundaColuna.equalsIgnoreCase("N/A")) {
                        proximaExecFormatada = formatarDataParaYYYYMMDD(parseOutroFormatoData(segundaColuna));
                    }

                    String novaLinha = String.format("\"%s\";\"N/A\";\"%s\"",
                            primeiraColuna, proximaExecFormatada);
                    bw.write(novaLinha);
                    bw.newLine();
                }
            }

            System.out.println("Arquivo processado com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao processar CSV: " + e.getMessage());
        }
    }

    // Método para formatar datas no padrão YYYY-MM-DD
    private String formatarDataParaYYYYMMDD(String data) {
        if (data == null || data.equalsIgnoreCase("N/A")) {
            return "N/A";
        }

        try {
            // Tenta parsear no formato do segundo CSV (2025-07-09)
            SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdfInput.parse(data);
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd");
            return sdfOutput.format(date);
        } catch (ParseException e1) {
            try {
                // Tenta parsear no formato do primeiro CSV (8/6/2025 4:00:00 AM)
                SimpleDateFormat sdfInput = new SimpleDateFormat("M/d/yyyy h:mm:ss a");
                Date date = sdfInput.parse(data);
                SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd");
                return sdfOutput.format(date);
            } catch (ParseException e2) {
                System.err.println("Erro ao formatar data: " + data);
                return "N/A";
            }
        }
    }

    // Método para parsear datas no formato do primeiro CSV (8/6/2025 4:00:00 AM)
    private String parseOutroFormatoData(String data) {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("M/d/yyyy h:mm:ss a");
            Date date = sdfInput.parse(data);
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd");
            return sdfOutput.format(date);
        } catch (ParseException e) {
            System.err.println("Erro ao parsear data: " + data);
            return "N/A";
        }
    }

  public Retorno buscarDadosNoOracle() {
        String jdbcUrl = "jdbc:oracle:thin:@10.184.1.60:1521/cdbhomol_x9m";
        String usuario = "c##python";
        String senha = "PY_7h0n#";

        List<String> bases = new ArrayList<>();
        List<Timestamp> ultimasOperacoes = new ArrayList<>();

        String sql = "SELECT PDB_NAME, MAX(OP_TIMESTAMP) FROM CDB_PDB_HISTORY where OPERATION = 'CLONE' GROUP BY PDB_NAME ORDER BY 2 DESC";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, usuario, senha);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            //stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String base = rs.getString("PDB_NAME");
                Timestamp ultimaOperacao = rs.getTimestamp("MAX(OP_TIMESTAMP)");

                bases.add(base);
                ultimasOperacoes.add(ultimaOperacao);

                System.out.println(base + " | data: " + ultimaOperacao);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao consultar Oracle: " + e.getMessage());
            e.printStackTrace();
        }
        return new Retorno(bases, ultimasOperacoes);
    }
}