package gui;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;


import java.io.File;
import java.util.*;
import java.util.stream.StreamSupport;

public class GameField {
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private Field field;
    private static ArrayList loseArrayList = new ArrayList();
    private double lastDirection = Directions.LEFT;

    private Load loadObjects = new Load();
    private ArrayList<Mine> mines = loseArrayList;
    private ArrayList<Wall> walls = loseArrayList;

    private final Canvas canvas;
    private final Pane pane;
    private final ImageView grass;

    public static ArrayList getLoseArrayList(){
        return loseArrayList;
    }

    public GameField(Pane panne) {
        this.pane = panne;
        grass = loadFile("grass.jpg", this.pane.getWidth(), this.pane.getHeight());

        Target apple = new Target(250, 100);
        this.pane.getChildren().add(apple.Picture);

        Snake snake = new Snake(100, 100);
        this.pane.getChildren().add(snake.Head.Picture);

        Zombie zombies[] = {new Zombie(600, 200)};
        //for(Zombie zombie: zombies)
        //{
        //    this.pane.getChildren().add(zombie.Picture);
        //}
        Arrays.stream(zombies).forEach(zombie -> this.pane.getChildren().add(zombie.Picture));


        mines = loadObjects.returnMines();
        //for(Mine mine: mines){
        //    this.pane.getChildren().add(mine.Picture);
        //}
        StreamSupport.stream(mines.spliterator(), false).forEach(mine -> this.pane.getChildren().add(mine.Picture));

        walls = loadObjects.returnWall();
        //for(Wall wall: walls){
        //    this.pane.getChildren().add(wall.Picture);
        //}
        StreamSupport.stream(walls.spliterator(), false).forEach(wall -> this.pane.getChildren().add(wall.Picture));

        gui.Platform[] platforms = {new gui.Platform(300, 200, Directions.DOWN)};
        this.pane.getChildren().add(platforms[0].Picture);

        field = new Field(snake, apple, zombies, platforms, walls, mines);

        canvas = new Canvas();
        this.pane.getChildren().add(canvas);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ImageView pict = field.onModelUpdateEvent(lastDirection);
                if (pict != Field.getLoseImageView())
                {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            incrementSnake(pict);
                        }
                    });
                }
            }
        }, 0, 5);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                paint();
            }
        }, 0, 20);

        this.pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if (event.getCode() == KeyCode.W & lastDirection != Directions.DOWN)
                    lastDirection = Directions.UP;
                if (event.getCode() == KeyCode.S & lastDirection != Directions.UP)
                    lastDirection = Directions.DOWN;
                if (event.getCode() == KeyCode.D & lastDirection != Directions.LEFT)
                    lastDirection = Directions.RIGHT;
                if (event.getCode() == KeyCode.A & lastDirection != Directions.RIGHT)
                    lastDirection = Directions.LEFT;
            }
        });
    }

    private void incrementSnake(ImageView pict)
    {
        this.pane.getChildren().add(pict);
    }

    private ImageView loadFile(String fileName, double width, double height){
        File file = new File(fileName);
        String localUrl = file.toURI().toString();
        Image image = new Image(localUrl, width, height, false, true);
        ImageView picture = new ImageView(image);
        this.pane.getChildren().add(picture);
        return picture;
    }

    private void paint() {
        grass.setFitHeight(pane.getHeight());
        grass.setFitWidth(pane.getWidth());
        canvas.setHeight(pane.getHeight());
        canvas.setWidth(pane.getWidth());
        field.draw();
    }
}