class Difference{
    // 差分数组
    private int[] diff;
    
    /* 输⼊⼀个初始数组，区间操作将在这个数组上进⾏ */
    public Difference(int[] nums) {
        assert nums.length > 0;
        diff = new int[nums.length];
        // 根据初始数组构造差分数组
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
        diff[i] = nums[i] - nums[i - 1];
        }
    }
     
    /* 给闭区间 [i, j] 增加 val（可以是负数）*/
     public void increment(int i, int j, int val) {
        diff[i] += val;
        if (j + 1 < diff.length) {
        diff[j + 1] -= val;
        }
    }
    public int[] result() {
        int[] res = new int[diff.length];
        // 根据差分数组构造结果数组
        res[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
        res[i] = res[i - 1] + diff[i];
        }
        for(int j=0; j<res.length;j++){
            System.out.print(res[j]);
        }
       
        return res;
    }
}
public class test{
    public static void main(String[] args) {
        int[] nums = {1,2,3};
        Difference diff = new Difference(nums);
        diff.increment(0, 2, 3);
        // return;
        // for (int num ; diff.result()) {
        //     System.out.print(nums[i] + "、");
        // }
        // System.out.println(diff.result());
        diff.result();
    }
}