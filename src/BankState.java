

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;



public class BankState extends State{
	
	//Panels and Layout Managers
	public static final String OVERVIEW_STATE = "overview";
	public static final String INSPECT_STATE = "inspect";
	
	
	private static CardLayout cl = new CardLayout();
	private static OverviewPasswordState overviewPasswordsContainer;
	private static InspectPasswordState inspectPasswordContainer;
	private static JPanel componentsExcludingMenuBar = new JPanel();
	

	
	//Menu bar variables
	private JMenuBar toolBar = new JMenuBar();
	private JMenu setting = new JMenu("Setting");
	private JMenuItem[] opts = {new JMenuItem("Configure Settings"), new JMenuItem("Log out"),new JMenuItem("Close Program")};
	
	private static char[] key;
	
	
	//Processing Variables
	private static String username;

	
	
	public BankState(String username) {
		BankState.username = username;
		
		//Ensure that the file is created.
		Util.createTextFile(username + ".txt");
		Util.createTextFile(username + "hashes.txt");
		Util.createTextFile(username + "settings.txt");
		initMenuBarEvents();
		//Menu Bar
		initMenuBar();
		initSubContainers();
		
		panel.add(componentsExcludingMenuBar);
	}
	
	
	public static void initSubContainers() {
		componentsExcludingMenuBar.removeAll();
		inspectPasswordContainer = new InspectPasswordState();
		overviewPasswordsContainer = new OverviewPasswordState();
		componentsExcludingMenuBar.setLayout(cl);
		componentsExcludingMenuBar.add(inspectPasswordContainer, INSPECT_STATE);
		componentsExcludingMenuBar.add(overviewPasswordsContainer, OVERVIEW_STATE);
		//Default to show the overview state.
		cl.show(componentsExcludingMenuBar, OVERVIEW_STATE);
		
	}
	
	
	
	private void initMenuBar() {
		//https://www.youtube.com/watch?v=dwLkDGm5EBc
		for(int i =0; i < opts.length; i++) {
			setting.add(opts[i]);
		}
		//https://stackoverflow.com/questions/4299846/add-jmenubar-to-a-jpanel
		toolBar.add(setting);
		panel.setLayout(new BorderLayout());
		panel.add(toolBar, BorderLayout.NORTH);
		
	}
	
	public static void setCardLayout(String str) {
		//Refresh all the states of the program
		
		
		//Remove and add all the containers again.
		initSubContainers();
		
		//Show specific card.
		if(str.equals(INSPECT_STATE)) {
			cl.show(componentsExcludingMenuBar, INSPECT_STATE);
		}
		if(str.equals(OVERVIEW_STATE)) {
			cl.show(componentsExcludingMenuBar, OVERVIEW_STATE);
		}
	}
	
	private void initMenuBarEvents() {
		//Settings
		opts[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Display.setCardLayout(3);
			}
		});
		//Log out
		opts[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.gc();
				try {
					AESFileEncryption.encrypt(key, username, "");
					AESFileEncryption.encrypt(key, username, "hashes");
					AESFileEncryption.encrypt(key, username, "settings");
					Util.deleteFile(username + ".txt");
					Util.deleteFile(username + "hashes.txt");
					Util.deleteFile(username + "settings.txt");
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Display.setCardLayout(1);
			}
		});
		
		opts[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.gc();
				try {
					AESFileEncryption.encrypt(BankState.getKey(), BankState.getUsername(), "");
					AESFileEncryption.encrypt(BankState.getKey(), BankState.getUsername(), "hashes");
					AESFileEncryption.encrypt(BankState.getKey(), BankState.getUsername(), "settings");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Display.getFrame().dispose();
				System.exit(0);
				
				
			}
		});
	}
	
	public static void setKey(char[] key) {
		BankState.key = key;
	}
	public static char[] getKey() {
		return key;
	}
	

	//Getters
	public static String getUsername() {
		return username;
	}
}
