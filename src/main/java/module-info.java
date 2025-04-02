module com.hcmus.exammanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires com.jfoenix;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.hcmus.exammanagement to javafx.fxml;
    exports com.hcmus.exammanagement;
    requires java.sql;
    requires org.postgresql.jdbc;
}