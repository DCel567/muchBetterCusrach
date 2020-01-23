module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires org.apache.commons.io;
	//requires htmlunit;
	requires org.jsoup;
	//requires DownloadHtml;

	opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}