package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class TableController {

	@FXML
	private TableView<Point> table;

	@FXML
	private Label labelTableName;

	@FXML
	private Button buttonBack;

	PointsDBHandler dbHandler = new PointsDBHandler();

	@FXML
	void initialize() {

		TableColumn<Point, Integer> n1 = new TableColumn<>("ID");
		n1.setMinWidth(50);
		n1.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Point, Double> x1 = new TableColumn<>("x");
		x1.setMinWidth(75);
		x1.setCellValueFactory(new PropertyValueFactory<>("x"));

		TableColumn<Point, Double> y1 = new TableColumn<>("y");
		y1.setMinWidth(75);
		y1.setCellValueFactory(new PropertyValueFactory<>("y"));

		System.out.println("initializing tableController");

		buttonBack.setOnAction(actionEvent -> {
			buttonBack.getScene().getWindow().hide();
			App.openNewScene("mainMenu");
		});

		if (App.gotDataFromFile) {
			System.out.println("We got data from file");
			draw_table(create_list_from_file(), n1, x1, y1);
		} else if(App.gotDataFromSite) {
			System.out.println("Creating table from site...");
			draw_table(create_list_from_site(), n1, x1, y1);
		}
	}

	private void draw_table(ObservableList<Point> series, TableColumn<Point, Integer> n1,
							TableColumn<Point, Double> x1, TableColumn<Point, Double> y1) {
		System.out.println("Drawing table");
		table.setItems(series);
		table.getColumns().addAll(n1, x1, y1);
	}

	private ObservableList<Point> create_list_from_file() {
		dbHandler.clear_table();
		System.out.println("Reading data");
		ObservableList<Point> points = FXCollections.observableArrayList();

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
				labelTableName.setText(seriesName);

			NodeList ps = document.getElementsByTagName("point");

			for (int i = 0; i < ps.getLength(); i++) {
				int id = i+1;
				Node point = ps.item(i);
				int x = Integer.parseInt(point.getAttributes().getNamedItem("x").getNodeValue());
				double y = Double.parseDouble(point.getAttributes().getNamedItem("y").getNodeValue());

				Point p = new Point(id, x, y);
				points.add(p);
				dbHandler.add_point(p);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return points;
	}

	private ObservableList<Point> create_list_from_site() {
		ObservableList<Point> points = FXCollections.observableArrayList();
		try{
			dbHandler.clear_table();
			org.jsoup.nodes.Document document = Jsoup.connect("https://bankirsha.com/UROVEN-INFLYACII-V-ROSSIYSKOY-FEDERACII-PO-GODAM.HTML").get();
			int i = 0;
			for(org.jsoup.nodes.Element row : document.select("table.table tr")){
				if(row.select("td:nth-of-type(1)").text().equals("")){ continue; }
				else {
					final String ticker = row.select("td:nth-of-type(1)").text();
					if (ticker.length() == 4){
						int id = i+++1;

						final double year = Double.parseDouble(ticker);
						String valTemp = row.select("td:nth-of-type(2)").text().replace(",", ".");
						final double val = Double.parseDouble(valTemp);

						Point p = new Point(id, year, val);
						points.add(p);
						dbHandler.add_point(p);
					}
				}
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		return points;
	}
}