module com.lds.flyaway {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.media;
    requires java.desktop;

    exports com.lds7871.flyaway.B_Show;
    opens com.lds7871.flyaway.B_Show to javafx.fxml;

    exports com.lds7871.flyaway.A_Contorller;
    opens com.lds7871.flyaway.A_Contorller to javafx.fxml;

    exports com.lds7871.flyaway.C_LGUI;
    opens com.lds7871.flyaway.C_LGUI to javafx.fxml;
}