

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;


public class ChangePasswordState extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int PASSWORD_CHANGE = 1;
	public static final int SECURITY_QUESTION_CHANGE = 0;
	
	private JPasswordField oldPass = new  JPasswordField(20);
	private JPasswordField newPass = new  JPasswordField(20);
	private JPasswordField newPassConfirm = new  JPasswordField(20);
	private JButton confirm = new JButton("Confirm");
	private JButton backButton = new JButton("Back");
	private JButton passwordVisibilityButton = new JButton("Show");
	private boolean passwordsAreVisible = false;
	
	public ChangePasswordState() {
		initJComponentsForPassChange();
		initButtonEvents();
	}
	
	private void initJComponentsForPassChange() {
		this.setBorder(BorderFactory.createTitledBorder("Change Password"));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);
		c.gridx = 0;
		c.gridy = 0;
		
		this.add(new JLabel("Enter Current Password: "),c);
		c.gridx++;
		this.add(oldPass, c);
		c.gridx++;
		this.add(passwordVisibilityButton,c);
		c.gridx = 0;
		c.gridy++;

		this.add(new JLabel("Enter New Password: "),c);
		c.gridx++;
		this.add(newPass, c);
		c.gridx = 0;
		c.gridy++;

		this.add(new JLabel("Confirm New Password: "),c);
		c.gridx++;
		this.add(newPassConfirm, c);
		c.gridx = 0;
		
		c.gridy++;
		this.add(backButton,c);
		c.gridx++;
		this.add(confirm,c);
	}
	
	private void initButtonEvents() {
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] loginEntries = Util.readATextFile("login.txt");
				int lineOfUsername = Util.linearSearch(loginEntries, BankState.getUsername());
				
				if(Hash.hashPassword(oldPass.getPassword()).equals(Hash.hashPassword(BankState.getKey()))) {
					if(Util.arePasswordsTheSame(newPass.getPassword(), newPassConfirm.getPassword())) {
						if(!Util.arePasswordsTheSame(oldPass.getPassword(), newPass.getPassword())) {
							Display.informationBox("Password Successfully updated", "Success!");
						}
						else {
							Display.warningBox("Current Password cannot be the same as new password.", "Warning");
							return;
						}
					} else {
						Display.warningBox("Invalid credentials", "Warning");
						return;
					}
				}
				else {
					Display.warningBox("Invalid credentials", "Warning");
					return;
				}
				
				BankState.setKey(newPass.getPassword());
				Util.replaceLine("login.txt", Hash.hashPassword(newPass.getPassword()), lineOfUsername  + 1);
				SettingState.getChangePassword().dispose();
			}
		});
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingState.getChangePassword().dispose();
			}
		});
		passwordVisibilityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!passwordsAreVisible) {
				for(int i=0; i<2; i++)
					oldPass.setEchoChar((char)0);
					newPass.setEchoChar((char)0);
					newPassConfirm.setEchoChar((char)0);
					passwordVisibilityButton.setText("Hide");
					passwordsAreVisible = true;
				}
				else {
					for(int i=0; i<2; i++)
					oldPass.setEchoChar((char) UIManager.get("PasswordField.echoChar"));
					newPass.setEchoChar((char) UIManager.get("PasswordField.echoChar"));
					newPassConfirm.setEchoChar((char) UIManager.get("PasswordField.echoChar"));
					passwordVisibilityButton.setText("Show");
					passwordsAreVisible = false;
				}
			}
		});
	}
}
