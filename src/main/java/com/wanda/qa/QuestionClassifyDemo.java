package com.wanda.qa;

import com.wanda.qa.model.Question;
import com.wanda.qa.questiontypeanalysis.QuestionClassifier;
import com.wanda.qa.questiontypeanalysis.patternbased.DefaultPatternMatchResultSelector;
import com.wanda.qa.questiontypeanalysis.patternbased.PatternBasedMultiLevelQuestionClassifier;
import com.wanda.qa.questiontypeanalysis.patternbased.PatternMatchResultSelector;
import com.wanda.qa.questiontypeanalysis.patternbased.PatternMatchStrategy;
import com.wanda.qa.questiontypeanalysis.patternbased.QuestionPattern;

public class QuestionClassifyDemo {

	public static void main(String[] args) {
		 PatternMatchStrategy patternMatchStrategy = new PatternMatchStrategy();
	        patternMatchStrategy.addQuestionPattern(QuestionPattern.Question);
	        patternMatchStrategy.addQuestionPattern(QuestionPattern.TermWithNatures);
	        patternMatchStrategy.addQuestionPattern(QuestionPattern.Natures);
	        patternMatchStrategy.addQuestionPattern(QuestionPattern.MainPartPattern);
	        patternMatchStrategy.addQuestionPattern(QuestionPattern.MainPartNaturePattern);
	        patternMatchStrategy.addQuestionTypePatternFile("QuestionTypePatternsLevel1_true.txt");
	        patternMatchStrategy.addQuestionTypePatternFile("QuestionTypePatternsLevel2_true.txt");
	        patternMatchStrategy.addQuestionTypePatternFile("QuestionTypePatternsLevel3_true.txt");
	        PatternMatchResultSelector patternMatchResultSelector = new DefaultPatternMatchResultSelector();
	        QuestionClassifier questionClassifier = new PatternBasedMultiLevelQuestionClassifier(patternMatchStrategy, patternMatchResultSelector);
	        
	        Question question = new Question();
	        question.setQuestion("明天是周几？");
	        question = questionClassifier.classify(question);
	        System.out.println("type:" + question.getQuestionType());
	}
}
