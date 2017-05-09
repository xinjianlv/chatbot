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

package com.wanda.qa.score.evidence;

import java.util.ArrayList;
import java.util.List;

import com.wanda.qa.model.Evidence;
import com.wanda.qa.model.Question;
import com.wanda.qa.system.ScoreWeight;
import com.wanda.qa.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对证据进行评分 【跳跃二元模型评分组件】 利用跳跃二元模型构造出问题的所有正则表达式 在证据中进行匹配，匹配1次得2分
 *
 * @author 杨尚川
 */
public class SkipBigramEvidenceScore implements EvidenceScore {

    private static final Logger LOG = LoggerFactory.getLogger(SkipBigramEvidenceScore.class);
    private ScoreWeight scoreWeight = new ScoreWeight();

    @Override
    public void setScoreWeight(ScoreWeight scoreWeight) {
        this.scoreWeight = scoreWeight;
    }

    @Override
    public void score(Question question, Evidence evidence) {
        LOG.debug("*************************");
        LOG.debug("Evidence 跳跃二元模型评分开始");
        //1、对问题进行分词
        List<String> questionTerms = question.getWords();
        //2、利用跳跃二元模型构造出问题的所有跳跃二元表达式
        List<String> patterns = new ArrayList<>();
        for (int i = 0; i < questionTerms.size() - 2; i++) {
            String pattern = questionTerms.get(i) + "." + questionTerms.get(i + 2);
            LOG.debug("跳跃二元模型表达式：" + pattern);
            patterns.add(pattern);
        }
        //3、在evidence中寻找模式，命中1个加2分
        String text = evidence.getTitle() + evidence.getSnippet();
        double score = 0;
        for (String pattern : patterns) {
            //计算跳跃二元表达式在证据中出现的次数，出现1次加2分
            int count = Tools.countsForSkipBigram(text, pattern);
            if (count > 0) {
                LOG.debug("模式: " + pattern + " 在文本中出现 " + count + "次");
                score = score + count * 2;
            }
        }
        score *= scoreWeight.getSkipBigramEvidenceScoreWeight();
        LOG.debug("Evidence 跳跃二元模型评分:" + score);
        evidence.addScore(score);
        LOG.debug("Evidence 跳跃二元模型评分结束");
        LOG.debug("*************************");
    }
}