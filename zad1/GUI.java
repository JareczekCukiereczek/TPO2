package zad1;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private final JTextField cityField;
    private final JTextField currencyField;
    private final JTextField countryField;
    private final JTextArea rateArea;
    private final JTextArea nbpRateArea;
    private final JTextArea weatherArea;
    private final JFXPanel webWiewPanel;

    public GUI() {
        setTitle("Weather & Currency Info");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.LIGHT_GRAY);

        // Panele dla inputow i wyniku
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.setBackground(Color.LIGHT_GRAY);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel resultPanel = new JPanel(new GridLayout(3, 1));
        resultPanel.setBackground(Color.LIGHT_GRAY);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Inputy itd
        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField();
        JLabel countryLabel = new JLabel("Country:");
        countryField = new JTextField();
        JLabel currencyLabel = new JLabel("Rate for:");
        currencyField = new JTextField();

        inputPanel.add(cityLabel);
        inputPanel.add(cityField);

        inputPanel.add(countryLabel);
        inputPanel.add(countryField);

        inputPanel.add(currencyLabel);
        inputPanel.add(currencyField);

        JLabel weatherLabel = new JLabel("Weather Info:");
        weatherArea = new JTextArea(5, 20);
        weatherArea.setEditable(false);


        JLabel nbpRateLabel = new JLabel("NBPRate Info:");
        nbpRateArea = new JTextArea(5, 20);
        nbpRateArea.setEditable(false);

        JLabel rateLabel = new JLabel("CurrencyRate Info:");
        rateArea = new JTextArea(5, 20);
        rateArea.setEditable(false);


        resultPanel.add(weatherLabel);
        resultPanel.add(new JScrollPane(weatherArea));

        resultPanel.add(rateLabel);
        resultPanel.add(new JScrollPane(rateArea));

        resultPanel.add(nbpRateLabel);
        resultPanel.add(new JScrollPane(nbpRateArea));

        // input panel/result panel po lewej stronie
        JPanel lewyPanel = new JPanel(new BorderLayout());
        lewyPanel.add(inputPanel, BorderLayout.NORTH);
        lewyPanel.add(resultPanel, BorderLayout.CENTER);
        add(lewyPanel, BorderLayout.WEST);

        // web wiev panelik
        webWiewPanel = new JFXPanel();
        JPanel webPanel = new JPanel(new BorderLayout());
        webPanel.setBackground(Color.LIGHT_GRAY);
        webPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        webPanel.add(webWiewPanel, BorderLayout.CENTER);
        add(webPanel, BorderLayout.CENTER);

        // przcisk
        JPanel przycisk = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.DARK_GRAY);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchData();
            }
        });
        przycisk.add(submitButton);
        add(przycisk, BorderLayout.SOUTH);

        //wysokosc
        setSize(1024, 800);
        setVisible(true);
    }

    private void fetchData() {
        String miasto = cityField.getText();
        String kraj = countryField.getText().replace(" ", "%20");
        String waluta = currencyField.getText();

        Service service = new Service(kraj);
        String weatherInfo = service.getWeather(miasto);
        String currencyRate = String.format("%.2f", service.getRateFor(waluta));
        String nbpRate = String.format("%.2f", service.getNBPRate());

        weatherArea.setText(weatherInfo);
        rateArea.setText(currencyRate);
        nbpRateArea.setText(nbpRate);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            webEngine.load("https://en.wikipedia.org/wiki/" + miasto);
            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, state) -> {
                if (state == Worker.State.SUCCEEDED) {
                    Scene scenka = new Scene(new StackPane(webView));
                    webWiewPanel.setScene(scenka);
                }
            });
        });
    }
}
