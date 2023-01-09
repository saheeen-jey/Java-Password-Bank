


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
public class RegisterState extends State{

	private final int TEXT_FIELD_LENGTH = 24;
	
	private JTextField[] userTextBoxes = new JTextField[3];
	private JPasswordField[] passwordEntry = new JPasswordField[2];
	private static String[] securityQuestions = {"Please choose a security question",
			"What was your childhood nickname?",
					"What is the name of your favorite childhood friend?",
					"What street did you live on in third grade?",
					"What is the middle name of your youngest child?"
					,"What is your oldest sibling's middle name?"
					,"What school did you attend for sixth grade?"
					,"What is your oldest cousin's first and last name?"
					,"What was the name of your first stuffed animal?"
					,"In what city or town did your mother and father meet?"
					,"Where were you when you had your first kiss?"
					,"What was the last name of your third grade teacher?"
					,"In what city does your nearest sibling live?"
	};
	private JComboBox<?> securityQuestionComponent = new JComboBox<Object>(securityQuestions);
	private boolean passwordsAreVisible = false;
	private JButton passwordVisibilityButton = new JButton("View");
	private JButton registerButton = new JButton("Register");
	private JButton backButton = new JButton("Back");
	private String[] loginTextInformation;
	//private final String[] BLACKLISTED_NAMES = {"login", "securityQuestions"};
	
	public RegisterState() {
		initTextFields();
		initButtonEvents();
		initJComponents();
		
	}
	
	private void initJComponents() {
		panel.setBorder(BorderFactory.createTitledBorder("Register"));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(20, 25, 20, 25);
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(new JLabel("Create a username: "), constraints);
		constraints.gridy++;
		panel.add(new JLabel("Create a password: "), constraints);
		constraints.gridy++;
		panel.add(new JLabel("Confirm the password: "), constraints);
		constraints.gridy++;
		panel.add(new JLabel("Choose a security question: "), constraints);
		constraints.gridy++;
		panel.add(new JLabel("Answer the security question: "), constraints);
		constraints.gridy++;
		panel.add(new JLabel("Recovery Message"), constraints);
		constraints.gridy++;
		panel.add(backButton, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(userTextBoxes[0], constraints);
		constraints.gridy++;
		panel.add(passwordEntry[0],constraints);
		constraints.gridy++;
		panel.add(passwordEntry[1],constraints);
		constraints.gridy++;
		panel.add(securityQuestionComponent, constraints);
		constraints.gridy++;
		panel.add(userTextBoxes[1], constraints);
		constraints.gridy++;
		panel.add(userTextBoxes[2], constraints);
		constraints.gridy++;
		panel.add(registerButton, constraints);
		constraints.gridx = 2;
		constraints.gridy = 1;
		panel.add(passwordVisibilityButton, constraints);
		
		
		
		
	}
	
	private void initTextFields() {
		
		for(int i =0; i < userTextBoxes.length; i++) {
			userTextBoxes[i] = new JTextField(TEXT_FIELD_LENGTH);
		}
		for(int i =0; i < 2; i++) {
			passwordEntry[i] = new JPasswordField(TEXT_FIELD_LENGTH);
		}
			
	}
	private void initButtonEvents() {
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Display.setCardLayout(Display.LOGIN_STATE_NUMBER);
			}
		});
		
		passwordVisibilityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!passwordsAreVisible) {
				for(int i=0; i<2; i++)
					passwordEntry[i].setEchoChar((char)0);
					passwordVisibilityButton.setText("Hide");
					passwordsAreVisible = true;
				}
				else {
					for(int i=0; i<2; i++)
						passwordEntry[i].setEchoChar((char) UIManager.get("PasswordField.echoChar"));
					passwordVisibilityButton.setText("Show");
					passwordsAreVisible = false;
				}
			}
		});
		
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				//Check text fields for entries
				for(int i =0; i < userTextBoxes.length; i++) {
					if(userTextBoxes[i].getText().length() == 0) {
						Display.warningBox("Please enter for the following fields", "Warning");
						return;
					}
				}
				
				//Check password fields for entries
				for(int i =0; i < passwordEntry.length; i++) {
					if(passwordEntry[i].toString().length() == 0) {
						Display.warningBox("Please enter for the following fields", "Warning");
						return;
					}
				}
				
				
				//Check if security q is chosen
				if(securityQuestionComponent.getSelectedIndex() == 0) {
					Display.warningBox("Please choose a security question", "Warning");
					return;
				}
				
				
				//Check if password match
				if(!(Hash.hashPassword(passwordEntry[0].getPassword()).equals(Hash.hashPassword(passwordEntry[1].getPassword())))) {
					Display.warningBox("Passwords do not match", "Warning");
					return;
				}
				
				
				loginTextInformation = Util.readATextFile("login.txt");
				
				
				for(int i = 0; i < loginTextInformation.length; i+= 5) {
					if(userTextBoxes[0].getText().equals(loginTextInformation[i])) {
						Display.warningBox("Username is taken.", "Warning");
						return;
					}
				}
				
					
				//Write username into text file
				Util.writeToFile("login.txt", userTextBoxes[0].getText());
				
				Util.writeToFile("login.txt", Hash.hashPassword(passwordEntry[0].getPassword()));
				
				//Write security question and answer into text file.
				Util.writeToFile("login.txt", securityQuestionComponent.getSelectedItem().toString());
				Util.writeToFile("login.txt",Hash.hashPassword(userTextBoxes[1].getText().toCharArray()));
				Util.writeToFile("login.txt"," ");
				
				Util.createTextFile(userTextBoxes[0].getText() + "recovery.txt");
				Util.writeToFile(userTextBoxes[0].getText() + "recovery.txt", userTextBoxes[2].getText());
				try {
					AESFileEncryption.encrypt(userTextBoxes[1].getText().toCharArray(), userTextBoxes[0].getText(), "recovery");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Display.setCardLayout(Display.LOGIN_STATE_NUMBER);
			}
		});
	}
	
	public static String[] getSecurityQuestions() {
		return securityQuestions;
	}
}
