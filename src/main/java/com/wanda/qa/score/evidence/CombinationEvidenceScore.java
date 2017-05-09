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

/**
 * 组合证据评分组件
 *
 * @author 杨尚川
 */
public class CombinationEvidenceScore implements EvidenceScore {

    private final List<EvidenceScore> evidenceScores = new ArrayList<>();
    private ScoreWeight scoreWeight = new ScoreWeight();

    @Override
    public void setScoreWeight(ScoreWeight scoreWeight) {
        this.scoreWeight = scoreWeight;
    }

    public void addEvidenceScore(EvidenceScore evidenceScore) {
        evidenceScores.add(evidenceScore);
    }

    public void removeEvidenceScore(EvidenceScore evidenceScore) {
        evidenceScores.remove(evidenceScore);
    }

    public void clear() {
        evidenceScores.clear();
    }

    @Override
    public void score(Question question, Evidence evidence) {
        for (EvidenceScore evidenceScore : evidenceScores) {
            evidenceScore.score(question, evidence);
        }
    }
}