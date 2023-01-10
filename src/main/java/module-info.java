module com.example.konyvtarasztalifx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.konyvtarasztalifx to javafx.fxml;
    exports com.example.konyvtarasztalifx;
}