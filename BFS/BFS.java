package BFS;

import java.util.LinkedList;
import java.util.Queue;

public class BFS {
    /**
     * calculate the shortest way in a tree
     */
    public static void main(String[] args) {
        TreeNode nine = new TreeNode(9, null, null);
        TreeNode fifteen = new TreeNode(15, null, null);
        TreeNode seven = new TreeNode(7, null, null);
        TreeNode twenty = new TreeNode(20, fifteen, seven);
        TreeNode root = new TreeNode(3, nine, twenty);
        BFS bfs = new BFS();
        int depth = bfs.minDepth(root);
        System.out.println("minDepth:" + depth);
    }

    /**
     * find the shortest way
     */
    int minDepth(TreeNode root) {
        if (root == null)
            return 0;
        // use queue to store path
        Queue<TreeNode> q = new LinkedList<>();
        // put root node into queue
        q.offer(root);
        // generate initial depth, which is 1
        int depth = 1;

        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0;i < size;i++) {
                TreeNode cur = q.poll();
                // return res
                if (cur.left == null && cur.right == null)
                    return depth;
                if (cur.left != null)
                    q.offer(cur.left);
                if (cur.right != null)
                    q.offer(cur.right);
            }
            depth++;
        }
        return depth;
    }

    
}

class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;
    public TreeNode(int value, TreeNode left, TreeNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        if (left != null && right == null)
            return value + " " + left.toString() + " " + "null ";
        else if (left == null && right != null)
            return value + " null " + right.toString() + " ";
        else if (left == null && right == null)
            return value + " null null ";
        return value + " " + left.toString() + " " + right.toString() + " ";
    }
}