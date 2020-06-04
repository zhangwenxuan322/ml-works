package Binary;

public class BinarySearch {
    public static void main(String[] args) {
        BinarySearch binarySearch = new BinarySearch();
        // basic bs test
        int[] basicnNums = {1, 2, 3, 4};
        int basicTarget = 4;
        System.out.println(binarySearch.binarySearch(basicnNums, basicTarget));
    }

    /**
     * bacis binary search function
     */
    int binarySearch(int[] nums, int target) {
        System.out.println("start basic binary search");
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) 
                return mid;
            else if (nums[mid] > target)
                right = mid - 1;
            else if (nums[mid] < target)
                left = mid + 1;
        }
        return -1;
    }
}