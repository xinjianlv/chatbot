/**
 * 
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.wanda.qa.parser;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.tagging.PartOfSpeechTagging;
import org.apdplat.word.util.WordConfTools;

import com.wanda.qa.util.Tools;

/**
 * 分词器
 *
 * @author 杨尚川
 */
public class WordParser {

    private static final Logger LOG = Logger.getLogger(WordParser.class);

    static {
        String appPath = Tools.getAppPath(WordParser.class);
        String confFile = appPath + "/web/dic/word_v_1_3/word.local.conf";
        if(!new File(confFile).exists()){
            confFile = appPath + "/jar/dic/word_v_1_3/word.local.conf";
        }
        if(new File(confFile).exists()){
            LOG.info("word分词的自定义配置文件："+confFile);
            WordConfTools.forceOverride(confFile);
        }else{
            LOG.info("不存在word分词的自定义配置文件："+confFile);
        }
    }

    /**
     * 带词性标注（包括细分词性标注）的分析方法
     *
     * @param str 需要分词的文本
     * @return 分词结果
     */
    public static List<Word> parseWithoutStopWords(String str) {
        List<Word> words = WordSegmenter.seg(str, SegmentationAlgorithm.MaxNgramScore);
        //词性标注
        PartOfSpeechTagging.process(words);
        return words;
    }
    public static List<Word> parse(String str) {
        List<Word> words = WordSegmenter.segWithStopWords(str, SegmentationAlgorithm.MaxNgramScore);
        //词性标注
        PartOfSpeechTagging.process(words);
        return words;
    }
    
    public static void printWords(List<Word> words){
    	for(Word w : words)
    		System.out.print(w.toString() + "\t" );
    	System.out.println();
    }
//    public static List<Word> parserv2(String str){
//    	Result re = NlpAnalysis.parse(str);
//    	List<Word> words = new ArrayList<Word>();
//    	for(Term t : re){
//    		Word w = new Word(t.getName());
//    		PartOfSpeech ps = new PartOfSpeech(t.getNatureStr() , "none");
//    		w.setPartOfSpeech(ps);
//    		words.add(w);
//    	}
//    	return words;
//    }
    
    public static void main(String[] args) {
//    	String input = "在河边一排排梨树下面有许多的非洲象和熊猫，还有很多的桉树，红色的金鱼在水里游来游去，猎豹在绿色的草地上跑来跑去!";
    	String input = "王健林董事长，美国总统王座属于谁?油市该怎样就怎样-搜狐财经	2016年10月21日 - 周五(10月21日),在市场还未完全消化美国总统大选事件时,这里先根据一些数据来探究一下最终谁将入驻白宫,对于后市油价又有什么影响?美国大选造成经济...";
        List<Word> parse = parse(input);
        System.out.println(parse);
        printWords(parse);
//        parse = parserv2(input);
        printWords(parse);
//        parse = parse("布什是个什么样的人");
//        System.out.println(parse);
//        parse = parse("张三和");
//        System.out.println(parse);
//        parse = parse("哈雷彗星的发现者是六小龄童和伦琴,专访微软亚洲研究院院长洪小文");
//        System.out.println(parse);
//        String str = " 《创业邦》杂志记者对微软亚洲研究院院长洪小文进行了专访。 《创业邦》：微软亚洲  研究院 ... 从研发的角度来说，研究院是一个战略性的部门。因为一家公司最后成功与   ...";
//        parse = parse(str);
//        System.out.println(parse);
    }
}