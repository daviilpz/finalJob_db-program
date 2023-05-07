module trabajoFinSpace {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.desktop;
	requires db4o;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
