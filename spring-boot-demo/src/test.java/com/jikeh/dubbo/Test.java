package com.jikeh.dubbo;

import java.util.BitSet;

public class Test {

    private BitSet used = new BitSet();

    public Test(String str) {
        for (int i = 0; i < str.length(); i++) {
            used.set(str.charAt(i));  // set bit for char
        }
    }

    @Override
    public String toString() {
        String desc = "[";
        int size = used.size();
        for (int i = 0; i < size; i++) {
            if (used.get(i)) {
                desc += (char) i;
            }
        }
        return desc + "]";
    }

    public static void main(String args[]) {
        Test w = new Test("How do you do");
        System.out.println(w);
    }

}
