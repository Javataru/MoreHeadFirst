import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;

public class SimpleTextResponse implements ActionListener {
	JTextArea text;
	

	public static void main(String[] args) {
		SimpleTextResponse gui = new SimpleTextResponse();
		gui.go();

	}
	public void go() {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JButton button = new JButton("Click this to make magic");
		button.addActionListener(this);
		text = new JTextArea(10,20);
		text.setLineWrap(true);
		
		JScrollPane scroller = new JScrollPane(text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scroller);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.getContentPane().add(BorderLayout.SOUTH, button);
		
		frame.setSize(350,300);
		frame.setVisible(true);
	}
	public void actionPerformed (ActionEvent ev) {
		text.append("Button Pressed \n");
	}

}
