import java.io.*;

public class ANode extends Node {
    public ANode(Field field, int x, int y, int step) {
        super(field, x, y, step);
        super._cost = super._field.aStarHeuristicFunc(x, y, super.step());
    }

    @Override
    public ANode generateNewNode(int x, int y) {
        return new ANode(this._field, x, y, super.step() + 1);
    }
}