package com.wanda.chatbot.extractor;

import com.wanda.chatbot.pojo.Answer;
import com.wanda.chatbot.pojo.ExtraInformation;
import com.wanda.chatbot.process.time.DateAskProcess;
import org.apache.log4j.Logger;


/**
 * Created by nocml on 2017/5/18.
 */
public class DateAskAnswerExtractor extends AbstractAnswerExtractor{
    Logger log = Logger.getLogger(DateAskAnswerExtractor.class);
    private DateAskProcess dateAskProcess = null;

    public DateAskAnswerExtractor(int level) {
        this.level = level;
        this.dateAskProcess = new DateAskProcess();
    }

    @Override
    protected void findAnswer(String message, Answer answer) {
        Answer as =  dateAskProcess.getAnswer(message);
        log.info("find answer:" + as);
        if(this.level < answer.getLevel() && as != null && as.getAnswer() != null && as.getAnswer().trim().length() > 0){
            log.info("overwrite answer(dateAsk)");
            answer.setAnswer(as);
            answer.setLevel(this.level);
        }
    }
}
