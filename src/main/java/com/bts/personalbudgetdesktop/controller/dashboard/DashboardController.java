package com.bts.personalbudgetdesktop.controller.dashboard;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    protected Label saldoTotalLabel;

    @FXML
    protected PieChart graficoDespesas;

    @FXML
    public void initialize() {
        carregarResumoFinanceiro();
    }

    protected void carregarResumoFinanceiro() {
        // Simulação de valores (depois pode vir da API)
        double saldoTotal = 1500.75;
        double despesasFixas = 1800.00;
        double despesasVariaveis = 500.00;
        double lazer = 200.00;

        // Atualiza o saldo total na interface
        saldoTotalLabel.setText(String.format("Saldo Total: R$ %.2f", saldoTotal));

        // Adiciona os dados ao gráfico de pizza
        graficoDespesas.getData().addAll(
                new PieChart.Data("Despesas Fixas", despesasFixas),
                new PieChart.Data("Despesas Variáveis", despesasVariaveis),
                new PieChart.Data("Lazer", lazer)
        );
    }
}
