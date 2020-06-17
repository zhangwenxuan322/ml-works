package BFS;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Lock {
    public static void main(String[] args) {
        String[] deadEnds = { "8888" };
        String target = "0009";
        Lock lock = new Lock();
        int res = lock.openLock(deadEnds, target);
        System.out.println(res);
    }

    /**
     * password can be 1234 
     * deadEnds can be {1111, 1211, ...} 
     * one char can be turned up or down
     */
    int openLock(String[] deadEnds, String target) {
        // store deadEnds
        Set<String> deadSet = new HashSet<>();
        for (String string : deadEnds) {
            deadSet.add(string);
        }
        // store visited in case of repeat
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        int step = 0;
        // start from 0000
        queue.offer("0000");
        visited.add("0000");

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0;i < size;i++) {
                String cur = queue.poll();

                // jump over deadset
                if (deadSet.contains(cur))
                    continue;
                // return target
                if (cur.equals(target))
                    return step;
                // change password
                for (int j = 0;j < 4;j++) {
                    // up
                    String up = plusOne(cur, j);
                    if (!visited.contains(up)) {
                        queue.offer(up);
                        visited.add(up);
                    }
                    // down
                    String down = minusOne(cur, j);
                    if (!visited.contains(down)) {
                        queue.offer(down);
                        visited.add(down);
                    }
                }
            }
            step++;
        }
        // doesn't find answer
        return -1;
    }

    // turn up
    String plusOne(String s, int j) {
        char[] ch = s.toCharArray();
        if (ch[j] == '9')
            ch[j] = '0';
        else
            ch[j] += 1;
        return new String(ch);
    }

    // turn down
    String minusOne(String s, int j) {
        char[] ch = s.toCharArray();
        if (ch[j] == '0')
            ch[j] = '9';
        else
            ch[j] -= 1;
        return new String(ch);
    }
}