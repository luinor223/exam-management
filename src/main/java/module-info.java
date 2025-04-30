module com.hcmus.exammanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires com.jfoenix;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires com.zaxxer.hikari;
    requires static lombok;
    requires org.slf4j;
    requires java.desktop;

    opens com.hcmus.exammanagement to javafx.fxml;
    exports com.hcmus.exammanagement;
    exports com.hcmus.exammanagement.controller;
    opens com.hcmus.exammanagement.controller to javafx.fxml;
    exports com.hcmus.exammanagement.dto to javafx.base;
}