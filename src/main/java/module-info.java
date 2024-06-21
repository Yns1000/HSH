module com.example.hsh {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jbcrypt;
    requires java.desktop;

    opens com.example.hsh to javafx.fxml;
    exports com.example.hsh;
    opens metier to javafx.base, org.hibernate.orm.core;
    }