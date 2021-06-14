package info.itsthesky.vixio2disky;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vixio2DiSky");
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("resources/icon.png")));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 720, 300);
        primaryStage.setScene(scene);

        HBox title = new HBox();
        title.setAlignment(Pos.CENTER);

        Text maintitle = new Text("Vixio2DiSky");
        maintitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        title.getChildren().add(maintitle);
        grid.add(title, 1, 0, 1, 1);

        grid.add(new Label("Vixio Code: (Input)"), 0, 1);
        TextArea vixioArea = new TextArea();
        grid.add(vixioArea, 1, 1);

        grid.add(new Label("DiSky code: (Output)"), 0, 3);
        TextArea diskyArea = new TextArea();
        grid.add(diskyArea, 1, 3);

        HBox convertLine = new HBox();
        Text validationText = new Text();
        Button convert = new Button("Convert");
        convert.setStyle("-fx-border-radius: 0 0 0 0;\n" +
                "-fx-background-radius: 0 0 0 0;");
        convert.setOnAction(e -> {
            try {
                String txt = convert(vixioArea.getText());
                validationText.setFill(Color.DARKGREEN);
                validationText.setText("The code has been converted!");
                diskyArea.setText(txt);
            } catch (Exception ex) {
                validationText.setFill(Color.DARKRED);
                validationText.setText("An error occurred with the code conversion: " + ex.getMessage());
            }
        });
        convertLine.setSpacing(5);
        convertLine.setAlignment(Pos.CENTER_LEFT);
        convertLine.getChildren().add(convert);
        convertLine.getChildren().add(validationText);
        grid.add(convertLine, 1, 2);

        primaryStage.show();
    }

    public String convert(final String current) throws Exception {
        if (current.isEmpty() || current.split("").length == 0)
            throw new IllegalArgumentException("Convert code cannot be empty!");
        return current;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
