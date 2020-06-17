package BackTrack;

import java.util.LinkedList;
import java.util.List;

public class BackTrack {
    // store result
    List<List<Integer>> result = new LinkedList<>();
    public static void main(String[] args) {
        BackTrack backTrack = new BackTrack();
        LinkedList<Integer> track = new LinkedList<>();
        int[] nums = {1, 2, 3};
        backTrack.backtrack(nums, track);
        System.out.println(backTrack.result);
    }

    /**
     * we need to focus on three things
     * 1.track: numbers that we have covered
     * 2.choices: nums that doesn't exist in tack
     * 3.end: nums size equals to track size
     */
    void backtrack(int[] nums, LinkedList<Integer> track) {
        System.out.println(track);
        // end condition
        if (nums.length == track.size()) {
            result.add(new LinkedList<>(track));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (track.contains(nums[i]))
                continue;
            track.add(nums[i]);
            backtrack(nums, track);
            track.removeLast();
        }
    }

}