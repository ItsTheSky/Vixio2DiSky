package info.itsthesky.vixio2disky;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Main extends Application {

    public final static Image disky = new Image(getFromURL("https://zupimages.net/up/21/24/3lot.png"));

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vixio2DiSky");
        primaryStage.getIcons().add(disky);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 720, 720);
        primaryStage.setScene(scene);

        VBox main = new VBox();
        HBox title = new HBox();
        title.setAlignment(Pos.CENTER);
        main.setAlignment(Pos.TOP_CENTER);
        main.setSpacing(10);

        Text maintitle = new Text("Vixio2DiSky");
        maintitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Circle cir2 = new Circle(170,170,50);
        cir2.setFill(new ImagePattern(disky));
        title.getChildren().add(maintitle);
        main.getChildren().add(cir2);
        main.getChildren().add(title);
        grid.add(main, 1, 0, 1, 1);

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
                String txt = Converter.convert(vixioArea.getText());
                validationText.setFill(Color.DARKGREEN);
                validationText.setText("The code has been converted!");
                diskyArea.setText(txt);
            } catch (Exception ex) {
                validationText.setFill(Color.DARKRED);
                validationText.setText("An error occurred with the code conversion: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        convertLine.setSpacing(5);
        convertLine.setAlignment(Pos.CENTER_LEFT);
        convertLine.getChildren().add(convert);
        convertLine.getChildren().add(validationText);
        grid.add(convertLine, 1, 2);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static InputStream getFromURL(String url) {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
