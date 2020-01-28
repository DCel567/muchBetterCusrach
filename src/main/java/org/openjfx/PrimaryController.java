package org.openjfx;

import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    private TextField fieldUsername;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private Button buttonLogIn;

    @FXML
    private Button buttonToRegistration;

	@FXML
	private Label labelWrongInput;

    @FXML
    void initialize() {

        buttonToRegistration.setOnAction(actionEvent -> {
            System.out.println("To registration button hitted!");
            try {
                App.setRoot("signInScene");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        buttonLogIn.setOnAction(actionEvent -> {
            System.out.println("Log in button hitted!");
            String nickname = fieldUsername.getText().trim();
            String password = fieldPassword.getText().trim();

            if (!nickname.equals("") && !password.equals("")){
                if (loginUser(nickname, password)){
					buttonLogIn.getScene().getWindow().hide();
					App.openNewScene("mainMenu");
				} else {
					System.out.println("Incorrect input");
				}
			} else {
                System.out.println("Incorrect input");
            }
        });
    }

    private boolean loginUser(String nickname, String password) {
		UserDBHandler dbHandler = new UserDBHandler();
		User user = new User();
		user.username = nickname;
		user.password = password;
		ResultSet results = dbHandler.getUser(user);
		try {
			int resultSetSize = 0;
			while(results.next()){
				resultSetSize++;
			}
			System.out.println(resultSetSize);
			if (resultSetSize >= 1){
				System.out.println("Logged in");
				return true;
			} else {
				labelWrongInput.setVisible(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}