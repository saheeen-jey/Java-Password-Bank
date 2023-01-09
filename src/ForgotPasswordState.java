

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;


public class ForgotPasswordState extends State{
	
	private static int usernameLineNumber;
	
	public ForgotPasswordState(String username) {
	
	String[] creds = Util.readATextFile("login.txt");
	JButton goBack = new JButton("Back");
	JButton confirm = new JButton("Confirm");
	
	
	for(int i =0; i < creds.length; i+= 5)
		if(creds[i].equals(username))
			usernameLineNumber = i+1;
	

	
	TextField tf = new TextField(40);
	panel.setLayout(new GridBagLayout());
	
	GridBagConstraints constraints = new GridBagConstraints();
	constraints.insets = new Insets(25, 25, 50, 100);
	constraints.anchor = GridBagConstraints.WEST;
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 2;
	panel.add(new JLabel(Util.readOneLine("login.txt", usernameLineNumber + 2)),constraints);
	constraints.gridy++;
	panel.add(tf,constraints);
	constraints.gridy++;
	constraints.gridwidth = 1;
	panel.add(goBack,constraints);
	constraints.gridx++;
	panel.add(confirm,constraints);

	
	
	confirm.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if(Hash.hashPassword(tf.getText().toCharArray()).equals(Util.readOneLine("login.txt", usernameLineNumber + 3))) {
				try {
					AESFileDecryption.decrypt(tf.getText().toCharArray(), Display.getCurrentUser(), "recovery");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Display.informationBox(Util.readOneLine(Display.getCurrentUser() + "recovery.txt", 1) ,"Note");
				
				try {
					AESFileEncryption.encrypt(tf.getText().toCharArray(), Display.getCurrentUser(), "recovery");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				Display.warningBox("Incorrect Answer." ,"Warning");
			}
		}
	});
	
	goBack.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			Display.setCardLayout(Display.LOGIN_STATE_NUMBER);
		}
	});
}
}
