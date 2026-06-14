package com.techchat.controller;

import com.techchat.service.ActivityLogService;
import com.techchat.service.ActivityLogService.ActivityLog;
import com.techchat.service.UserDataService;
import com.techchat.service.UserDataService.UserData;
import com.techchat.util.FadeInUtil;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminViewController implements Initializable {

    @FXML
    private TabPane adminTabPane;

    @FXML
    private TableView<ActivityLog> activityTable;
    @FXML
    private TableColumn<ActivityLog, String> timestampColumn;
    @FXML
    private TableColumn<ActivityLog, String> usernameColumn;
    @FXML
    private TableColumn<ActivityLog, String> actionColumn;
    @FXML
    private TableColumn<ActivityLog, String> detailsColumn;
    @FXML
    private Label statsLabel;

    @FXML
    private TableView<UserData> userTable;
    @FXML
    private TableColumn<UserData, String> userIdColumn;
    @FXML
    private TableColumn<UserData, String> usernameUserColumn;
    @FXML
    private TableColumn<UserData, String> passwordColumn;
    @FXML
    private TableColumn<UserData, String> nameColumn;
    @FXML
    private TableColumn<UserData, String> roleColumn;
    @FXML
    private TableColumn<UserData, Void> userActionsColumn;
    @FXML
    private Label userStatsLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Play entrance animations
        playEntranceAnimations();

        ActivityLogService.initializeLogFile();
        UserDataService.initializeUserData();

        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));

        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameUserColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        setupUserActionsColumn();

        loadActivityLogs();
        loadUsers();
    }

    /**
     * Play entrance animations for admin view elements
     */
    private void playEntranceAnimations() {
        // Fade in tab pane
        if (adminTabPane != null) {
            FadeInUtil.fadeIn(adminTabPane, 0, 400);
        }
    }

    @FXML
    private void handleRefreshLogs() {
        loadActivityLogs();
    }

    @FXML
    private void handleRefreshUsers() {
        loadUsers();
    }

    private void loadActivityLogs() {
        List<ActivityLog> logs = ActivityLogService.getAllLogs();
        java.util.Collections.reverse(logs);
        ObservableList<ActivityLog> observableLogs = FXCollections.observableArrayList(logs);
        activityTable.setItems(observableLogs);
        statsLabel.setText("Total Logs: " + logs.size());
    }

    private void loadUsers() {
        List<UserData> users = UserDataService.getAllUsers();
        ObservableList<UserData> observableUsers = FXCollections.observableArrayList(users);
        userTable.setItems(observableUsers);
        userStatsLabel.setText("Total Users: " + users.size());
    }

    private void setupUserActionsColumn() {
        userActionsColumn.setCellFactory(param -> new javafx.scene.control.TableCell<>() {
            private final javafx.scene.control.Button deleteBtn = new javafx.scene.control.Button("🗑️ Delete");

            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setOnAction(event -> {
                    UserData user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    private void handleDeleteUser(UserData user) {
        String currentUsername = com.techchat.service.UserSession.getInstance().getCurrentUser().getName();

        if (user.getName().equals(currentUsername)) {
            javafx.scene.control.Alert errorAlert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            errorAlert.setTitle("Cannot Delete User");
            errorAlert.setHeaderText("You cannot delete yourself!");
            errorAlert.setContentText("You are currently logged in as this user.");
            errorAlert.showAndWait();
            return;
        }

        javafx.scene.control.Alert confirmAlert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete User");
        confirmAlert.setHeaderText("Delete: " + user.getUsername());
        confirmAlert.setContentText("Are you sure you want to delete this user? This action cannot be undone.");

        java.util.Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            boolean deleted = UserDataService.deleteUser(user.getUserId());

            if (deleted) {
                loadUsers();

                javafx.scene.control.Alert successAlert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("User \"" + user.getUsername() + "\" has been deleted.");
                successAlert.showAndWait();

                ActivityLogService.logActivity(currentUsername, "DELETE_USER", "Deleted user: " + user.getUsername());
            } else {
                javafx.scene.control.Alert errorAlert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to delete user.");
                errorAlert.showAndWait();
            }
        }
    }
}
