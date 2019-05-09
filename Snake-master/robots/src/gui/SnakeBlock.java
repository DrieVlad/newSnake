package gui;

import java.util.ArrayDeque;
import java.util.Queue;

public class SnakeBlock extends GameObject {

    private Queue<SnakeBlockPosition> queue = new ArrayDeque<>();
    private double queueSize = 30;

    SnakeBlock(double x, double y)
    {
        super(x, y, "bug_1.png", FieldCell.translateFactor);
    }

    public boolean isActiveBlock()
    {
        return queue.size() >= queueSize;
    }

    public SnakeBlockPosition getLastPosition(){
        return queue.poll();
    }

    public void addNextPosition(SnakeBlockPosition nextPosition){
        X_Position = nextPosition.X;
        Y_Position = nextPosition.Y;
        Direction = nextPosition.Direction;
        queue.add(nextPosition);
    }
}
