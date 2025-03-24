package com.bts.personalbudgetdesktop.client.personalbudgetapi;

import com.bts.personalbudgetdesktop.mapper.FixedBillMapper;
import com.bts.personalbudgetdesktop.model.FixedBill;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersonalBudgetApiClient {

    private final FixedBillMapper fixedBillMapper;
    private final ObjectMapper objectMapper;

    private static final String API_URL = "http://localhost:8080/fixed_bill";

    public PersonalBudgetApiClient() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        fixedBillMapper = FixedBillMapper.INSTANCE;
    }

    public void saveFixedBill(FixedBill fixedBill) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json"); // Opcional, mas recomendado
            connection.setDoOutput(true);

            // Escreve os dados no corpo da requisição
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = objectMapper.writeValueAsBytes(fixedBillMapper.dtoToRequest(fixedBill));
                os.write(input);
                os.flush();
            }

            // Obtém a resposta do servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Conta fixa salva com sucesso!");
            } else {
                System.err.println("Erro ao salvar conta fixa: Código HTTP " + responseCode);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    System.err.println("Detalhes do erro: " + response);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Optional<FixedBill> find(UUID code) {
        try {
            URL url = new URL(API_URL + "/" + code);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            FixedBillResponse fixedBillResponse = objectMapper.readValue(connection.getInputStream(), FixedBillResponse.class);

            connection.disconnect();

            return Optional.ofNullable(fixedBillMapper.toModel(fixedBillResponse));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<FixedBill> findFixedBills() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            List<FixedBillResponse> fixedBillResponseList = Arrays.asList(
                    objectMapper.readValue(connection.getInputStream(), FixedBillResponse[].class)
            );

            connection.disconnect();

            return fixedBillMapper.responseToModelList(fixedBillResponseList);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void delete(UUID fixedBillCode) {
        try {
            URL url = new URL(API_URL + "/" + fixedBillCode);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Deletado com sucesso!");
            } else {
                System.err.println("Erro ao deletar: Código HTTP " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
