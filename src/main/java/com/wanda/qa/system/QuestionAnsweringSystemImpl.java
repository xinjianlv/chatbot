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

package com.wanda.qa.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wanda.qa.datasource.DataSource;
import com.wanda.qa.datasource.FileDataSource;
import com.wanda.qa.files.FilesConfig;
import com.wanda.qa.model.CandidateAnswer;
import com.wanda.qa.model.CandidateAnswerCollection;
import com.wanda.qa.model.Evidence;
import com.wanda.qa.model.Question;
import com.wanda.qa.model.QuestionType;
import com.wanda.qa.questiontypeanalysis.QuestionClassifier;
import com.wanda.qa.score.answer.CandidateAnswerScore;
import com.wanda.qa.score.answer.CombinationCandidateAnswerScore;
import com.wanda.qa.score.answer.HotCandidateAnswerScore;
import com.wanda.qa.score.answer.MoreTextualAlignmentCandidateAnswerScore;
import com.wanda.qa.score.answer.RewindTextualAlignmentCandidateAnswerScore;
import com.wanda.qa.score.answer.TermDistanceCandidateAnswerScore;
import com.wanda.qa.score.answer.TermDistanceMiniCandidateAnswerScore;
import com.wanda.qa.score.answer.TermFrequencyCandidateAnswerScore;
import com.wanda.qa.score.answer.TextualAlignmentCandidateAnswerScore;
import com.wanda.qa.score.evidence.BigramEvidenceScore;
import com.wanda.qa.score.evidence.CombinationEvidenceScore;
import com.wanda.qa.score.evidence.EvidenceScore;
import com.wanda.qa.score.evidence.SkipBigramEvidenceScore;
import com.wanda.qa.score.evidence.TermMatchEvidenceScore;
import com.wanda.qa.select.CandidateAnswerSelect;
import com.wanda.qa.select.CommonCandidateAnswerSelect;

/**
 * 使用此问答系统实现要指定4个组件： 1、问答系统使用的数据源(不可以同时使用多个数据源) 2、候选答案提取器(不可以同时使用多个提取器)
 * 3、证据评分组件(可以同时使用多个组件) 4、候选答案评分组件(可以同时使用多个组件)
 *
 * @author 杨尚川
 */
public class QuestionAnsweringSystemImpl implements QuestionAnsweringSystem {

    private static final Logger log = Logger.getLogger(QuestionAnsweringSystemImpl.class);

    private int questionIndex = 1;
    private double mrr;

    private final List<Question> perfectQuestions = new ArrayList<>();
    private final List<Question> notPerfectQuestions = new ArrayList<>();
    private final List<Question> wrongQuestions = new ArrayList<>();
    private final List<Question> unknownTypeQuestions = new ArrayList<>();

    private QuestionClassifier questionClassifier;
    private DataSource dataSource;
    private CandidateAnswerSelect candidateAnswerSelect;
    private EvidenceScore evidenceScore;
    private CandidateAnswerScore candidateAnswerScore;

    @Override
    public void setQuestionClassifier(QuestionClassifier questionClassifier) {
        this.questionClassifier = questionClassifier;
    }

