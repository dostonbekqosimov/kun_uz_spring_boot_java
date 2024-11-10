package dasturlash.uz.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Hello {
    public static void main(String[] args) {

        Hello hello = new Hello();
        int[] arr = new int[]{0, 1, 0, 3, 12};


        System.out.println(Arrays.toString(hello.moveZeroes(arr)));

        System.out.println(is_valid("(((hello)))"));

        Random random = new Random();  

    }

    private int[] moveZeroes(int[] nums) {


        // Solution 1
//        int numberOfZeros = 0;
//
//
//        for (int i = 0; i < nums.length; i++) {
//            if (nums[i] == 0) {
//                numberOfZeros += 1;
//            } else if (nums[i] != 0 && numberOfZeros > 0) {
//                int temp = nums[i - numberOfZeros];
//                nums[i - numberOfZeros] = nums[i];
//                nums[i] = temp;
//            }
//        }


        // Solution 2
        int numberOfZeros = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[numberOfZeros] = nums[i];
                if (numberOfZeros != i) {
                    nums[i] = 0;
                }
                numberOfZeros++;
            }
        }


        return nums;
    }

    public static boolean is_valid(String s) {

        final char OPEN = '(';
        final char CLOSE = ')';

        // Solution 1
//        int count1 = 0;
//        int count2 = 0;
//        for (char ch : s.toCharArray()) {
//            if (ch == OPEN) {
//                count1++;
//            } else if (ch == CLOSE)
//                count2++;
//        }
//        boolean isStarts = s.startsWith("(");
//        boolean isEnds = s.endsWith(")");
//        return isStarts && isEnds && (count1 == count2);

        // Solution 2
        int balance = 0;

        for (char ch : s.toCharArray()) {

            if (ch == OPEN) {
                balance++;
            } else if (ch == CLOSE) {
                balance--;
            }

            if (balance < 0) {
                return false;
            }

        }

        return balance == 0;


    }
}
