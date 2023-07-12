import javafx.animation.PauseTransition;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.DataOutputStream;
import java.io.FileOutputStream;


public class Main extends Application {
    ComboBox<String> combo;
    ArrayList<Color> availableColors;
    HashSet<Color> hidden;
    boolean firstColorRevealed = false;
    boolean isTimePassed = true;
    HashSet<Color> opened;
    Color tempOpened;
    Button btn;
    int numColors;
    Rectangle[] rectangles;
    HashMap<Rectangle, Color> rectColors;
    Rectangle firstRect;
    int moves;
    Text message;
    int highestScore;
    Text txtHighestScore;
    int stageWidth;
    int stageHeight;
    File f;

    @Override
    public void start(Stage stage) {
          //Image
          Image image = new Image("D:\\Java\\Shapes\\MemoryGame\\memory_game.png");
          ImageView view = new ImageView(image);
          view.setFitWidth(160);
          view.setFitHeight(180);

          //choose the difficulty level
          combo = new ComboBox<>();
          combo.getItems().add("Easy");//3*4(3 rows 4 columns)
          combo.getItems().add("Medium");//4*5(4 rows 5 columns)
          combo.getItems().add("Hard");//5*6(5 rows 6 columns)
          combo.setValue("Easy");

          //Button
          btn = new Button("Start");

          VBox vBox = new VBox(view, combo, btn);
          vBox.setSpacing(20);
          vBox.setAlignment(Pos.CENTER);



          //Scene
          Scene scene = new Scene(vBox);
          btn.setOnAction(e->playGame(scene, stage));
          scene.widthProperty().addListener((observable, oldVal, newVal)->{
              view.setFitWidth(Math.max(250, scene.getWidth()-120));
          });

          scene.heightProperty().addListener((observable, oldVal, newVal)->{
              view.setFitHeight(Math.max(300, scene.getHeight()-120));
          });



          //stage
          stage.setTitle("Memory Card Game");
          stage.setScene(scene);
          stage.setMinWidth(300);
          stage.setMinHeight(300);
          stage.setWidth(300);
          stage.setHeight(500);
          //System.out.println(scene.getWidth());
          //System.out.println(scene.getHeight());
          stage.show();



    }

    public void playGame(Scene mainScene, Stage stage){
        //open the score.bin file to take the highest score and in the case does not exist
        // set Highest score equal to zero

        f = new File("D:\\Java\\Shapes\\MemoryGame\\score.bin");
        try(DataInputStream ds = new DataInputStream(new FileInputStream(f))){
            highestScore = ds.readInt();
        }
        catch(FileNotFoundException e){
            highestScore = 1000;

        }
        catch(IOException e){
            highestScore = 1000;
        }

        //Menu Exit
        MenuItem menuItemQuit = new MenuItem("_Quit");
        menuItemQuit.setOnAction(e->{
            //System.out.println("Exit from the game.");
            stage.setScene(mainScene);
            if (numColors == 10){
                stage.setWidth(300);
                stage.setHeight(600);
            }
            else {
                stage.setWidth(300);
                stage.setHeight(500);
            }
        });
        Menu menuExit = new Menu("Exit");
        menuExit.getItems().add(menuItemQuit);

        //start new game
        MenuItem menuItemNewGame = new MenuItem("Restart game");
        menuItemNewGame.setOnAction(e->System.out.println("New Game"));
        Menu menuNew = new Menu("New");
        menuNew.getItems().add(menuItemNewGame);

        //Main menu
        Menu menu = new Menu("_Main Menu");
        menu.getItems().addAll(menuNew, menuExit);


        //Menu Help with rules
        MenuItem menuItemRules = new MenuItem("_Rules");
        menuItemRules.setOnAction(e->{
            new RulesBox();
        });
        Menu menuHelp = new Menu("_Help");
        menuHelp.getItems().add(menuItemRules);

        //MenuBar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu, menuHelp);
        menuBar.setStyle("-fx-background-color: transparent;");

        //Place the menu bar in a HBox
        HBox hBox = new HBox(menuBar);
        hBox.setAlignment(Pos.TOP_LEFT);

        //the difficulty level can be easy,medium or hard
        String difficultyLevel = combo.getValue();

        //permanent opened cards
        opened = new HashSet<>();

        //in the beginning of the game we don't have any color in the tempOpened variable
        //it is used as an intermediate state variable where we store the first color
        //and we want to check if the second color is the same with the first color

        //hidden Colors
        hidden = new HashSet<>();
        hidden.add(Color.web("00FFFF"));
        hidden.add(Color.web("8A2BE2"));
        hidden.add(Color.web("A52A2A"));
        hidden.add(Color.web("7FFF00"));
        hidden.add(Color.web("FFD700"));
        hidden.add(Color.web("808080"));
        hidden.add(Color.web("FF00FF"));
        hidden.add(Color.web("9ACD32"));
        hidden.add(Color.web("FFFFFF"));
        hidden.add(Color.web("008080"));
        hidden.add(Color.web("4682B4"));
        hidden.add(Color.web("708090"));
        hidden.add(Color.web("FA8072"));
        hidden.add(Color.web("DDA0DD"));
        hidden.add(Color.web("8B4513"));

        menuItemNewGame.setOnAction(e->{
            opened.clear();
            tempOpened = null;
            hidden.add(Color.web("00FFFF"));
            hidden.add(Color.web("8A2BE2"));
            hidden.add(Color.web("A52A2A"));
            hidden.add(Color.web("7FFF00"));
            hidden.add(Color.web("FFD700"));
            hidden.add(Color.web("808080"));
            hidden.add(Color.web("FF00FF"));
            hidden.add(Color.web("9ACD32"));
            hidden.add(Color.web("FFFFFF"));
            hidden.add(Color.web("008080"));
            hidden.add(Color.web("4682B4"));
            hidden.add(Color.web("708090"));
            hidden.add(Color.web("FA8072"));
            hidden.add(Color.web("DDA0DD"));
            hidden.add(Color.web("8B4513"));
            moves = 0;
            message.setText("Moves: " + 0);
            if (numColors == 6)
                message.setX(70);
            else if (numColors == 10)
                message.setX(100);
            else
                message.setX(120);
            for (int i=0;i<2*numColors;i++)
                rectangles[i].setFill(Color.BLACK);
        });

        //crete the instance for arraylist and after that based on difficulty level
        //store the colors in arraylist

        availableColors = new ArrayList<>();
        int counter = 0;
        int rows;
        int cols;
        //define the Text of highest score and moves that takes on you to complete the game
        message = new Text("Moves: " + 0);
        message.setFont(Font.font(20));
        txtHighestScore = new Text("Highest score: " + highestScore);
        txtHighestScore.setFont(Font.font(20));
        if (difficultyLevel.equals("Easy")) {
            numColors = 6;
            rows = 3;
            cols = 4;
            stageWidth = 240;
            stageHeight = 400;
            message.setX(70);
            message.setY(280);
            txtHighestScore.setX(40);
            txtHighestScore.setY(310);

        }
        else if (difficultyLevel.equals("Medium")) {
            numColors = 10;
            rows = 4;
            cols = 5;
            stageWidth = 300;
            stageHeight = 500;
            message.setX(100);
            message.setY(360);
            txtHighestScore.setX(70);
            txtHighestScore.setY(390);
        }
        else {
            numColors = 15;
            rows = 5;
            cols = 6;
            stageWidth = 360;
            stageHeight = 600;
            message.setX(120);
            message.setY(440);
            txtHighestScore.setX(90);
            txtHighestScore.setY(470);
        }

        for (Color color:hidden){
            counter++;
            availableColors.add(color);
            availableColors.add(color);
            if (counter == numColors)
                break;
        }
        //shuffle the colors;
        Collections.shuffle(availableColors);

        //create the rectangles
        rectangles = new Rectangle[2*numColors];
        counter = 0;
        for (int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                rectangles[counter] = new Rectangle(20 + j*50, i*80+20, 30, 60);
                rectangles[counter].setArcWidth(10);
                rectangles[counter].setArcHeight(20);
                rectangles[counter].setFill(Color.BLACK);
                final Rectangle  rect = rectangles[counter];
                rectangles[counter].setOnMouseClicked(e->open_cards(rect));
                counter++;
            }
        }

