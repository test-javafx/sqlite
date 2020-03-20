package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TableView<Usuario> tblUsuarios;

    @FXML
    private TableColumn<Usuario, Integer> col_id;

    @FXML
    private TableColumn<Usuario, String> col_username;

    @FXML
    private TableColumn<Usuario, String> col_password;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    private Connection con;

    private ObservableList<Usuario> obUsuarios = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            String current_diretory = System.getProperty("user.dir") + System.getProperty("file.separator") + "sqlite.db";

            this.con = DriverManager.getConnection("jdbc:sqlite:" + current_diretory);

            this.cargar();

        } catch (SQLException e) {
            System.err.println("Message: " + e.getMessage());
        }

        this.col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        this.col_password.setCellValueFactory(new PropertyValueFactory<>("password"));

    }

    public void cargar() throws SQLException {

        ResultSet rs = this.con.createStatement().executeQuery("SELECT * FROM users");

        this.obUsuarios.clear();

        while (rs.next()){

            Integer id = rs.getInt("id");
            String username = rs.getString("username");
            String password = rs.getString("password");

            this.obUsuarios.add(new Usuario(id, username, password));

            this.tblUsuarios.setItems(this.obUsuarios);

        }

    }

    @FXML
    void clickAgregar(ActionEvent event) throws SQLException {

        String username = this.txtUsername.getText();
        String password = this.txtPassword.getText();

        this.con.createStatement().executeUpdate("INSERT INTO users(username,password) VALUES('" + username + "','" + password +"')");

        this.clearForm();
        this.cargar();

    }

    public void clearForm(){
        this.txtUsername.setText("");
        this.txtPassword.setText("");
    }

}