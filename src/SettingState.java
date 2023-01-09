


import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

public class SettingState extends State{
	//Menu bar variables
	
	private JMenuBar toolBar = new JMenuBar();
	private JMenu setting = new JMenu("Setting");
	private JMenuItem[] opts = {new JMenuItem("Return to Bank"), new JMenuItem("Log out"),new JMenuItem("Close Program")};
	private JCheckBox[] checkBoxes = new JCheckBox[2];
	

	private JFormattedTextField numberOfPasswordRepeatsAcceptedTextField;
	private JFormattedTextField lengthOfGeneratedPasswordTextField;
	private JTextField charactersForPasswordGenerationTextField = new JTextField(20);
	
	private JPanel settingSubContainer = new JPanel();
	private JPanel updateSubContainer = new JPanel();
	private JPanel pageContentsContainer = new JPanel();
	private JButton[] buttons = {new JButton("Update Password"), new JButton("Update Security Question"), new JButton("Go Back"), new JButton("Save Changes"),};
	
	
	//Reading from the text file. Each set to their default settings. Overwritten when reading from text file.
	
	private static boolean displayPasswordsByDefault = false;
	private static boolean enableRepeatPassword = false;
	private static int numberOfPasswordRepeatsAccepted = 5;
	private static int lengthOfGeneratedPassword = 10;
	private static String charactersForPasswordGeneration = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*(";

	private static JFrame changePassword = new JFrame();
	
	
	public SettingState(String username) {
		initSettingVariables();
		initMenuBar();
		initMenuBarEvents();
		initSubPanelsToMainPanel();
		initJComponentsInUpdate();
		initCheckBoxes();
		initJTextField();
		initCheckBoxEvents();
		initButtonEvents();
		initJComponentsInSetting();
	}
	
	private void initSettingVariables() {
		String[] entries = Util.readATextFile(BankState.getUsername() + "settings.txt");
		if(entries.length != 5) {
			Util.writeToFile(BankState.getUsername() + "settings.txt", "false");
			Util.writeToFile(BankState.getUsername() + "settings.txt", "false");
			Util.writeToFile(BankState.getUsername() + "settings.txt", "5");
			Util.writeToFile(BankState.getUsername() + "settings.txt", "" + lengthOfGeneratedPassword);
			Util.writeToFile(BankState.getUsername() + "settings.txt", charactersForPasswordGeneration);
		}
		else {
			try {
				displayPasswordsByDefault = Boolean.parseBoolean(entries[0]);
				enableRepeatPassword = Boolean.parseBoolean(entries[1]);
				numberOfPasswordRepeatsAccepted = Integer.parseInt(entries[2]);
				lengthOfGeneratedPassword = Integer.parseInt(entries[3]);
				charactersForPasswordGeneration = entries[4];
			}
			catch (Exception e) {
				Display.errorBox("Error has occured", "Error");
				
				Util.replaceLine(BankState.getUsername() + "settings.txt", "false", 1);
				Util.replaceLine(BankState.getUsername() + "settings.txt", "false", 2);
				Util.replaceLine(BankState.getUsername() + "settings.txt", "5", 3);
				Util.replaceLine(BankState.getUsername() + "settings.txt", "" + lengthOfGeneratedPassword, 5);
				Util.replaceLine(BankState.getUsername() + "settings.txt", charactersForPasswordGeneration, 4);
			}
		}
	}
	
	private void initSubPanelsToMainPanel() {
		panel.add(pageContentsContainer);
		pageContentsContainer.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 0;
		constraints.gridy = 0;
		pageContentsContainer.add(settingSubContainer, constraints);
		constraints.gridy++;
		pageContentsContainer.add(updateSubContainer, constraints);
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
	
	private void initMenuBarEvents() {
		opts[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Display.setCardLayout(0);
			}
		});
		opts[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AESFileEncryption.encrypt(BankState.getKey(), BankState.getUsername(), "");
					AESFileEncryption.encrypt(BankState.getKey(), BankState.getUsername(), "hashes");
					AESFileEncryption.encrypt(BankState.getKey(), BankState.getUsername(), "settings");
					Util.deleteFile(BankState.getUsername() + ".txt");
					Util.deleteFile(BankState.getUsername() + "hashes.txt");
					Util.deleteFile(BankState.getUsername() + "settings.txt");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Display.setCardLayout(1);
			}
		});
		opts[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);			
			}
		});
	}
	
	
	
	private void initJComponentsInSetting() {
		settingSubContainer.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 0;
		constraints.gridy = 0;
		settingSubContainer.add(new JLabel("Display Passwords \n Visible by default"),constraints);
		constraints.gridy++;

		settingSubContainer.add(new JLabel("Enable repeats on \n previous passwords"), constraints);
		constraints.gridy++;
		settingSubContainer.add(new JLabel("\tNumber of recent passwords \n which cannot be used"), constraints);
		constraints.gridy++;
		settingSubContainer.add(new JLabel("\tLength of generated passwords"), constraints);
		constraints.gridy++;
		constraints.gridwidth = 2;
		settingSubContainer.add(new JLabel("Password generation contains \n the following characters"), constraints);
		constraints.gridwidth = 1;
		constraints.gridx = 1;
		constraints.gridy = 0;
		settingSubContainer.add(checkBoxes[constraints.gridy], constraints);
		constraints.gridy++;
		settingSubContainer.add(checkBoxes[constraints.gridy], constraints);
		constraints.gridy++;
		settingSubContainer.add(numberOfPasswordRepeatsAcceptedTextField,constraints);
		constraints.gridy++;
		settingSubContainer.add(lengthOfGeneratedPasswordTextField,constraints);
		constraints.gridwidth = 2;
		constraints.gridy++;
		constraints.gridy++;
		constraints.gridx = 0;
		settingSubContainer.add(charactersForPasswordGenerationTextField,constraints);
	}
	private void initJComponentsInUpdate() {
		updateSubContainer.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		updateSubContainer.add(buttons[0], constraints);
		constraints.gridx++;
		updateSubContainer.add(buttons[1], constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		updateSubContainer.add(buttons[2], constraints);
		constraints.gridx++;
		updateSubContainer.add(buttons[3], constraints);
		
	}
	private void initCheckBoxes() {
		for(int i =0; i < checkBoxes.length; i++) {
			checkBoxes[i] = new JCheckBox();
		}
		
		checkBoxes[0].setSelected(displayPasswordsByDefault);
		checkBoxes[1].setSelected(enableRepeatPassword);
	}
	
	private void initJTextField() {
		 NumberFormat longFormat = NumberFormat.getIntegerInstance();
		 
		 //Cannot use a KeyListener since it doesn't prevent a copy and paste of text into integer field.
		 //https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers
		 NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		 numberFormatter.setValueClass(Long.class);
		 numberFormatter.setAllowsInvalid(false);
		 numberFormatter.setMinimum(0l);
		 numberFormatter.setMaximum(Long.MAX_VALUE);
		 
		 
		  numberOfPasswordRepeatsAcceptedTextField = new JFormattedTextField(numberFormatter);
		  numberOfPasswordRepeatsAcceptedTextField.setColumns(2);
		  
		  NumberFormat longFormat2 = NumberFormat.getIntegerInstance();
			 
			 //Cannot use a KeyListener since it doesn't prevent a copy and paste of text into integer field.
			 //https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers
			 NumberFormatter numberFormatter2 = new NumberFormatter(longFormat2);
			 numberFormatter2.setValueClass(Long.class);
			 numberFormatter2.setAllowsInvalid(false);
			 numberFormatter2.setMinimum(0l);
			 numberFormatter2.setMaximum(Long.MAX_VALUE);
		  lengthOfGeneratedPasswordTextField = new JFormattedTextField(numberFormatter2);
		  lengthOfGeneratedPasswordTextField.setColumns(2);

		numberOfPasswordRepeatsAcceptedTextField.setEnabled(!enableRepeatPassword);
		
		numberOfPasswordRepeatsAcceptedTextField.setText("" + numberOfPasswordRepeatsAccepted);
		lengthOfGeneratedPasswordTextField.setText("" + lengthOfGeneratedPassword);
		charactersForPasswordGenerationTextField.setText(charactersForPasswordGeneration);
	}
	private void initCheckBoxEvents() {
		//https://stackoverflow.com/questions/16627702/grayed-out-textfield-unless-checkbox-ticked
		checkBoxes[1].addItemListener(new ItemListener() {
			  public void itemStateChanged(ItemEvent itemEvent){
			    // the line below is the line that matters, that enables/disables the text field
				  numberOfPasswordRepeatsAcceptedTextField.setEnabled(!(itemEvent.getStateChange() == ItemEvent.SELECTED));
			  }
			});
	}
	
	
	private void initButtonEvents() {
		
		//Password
		buttons[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePassword = new JFrame();
				changePassword.setSize(800, 300);
				changePassword.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				changePassword.setResizable(false);
				changePassword.setLocationRelativeTo(null);
				changePassword.setVisible(true);
				
				ChangePasswordState cp = new ChangePasswordState();
				changePassword.add(cp);
			}
		});
		
		//Security question
		buttons[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changePassword = new JFrame();
				changePassword.setSize(800, 300);
				changePassword.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				changePassword.setResizable(false);
				changePassword.setLocationRelativeTo(null);
				changePassword.setVisible(true);
				
				ChangeSecurityQuestionState csq = new ChangeSecurityQuestionState();
				changePassword.add(csq);
			}
		});
		buttons[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Display.setCardLayout(Display.BANK_STATE_NUMBER);
			}
		});
		buttons[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayPasswordsByDefault = checkBoxes[0].isSelected();
				enableRepeatPassword = checkBoxes[1].isSelected();
				
				int first = numberOfPasswordRepeatsAccepted;
				int second = Integer.parseInt(numberOfPasswordRepeatsAcceptedTextField.getText());
				
				if(first < second) {
					try {
				    	int counter = 0;
				    	StringBuffer newContent = new StringBuffer();
				    	String line;
				    	BufferedReader br = new BufferedReader(new FileReader(BankState.getUsername() +"hashes.txt"));
				    	while ((line = br.readLine()) != null) {
				    		counter++;
				    		if (counter == first + 2) {
				    			counter = 0;
				    			for(int i =0; i < second - first; i++) { // new line
				    				newContent.append(" ");
						    		newContent.append("\n");
				    			}
				    		}
				    		newContent.append(line);
				    		newContent.append("\n"); // new line
				    	}
				    	br.close();
				    	
				    	//Since the end of the text is null, it wont add to the last entry. manually add in.
				    	for(int i =0; i < second - first; i++) { // new line
		    				newContent.append(" ");
				    		newContent.append("\n");
		    			}
				    	
				    	FileWriter removeLine = new FileWriter(BankState.getUsername() +"hashes.txt");
				    	BufferedWriter change = new BufferedWriter(removeLine);
				    	PrintWriter replace = new PrintWriter(change);
				    	replace.write(newContent.toString());
				    	replace.close();
				    }
				     catch (Exception e) {
				        e.printStackTrace();
				    }
				}
				else if(first > second) {
					try {
				    	int counter = 0;
				    	StringBuffer newContent = new StringBuffer();
				    	String line;
				    	BufferedReader br = new BufferedReader(new FileReader(BankState.getUsername() +"hashes.txt"));
				    	while ((line = br.readLine()) != null) {
				    		counter++;
				    		if (counter == second) {
				    			counter = 0;
				    			for(int i =0; i < first - second; i++) { // new line
						    		br.readLine();
				    			}
				    		}
				    		newContent.append(line);
				    		newContent.append("\n"); // new line
				    	}
				    	br.close();

				    	FileWriter removeLine = new FileWriter(BankState.getUsername() +"hashes.txt");
				    	BufferedWriter change = new BufferedWriter(removeLine);
				    	PrintWriter replace = new PrintWriter(change);
				    	replace.write(newContent.toString());
				    	replace.close();
				    }
				     catch (Exception e) {
				        e.printStackTrace();
				    }
				}
			
				numberOfPasswordRepeatsAccepted = Integer.parseInt(numberOfPasswordRepeatsAcceptedTextField.getText());
				lengthOfGeneratedPassword = Integer.parseInt(lengthOfGeneratedPasswordTextField.getText());
				charactersForPasswordGeneration = charactersForPasswordGenerationTextField.getText();
				
				Util.replaceLine(BankState.getUsername() + "settings.txt", "" + checkBoxes[0].isSelected(), 1);
				Util.replaceLine(BankState.getUsername() + "settings.txt", "" + checkBoxes[1].isSelected(), 2);
				Util.replaceLine(BankState.getUsername() + "settings.txt", "" + numberOfPasswordRepeatsAcceptedTextField.getText(), 3 );
				Util.replaceLine(BankState.getUsername() + "settings.txt", "" + lengthOfGeneratedPasswordTextField.getText(), 4);
				Util.replaceLine(BankState.getUsername() + "settings.txt", "" + charactersForPasswordGenerationTextField.getText(), 5);
				Display.setCardLayout(Display.BANK_STATE_NUMBER);
			}
		});
		
	}
	
	
	public static int getNumberOfPasswordRepeatsAccepted() {
		return numberOfPasswordRepeatsAccepted;
	}
	
	public static boolean isRepeatEnabled() {
		return enableRepeatPassword;
	}
	
	public static boolean getDisplayPasswordsByDefault() {
		return displayPasswordsByDefault;
	}
	
//https://www.techiedelight.com/generate-random-alphanumeric-password-java/
	public static String generateRandomPassword()
	{
	// ASCII range - alphanumeric (0-9, a-z, A-Z)
	

	SecureRandom random = new SecureRandom();
	StringBuilder sb = new StringBuilder();
 
	// each iteration of loop choose a character randomly from the given ASCII range
	// and append it to StringBuilder instance
 
	for (int i = 0; i < lengthOfGeneratedPassword; i++) {
		int randomIndex = random.nextInt(charactersForPasswordGeneration.length());
		sb.append(charactersForPasswordGeneration.charAt(randomIndex));
	}
 
	return sb.toString();
	}
	
	public static JFrame getChangePassword() {
		return changePassword;
	}
	
	public static boolean getPasswordVisibleByDefault() {
		return displayPasswordsByDefault;
	}
}
