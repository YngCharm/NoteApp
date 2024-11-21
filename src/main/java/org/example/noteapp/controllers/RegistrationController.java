package org.example.noteapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.noteapp.DatabaseManager;
import org.example.noteapp.supportsClass.PasswordUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistrationController {
    public PasswordField registrationPassword;
    public TextField registrationLogin;
    public TextField registrationEmail;
    public AnchorPane anchorPane;
    @Setter
    private DatabaseManager databaseManager;

    public RegistrationController() {
        databaseManager = new DatabaseManager();
    }

    public void registrationButtonClick(ActionEvent actionEvent) {
        String login = registrationLogin.getText();
        String email = registrationEmail.getText();
        String password = registrationPassword.getText();

        registerUser(login, email, password);
    }


    private void registerUser(String login, String email, String password) {
        String checkExistUser = "SELECT login, email FROM users WHERE login = ? OR email = ?";
        String addUserQuery = "INSERT INTO users (login, email, password_hash) VALUES (?, ?, ?)";
        Connection connection = null;
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        try {
            connection = databaseManager.getConnection();
            if (connection == null){
                System.err.println("Не удалось установить соединение с базой данных.");
                return;
            }
            PreparedStatement insertStmt = connection.prepareStatement(addUserQuery);
            PreparedStatement checkStmt = connection.prepareStatement(checkExistUser);
            checkStmt.setString(1, login);
            checkStmt.setString(2, email);
            try(ResultSet rs = checkStmt.executeQuery()){
                if (rs.next()){
                    System.out.println("Пользователь с таким логином или почтой уже существует.");
                    return;
                }
            }
            if (email.matches(emailPattern)) {
                String passwordHash = PasswordUtils.hashPassword(password);
                insertStmt.setString(1, login);
                insertStmt.setString(2, email);
                insertStmt.setString(3, passwordHash);
                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Пользователь успешно зарегистрирован.");
                } else {
                    System.out.println("Ошибка при регистрации пользователя.");
                }
            }else {
                System.out.println("Неверный формат почты.");
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Ошибка при регистрации пользователя.");

        }
    }

    public void registeredLabelClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/noteapp/entrance-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
