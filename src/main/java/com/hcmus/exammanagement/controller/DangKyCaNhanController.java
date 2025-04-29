package com.hcmus.exammanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class DangKyCaNhanController {

    @FXML
    private TableColumn<?, ?> chungChiActionColumn;

    @FXML
    private TableColumn<?, ?> chungChiColumn;

    @FXML
    private ComboBox<?> chungChiCombo;

    @FXML
    private Label chungChiLabel;

    @FXML
    private AnchorPane examScheduleSection;

    @FXML
    private TableView<?> lichThiTable;

    @FXML
    private TableColumn<?, ?> maLichThiColumn;

    @FXML
    private Label maLichThiLabel;

    @FXML
    private TableColumn<?, ?> ngayGioThiColumn;

    @FXML
    private Label ngayGioThiLabel;

    @FXML
    private DatePicker ngayThiDP;

    @FXML
    private TableColumn<?, ?> soLuongTSColumn;

    @FXML
    private Label soLuongTSLabel;

    @FXML
    private TableColumn<?, ?> thoiLuongColumn;

    @FXML
    private Label thoiLuongLabel;

    @FXML
    void handleChooseSchedule(ActionEvent event) {

    }

}
