import java.io.*;

public class Node {
    protected Field _field;

    private int _x;
    public int x() {
        return this._x;
    }

    private int _y;
    public int y() {
        return this._y;
    }

    protected double _cost;
    public double cost() {
        return this._cost;
    }

    private int _step;
    public int step() {
        return this._step;
    }

    public Node(Field field, int x, int y, int step) {
        this._field = field;
        this._x = x;
        this._y = y;
        this._cost = -1;
        this._step = step;
    }

    public Node generateNewNode(int x, int y) {
        return new Node(this._field, x, y, this._step + 1);
    }
}