        //map every rectangle in a color
        rectColors = new HashMap<>();
        for (int i = 0;i<2*numColors;i++)
            rectColors.put(rectangles[i], availableColors.get(i));







        //configure scene
        Pane pane = new Pane();
        for (int i=0;i<2*numColors;i++)
            pane.getChildren().add(rectangles[i]);
        pane.getChildren().add(message);
        pane.getChildren().add(txtHighestScore);

        VBox vBox = new VBox(hBox, pane);
        Scene scene2 = new Scene(vBox);

        //configure stage
        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);
        stage.setMinHeight(stageHeight);
        stage.setMinWidth(stageWidth);
        stage.setScene(scene2);

        stage.show();



    }
    public void open_cards(Rectangle rect){
        Color color = rectColors.get(rect);
        if (firstColorRevealed){
            if (opened.contains(color)){
                return;
            }
            else if (tempOpened.equals(color)){
                if (rect!=firstRect) {
                    tempOpened = null;
                    opened.add(color);
                    rect.setFill(color);
                    firstColorRevealed = false;
                    moves++;
                    if (opened.size() == numColors) {
                        message.setFont(Font.font(18));
                        message.setX(message.getX()-30);
                        message.setText("Done(total moves " + moves + ")");
                        if (moves < highestScore) {
                            highestScore = moves;
                            txtHighestScore.setText("Highest score: " + highestScore);
                            try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(f))){
                                dos.writeInt(highestScore);
                            }
                            catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                        message.setText("Moves: " + moves);
                }
            }
            else{
                rect.setFill(color);
                firstColorRevealed = false;
                isTimePassed = false;
                moves++;
                message.setText("Moves: " + moves);
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(e->{
                    hidden.add(tempOpened);
                    tempOpened = null;
                    firstRect.setFill(Color.BLACK);
                    rect.setFill(Color.BLACK);
                    firstRect = null;
                    isTimePassed = true;

                });
                pause.play();

            }

        }
        else{
            if (isTimePassed) {
                if (opened.contains(color))
                    return;
                else {
                    firstColorRevealed = true;
                    hidden.remove(color);
                    tempOpened = color;
                    firstColorRevealed = true;
                    firstRect = rect;
                    rect.setFill(color);
                    moves++;
                    message.setText("Moves: " + moves);
                }
            }

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}