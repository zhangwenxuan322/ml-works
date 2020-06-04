package Binary;

public class BinarySearch {
    public static void main(String[] args) {
        BinarySearch binarySearch = new BinarySearch();
        // basic bs test
        int[] basicnNums = {1, 2, 3, 4};
        int basicTarget = 10;
        System.out.println(binarySearch.binarySearch(basicnNums, basicTarget));
        // left bound search test
        int[] leftNums = {1, 2, 3, 3, 3, 4};
        int leftTarget = 5;
        System.out.println(binarySearch.leftBoundSearch(leftNums, leftTarget));
    }

    /**
     * bacis binary search function
     */
    int binarySearch(int[] nums, int target) {
        System.out.println("start basic binary search");
        // invalid dealing
        if (nums == null || nums.length == 0) 
            return -1;
        
        // search in range [left, right]
        int left = 0;
        int right = nums.length - 1;

        // jump out while left == right + 1
        while (left <= right) {
            // get mid index
            int mid = left + (right - left) / 2;
            // compare mid value with target
            if (nums[mid] == target) 
                return mid;
            // update array's left and right index
            else if (nums[mid] > target)
                right = mid - 1;
            else if (nums[mid] < target)
                left = mid + 1;
        }

        // return -1 if target doesn't exist
        return -1;
    }

    /**
     * left bound search
     */
    int leftBoundSearch(int[] nums, int target) {
        System.out.println("start left bound search");
        // invalid dealing
        if (nums == null || nums.length == 0) 
            return -1;

        // search in range [left, right)
        int left = 0;
        int right = nums.length;

        // jump out while left == right
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) 
                right = mid;
            else if (nums[mid] > target)
                right = mid;
            else if (nums[mid] < target)
                left = mid + 1;
        }
        if (left == 0 || left >= nums.length)
            left = -1;
        return left;
    }
}