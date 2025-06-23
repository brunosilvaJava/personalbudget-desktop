package com.bts.personalbudgetdesktop.client.personalbudgetapi;

import com.bts.personalbudgetdesktop.mapper.FinancialMovementMapper;
import com.bts.personalbudgetdesktop.mapper.FixedBillMapper;
import com.bts.personalbudgetdesktop.mapper.InstallmentBillMapper;
import com.bts.personalbudgetdesktop.model.FinancialMovement;
import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.InstallmentBill;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;

public class PersonalBudgetApiClient {

    private final FixedBillMapper fixedBillMapper;
    private final InstallmentBillMapper installmentBillMapper;
    private final FinancialMovementMapper financialMovementMapper;
    private final ObjectMapper objectMapper;

    private static final String API_URL_FB = "http://localhost:8080/fixed_bill";
    private static final String API_URL_IB = "http://localhost:8080/installment_bill";
    private static final String API_URL_FM = "http://localhost:8080/financial_movement";

    public PersonalBudgetApiClient() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        fixedBillMapper = FixedBillMapper.INSTANCE;
        installmentBillMapper = InstallmentBillMapper.INSTANCE;
        financialMovementMapper = FinancialMovementMapper.INSTANCE;
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void saveFixedBill(FixedBill fixedBill) {
        try {
            URL url = new URL(API_URL_FB);
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

    public void saveInstallmentBill(InstallmentBill installmentBill) {
        // Validação do campo installmentCount
        if (installmentBill.installmentCount() == null) {
            showErrorMessage("O total de parcelas não pode estar vazio!");
            return;
        }

        if (installmentBill.installmentCount() <= 0) {
            showErrorMessage("O total de parcelas deve ser maior que zero!");
            return;
        }

        try {
            URL url = new URL(API_URL_IB);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Escreve os dados no corpo da requisição
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = objectMapper.writeValueAsBytes(installmentBillMapper.dtoToRequest(installmentBill));
                os.write(input);
                os.flush();
            }

            // Lê a resposta do servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Conta parcelada salva com sucesso!");
            } else {
                System.err.println("Erro ao salvar conta parcelada: Código HTTP " + responseCode);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    System.err.println("Detalhes do erro: " + response);
                    showErrorMessage("Erro ao salvar: " + response); // opcional
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Erro de conexão ao salvar conta parcelada.");
        }
    }

    public void saveFinancialMovement(FinancialMovement financialMovement) {
        try {
            URL url = new URL(API_URL_FM);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json"); // Opcional, mas recomendado
            connection.setDoOutput(true);

            // Escreve os dados no corpo da requisição
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = objectMapper.writeValueAsBytes(financialMovementMapper.dtoToRequest(financialMovement));
                os.write(input);
                os.flush();
            }

            // Obtém a resposta do servidor
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Conta avulsa salva com sucesso!");
            } else {
                System.err.println("Erro ao salvar conta avulsa: Código HTTP " + responseCode);
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


    public Optional<FixedBill> findFixedBill(UUID code) {
        try {
            URL url = new URL(API_URL_FB + "/" + code);
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

    public Optional<InstallmentBill> findInstallmentBill(UUID code) {
        try {
            URL url = new URL(API_URL_IB + "/" + code);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            InstallmentBillResponse installmentBillResponse = objectMapper.readValue(connection.getInputStream(), InstallmentBillResponse.class);

            connection.disconnect();

            return Optional.ofNullable(installmentBillMapper.toModel(installmentBillResponse));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<FinancialMovement> findFinancialMovement(UUID code) {
        try {
            URL url = new URL(API_URL_FM + "/" + code);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            FinancialMovementResponse financialMovementResponse = objectMapper.readValue(connection.getInputStream(), FinancialMovementResponse.class);

            connection.disconnect();

            return Optional.ofNullable(financialMovementMapper.toModel(financialMovementResponse));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<FixedBill> findFixedBills() {
        try {
            URL url = new URL(API_URL_FB);
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

    public List<InstallmentBill> findInstallmentBills() {
        try {
            URL url = new URL(API_URL_IB);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            List<InstallmentBillResponse> installmentBillResponseList = Arrays.asList(
                    objectMapper.readValue(connection.getInputStream(), InstallmentBillResponse[].class)
            );

            connection.disconnect();

            return installmentBillMapper.responseToModelList(installmentBillResponseList);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<FinancialMovement> findFinancialMovements(LocalDate startDate,
                                                          LocalDate endDate,
                                                          List<String> status,
                                                          String operationType,
                                                          String description) {
        HttpURLConnection connection = null;
        try {
            StringBuilder urlBuilder = new StringBuilder(API_URL_FM + "?");
            if (startDate != null) {
                urlBuilder.append("start_date=").append(startDate).append("&");
            }
            if (endDate != null) {
                urlBuilder.append("end_date=").append(endDate).append("&");
            }
            if (status != null && !status.isEmpty()) {
                String statusParam = String.join(",", status);
                urlBuilder.append("status=").append(java.net.URLEncoder.encode(statusParam, java.nio.charset.StandardCharsets.UTF_8)).append("&");
            }
            if (operationType != null && !operationType.isEmpty()) {
                urlBuilder.append("operation_type=").append(operationType).append("&");
            }
            if (description != null && !description.isEmpty()) {
                urlBuilder.append("description=").append(java.net.URLEncoder.encode(description, java.nio.charset.StandardCharsets.UTF_8)).append("&");
            }
            if (urlBuilder.charAt(urlBuilder.length() - 1) == '&' || urlBuilder.charAt(urlBuilder.length() - 1) == '?') {
                urlBuilder.deleteCharAt(urlBuilder.length() - 1);
            }

            URL url = new URL(urlBuilder.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                List<FinancialMovementResponse> financialMovementResponseList = Arrays.asList(
                        objectMapper.readValue(connection.getInputStream(), FinancialMovementResponse[].class)
                );
                return financialMovementMapper.responseToModelList(financialMovementResponseList);
            } else {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    String errorResponse = in.lines().collect(Collectors.joining("\n"));
                    System.err.println("Erro HTTP " + responseCode + ": " + errorResponse);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return List.of();
    }

    public void deleteFixedBill(UUID fixedBillCode) {
        try {
            URL url = new URL(API_URL_FB + "/" + fixedBillCode);
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

    public void deleteInstallmentBill(UUID installmentBillCode) {
        try {
            URL url = new URL(API_URL_IB + "/" + installmentBillCode);
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

    public void deleteFinancialMovement(UUID financialMovementCode) {
        try {
            URL url = new URL(API_URL_FM + "/" + financialMovementCode);
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