    @Override
    public QuestionClassifier getQuestionClassifier() {
        return this.questionClassifier;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public void setCandidateAnswerSelect(CandidateAnswerSelect candidateAnswerSelect) {
        this.candidateAnswerSelect = candidateAnswerSelect;
    }

    @Override
    public CandidateAnswerSelect getCandidateAnswerSelect() {
        return this.candidateAnswerSelect;
    }

    @Override
    public void setEvidenceScore(EvidenceScore evidenceScore) {
        this.evidenceScore = evidenceScore;
    }

    @Override
    public EvidenceScore getEvidenceScore() {
        return this.evidenceScore;

    }

    @Override
    public void setCandidateAnswerScore(CandidateAnswerScore candidateAnswerScore) {
        this.candidateAnswerScore = candidateAnswerScore;
    }

    @Override
    public CandidateAnswerScore getCandidateAnswerScore() {
        return this.candidateAnswerScore;
    }

    @Override
    public int getQuestionCount() {
        return getPerfectCount() + getNotPerfectCount() + getWrongCount();
    }

    @Override
    public int getPerfectCount() {
        return perfectQuestions.size();
    }

    @Override
    public int getNotPerfectCount() {
        return notPerfectQuestions.size();
    }

    @Override
    public int getWrongCount() {
        return wrongQuestions.size();
    }

    @Override
    public int getUnknownTypeCount() {
        return unknownTypeQuestions.size();
    }

    @Override
    public Question answerQuestion(String questionStr) {
        Question question = dataSource.getQuestion(questionStr);
        if (question != null) {
            return answerQuestion(question);
        }
        return null;
    }

    @Override
    public List<Question> answerQuestions() {
        return dataSource.getAndAnswerQuestions(this);
    }

    @Override
    public Question answerQuestion(Question question) {
        if (question != null) {
            List<Question> questions = new ArrayList<>();
            questions.add(question);

            return answerQuestions(questions).get(0);
        }
        return null;
    }

    @Override
    public List<Question> answerQuestions(List<Question> questions) {
        for (Question question : questions) {
            question = questionClassifier.classify(question);
            log.info("开始处理Question " + (questionIndex++) + "：" + question.getQuestion() + " 【问题类型：" + question.getQuestionType() + "】");
            if (question.getQuestionType() == QuestionType.NULL) {
                unknownTypeQuestions.add(question);
                //未知类型按回答错误处理
                wrongQuestions.add(question);
                log.error("未知的问题类型，拒绝回答！！！");
                continue;
            }
            int i = 1;
            for (Evidence evidence : question.getEvidences()) {
                log.debug("开始处理Evidence " + (i++));
                //对证据进行评分
                //证据的分值存储在evidence对象里面
                evidenceScore.score(question, evidence);

                log.debug("Evidence Detail");
                log.debug("Title:" + evidence.getTitle());
                log.debug("Snippet:" + evidence.getSnippet());
                log.debug("Score:" + evidence.getScore());
                log.debug("Words:" + evidence.getWords());
                //提取候选答案
                //候选答案存储在evidence对象里面
                candidateAnswerSelect.select(question, evidence);
                //从evidence对象里面获得候选答案
                CandidateAnswerCollection candidateAnswerCollection = evidence.getCandidateAnswerCollection();

                if (!candidateAnswerCollection.isEmpty()) {
                    log.debug("Evidence候选答案(未评分)：");
                    candidateAnswerCollection.showAll();
                    log.debug("");
                    //对候选答案进行打分
                    candidateAnswerScore.score(question, evidence, candidateAnswerCollection);
                    log.debug("Evidence候选答案(已评分)：");
                    candidateAnswerCollection.showAll();
                    log.debug("");
                } else {
                    log.debug("Evidence无候选答案");
                }

                log.debug("");
            }
            log.info("************************************");
            log.info("************************************");
            log.info("Question " + question.getQuestion());
            log.info("Question 候选答案：");
            for (CandidateAnswer candidateAnswer : question.getAllCandidateAnswer()) {
                log.info(candidateAnswer.getAnswer() + "  " + candidateAnswer.getScore());
            }
            int rank = question.getExpectAnswerRank();
            log.info("ExpectAnswerRank: " + rank);
            log.info("");
            //完美答案
            if (rank == 1) {
                perfectQuestions.add(question);
            }
            //不完美答案
            if (rank > 1) {
                notPerfectQuestions.add(question);
            }
            //错误答案
            if (rank == -1) {
                wrongQuestions.add(question);
            }
            //计算mrr
            if (rank > 0) {
                mrr += (double) 1 / rank;
            }
            log.info("mrr: " + mrr);
            log.info("perfectCount: " + getPerfectCount());
            log.info("notPerfectCount: " + getNotPerfectCount());
            log.info("wrongCount: " + getWrongCount());
            log.info("unknownTypeCount: " + getUnknownTypeCount());
            log.info("questionCount: " + getQuestionCount());
        }
        log.info("");

        log.info("MRR：" + getMRR() * 100 + "%");
        log.info("回答完美率：" + (double) getPerfectCount() / getQuestionCount() * 100 + "%");
        log.info("回答不完美率：" + (double) getNotPerfectCount() / getQuestionCount() * 100 + "%");
        log.info("回答错误率：" + (double) getWrongCount() / getQuestionCount() * 100 + "%");
        log.info("未知类型率：" + (double) getUnknownTypeCount() / getQuestionCount() * 100 + "%");

        log.info("");

        return questions;
    }

    @Override
    public double getMRR() {
        return (double) mrr / getQuestionCount();
    }

    @Override
    public void showPerfectQuestions() {
        log.info("回答完美的问题：");
        int i = 1;
        for (Question question : perfectQuestions) {
            log.info((i++) + "、" + question.getQuestion() + " : " + question.getExpectAnswerRank());
        }
    }

    @Override
    public void showNotPerfectQuestions() {
        log.info("回答不完美的问题：");
        int i = 1;
        for (Question question : notPerfectQuestions) {
            log.info((i++) + "、" + question.getQuestion() + " : " + question.getExpectAnswerRank());
        }
    }

    @Override
    public void showWrongQuestions() {
        log.info("回答错误的问题：");
        int i = 1;
        for (Question question : wrongQuestions) {
            log.info((i++) + "、" + question.getQuestion());
        }
    }

    @Override
    public void showUnknownTypeQuestions() {
        log.info("未知类型的问题：");
        int i = 1;
        for (Question question : unknownTypeQuestions) {
            log.info((i++) + "、" + question.getQuestion());
        }
    }

    @Override
    public List<Question> getPerfectQuestions() {
        return perfectQuestions;
    }

    @Override
    public List<Question> getNotPerfectQuestions() {
        return notPerfectQuestions;
    }

    @Override
    public List<Question> getWrongQuestions() {
        return wrongQuestions;
    }

    @Override
    public List<Question> getUnknownTypeQuestions() {
        return unknownTypeQuestions;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //1、默认评分组件权重
        ScoreWeight scoreWeight = new ScoreWeight();

        //2、问答系统数据源（人名文件数据源）
        DataSource dataSource = new FileDataSource(FilesConfig.personNameMaterial);

        //3、候选答案提取器(不可以同时使用多个提取器)
        CandidateAnswerSelect candidateAnswerSelect = new CommonCandidateAnswerSelect();

        //4、证据评分组件(可以同时使用多个组件)
        //***********************
        //4.1、TermMatch评分组件
        EvidenceScore termMatchEvidenceScore = new TermMatchEvidenceScore();
        termMatchEvidenceScore.setScoreWeight(scoreWeight);
        //4.2、二元模型评分组件
        EvidenceScore bigramEvidenceScore = new BigramEvidenceScore();
        bigramEvidenceScore.setScoreWeight(scoreWeight);
        //4.3、跳跃二元模型评分组件
        EvidenceScore skipBigramEvidenceScore = new SkipBigramEvidenceScore();
        skipBigramEvidenceScore.setScoreWeight(scoreWeight);
        //4.4、组合证据评分组件
        CombinationEvidenceScore combinationEvidenceScore = new CombinationEvidenceScore();
        combinationEvidenceScore.addEvidenceScore(termMatchEvidenceScore);
        combinationEvidenceScore.addEvidenceScore(bigramEvidenceScore);
        combinationEvidenceScore.addEvidenceScore(skipBigramEvidenceScore);

        //5、候选答案评分组件(可以同时使用多个组件)
        //***********************
        //5.1、词频评分组件
        CandidateAnswerScore termFrequencyCandidateAnswerScore = new TermFrequencyCandidateAnswerScore();
        termFrequencyCandidateAnswerScore.setScoreWeight(scoreWeight);
        //5.2、词距评分组件
        CandidateAnswerScore termDistanceCandidateAnswerScore = new TermDistanceCandidateAnswerScore();
        termDistanceCandidateAnswerScore.setScoreWeight(scoreWeight);
        //5.3、词距评分组件(只取候选词和问题词的最短距离)
        CandidateAnswerScore termDistanceMiniCandidateAnswerScore = new TermDistanceMiniCandidateAnswerScore();
        termDistanceMiniCandidateAnswerScore.setScoreWeight(scoreWeight);
        //5.4、文本对齐评分组件
        CandidateAnswerScore textualAlignmentCandidateAnswerScore = new TextualAlignmentCandidateAnswerScore();
        textualAlignmentCandidateAnswerScore.setScoreWeight(scoreWeight);
        //5.5、文本对齐评分组件
        CandidateAnswerScore moreTextualAlignmentCandidateAnswerScore = new MoreTextualAlignmentCandidateAnswerScore();
        moreTextualAlignmentCandidateAnswerScore.setScoreWeight(scoreWeight);
        //5.6、回带文本对齐评分组件
        CandidateAnswerScore rewindTextualAlignmentCandidateAnswerScore = new RewindTextualAlignmentCandidateAnswerScore();
        rewindTextualAlignmentCandidateAnswerScore.setScoreWeight(scoreWeight);
        //5.7、热词评分组件
        CandidateAnswerScore hotCandidateAnswerScore = new HotCandidateAnswerScore();
        hotCandidateAnswerScore.setScoreWeight(scoreWeight);
        //5.8、组合候选答案评分组件
        CombinationCandidateAnswerScore combinationCandidateAnswerScore = new CombinationCandidateAnswerScore();
        combinationCandidateAnswerScore.addCandidateAnswerScore(termFrequencyCandidateAnswerScore);
        combinationCandidateAnswerScore.addCandidateAnswerScore(termDistanceCandidateAnswerScore);
        combinationCandidateAnswerScore.addCandidateAnswerScore(termDistanceMiniCandidateAnswerScore);
        combinationCandidateAnswerScore.addCandidateAnswerScore(textualAlignmentCandidateAnswerScore);
        combinationCandidateAnswerScore.addCandidateAnswerScore(moreTextualAlignmentCandidateAnswerScore);
        //combinationCandidateAnswerScore.addCandidateAnswerScore(rewindTextualAlignmentCandidateAnswerScore);
        combinationCandidateAnswerScore.addCandidateAnswerScore(hotCandidateAnswerScore);

        QuestionAnsweringSystem questionAnsweringSystem = new QuestionAnsweringSystemImpl();
        questionAnsweringSystem.setDataSource(dataSource);
        questionAnsweringSystem.setCandidateAnswerSelect(candidateAnswerSelect);
        questionAnsweringSystem.setEvidenceScore(combinationEvidenceScore);
        questionAnsweringSystem.setCandidateAnswerScore(combinationCandidateAnswerScore);
        questionAnsweringSystem.answerQuestions();
    }
}