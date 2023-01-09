

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class InspectPasswordState extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] INSPECT_PASSWORD_FIELD_LABELS = {"Name", "URL", "Username", "Password", "Password Strength", "Last Updated", "Last Used", "Notes"};
	private final char[] SPECIAL_CHARACTERS = "(.*[ ! # @ $ % ^ & * ( ) - _ = + [ ] ; : ' \" , < . > / ?].*)".toCharArray();
	private final int RECOMMENDED_PASSWORD_LENGTH = 8;
	private final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	
	private JTextField[] textFields = new JTextField[6];
	private JPasswordField passwordField = new JPasswordField(24);
	private JButton delete = new JButton("Delete Entry");
	private JButton back = new JButton("Back");
	private JButton passwordVisibilityButton = new JButton("Show");
	private boolean passwordsAreVisible = SettingState.getPasswordVisibleByDefault();
	private JButton generatePassword = new JButton("Generate Random Password");
	private JButton save = new JButton("Save Changes");
	private String passwordHash = "";
	
	private CardLayout passwordCL = new CardLayout();
	private JPanel strengthBarContainer = new JPanel();
	private JPanel[] strengthBar = new JPanel[5];
	
	
	public InspectPasswordState() {
		initStrengthBar();
		initJComponents();
		initButtonEvents();
		setTextFields();
		initPasswordCheck();
		
	}
	
	public void initJComponents() {
		JLabel[] labels = new JLabel[8];
		
		
		for(int i = 0; i < 8; i++)
			labels[i] = new JLabel(INSPECT_PASSWORD_FIELD_LABELS[i]);
		
		//if(creatingPassword)
		//Date date = Calendar.getInstance
		
		
		
		for(int i = 0; i < 4; i++) {
			textFields[i] = new JTextField(24);
		}
		
		for(int i = 4; i < 6; i++) {
			textFields[i] = new JTextField(12);
			textFields[i].setEnabled(false);
		}
		
		
		
		if(passwordsAreVisible) {
			passwordField.setEchoChar((char)0);
			passwordVisibilityButton.setText("Hide");
			passwordsAreVisible = true;
		}else {
			passwordField.setEchoChar((char) UIManager.get("PasswordField.echoChar"));
			passwordVisibilityButton.setText("Show");
			passwordsAreVisible = false;
		}
		this.setLayout(new GridBagLayout());
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		//Left align	
		//https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		//Set up input fields and labels in a gridbag layout.
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		for(int i = 0; i < 3; i++) {
			constraints.gridy++;
			this.add(labels[i], constraints);
			constraints.gridy++;
			this.add(textFields[i], constraints);
		}
		
		constraints.gridy++;
		this.add(labels[3], constraints);
		constraints.gridy++;
		this.add(passwordField, constraints);
		constraints.gridx++;
		constraints.gridx++;
		this.add(passwordVisibilityButton, constraints);
		constraints.gridy++;
		this.add(generatePassword, constraints);
		constraints.gridx--;
		constraints.gridx--;
		this.add(labels[4], constraints);
		constraints.gridy++;
		this.add(strengthBarContainer, constraints);
		constraints.gridy++;
		constraints.gridwidth = 1;
		this.add(labels[5], constraints);
		constraints.gridy++;
		this.add(textFields[4], constraints);
		constraints.gridx++;
		constraints.gridy--;
		this.add(labels[6], constraints);
		constraints.gridy++;
		this.add(textFields[5], constraints);
		constraints.gridy++;
		constraints.gridx--;
		constraints.gridwidth = 2;
		//this.add(new JLabel("Notes"),constraints);

		constraints.gridwidth = 1;
		constraints.gridy++;
		
		this.add(back, constraints);
		constraints.gridx++;
		this.add(save, constraints);
		constraints.gridx++;
		if(Entry.getInspectItemID() > 0)
			this.add(delete, constraints);
	}
	
	public void setTextFields() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);  
		LocalDateTime now = LocalDateTime.now();  
		   
		if(Entry.getInspectItemID() != -1)
			for(int i =1; i < 4; i++) {
				textFields[i-1].setText(Util.readOneLine(BankState.getUsername() +".txt", (Entry.getInspectItemID()-1)*8 + (i+1)));
			}
		if(Entry.getInspectItemID() != -1) {
			passwordField.setText(Util.readOneLine(BankState.getUsername() +".txt", (Entry.getInspectItemID()-1)*8 + (4+1)));
		}
			
			
		if(Entry.getInspectItemID() == -1) {
			textFields[4].setText(dtf.format(now));
		}
		else {
			textFields[4].setText(Util.readOneLine(BankState.getUsername() +".txt", (Entry.getInspectItemID()-1)*8 + 7));
		}
		textFields[5].setText(dtf.format(now));
	}
	
	private void initStrengthBar() {		
		strengthBarContainer.setLayout(passwordCL);
		JLabel[] strengthLabels = {new JLabel("Very Weak"), new JLabel("Weak"), new JLabel("Okay"), new JLabel("Strong"), new JLabel("Very Strong")}; 
		
		for(int i =0; i < strengthBar.length; i++) {
			strengthBar[i] = new JPanel();
			strengthBar[i].setLayout(new BorderLayout());
			strengthBar[i].add(strengthLabels[i], BorderLayout.WEST);
		}
		
		
		strengthLabels[0].setForeground(Color.red);

		strengthLabels[1].setForeground(new Color(207, 158, 0));

		strengthLabels[2].setForeground(Color.YELLOW);
		


		strengthLabels[3].setForeground(Color.green);

		strengthLabels[4].setForeground(new Color(1, 135, 13));

		
		for(int i =0; i < strengthBar.length; i++) {
			strengthBarContainer.add(strengthBar[i], "" + i);
		}
		
		//INIT PASSWORD FIELD
		passwordField.setText(Util.readOneLine(BankState.getUsername() +".txt", (Entry.getInspectItemID()-1)*8 + (4+1)));
		
		if(Entry.getInspectItemID() == -1)
			passwordCL.show(strengthBarContainer, "0");
		else
			passwordCL.show(strengthBarContainer, "" + checkPassword());
	}
	
	private void initPasswordCheck() {
	passwordField.addKeyListener(
            new KeyListener(){

				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					passwordCL.show(strengthBarContainer, "" + checkPassword());
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					passwordCL.show(strengthBarContainer, "" + checkPassword());
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					System.out.println(checkPassword());
					passwordCL.show(strengthBarContainer, "" + checkPassword());

				}
            });
            
	}
            

	public int checkPassword() {
	char[] passwordChar = passwordField.getPassword();
	int points = 0;
	
	boolean isLongPassword = false;
	if(passwordChar.length >= RECOMMENDED_PASSWORD_LENGTH) {
		points++;
		isLongPassword = true;
				
	}
	boolean upperCaseLetterFound = false;
	boolean lowerCaseLetterFound = false;
	//System.out.println("String length: " + passwordChar.length);
		for(int i = 0; i < passwordChar.length && (!upperCaseLetterFound || !lowerCaseLetterFound); i++) {
			if(!upperCaseLetterFound && (int)passwordChar[i] >= (int)'A' && (int)passwordChar[i] <= (int)'Z')
				upperCaseLetterFound = true;
			if(!lowerCaseLetterFound && (int)passwordChar[i] >= (int)'a' && (int)passwordChar[i] <= (int)'z')
				lowerCaseLetterFound = true;
		}
		
		
	if(isLongPassword && upperCaseLetterFound && lowerCaseLetterFound) {
		points++;
	}

	boolean containsSpecialChars = false;
	for(int i =0; i< passwordChar.length && !containsSpecialChars; i++) {
		for(int j =0; j < SPECIAL_CHARACTERS.length && !containsSpecialChars; j++) {
			if(passwordChar[i] == SPECIAL_CHARACTERS[j]) {
				//System.out.println("special char");
				containsSpecialChars = true;
			}
		}
	}
	
	if(isLongPassword && containsSpecialChars) {
		points++;
	}
	
	boolean containsNumber = false;

	for(int i =0; i< passwordChar.length && !containsNumber;i++) {
		for(int j =0; j < 10 && !containsNumber; j++) {
			if(passwordChar[i] == "1234567890".charAt(j)) {
				//System.out.println("number");
				containsNumber = true;
			}
		}
	}
	
	if(isLongPassword && containsNumber) {
		points++;
	}
	
	return points;
	
	}

	
	private void initButtonEvents() {
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);  
				LocalDateTime now = LocalDateTime.now(); 
				//Change the time it was used.
				Util.replaceLine(BankState.getUsername() + ".txt", dtf.format(now), (Entry.getInspectItemID()-1) * 8 + 7 + 1);
				
				
				BankState.setCardLayout(BankState.OVERVIEW_STATE);
				Entry.resetInspectItemID();
			
			}
		});
		
		passwordVisibilityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!passwordsAreVisible) {
					passwordField.setEchoChar((char)0);
					passwordVisibilityButton.setText("Hide");
					passwordsAreVisible = true;
				}
				else {
					passwordField.setEchoChar((char) UIManager.get("PasswordField.echoChar"));
					passwordVisibilityButton.setText("Show");
					passwordsAreVisible = false;
				}
			}
		});
		
		
		save.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				String[] entries = new String[8];
				if(Entry.getInspectItemID() == -1) {
					entries[0] = "" + (OverviewPasswordState.getNumberOfEntries() + 1);
				}
				else {
					entries[0] = "" + Entry.getInspectItemID();
				}
				
				
				for(int i =1; i < 4; i++) {
					entries[i] = textFields[i-1].getText();
				}
				entries[4] = passwordField.getText();
				entries[5] = "" + checkPassword();
				
				//ADDING TO THE HASH TEXT FILE.
				
				if(Entry.getInspectItemID() != -1)
					passwordHash = Hash.hashPassword(Util.readOneLine(BankState.getUsername() +".txt", (Entry.getInspectItemID()-1)*8 + (4+1)).toCharArray());

				
				if(Entry.getInspectItemID() == -1) {
					Util.writeToFile(BankState.getUsername() + "hashes.txt", "" + (OverviewPasswordState.getNumberOfEntries() + 1));
					Util.writeToFile(BankState.getUsername() + "hashes.txt", Hash.hashPassword(passwordField.getPassword()));
					for(int i = 0; i < SettingState.getNumberOfPasswordRepeatsAccepted() - 1; i++) {
						Util.writeToFile(BankState.getUsername() + "hashes.txt", " ");
					}
				}
				else {
						MyLinkedList list = new MyLinkedList(Entry.getInspectItemID());
						if(!passwordHash.equals(Hash.hashPassword(passwordField.getPassword())) && !SettingState.isRepeatEnabled()) {
							if(!list.isRepeatingPassword(Hash.hashPassword(passwordField.getPassword()))) {
								list.add(Hash.hashPassword(passwordField.getPassword()));
								list.pop();
								list.replacePreviousHashes();
							}
							else {
								Display.warningBox("You entered a password that you've used in the past. Please enter a new one.", "Warning");
								passwordField.setText("");
								return;
							}
						}
				}
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);  
				LocalDateTime now = LocalDateTime.now();  
				
				//In both instances, for last updated and last used, if the user saves their credentials both fields should take the time now.
				for(int i =6; i < 8; i++) {
					entries[i] = dtf.format(now);
				
				}
				
				
				if(Entry.getInspectItemID() == -1) {
					for(int i =0; i < 8; i++) {
						Util.writeToFile(BankState.getUsername() +".txt", entries[i]);
					}
				}
				else {
					for(int i =0; i < 8; i++) {
						Util.replaceLine(BankState.getUsername() + ".txt", entries[i], (Entry.getInspectItemID()-1) * 8 + i + 1);
					}
				}
					
				Entry.resetInspectItemID();
				BankState.setCardLayout(BankState.OVERVIEW_STATE);
			}
		});
		
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Deletes desired entry from text file which will later be copied from by the overview class since its refreshed each time.
				Util.deleteLines(BankState.getUsername() + ".txt", (Entry.getInspectItemID()-1) * 8 + 1, 8);
				Util.deleteLines(BankState.getUsername() + "hashes.txt", (Entry.getInspectItemID()-1) * SettingState.getNumberOfPasswordRepeatsAccepted() + Entry.getInspectItemID(), SettingState.getNumberOfPasswordRepeatsAccepted() + 1);
				
				//At this point the current password that was deleted is removed, therefore the ids need to be moved up or else replacing changes to the text file wont be possible.
				for(int i =Entry.getInspectItemID(); i < OverviewPasswordState.getNumberOfEntries(); i++) {
					Util.replaceLine(BankState.getUsername() + ".txt","" + i, (i-1)*8 + 1);
				}
				
				for(int i =Entry.getInspectItemID(); i < OverviewPasswordState.getNumberOfEntries(); i++) {
					Util.replaceLine(BankState.getUsername() + "hashes.txt","" + i, (i-1)*(SettingState.getNumberOfPasswordRepeatsAccepted()+1) + 1);
				}
					
				BankState.setCardLayout(BankState.OVERVIEW_STATE);
				Entry.resetInspectItemID();
			}
		});
		
		generatePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText(SettingState.generateRandomPassword());
				
				passwordCL.show(strengthBarContainer, "" + checkPassword());
			}
		});
	}
}
