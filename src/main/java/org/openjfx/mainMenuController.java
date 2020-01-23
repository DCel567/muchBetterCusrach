package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;

public class mainMenuController {

	@FXML
	private Button buttonReadFromFile;

	@FXML
	private Button buttonDownloadData;

	@FXML
	private Button buttonShowInTable;

	@FXML
	private Button buttonDrawGraphic;

	@FXML
	private Button buttonToLogin;

	@FXML
	void initialize(){

		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("XML", "*.xml"));

		if (App.gotDataFromSite || App.gotDataFromFile) activateFunctions();

		buttonToLogin.setOnAction(actionEvent -> {
			buttonToLogin.getScene().getWindow().hide();
			App.openNewScene("primary");
		});

		buttonDownloadData.setOnAction(actionEvent -> {
			App.gotDataFromSite = true;
			App.gotDataFromFile = false;
			activateFunctions();
		});

		buttonReadFromFile.setOnAction(actionEvent -> {
			File file = fileChooser.showOpenDialog(null);
			if (file != null)
				App.filePath = file.getAbsolutePath();
				App.gotDataFromFile = true;
				App.gotDataFromSite = false;

			activateFunctions();
		});

		buttonDrawGraphic.setOnAction(actionEvent -> {
			try {
				buttonDrawGraphic.getScene().getWindow().hide();
				App.openNewScene("chart");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		buttonShowInTable.setOnAction(actionEvent -> {
			try {
				buttonShowInTable.getScene().getWindow().hide();
				App.openNewScene("table");
			} catch (Exception e){
				e.printStackTrace();
			}
		});
	}

	private void activateFunctions() {
		buttonShowInTable.setDisable(false);
		buttonDrawGraphic.setDisable(false);
	}
}