package com.wanda.chatbotv1.process;

import com.wanda.chatbotv1.pojo.Answer;

public abstract class AbstractProcess {

	protected int level;

   //责任链中的下一个元素
   protected AbstractProcess nextProcess;

   public void setNextProcess(AbstractProcess nextLogger){
      this.nextProcess = nextLogger;
   }

   public void process(int level, String message , Answer answer){
      if(this.level <= level){
    	  findAnswer(message , answer);
      }
      if(nextProcess !=null){
         nextProcess.process(level, message,answer);
      }
   }

   abstract protected void findAnswer(String message , Answer answer);
	
}