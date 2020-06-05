package BackTrack;

import java.util.LinkedList;
import java.util.List;

public class NQueens {
    // store result
    List<List<String>> result = new LinkedList<>();

    public static void main(String[] args) {
        NQueens nQueens = new NQueens();
        // size of board, n * n
        int n = 4;
        // initial board,blank with '.', queen with 'Q'
        LinkedList<String> board = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int j = 0; j < n; j++) {
                stringBuffer.append(".");
            }
            board.add(stringBuffer.toString());
        }

        // * keypoint: backtrack
        nQueens.backtrack(board, 0);

        // print result
        for (List<String> line : nQueens.result) {
            System.out.println("[");
            for (String string : line) {
                System.out.println(string);
            }
            System.out.println("]");
        }
        System.out.println(nQueens.result.size());
    }

    /**
     * we need to focus on three things 
     * 1.track: rows less than row in board has put queens 
     * 2.choices: every cols in row 
     * 3.end: row == board.size()
     */
    void backtrack(LinkedList<String> board, int row) {
        // end condition
        if (row == board.size()) {
            result.add(new LinkedList<>(board));
            return;
        }
        // get col length
        int n = board.get(row).length();
        for (int col = 0; col < n; col++) {
            // deal validation of queen location
            if (!isValid(board, row, col))
                continue;
            // replace . with Q
            StringBuffer stringBuffer = new StringBuffer(board.get(row));
            stringBuffer.setCharAt(col, 'Q');;
            board.set(row, stringBuffer.toString());
            // next backtrack
            backtrack(board, row + 1);
            // replace Q with .
            StringBuffer stringBuffer2 = new StringBuffer(board.get(row));
            stringBuffer2.setCharAt(col, '.');;
            board.set(row, stringBuffer2.toString());
        }
    }

    /**
     * deal if this col can put queen
     */
    boolean isValid(LinkedList<String> board, int row, int col) {
        int n = board.size();
        // check same col
        for (int i = 0; i < n; i++) {
            String line = board.get(i);
            char target = line.charAt(col);
            if (target == 'Q')
                return false;
        }
        // check right up
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
            String line = board.get(i);
            char target = line.charAt(j);
            if (target == 'Q')
                return false;
        }
        // check left up
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            String line = board.get(i);
            char target = line.charAt(j);
            if (target == 'Q')
                return false;
        }
        return true;
    }
}