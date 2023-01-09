

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



public class OverviewPasswordState extends JPanel{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String[] sortingOptions = {"Application Name (A-Z)", "Username (A-Z)", "Passwords Last Used (Oldest - Most Recent)", "Password Strength (Weakest-Strongest)"};

	private final int NUMBER_OF_PANELS_PER_ROW = 3;
	private JButton addPassword = new JButton("Add new Entry");
	private JButton searchButton = new JButton("Search");
	private JComboBox<?> sortingComboBox = new JComboBox<Object>(sortingOptions);
	private JTextField searchBar = new JTextField(24);
	private static String searchKeyWord;
	
	private Entry[] entries;
	private static int numberOfEntries;
	
	
	private static int sortingOption;
	private boolean oneOrMorePasswordContainersVisible = false;
	public OverviewPasswordState() {

		initButtonEvents();
		initEntries();
		numberOfEntries = entries.length;
		initOverviewPanel();
		initComboBoxEvents();
	}
	
	
	private void initEntries() {
		String[] lines = Util.readATextFile(BankState.getUsername() + ".txt"); 
		entries = new Entry[lines.length/8];
		
		for(int i =0; i < lines.length/8; i++) {
			String[] str = new String[8];
			for(int j = 0; j < 8; j++) {
				str[j] = lines[i * 8 + j];
			}
			entries[i] = new Entry(str);
		}
		
			entries = sortEntries(sortingOption, entries);
		}
	
	
	private Entry[] sortEntries(int sortingOptionIndex, Entry[] entries) {
		// TODO Auto-generated method stub
		Entry temp;
		int indexOfContent;
		switch(sortingOptions[sortingOptionIndex]) {
		case "Application Name (A-Z)":
			indexOfContent = Entry.APPLICATION_NAME;
			break;
		case "Username (A-Z)":
			indexOfContent = Entry.USERNAME;
			break;
		
		case "Passwords Last Used (Oldest - Most Recent)":
			indexOfContent = Entry.LAST_USED;
			break;
		
		case "Password Strength (Weakest-Strongest)":
			indexOfContent = Entry.PASSWORD_STRENGTH;
			break;
		default:
			return entries;
		}
		
		for(int i = 0; i< entries.length - 1 ; i++) {
			for(int j = i; j < entries.length; j++) {
				
				if(entries[i].getContent(indexOfContent).toLowerCase().compareTo(entries[j].getContent(indexOfContent).toLowerCase()) > 0) {
					temp = entries[i];
					entries[i] = entries[j];
					entries[j] = temp;
				}
				else
					//Sort alphabetically if the entries are of same value.
					if(entries[i].getContent(indexOfContent).toLowerCase().compareTo(entries[j].getContent(indexOfContent).toLowerCase()) == 0)
						if(entries[i].getContent(Entry.APPLICATION_NAME).toLowerCase().compareTo(entries[j].getContent(Entry.APPLICATION_NAME).toLowerCase()) > 0) {
							temp = entries[i];
							entries[i] = entries[j];
							entries[j] = temp;
						}
							
							
				
			}
		}
		
		return entries;
	}
	
	
	

	
	public void initOverviewPanel() {
		//TOP PANEL SECTION
		
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.GRAY);
		topPanel.add(new JLabel("Order"));
		
		sortingComboBox.setSelectedIndex(sortingOption);
		topPanel.add(sortingComboBox);
		
		topPanel.add(new JLabel("Search"));
		topPanel.add(searchBar);
		searchBar.setText(searchKeyWord);
		topPanel.add(searchButton);
		this.setLayout(new BorderLayout());
		this.add(topPanel, BorderLayout.NORTH);
		
		//MIDDLE PANEL SECTION
		
		JPanel middlePanel = new JPanel();
		JLabel middlePanelTitle = new JLabel("Password Entries");
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		middlePanel.setLayout(new GridBagLayout());
		constraints.anchor = GridBagConstraints.NORTHWEST;
		
		middlePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
		middlePanelTitle.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		

			
		
		middlePanel.add(middlePanelTitle,constraints);
		
		constraints.gridy = 0;
		constraints.gridy = 1;
		
		
		
		
		//ADD MINI-PASSWORD CONTAINERS HERE
		
		//For the first instance of the class, instantiate the keyword as an empty string.
		if(searchKeyWord == null)
			searchKeyWord = "";
		
		//Go through all entries and add panels
		for(int i =0; i < entries.length; i++) {
			//Only add panels if they contain the key word or key word is nothing.
			if(entries[i].containsKeyWord(searchKeyWord)) {
				oneOrMorePasswordContainersVisible = true;
				if(i % NUMBER_OF_PANELS_PER_ROW == 0) {

					constraints.gridy++;
					constraints.gridx = 0;
				}
				middlePanel.add(entries[i].getOverviewPanel(), constraints);	
				constraints.gridx++;
					
			}
		}
		
		//When there are no panels visible, prompt the user appropiately.
		if(!oneOrMorePasswordContainersVisible && searchKeyWord.equals("")) {
			constraints.gridy++;
			middlePanel.add(new JLabel("You have no entries saved."),constraints);
		}
		else
			if(!oneOrMorePasswordContainersVisible && !searchKeyWord.equals("")) {
				constraints.gridy++;
				middlePanel.add(new JLabel("No matches were found."),constraints);
			}
		
		
		//Create scroll pane of right section here.
		JScrollPane scroll = new JScrollPane(middlePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
		        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//Increase scroll speed
		// https://alvinalexander.com/blog/post/jfc-swing/how-increase-speed-jscrollpane-mouse-wheel/#:~:text=Just%20use%20the%20reference%20to,setUnitIncrement(16)%3B
		scroll.getVerticalScrollBar(). setUnitIncrement(16);
		
		
		
		
		//BOTTOM PANEL SECTION
		JPanel bottomPanel = new JPanel();
		
		bottomPanel.add(addPassword);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(150/2-25, 0, 0, 0));
		bottomPanel.setMaximumSize(new Dimension(Display.getWindowWidth(), 150));
		bottomPanel.setPreferredSize(new Dimension(1000, 150));
		JPanel temporaryPanel = new JPanel();
		temporaryPanel.setLayout(new BoxLayout(temporaryPanel, BoxLayout.Y_AXIS));
		
		
		temporaryPanel.add(scroll, BorderLayout.CENTER);
		temporaryPanel.add(bottomPanel, BorderLayout.WEST);
		this.add(temporaryPanel);
		
		
	}
	
	
	
	private void initButtonEvents() {
		addPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BankState.setCardLayout(BankState.INSPECT_STATE);
			}
		});
		
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Set the keyword.
				searchKeyWord = searchBar.getText();
				//Refresh the panel.
				BankState.setCardLayout(BankState.OVERVIEW_STATE);
			}
		});
	}
	private void initComboBoxEvents() {
		sortingComboBox.addItemListener(new ItemListener() {	
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() ==ItemEvent.SELECTED) {
					sortingOption = sortingComboBox.getSelectedIndex();
				}
				BankState.setCardLayout(BankState.OVERVIEW_STATE);
			}
		});	
	}	
	
	public int getComboBoxIndex() {
		return sortingComboBox.getSelectedIndex();
	}
	
	
	public static int getNumberOfEntries() {
		return numberOfEntries;
	}
	
	public static void removeOneFromNumberOfEntries() {
		numberOfEntries--;
	}
	
}
