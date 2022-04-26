package com.blog.demo;

import com.blog.demo.mapper.RoleMapper;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    RoleMapper roleMapper;

    @Test
    void contextLoads() {
//        List<Pair<Integer,Integer>> entity = new ArrayList<>();
//        Random random = new Random();
//        for(int i = 0 ; i < 10 ; i ++){
//            Pair<Integer,Integer> item = new Pair<>(i, random.nextInt());
//            entity.add(item);
//        }
//        entity.forEach( temp -> {
//            Integer key = temp.getKey();
//            Integer value = temp.getValue();
//            System.out.println("key: " + key + " value: " + value);
//        });
//        System.out.println(entity.toString());
        longestPalindromeSubseq("bbbab");
    }

    public int longestPalindromeSubseq(String s) {
        int slen = s.length();
        int bitArray = 1 << slen;
        int maxLen = 0;
        StringBuffer temp;
        System.out.println(bitArray);
        char[] charArray = s.toCharArray();
        for(int state = 0 ; state < bitArray; state++){
            temp = new StringBuffer();
            for(int i = 0 ; i < slen ; i++){
                if(((state >> i) & 1 )== 1){
                    temp.append((charArray[slen - i - 1]));
                }
            }
            String BeforeReserve = temp.toString();
            temp.reverse();
            if(BeforeReserve.equals(temp.toString())){
                maxLen = Math.max(maxLen, BeforeReserve.length());
            }
        }
        return maxLen;
    }

}
