package gui;

public class Platform extends GameObject {

    public Platform(double x, double y, double direction)
    {
        super(x, y, "platform.png", FieldCell.translateFactor);
        Direction = direction;
    }
}
