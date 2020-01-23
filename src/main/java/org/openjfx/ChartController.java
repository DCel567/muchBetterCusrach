package org.openjfx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

public class ChartController {

	@FXML
	private LineChart<Integer, Double> lineChart;

	@FXML
	private Button buttonBack;

	@FXML
	private NumberAxis xAxis;

	@FXML
	private Label labelChartName;

	@FXML
	private TextField fieldColor;

	@FXML
	private Button buttonColor;

	@FXML
	private Button buttonTrendLine;

	private XYChart.Series<Integer, Double> series;
	PointsDBHandler dbHandler = new PointsDBHandler();

	@FXML
	void initialize() {

		if (App.gotDataFromFile) {
			draw_chart(create_series_from_file());
		} else if(App.gotDataFromSite) {
			draw_chart(create_series_from_site());
		}


		buttonBack.setOnAction(actionEvent -> {
			buttonBack.getScene().getWindow().hide();
			App.openNewScene("mainMenu");
		});

		buttonColor.setOnAction(actionEvent -> {
			String color = fieldColor.getText().trim();
			String regex = "#[0-9a-fA-F]{6}";
			Pattern pattern = Pattern.compile(regex);

			Matcher matcher = pattern.matcher(color);
			if(matcher.matches()){

				Platform.runLater(() -> {
					for (XYChart.Data<Integer, Double> entry : series.getData()) {
						entry.getNode().lookup(".default-color0.chart-line-symbol").setStyle("-fx-background-color: " + color + ", white;");
					}
					lineChart.lookup(".default-color0.chart-series-line").setStyle("-fx-stroke: " + color + ";");
				});
			}
		});

		buttonTrendLine.setOnAction(actionEvent -> {
			if (App.gotDataFromFile) {
				draw_chart(create_series_from_file(), trend_line());
			} else if(App.gotDataFromSite) {
				draw_chart(create_series_from_site(), trend_line());
			}
		});
	}

	private XYChart.Series<Integer, Double> trend_line(){
		XYChart.Series<Integer, Double> series2 = new XYChart.Series<>();
		try{
			ResultSet points = dbHandler.getPoints();

			series2.setName("trendLine");
			points.next();
			int n = 0;
			double x1 = points.getDouble(2);

			double sumX = 0, sumXX = 0, sumY = 0, sumXY = 0;
			while(points.next()) {
				n++;
				double x = points.getDouble(2);
				double y = points.getDouble(3);
				sumX += x;
				sumY += y;
				sumXX += Math.pow(x, 2);
				sumXY += x * y;
			}
			double a = (sumXY - sumX * sumY / n) / (sumXX - sumX * sumX / n);
			double b = sumY / n - a * sumX / n;
			points.previous();
			System.out.println(n + " " + a + " " + b);
			double x2 = points.getDouble(2);

			series2.getData().add(new XYChart.Data<>((int)x1, a*x1+b));
			System.out.println("a = " + a*x1+b);
			series2.getData().add(new XYChart.Data<>((int)x2, a*x2+b));
			System.out.println("b = " + a*x2+b);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		return series2;
	}

	void draw_chart(XYChart.Series<Integer, Double> series){
		lineChart.getData().setAll(series);
	}

	void draw_chart(XYChart.Series<Integer, Double> s1, XYChart.Series<Integer, Double> s2){
		lineChart.getData().add(s2);
	}

	private XYChart.Series<Integer, Double> create_series_from_site(){
		lineChart.setLegendVisible(false);
		series = new XYChart.Series<>();
		series.setName("Inflation");
		labelChartName.setText("Inflation in Russia, %");

		xAxis.setAutoRanging(false);

		try{
			dbHandler.clear_table();
			org.jsoup.nodes.Document document = Jsoup.connect("https://bankirsha.com/UROVEN-INFLYACII-V-ROSSIYSKOY-FEDERACII-PO-GODAM.HTML").get();
			int i = 0;
			for(org.jsoup.nodes.Element row : document.select("table.table tr")){
				if(row.select("td:nth-of-type(1)").text().equals("")){ continue; }
				else {
					final String ticker = row.select("td:nth-of-type(1)").text();
					if (ticker.length() == 4){
						int id = i++-1;

						final int year = Integer.parseInt(ticker);
						String valTemp = row.select("td:nth-of-type(2)").text().replace(",", ".");
						final double val = Double.parseDouble(valTemp);

						if (i == 1)	xAxis.setUpperBound(year);
						else xAxis.setLowerBound(year);

						series.getData().add(new XYChart.Data<>(year, val));
						Point p = new Point(id, year, val);
						dbHandler.add_point(p);
					}
				}
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		xAxis.setTickUnit(1);

		return series;
	}

	private XYChart.Series<Integer, Double> create_series_from_file(){
		dbHandler.clear_table();
		lineChart.setLegendVisible(false);
		series = new XYChart.Series<>();
		series.setName("Your chart");

		File xmlFile = new File(App.filePath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		try{
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();

			NodeList names = document.getElementsByTagName("data");
			String seriesName = names.item(0).getAttributes().getNamedItem("name").getNodeValue();
			if(seriesName != null && !seriesName.equals(""))
				labelChartName.setText(seriesName);

			NodeList points = document.getElementsByTagName("point");
			xAxis.setAutoRanging(false);

			PointsDBHandler dbHandler = new PointsDBHandler();

			for (int i = 0; i < points.getLength(); i++) {
				int id = i+1;
				Node point = points.item(i);
				int x = Integer.parseInt(point.getAttributes().getNamedItem("x").getNodeValue());
				if (i == 0)
					xAxis.setLowerBound(x);
				xAxis.setUpperBound(x);
				double y = Double.parseDouble(point.getAttributes().getNamedItem("y").getNodeValue());

				series.getData().add(new XYChart.Data<>(x, y));

				Point p = new Point(id, x, y);
				dbHandler.add_point(p);
			}
			xAxis.setTickUnit(1);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return series;
	}
}