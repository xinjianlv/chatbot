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

package com.wanda.qa.questiontypeanalysis.patternbased;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wanda.qa.model.Question;
import com.wanda.qa.model.QuestionType;
import com.wanda.qa.questiontypeanalysis.QuestionTypeTransformer;
import com.wanda.qa.util.Tools;

/**
 * 默认模式匹配结果选择器
 *
 * @author 杨尚川
 */
public class DefaultPatternMatchResultSelector implements PatternMatchResultSelector {

    private static final Logger LOG = Logger.getLogger(DefaultPatternMatchResultSelector.class);

    @Override
    public Question select(Question question, PatternMatchResult patternMatchResult) {
        List<PatternMatchResultItem> allPatternMatchResultItems = patternMatchResult.getAllPatternMatchResult();
        if (allPatternMatchResultItems == null || allPatternMatchResultItems.isEmpty()) {
            LOG.info("所有问题类型模式匹配结果为空，不能识别问题类型，不能识别的问题类型统一指定为："+QuestionType.NULL);
            question.setQuestionType(QuestionType.NULL);
            return question;
        }
        for (QuestionTypePatternFile file : patternMatchResult.getQuestionTypePatternFilesFromCompactToLoose()) {
            List<PatternMatchResultItem> patternMatchResultItems = patternMatchResult.getPatternMatchResult(file);
            if (patternMatchResultItems == null || patternMatchResultItems.isEmpty()) {
                LOG.info("问题类型模式" + file + "匹配结果为空");
                continue;
            }
            LOG.info("处理问题类型模式：" + file.getFile() + " ，是否允许多个匹配：" + file.isMultiMatch());
            //分别统计类型的匹配次数
            Map<QuestionType, Integer> map = new HashMap<>();
            for (PatternMatchResultItem patternMatchResultItem : patternMatchResultItems) {
                String type = patternMatchResultItem.getType();
                QuestionType key = QuestionTypeTransformer.transform(type);
                Integer value = map.get(key);
                if (value == null) {
                    value = 1;
                } else {
                    value++;
                }
                map.put(key, value);
            }
            //对类型的匹配次数进行排序
            List<Map.Entry<QuestionType, Integer>> entrys = Tools.sortByIntegerValue(map);
            Collections.reverse(entrys);
            //是否有多个匹配
            if (entrys.size() > 1) {
                LOG.info("\t类型\t选中数目");
                for (Map.Entry<QuestionType, Integer> entry : entrys) {
                    LOG.info("\t" + entry.getKey() + "\t" + entry.getValue());
                    question.addCandidateQuestionType(entry.getKey());
                }
                //是否允许多个匹配
                if (!file.isMultiMatch()) {
                    LOG.info("找到多个匹配，不允许");
                    question.setQuestionType(QuestionType.NULL);
                    continue;
                }
                LOG.info("对于允许多个匹配结果的情况，默认模式匹配结果选择器，选择匹配类型最多的");
                QuestionType selectedType = entrys.get(0).getKey();
                question.setQuestionType(selectedType);
                //候选类型中不包括主类型
                if (question.getCandidateQuestionTypes().contains(selectedType)) {
                    question.removeCandidateQuestionType(selectedType);
                }
                return question;
            } else {
                //只有一个匹配结果
                LOG.info("只有一个匹配结果，匹配成功");
                LOG.info("\t类型\t选中数目");
                for (Map.Entry<QuestionType, Integer> entry : entrys) {
                    LOG.info("\t" + entry.getKey() + "\t" + entry.getValue());
                }
                QuestionType selectedType = entrys.get(0).getKey();
                question.setQuestionType(selectedType);
                LOG.info("问题类型模式" + file.getFile() + "有一个匹配，【找到类型】");
                LOG.info("找到类型，返回：" + question.getQuestionType().name());
                return question;
            }
        }
        LOG.info("匹配未成功，不能识别问题类型，不能识别的问题类型统一指定为："+QuestionType.NULL);
        question.setQuestionType(QuestionType.NULL);
        return question;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}