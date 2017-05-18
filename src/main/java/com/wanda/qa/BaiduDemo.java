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

package com.wanda.qa;

import java.util.ArrayList;
import java.util.List;

import com.wanda.qa.datasource.BaiduDataSource;
import com.wanda.qa.datasource.DataSource;
import com.wanda.qa.files.FilesConfig;
import com.wanda.qa.model.CandidateAnswer;
import com.wanda.qa.model.Question;
import com.wanda.qa.model.QuestionType;
import com.wanda.qa.system.CommonQuestionAnsweringSystem;
import com.wanda.qa.system.QuestionAnsweringSystem;

/**
 * 从配置文件中读取问题 然后从baidu搜索证据 然后计算候选答案
 *
 */
public class BaiduDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<String> files = new ArrayList<>();
        files.add(FilesConfig.personNameQuestions);
        files.add(FilesConfig.locationNameQuestions);
        files.add(FilesConfig.organizationNameQuestions);
        files.add(FilesConfig.numberQuestions);
        files.add(FilesConfig.timeQuestions);
        DataSource dataSource = new BaiduDataSource(files);

        QuestionAnsweringSystem questionAnsweringSystem = new CommonQuestionAnsweringSystem();
        questionAnsweringSystem.setDataSource(dataSource);
        Question qa = questionAnsweringSystem.answerQuestion("明天会更好");
        if (qa.getQuestionType() != QuestionType.NULL)
	        for(CandidateAnswer canswer :  qa.getAllCandidateAnswer()){
	        	System.out.printf("answer:%s\tscore:%f\n", canswer.getAnswer() ,canswer.getScore());
	        }
        
//        questionAnsweringSystem.showPerfectQuestions();
//        questionAnsweringSystem.getCandidateAnswerScore();
//        questionAnsweringSystem.showNotPerfectQuestions();
//        questionAnsweringSystem.showWrongQuestions();
//        questionAnsweringSystem.showUnknownTypeQuestions();
    }
}