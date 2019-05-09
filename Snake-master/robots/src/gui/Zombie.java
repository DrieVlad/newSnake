package gui;

import java.util.ArrayList;

public class Zombie extends GameObject {
    private double lastDirection = -10;
    private int counter = 0;
    private final int maxCounter = 500;


    public Zombie(double x, double y) {
        super(x, y, "zombie.png", FieldCell.translateFactor);
        Direction = Directions.UP;
    }

    private void moving(double nextDirection)
    {
        double newX = X_Position + Config.ZOMBIE_SPEED * Math.cos(nextDirection);
        double newY = Y_Position + Config.ZOMBIE_SPEED * Math.sin(nextDirection);
        if (newX >= 0 && newX < Field.Width)
            X_Position = newX;
        if (newY >= 0 && newY < Field.Height)
            Y_Position = newY;
    }

    public void move(ArrayList<Double> possibleDirections)
    {
        counter++;
        if (!possibleDirections.contains(lastDirection) || counter >= maxCounter)
        {
            int count = possibleDirections.size() - 1;
            if (count >= 0) {
                int res = (int) Math.round(count * Math.random());
                lastDirection = possibleDirections.get(res);
                counter = 0;
            }
            else
                return;
        }
        moving(lastDirection);
    }
}
