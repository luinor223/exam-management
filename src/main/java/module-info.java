module com.hcmus.exammanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.hcmus.exammanagement to javafx.fxml;
    exports com.hcmus.exammanagement;
}