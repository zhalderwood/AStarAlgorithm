package solver;

import java.util.Collection;
import java.util.HashSet;

public class Puzzle {

    // class variable initialization
    private final String[][] startNode;
    private final int size = 3;
    private String[][] currentNode;
    private String[][] goalNode = new String[3][3];
    private String EMPTY = "E", HSWAP = "hswap", VSWAP = "vswap";
    private int[] emptyPos = new int[]{-1,-1};
    private Collection<Puzzle> nextNodes;

    // constructor
    public Puzzle(String[][] tiles) {
        this.startNode = tiles.clone();
        this.currentNode = tiles.clone();
        updateEmptyPos();
        int i = 0, j = 0;
        for (int n = 0; n < size*size; n++) {
            i = n / size;
            j = n % size;
            goalNode[i][j] = String.valueOf(n+1);
            if (i == 2 && j == 2) { goalNode[i][j] = EMPTY; }
        }
    }

    // generates next node layer of solution tree
    public Collection<Puzzle> nextNodes() {
        if (nextNodes != null) { return nextNodes; }
        if ( (emptyPos[0] == -1) && (emptyPos[1] == -1)) { updateEmptyPos(); }

        nextNodes = new HashSet<>();

        // horizontal tile swap
        if (emptyPos[0] - 1 >=  0 ) { generateNode(emptyPos[0] - 1, HSWAP); }
        if (emptyPos[0] + 1 < size) { generateNode(emptyPos[0] + 1, HSWAP); }

        // vertical tile swap
        if (emptyPos[1] - 1 >=  0 ) { generateNode(emptyPos[1] - 1, HSWAP); }
        if (emptyPos[1] + 1 < size) { generateNode(emptyPos[1] + 1, HSWAP); }

        return nextNodes;
    }

    // creates node for solution tree
    private void generateNode(int newCoord, String direction) {
        Puzzle node = new Puzzle(this.currentNode);
        int[] newPos = new int[2];
        if (direction.equals(HSWAP)) { newPos[0] = newCoord; newPos[1] = emptyPos[1]; } // set horizontal coordinates
        else                         { newPos[0] = emptyPos[0]; newPos[1] = newCoord; } // set vertical coordinates
        swapTiles(node.currentNode, emptyPos, newPos);
        nextNodes.add(node);
    }

    // swaps two tiles with each other
    public void swapTiles(String[][] node, int[] firstTile, int[] secondTile) {
        String temp = node[firstTile[0]][firstTile[1]];
        node[firstTile[0]][firstTile[1]] = node[secondTile[0]][secondTile[1]];
        node[secondTile[0]][secondTile[1]] = temp;
        this.updateEmptyPos();
    }

    // gets private array currentNode
    public String[][] getCurrentNode() { return this.currentNode; }

    // Finds and returns a tile location
    public int[] getTileLoc(String[][] node, String id) {
        int i = -1, j = -1;
        int tempInt[] = new int[2];
        for (int n = 0; n < size*size; n++) {
            i = n / size;
            j = n % size;
            if (node[i][j].equals(id)) {
                tempInt[0] = i;
                tempInt[1] = j;
            }
        }
        return tempInt;
    }

    // returns tile as a String (how it is saved)
    public String getTileString(int i, int j) {
        return currentNode[i][j];
    }

    // returns tile as an int
    public int getTileInt(int i, int j) {
        String tempString = this.currentNode[i][j];
        if (tempString.equals(EMPTY)) { tempString = "0"; }
        return Integer.parseInt(tempString);
    }

    // Updates empty tile location for other methods
    public void updateEmptyPos() {
        int tempInt[] = getTileLoc(this.currentNode, EMPTY);
        this.emptyPos[0] = tempInt[0];
        this.emptyPos[1] = tempInt[1];
    }

    // finds manhattan distance of tiles from current node to goal node
    public int getManhattan() {
        int manhattan = 0;

        int i, j, nextI, nextJ, tempTile;
        for (int n = 0; n < size*size; n++) {
            i = n / size;
            j = n % size;
            tempTile = getTileInt(i,j);
            if (tempTile != 0 && tempTile != (i * size + j + 1) ) {
                // only true if tile is non-zero and is not already in correct position
                nextI = (tempTile - 1) / size;
                nextJ = (tempTile - 1) % size;
                manhattan += Math.abs(nextI - i) + Math.abs(nextJ - j);
            }
        }
        return manhattan;
    }

    // prints a nicely formatted puzzle
    public void printNode(String[][] node) {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.println("| " + node[i][0] + " " + node[i][1] + " " + node[i][2] + " |");
        }
        System.out.println("---------");
    }

    // checks if Puzzle is solved
    public boolean isSolved() {
        int i = 0, j = 0;
        for (int n = 0; n < size*size; n++) {
            i = n / size;
            j = n % size;
            if (!this.currentNode[i][j].equals(goalNode[i][j])) {
                return false;
            }
        }
        return true;
    }

    public boolean isSolvable() {
        int i, j, m, n, inversionCount = 0;
        for (int index = 0; index < size*size; index++) {
            i = index / size;
            j = index % size;
            for (int invIndex = index; invIndex < size*size; invIndex++) {
                m = invIndex / size;
                n = invIndex % size;
                if (getTileInt(m,n) != 0 && getTileInt(m,n) < getTileInt(i,j)) {
                    // is invertible
                    inversionCount++;
                }
            }
        }
        if (inversionCount % 2 != 0) { return false; } // even parity
        return true;
    }
}