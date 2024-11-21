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
import org.example.noteapp.DatabaseManager;
import org.example.noteapp.supportsClass.PasswordUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    public PasswordField loginPassword;
    public TextField loginLogin;
    public AnchorPane anchorPane;
    public DatabaseManager databaseManager;

    public LoginController() {
        databaseManager = new DatabaseManager();
    }

    public void entranceButtonClick(ActionEvent actionEvent) {
        String login = loginLogin.getText();
        String password = loginPassword.getText();
        loginUser(login, password);
    }

    private boolean loginUser(String login, String password) {
        String loginUserQuery = "SELECT login, password_hash FROM users WHERE login = ?";

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(loginUserQuery)) {

            if (connection == null) {
                System.err.println("Не удалось соединиться с базой данный");
                return false;
            }
            pstmt.setString(1, login);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    String storedHashedPassword = resultSet.getString("password_hash");

                    if (PasswordUtils.checkPassword(password, storedHashedPassword)) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/noteapp/main-view.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) anchorPane.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        return true;
                    } else {
                        System.out.println("Неверный пароль");
                    }
                } else {
                    System.out.println("Пользователь с таким логином не найден");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void entranceLabelClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/noteapp/registration-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
