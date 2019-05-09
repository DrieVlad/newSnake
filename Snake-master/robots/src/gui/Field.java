package gui;

import javafx.scene.image.ImageView;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Field {

    public static double Width = 1300;
    public static double Height = 850;

    private static ImageView loseImageView = new ImageView();

    private Set<FieldCell> emptyCells = new HashSet<>();
    private ArrayList<FieldCell> emptyCellsArray = new ArrayList<>();

    private Set<FieldCell> snakeCells = new HashSet<>();

    private HashMap<FieldCell, Platform> platformCells = new HashMap<FieldCell, Platform>();


    private Snake snake;
    private Target target;
    private Zombie[] zombies;



    public Field(Snake snake, Target target, Zombie[] zombies, Platform[] platforms, ArrayList<Wall> walls, ArrayList<Mine> mines)
    {
        this.snake = snake;
        this.target = target;
        this.zombies = zombies;
        for (int i = 0; i < Width; i++)
            for(int j = 0; j < Height; j++) {
                FieldCell cell = new FieldCell(i, j);
                emptyCells.add(cell);
                emptyCellsArray.add(cell);
            }
        if(walls != GameField.getLoseArrayList()) {
            //for (Wall wall : walls) {
            //    FieldCell cell = FieldCell.getCell(wall.X_Position, wall.Y_Position);
            //    emptyCells.remove(cell);
            //    emptyCellsArray.remove(cell);
            //}
            StreamSupport.stream(walls.spliterator(), false).forEach(wall -> {
                FieldCell cell = FieldCell.getCell(wall.X_Position, wall.Y_Position);
                emptyCells.remove(cell);
                emptyCellsArray.remove(cell);
            });
        }
        if(mines != GameField.getLoseArrayList()) {
            //for (Mine mine : mines) {
            //    FieldCell cell = FieldCell.getCell(mine.X_Position, mine.Y_Position);
            //    emptyCells.remove(cell);
            //    emptyCellsArray.remove(cell);
            //}
            StreamSupport.stream(mines.spliterator(), false).forEach(mine -> {
                FieldCell cell = FieldCell.getCell(mine.X_Position, mine.Y_Position);
                emptyCells.remove(cell);
                emptyCellsArray.remove(cell);
            });


        }
        //for (Platform platform : platforms) {
        //    platformCells.put(FieldCell.getCell(platform), platform);
        //}
        Arrays.stream(platforms).forEach(platform -> platformCells.put(FieldCell.getCell(platform), platform));


    }

    private void updateSnakeCells()
    {
        FieldCell newCell = FieldCell.getCell(snake.Head.X_Position, snake.Head.Y_Position);
        FieldCell lastCell = FieldCell.getCell(snake.Tail.X_Position, snake.Tail.Y_Position);
        snakeCells.add(newCell);
        snakeCells.remove(lastCell);
    }

    public boolean isHitTarget()
    {
        double x = target.X_Position - snake.Head.X_Position;
        double y = target.Y_Position - snake.Head.Y_Position;
        return Math.sqrt(x * x + y * y) < 10;
    }

    private void generateNextTargetPosition()
    {
        int number = (int)Math.round(Math.random() * (emptyCellsArray.size() - 2));
        FieldCell cell = emptyCellsArray.get(number);
        target.setTargetPosition(cell.X, cell.Y);
    }

    private ArrayList<Double> getZombiePossiblePositions(Zombie zombie)
    {
        FieldCell zombieCell = FieldCell.getCell(zombie.X_Position, zombie.Y_Position);
        ArrayList<Double> result = new ArrayList<>();
        double[] directions = {Directions.LEFT, Directions.UP, Directions.DOWN, Directions.RIGHT};
        int counter = -1;
        for (int i = -1; i <= 1;i++)
            for(int j = -1; j <= 1; j++)
            {
                if ((i + j) % 2 == 0)
                    continue;
                counter++;
                if (zombieCell.X + i < 0 || zombieCell.X + i > Width / FieldCell.translateFactor)
                    continue;
                if (zombieCell.Y + j < 0 || zombieCell.Y + j > Height / FieldCell.translateFactor)
                    continue;
                FieldCell nextCell = new FieldCell(zombieCell.X + i, zombieCell.Y + j);
                if (emptyCells.contains(nextCell))
                    result.add(directions[counter]);
            }
        return result;
    }

    private boolean zombieCatchSnake()
    {
        //for(Zombie zombie: zombies) {
        //    FieldCell zombiePosition = FieldCell.getCell(zombie.X_Position, zombie.Y_Position);
         //   if (snakeCells.contains(FieldCell.getCell(zombie.X_Position, zombie.Y_Position)))
         //       return true;
        //}
        boolean flag = Arrays.stream(zombies).anyMatch(zombie -> snakeCells.contains(FieldCell.getCell(zombie.X_Position, zombie.Y_Position)));
        if (flag == true)
            return true;
        return false;
    }

    public boolean isSmash()
    {
        return !emptyCells.contains(FieldCell.getCell(snake.Head));
    }

    public void moveZombies()
    {
        //for (Zombie zombie: zombies) {
         //   if (platformCells.containsKey(FieldCell.getCell(zombie)))
       //         moveOnPlatform(zombie);
        //    else
        //        zombie.move(getZombiePossiblePositions(zombie));
         //   if (zombieCatchSnake()) {
         //       System.out.println("Zombie is eating snake. Snake is dead...");
         //       System.exit(0);
          //  }
       // }
        Arrays.stream(zombies).forEach(zombie -> {
            if (platformCells.containsKey(FieldCell.getCell(zombie)))
                moveOnPlatform(zombie);
            else
                zombie.move(getZombiePossiblePositions(zombie));
            if (zombieCatchSnake()) {
                System.out.println("Zombie is eating snake. Snake is dead...");
                System.exit(0);
            }
        });


    }

    private void moveOnPlatform(GameObject gameObject)
    {
        Platform platform = platformCells.get(FieldCell.getCell(gameObject));
        if (gameObject instanceof Zombie)
        {
            ArrayList<Double> direction = new ArrayList<>();
            direction.add(platform.Direction);
            Zombie zombie = (Zombie)gameObject;
            zombie.move(direction);
        }
        if (gameObject instanceof SnakeBlock)
        {
            snake.onModelUpdateEvent(platform.Direction);
        }
    }

    public ImageView onModelUpdateEvent(double direction){
        FieldCell cell = FieldCell.getCell(snake.Head);
        if (platformCells.containsKey(cell))
            moveOnPlatform(snake.Head);
        else
            snake.onModelUpdateEvent(direction);
        if (isSmash() || zombieCatchSnake()) {
            System.out.println("Snake is dead...!");
            System.exit(0);
        }
        updateSnakeCells();
        if (isHitTarget())
        {
            generateNextTargetPosition();
            return snake.incrementSnake();
        }
        moveZombies();
        return loseImageView;
    }
    public static ImageView getLoseImageView(){
        return loseImageView;
    }


    void draw()
    {
        //for (Platform platform: platformCells.values())
         //   platform.draw();
        StreamSupport.stream(platformCells.values().spliterator(), false).forEach(platform -> platform.draw());

        snake.draw();
        target.draw();
        //for (Zombie zombie: zombies)
        //    zombie.draw();
        Arrays.stream(zombies).forEach(zombie -> zombie.draw());
    }
}
