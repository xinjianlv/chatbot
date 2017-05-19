package com.wanda.qa;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

import java.util.List;

/**
 * Created by nocml on 2017/5/18.
 */
public class WordTest {
   public static void main(String [] argvs){
       WordSegmenter wseg = new WordSegmenter();

       List<Word> rs1 = wseg.seg("大后天是多少号");
       System.out.println(rs1);
       System.out.println("WordTest.main");
       System.out.print("haha");
   }
}
