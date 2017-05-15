package com.wanda.chatbot.clent;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.alibaba.fastjson.JSONObject;

public class ChatRoom extends JFrame implements MouseListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new ChatRoom();
	}

	private JFrame frame;
	private JTextArea viewArea;
	private JTextField viewField;
	private JButton buttonSend;
	private JButton buttonQuit;
	private JButton buttonLocal;
	private JButton buttonOnline;
	private JButton buttonOnlineTest;
	private JLabel jlable;
	private JTextField MyName;
	private final String server_local = "http://127.0.0.1:11765/?q=";
	private final String server_online = "http://10.209.20.198:11765/?q=";
	private final String server_online_test = "http://10.209.20.198:12765/?q=";
	private String server = server_online;
	private String servername = "小飞(online):";
	
	public ChatRoom() {
		frame = new JFrame("Chat Room");
		viewArea = new JTextArea(10, 50);
		viewField = new JTextField(50);
		jlable = new JLabel();
		jlable.setText("请勿使用代理");
		buttonSend = new JButton("Send");
		buttonQuit = new JButton("Quit");
		buttonLocal = new JButton("Local");
		buttonOnline = new JButton("Online");
		buttonOnlineTest = new JButton("Test");
		MyName = new JTextField();
		MyName.setColumns(9);
		MyName.setText("飞翔的企鹅");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(8, 1));
		panel.add(jlable);
//		panel.add(MyName);
		panel.add(buttonSend);
		panel.add(buttonQuit);
		panel.add(buttonLocal);
		panel.add(buttonOnline);
		panel.add(buttonOnlineTest);
		JScrollPane sp = new JScrollPane(viewArea);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.add("Center", sp);
		frame.add("East", panel);
		frame.add("South", viewField);
		frame.setSize(400, 250);
		frame.setVisible(true);
		buttonSend.addMouseListener((MouseListener) this);
		buttonQuit.addMouseListener((MouseListener) this);
		buttonLocal.addMouseListener((MouseListener) this);
		buttonOnline.addMouseListener((MouseListener) this);
		buttonOnlineTest.addMouseListener((MouseListener) this);
		viewField.addKeyListener((KeyListener) this);
		viewArea.setText("你好，欢迎使用小飞测试工具\n");
	}

	public void mouseClicked(MouseEvent evt) {
		try {
			String message = "";
			StringBuilder sb = new StringBuilder();
			message = viewField.getText();
			if (evt.getSource() == buttonSend) {
				String answer = getAnswer(URLEncoder.encode(message, "utf-8"));

				sb.append("guest:" + message + "\n");
				if (answer != null)
					sb.append(servername + answer + "\n");
				else
					sb.append(servername + "balabala...\n");
				viewArea.setText(viewArea.getText() + sb.toString());
			}
			if (evt.getSource() == buttonQuit) {
				message = "退出";
				viewArea.setText(message);
				viewField.setText("");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			if(evt.getSource() == buttonLocal){
				server = server_local;
				servername = "小飞(local):";
			}
			else if (evt.getSource() == buttonOnline){
				server = server_online;
				servername = "小飞(online):";
			}else if(evt.getSource() == buttonOnlineTest){
				server = server_online_test;
				servername = "小飞(test):";
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mousePressed(MouseEvent evt) {
	}

	public void mouseReleased(MouseEvent evt) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}



	public String getAnswer(String question) {
		try {
			String result = "";
			String qString = server + question;
			String jsonRe = HttpsRequestUtil.getDataByHttp(qString);
			if(jsonRe != null && jsonRe.length() > 0){
				JSONObject j = JSONObject.parseObject(jsonRe);
				result = j.getString("tip");
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent evt) {
		try {
			String message = "";
			StringBuilder sb = new StringBuilder();
			message = viewField.getText();
			if (evt.getSource() == viewField) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					String answer = getAnswer(URLEncoder.encode(message, "utf-8"));

					sb.append("guest:" + message + "\n");
					if (answer != null)
						sb.append(servername + answer + "\n");
					else
						sb.append(servername + "balabala...\n");
					viewArea.setText(viewArea.getText() + sb.toString());
					viewField.setText("");
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}