package com.wanda.chatbot.clent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class text extends JFrame implements KeyListener
{  
 private static text frm;
 private static JTextField txt;
 text()
 {
  setTitle("TextField Test");  
  setLocation(200, 200);
  setSize(220, 100);
 } 
 public static void main(String[] args)
 {  
  frm=new text();
  frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
  frm.setLayout(new FlowLayout());  
  txt = new JTextField(12);
  txt.addKeyListener(frm);
  frm.add(txt); 
  frm.setVisible(true);
 } 
 public void keyPressed(KeyEvent  e)
 {   
  if(e.getSource()==txt)
  {
   if(e.getKeyCode() == KeyEvent.VK_ENTER) //判断按下的键是否是回车键
   {  
    txt.setText("");   
    txt.setText("Hello World!");
   }
  }  
 } 
 public void keyReleased(KeyEvent e)
 {  
 }
 public void keyTyped(KeyEvent  e)
 {  
 }
}
