package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecondaryController {

    @FXML
    private TextField fieldUsername;

    @FXML
    private TextField fieldEmail;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private Button buttonRegister;

    @FXML
    private Button buttonBack;

    @FXML
    private Label labelWrong;

    @FXML
    void initialize() {
        buttonBack.setOnAction(actionEvent -> {
           System.out.println("Back button hitted!");
            try {
                App.setRoot("primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        buttonRegister.setOnAction(actionEvent -> {
            System.out.println("Register button hitted!");
            if (signUpUser()){
                try{
                    App.setRoot("primary");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private boolean signUpUser() {
        UserDBHandler dbHandler = new UserDBHandler();
        String username = fieldUsername.getText().trim();
        String password = fieldPassword.getText().trim();
        String email = fieldEmail.getText().trim();

        if (!username.equals("") && !password.equals("") && !email.equals("")){
            if(user_already_exist(username)) {
                labelWrong.setText("User already exists");
                return false;
            }
            User user = new User(username, email, password);
            dbHandler.register_user(user);
            return true;
        } else {
        	labelWrong.setText("Fill all the fields");
        	return false;
        }
    }

    private boolean user_already_exist(String username){
        UserDBHandler dbHandler = new UserDBHandler();
        ResultSet results = dbHandler.get_user_by_name(username);

        try {
            int resultSetSize = 0;
            while(results.next()){
                resultSetSize++;
            }
            if (resultSetSize >= 1){
                System.out.println("User already exists");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
