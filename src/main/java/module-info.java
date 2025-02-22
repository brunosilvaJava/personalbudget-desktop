module com.bts.personalbudgetdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.kordamp.ikonli.fontawesome5;
    requires jakarta.cdi;
    requires jakarta.inject;
    requires weld.se.core;

    opens com.bts.personalbudgetdesktop to javafx.fxml;
    opens com.bts.personalbudgetdesktop.controller to javafx.fxml, weld.core.impl;
    opens com.bts.personalbudgetdesktop.service to weld.core.impl;

    exports com.bts.personalbudgetdesktop;
    exports com.bts.personalbudgetdesktop.controller;
    exports com.bts.personalbudgetdesktop.service;
    exports com.bts.personalbudgetdesktop.model;
    exports com.bts.personalbudgetdesktop.view;
    exports com.bts.personalbudgetdesktop.controller.fixedbill;
    opens com.bts.personalbudgetdesktop.controller.fixedbill to javafx.fxml, weld.core.impl;
    exports com.bts.personalbudgetdesktop.controller.dashboard;
    opens com.bts.personalbudgetdesktop.controller.dashboard to javafx.fxml, weld.core.impl;
}
