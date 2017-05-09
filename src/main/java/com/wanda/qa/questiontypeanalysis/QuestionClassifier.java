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

package com.wanda.qa.questiontypeanalysis;

import com.wanda.qa.questiontypeanalysis.patternbased.PatternMatchResultSelector;
import com.wanda.qa.questiontypeanalysis.patternbased.PatternMatchStrategy;
import com.wanda.qa.model.Question;

/**
 * 问题分类器
 *
 * @author 杨尚川
 */
public interface QuestionClassifier {

    public void setPatternMatchStrategy(PatternMatchStrategy patternMatchStrategy);

    public PatternMatchStrategy getPatternMatchStrategy();

    public void setPatternMatchResultSelector(PatternMatchResultSelector patternMatchResultSelector);

    public PatternMatchResultSelector getPatternMatchResultSelector();

    public Question classify(String question);

    public Question classify(Question question);
}