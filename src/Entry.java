

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Entry {
	private String[] contents;
	public static final int ENTRY_ID = 0;
	public static final int APPLICATION_NAME = 1;
	public static final int URL = 2;
	public static final int USERNAME = 3;
	public static final int PASSWORD = 4;
	public static final int PASSWORD_STRENGTH = 5;
	public static final int LAST_UPDATED = 6;
	public static final int LAST_USED = 7;
	
	private final int LONGEST_USERNAME_LENGTH_FOR_PANEL = 10;
	private final int LONGEST_APPLICATION_LENGTH_FOR_PANEL = 10;
	private JButton inspect = new JButton("Inspect");
	private static int inspectItemID = -1;
	
	
	public Entry(String[] contents) {
		this.contents = contents;
		initButtonEvent();
	}
	
	//Panels are created for each entry.
	public JPanel getOverviewPanel() {
		JPanel overviewPanel = new JPanel();
		overviewPanel.setPreferredSize(new Dimension(200,100));
		
		//Check the overview panel labels and see if they are too long.
		//If too long, shorten them and use an ellipsis.
		JLabel[] labels = new JLabel[2];
		if(contents[APPLICATION_NAME].length() < LONGEST_APPLICATION_LENGTH_FOR_PANEL)
			labels[0] = new JLabel(contents[APPLICATION_NAME]);
		else
			labels[0] = new JLabel((contents[APPLICATION_NAME].substring(0, LONGEST_APPLICATION_LENGTH_FOR_PANEL - 3) + "..."));
		
		if(contents[USERNAME].length() < LONGEST_USERNAME_LENGTH_FOR_PANEL)
			labels[1] = new JLabel("Username: " + contents[USERNAME]);
		else
			labels[1] = new JLabel("Username: " + (contents[USERNAME].substring(0, LONGEST_USERNAME_LENGTH_FOR_PANEL - 3) + "..."));
		overviewPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx =0;
		constraints.gridy =0;
		Border border = new LineBorder(Color.BLACK, 2, true);
		overviewPanel.setBorder(border);
		labels[0].setFont(new Font("Sans Serif", Font.PLAIN, 18));
		overviewPanel.add(labels[0], constraints);
		labels[1].setFont(new Font("Sans Serif", Font.BOLD, 12));
		constraints.gridy++;
		overviewPanel.add(labels[1], constraints);
		constraints.gridy++;
		overviewPanel.add(inspect, constraints);
		return overviewPanel;
	}
	
	private void initButtonEvent() {
		inspect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inspectItemID = Integer.parseInt(contents[ENTRY_ID]);
				BankState.setCardLayout(BankState.INSPECT_STATE);
			}
		});
		
	}
	
	public static void resetInspectItemID() {
		inspectItemID = -1;
	}
	
	public static int getInspectItemID() {
		return inspectItemID;
	}
	
	public String getContent(int index) {
		return contents[index];
	}
	
	//How will it know which contents to search?
	public boolean containsKeyWord(String keyWord) {
		
		//Since there is not search term, include in in display.
		if(keyWord.equals(""))
			return true;
		
		//If there is is an actual word, check if the entry contains it.
		for(int i =0; i < contents.length; i++) {
			if(contents[i].contains(keyWord))
				return true;
		}
		
		//Otherwise return false.
		return false;
	}
	
	
}
