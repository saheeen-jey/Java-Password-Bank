

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginState extends State{
	
	private JTextField username = new JTextField(12);
	private JPasswordField password = new JPasswordField(12);
	private JButton loginButton = new JButton("Log In");
	private JButton registerButton = new JButton("Register");
	private JButton forgotPasswordButton = new JButton("Forgot Password");

	public LoginState() {
		Util.createTextFile("login.txt");
		panel.setLayout(new GridBagLayout());
		// panel.setBackground(Color.BLUE);
		initJComponents(); 
		initButtonsActionEvents();
		
	}
	
	private void initJComponents() {
		panel.setSize(100,100);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(25, 25, 25, 25);
		panel.setBorder(BorderFactory.createTitledBorder("Login"));
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(new JLabel("Enter Username: "), constraints);
		constraints.gridy++;
		panel.add(new JLabel("Enter Password: "), constraints);
		constraints.gridy++;
		panel.add(registerButton, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(username, constraints);
		constraints.gridy++;
		panel.add(password, constraints);
		constraints.gridy++;
		panel.add(loginButton, constraints);
		constraints.gridx++;
		panel.add(forgotPasswordButton, constraints );
	}
	
	
	
	private void initButtonsActionEvents() {	
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] creds = Util.readATextFile("login.txt");
				
				if(!Util.fileExists("login.txt") || creds.length == 0)
					Display.warningBox("No users are currently registered.", "Warning");
				
				for(int i =0; i < creds.length; i+= 5) {
					if(creds[i].equals(username.getText()) && creds[i+1].equals(Hash.hashPassword(password.getPassword()))) {
						BankState.setKey(password.getPassword());
						Display.setCurrentUser(username.getText());
						System.out.println("\n" + username.getText());
						
						if(Util.fileExists(username.getText() + "encryptedfile.des")) {
							try {
								AESFileDecryption.decrypt(password.getPassword(), username.getText(), "");
								AESFileDecryption.decrypt(password.getPassword(), username.getText(), "hashes");
								AESFileDecryption.decrypt(password.getPassword(), username.getText(), "settings");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						username.setText("");
						password.setText("");
						
						Display.setCardLayout(Display.BANK_STATE_NUMBER);
						return;
					}
					else {
						Display.informationBox("Username/Password are not recognized", "Info");
					}
				}
			}
		});
		
		forgotPasswordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(username.getText().equals("")) {
					Display.warningBox("Please enter your username.", "Warning");
					return;
				}
				
				boolean usernameFound = false;
				String[] entries = Util.readATextFile("login.txt");
				
				
 				for(int i = 0; i < entries.length; i+= 5) {
					if(username.getText().equals(entries[i]))
						usernameFound = true;
				}
				
				//Check for their file or check the login text file
				if(Util.fileExists(username.getText() + ".txt") || usernameFound) {
					Display.setCurrentUser(username.getText());
					Display.setCardLayout(Display.FORGOT_PASSWORD_NUMBER);
				}
				else {
					Display.warningBox("Username not found.", "Error");
					return;
				}
				
			}
		});

		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Display.setCardLayout(Display.REGISTER_STATE_NUMBER);
			}
		});
	}
	

}
