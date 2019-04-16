import java.io.*;

public class GNode extends Node {
    public GNode(Field field, int x, int y, int step) {
        super(field, x, y, step);
        super._cost = super._field.greedyHeuristicFunc(x, y);
    }

    @Override
    public GNode generateNewNode(int x, int y) {
        return new GNode(this._field, x, y, super.step() + 1);
    }
}