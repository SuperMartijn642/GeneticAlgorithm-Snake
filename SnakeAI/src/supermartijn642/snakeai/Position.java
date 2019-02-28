package supermartijn642.snakeai;

import java.awt.*;

/**
 * Created 2/27/2019 by SuperMartijn642
 */
public class Position {

    public double x, y;

    public Position(double x,double y){
        this.x = x;
        this.y = y;
    }

    public Position(Point point){
        this(point.x,point.y);
    }

    public double distance(Position position){
        return Math.sqrt((this.x - position.x) * (this.x - position.x) + (this.y - position.y) * (this.y - position.y));
    }

    public Position translate(double x, double y){
        return new Position(this.x + x,this.y + y);
    }

    public Position translate(Position position){
        return this.translate(position.x,position.y);
    }

    public Position rotate(Position center,double angle){
        Position position = this.translate(-center.x,-center.y);
        position.x = position.x * Math.cos(angle) - position.y * Math.sin(angle);
        position.y = position.x * Math.sin(angle) + position.y * Math.cos(angle);
        return position;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Position(this.x,this.y);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof Position && ((Position)obj).x == this.x && ((Position)obj).y == this.y);
    }
}
