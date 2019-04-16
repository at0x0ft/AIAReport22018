import java.io.*;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Field {
    private int _width;
    private int _height;

    private int[][] _step;

    private int _startX;
    private int _startY;

    private int _goalX;
    private int _goalY;

    public Field(int width, int height) {
        this._width = width;
        this._height = height;
        this._step = new int[width][height];
    }

    public static Field loadFromFile(String fileName) throws IOException {
        Scanner sc = null;
        Field field = null;
        try {
            sc = new Scanner(new File(fileName));

            while(sc.hasNext()){
                switch(sc.next()) {
                    case "size": {
                        int width = sc.nextInt();
                        int height = sc.nextInt();
                        if(field == null) {
                            field = new Field(width, height);
                        }
                        break;
                    }
                    case "S": {
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        if(field != null) {
                            field.addStart(x, y);
                        }
                        break;
                    }
                    case "G": {
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        if(field != null) {
                            field.addGoal(x, y);
                        }
                        break;
                    }
                    case "-": {
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        if(field != null) {
                            field.addObstacle(x, y);
                        }
                        break;
                    }
                }
            }
        }
        catch(FileNotFoundException fne){
            fne.printStackTrace();
        }
        finally {
            if(sc != null) {
                sc.close();
            }
        }

        return field;
    }

    public void addStart(int x, int y) {
        this._startX = x;
        this._startY = y;
        this._step[x][y] = -1;
    }

    public void addGoal(int x, int y) {
        this._goalX = x;
        this._goalY = y;
        this._step[x][y] = -2;
    }

    public void addObstacle(int x, int y) {
        this._step[x][y] = Integer.MIN_VALUE;
    }

    public void print() {
        for (int i = 0; i < this._width; i++) {
            for(int j = 0; j < this._height; j++) {
                if(this._step[i][j] == -1) {
                    System.out.print("   S");
                }
                else if(this._step[i][j] == -2) {
                    System.out.print("   G");
                }
                else if(this._step[i][j] < -3) {
                    System.out.print("   -");
                }
                else {
                    System.out.print(String.format(" %3d", this._step[i][j]));
                }
            }
            System.out.println();
        }

        if(this._step[this._goalX][this._goalY] > 0) {
            System.out.println();
            System.out.println("Shortest Path costs " + this._step[this._goalX][this._goalY] + " steps.");
        }
    }

    public boolean greedySearch() {
        int dx[] = {1, 0, -1, 0};
        int dy[] = {0, 1, 0, -1};

        PriorityQueue<Node> pq = new PriorityQueue<Node>(new MyComparator());

        pq.add(new GNode(this, this._startX, this._startY, 0));

        while(true) {
            if(pq.size() == 0) {
                return false;
            }

            GNode p = (GNode)pq.poll();

            System.err.println("Greedy : (" + String.format("%3d", p.x()) +", " + String.format("%3d", p.y()) + "), cost : " + String.format("%.2f", p.cost()) + ", step : " + p.step());

            for (int i = 0; i < dx.length; i++) {
                if(isRange(p.x() + dx[i], p.y() + dy[i])) {
                    if(this._step[p.x() + dx[i]][p.y() + dy[i]] == 0) {
                        GNode np = p.generateNewNode(p.x() + dx[i], p.y() + dy[i]);
                        this._step[p.x() + dx[i]][p.y() + dy[i]] = np.step();
                        pq.add(np);
                    }
                    else if(this._step[p.x() + dx[i]][p.y() + dy[i]] == -2) {
                        this._step[p.x() + dx[i]][p.y() + dy[i]] = p.step() + 1;
                        return true;
                    }
                }
            }
        }
    }

    public double greedyHeuristicFunc(int x, int y) {
        return Math.sqrt((this._goalX - x) * (this._goalX - x) + (this._goalY - y) * (this._goalY - y));
    }

    private boolean isRange(int i, int j) {
        return i >= 0 && j >= 0 && i < this._width && j < this._height;
    }
    
    public boolean aStarSearch() {
        int dx[] = {1, 0, -1, 0};
        int dy[] = {0, 1, 0, -1};

        PriorityQueue<Node> pq = new PriorityQueue<Node>(new MyComparator());

        pq.add(new ANode(this, this._startX, this._startY, 0));

        while(true) {
            if(pq.size() == 0) {
                return false;
            }

            ANode p = (ANode)pq.poll();

            System.err.println("A* : (" + String.format("%3d", p.x()) +", " + String.format("%3d", p.y()) + "), cost : " + String.format("%.2f", p.cost()) +", step : " + p.step()); // 4debug

            for (int i = 0; i < dx.length; i++) {
                if(isRange(p.x() + dx[i], p.y() + dy[i])) {
                    if(this._step[p.x() + dx[i]][p.y() + dy[i]] == 0) {
                        ANode np = p.generateNewNode(p.x() + dx[i], p.y() + dy[i]);
                        this._step[p.x() + dx[i]][p.y() + dy[i]] = np.step();
                        pq.add(np);
                    }
                    else if(this._step[p.x() + dx[i]][p.y() + dy[i]] == -2) {
                        this._step[p.x() + dx[i]][p.y() + dy[i]] = p.step() + 1;
                        return true;
                    }
                }
            }
        }
    }

    public double aStarHeuristicFunc(int x, int y, int step) {
        return 1.0 * step + greedyHeuristicFunc(x, y);
    }
}

class MyComparator implements Comparator<Node> {
    @Override
    public int compare (Node n1, Node n2) {
        if(n1.cost() > n2.cost()) {
            return 1;
        }
        else if(n1.cost() < n2.cost()) {
            return -1;
        }
        else{
            return 0;
        }
    }
}