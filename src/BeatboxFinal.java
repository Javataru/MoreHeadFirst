import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.event.*; 

public class BeatboxFinal {
	JFrame theFrame;
	JPanel mainPanel;
	JList incomingList;
	JTextField userMessage;
	ArrayList <JCheckBox> checkboxList;
	int nextNum;
	Vector<String> listVector = new Vector<String>();
	String userName;
	ObjectOutputStream out;
	ObjectInputStream in;
	HashMap<String boolean[]> otherSeqsMap = new HashMap<String, boolean[]>();
	
	Sequencer sequencer;
	Sequence sequence;
	Sequence mySequence = null;
	Track track;
	
	String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal", "Hand Clap", "High Tom", "High Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibraslap", "Low-Mid Tom", "High Agogo", "Open Hi Conga"};
	int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
			

	public static void main(String[] args) {
	new BeatboxFinal().startUp(args[0]);
	}
	public void startUp(String name) {
		userName = name;
		//Start to open a connection to the server
		try {
			Socket sock = new Socket("127.0.0.1", 4242);
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
			Thread remote = new Thread(new RemoteReader());
			remote.start();
		}
		catch(Exception ex) {
			System.out.println("You couldn't connect so I guess you will have to just play with yourself, buddy.");
		}
		setUpMidi();
		buildGUI();
		
	}
	public void buildGUI() {
		theFrame= new JFrame("Drop da BEATZ!");
		BorderLayout layout =  new BorderLayout();
		JPanel background =  new JPanel(layout);
		background.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.GREEN));
		
		checkboxList = new ArrayList<JCheckBox>();
		
		Box buttonBox =  new Box(BoxLayout.Y_AXIS);
		JButton start = new JButton("Start!");
		start.addActionListener(new MyStartListener());
		buttonBox.add(start);
		
		JButton stop = new JButton("Stop!");
		stop.addActionListener(new MyStopListener());
		buttonBox.add(stop);
		
		JButton upTempo = new JButton("Raise Tempo!");
		upTempo.addActionListener(new MyUpTempoListener());
		buttonBox.add(upTempo);
		
		JButton downTempo =  new JButton("Lower Tempo!");
		downTempo.addActionListener(new MyDownTempoListener());
		buttonBox.add(downTempo);
		
		JButton sendIt = new JButton("Send Beat!");
		sendIt.addActionListener(new MySendItListener());
		buttonBox.add(sendIt);
		
		userMessage = new JTextField();
		buttonBox.add(userMessage);
		
		incomingList = new JList();
		incomingList.addListSelectionListener(new MyListSelectionListener());
		incomingList.setSelectionMode(ListSelectionMode1.SINGLE_SELECTION);
		JScrollPane theList = new JScrollPane(incomingList);
		buttonBox.add(theList);
		incomingList.setListData(listVector);
		
		Box nameBox =  new Box(BoxLayout.Y_AXIS);
		for (int i = 0; i < 16; i++) {
			nameBox.add(new Label (instrumentNames [i]));
		}
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		theFrame.getContentPane().add(background);
		GridLayout grid = new GridLayout(16,16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		
		for (int i = 0; i < 256; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkboxList.add(c);
			mainPanel.add(c);
		}
		theFrame.setBounds(50, 50, 300, 300);
		theFrame.pack();
		theFrame.setVisible(true);
		}
	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ,4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		}
		catch(Exception e) {e.printStackTrace();}
		
			
		
	}
	public void buildTrackAndStart() {
		ArrayList<Integer> trackList = null; //instruments selected 
				sequence.deleteTrack(track);
				track = sequence.createTrack();
				for(int i = 0; i < 16; i++) {
					trackList = new ArrayList<Integer>();
					for (int j = 0; j < 16; j++) {
						
					
					JCheckBox jc = (JCheckBox) checkboxList.get(j + (16*i));
					if (jc.isSelected()) {
						int key = instruments[i];
						trackList.add(new Integer(key));
						
					}
					else {
						trackList.add(null); //empty slot in track
					}
				}
				makeTracks(trackList);
	}
	track.add(makeEvent(192, 9, 1, 0, 15)); //for 16 beats total
	try {
		sequencer.setSequence(sequence);
		sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
		sequencer.start();
		sequencer.setTempoInBPM(120);
	} catch (Exception e) {e.printStackTrace();}

}
	public class MyStartListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			buildTrackAndStart();
		}
	}
	public class MyStopListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			sequencer.stop();
		}
	}
	public class MyUpTempoListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			float tempoFactor =  new sequencer.getTempoFactor();
			sequencer.setTempoFactor((float)(tempoFactor * 1.03));
					}
	}
	public class MyDownTempoListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * 0.97));
		}
	}
	
	public class MySendItListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			//ArrayList of checkbox state
			boolean[] checkboxState = new boolean[256];
			for (int i = 0; i < 256; i++) {
				JCheckBox check = (JCheckBox) checkboxList.get(i);
				if (check.isSelected()) {
					checkboxState[i] = true;
				}
			}
			String messageToSend = null;
			try {
				out.writeObject(userName + nextNum++ + ": " + userMessage.getText());
				out.writeObject(checkboxState);
			}
			catch(Exception ex) {
				System.out.println("I could not send your request to the server. Prepare for meltdown.");
			}
			userMessage.setText("");
		}
	}
	public class MyListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent le) {
			if (!le.getValueIsAdjusting()) {
				String selection = (String) incomingList.getSelectedValue();
				if (selected != null) {
					//go to map and change sequence
					boolean[] selectedState = (boolean[]) otherSeqsMap.get(selected);
					changeSequence(selectedState);
					sequencer.stop();
					buildTrackAndStart();
				}
			}
		}
	}
	public class RemoteReader implements Runnable {
		boolean[] checkboxState = null;
		String nameToShow = null;
		Object obj = null;
		public void run() {
			try {
				while ((obj=in.readObject()) != null) {
					System.out.println("I got an object from the server!");
					System.out.println(obj.getClass());
					String nameToShow = (String) obj;
					checkboxState = (boolean[]) in.readObject();
					otherSeqsMap.put(nameToShow, checkboxState);
					listVector.add(nameToShow);
					incomingList.setListData(listVector);
				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	public class MyPlayMineListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			if (mySequence != null) {
				sequence = mySequence; //restore to the original
			}
		}
	}
	public void changeSequence(boolean[] checkboxState) {
		for (int i=0; i < 256; i++) {
			JCheckBox check = (JCheckBox) checkboxList.get(i);
			if (checkboxState[i]) {
				check.setSelected(true);
			}
			else {
				check.setSelected(false);
			}
		}
	}
	public void makeTracks(ArrayList list) {
		Iterator it = list.iterator();
		for (int i=0; i < 16; i++) {
			Integer num = (Integer) it.next();
			if (num != null) {
				int numKey = num.intValue();
				track.add(makeEvent (144, 9, numKey, 100, i));
				track.add(makeEvent(128, 9, numKey, 100, i + 1));
			}
		}
	}
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
			
		}
		catch(Exception e) {
			
		}
		return event;
	}
}
	
	

