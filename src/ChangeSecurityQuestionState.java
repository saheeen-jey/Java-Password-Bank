

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChangeSecurityQuestionState extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] securityQuestions = RegisterState.getSecurityQuestions();
	private JComboBox<?> securityQuestionComponent = new JComboBox<Object>(securityQuestions);
	private JLabel originalQuestion = new JLabel();
	private JTextField originalAnswer = new JTextField(20);
	
	private JTextField answer = new JTextField(20);
	private JTextField confirm = new JTextField(20);
	private JButton save = new JButton("Confirm");
	private JButton back = new JButton("Back");
	
	
	
	public ChangeSecurityQuestionState() {
		
		initJComponentsForPassChange();
		initButtonEvents();
	}
	private void initJComponentsForPassChange() {
		this.setBorder(BorderFactory.createTitledBorder("Change Security Question/Response"));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);
		c.gridx = 0;
		c.gridy = 0;
		
		String[] loginEntries = Util.readATextFile("login.txt");
		int lineOfUsername = Util.linearSearch(loginEntries, BankState.getUsername());
		originalQuestion.setText(Util.readOneLine("login.txt", lineOfUsername + 2));
		this.add(originalQuestion,c);
		c.gridx++;
		this.add(originalAnswer,c);
		c.gridx = 0;
		c.gridy++;
		this.add(new JLabel("Choose a new security question: "),c);
		c.gridx++;
		this.add(securityQuestionComponent, c);
		c.gridx = 0;
		c.gridy++;

		this.add(new JLabel("Enter your new answer: "),c);
		c.gridx++;
		this.add(answer, c);
		c.gridx = 0;
		c.gridy++;

		this.add(new JLabel("Enter your new recovery message: "),c);
		c.gridx++;
		this.add(confirm, c);
		c.gridx = 0;
		c.gridy++;
		this.add(back,c);
		c.gridx++;
		this.add(save,c);
	}
	private void initButtonEvents() {
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SettingState.getChangePassword().dispose();
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] loginEntries = Util.readATextFile("login.txt");
				int lineOfUsername = Util.linearSearch(loginEntries, BankState.getUsername());
				
				if(!Hash.hashPassword(originalAnswer.getText().toCharArray()).equals(Util.readOneLine("login.txt", lineOfUsername + 3))) {
					Display.warningBox("Your original answer is not correct.", "Warning");
					return;
				}
				
				try {
					AESFileDecryption.decrypt(originalAnswer.getText().toCharArray(), BankState.getUsername(), "recovery");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Display.errorBox("Error occured decrypting the recovery message", "Error");
					e.printStackTrace();
				}
				if(securityQuestionComponent.getSelectedIndex() == 0) {
					Display.warningBox("Please choose a security question", "Warning");
					return;	
				}
				
				if(answer.getText().equals("")) {
					Display.warningBox("Please enter an answer to the security question", "Warning");
					return;	
				}
				if(confirm.getText().equals("")) {
					Display.warningBox("Please enter an answer to the security question", "Warning");
					return;
				}
				
				Util.replaceLine("login.txt", securityQuestionComponent.getSelectedItem().toString(), lineOfUsername+2);
				
				Util.replaceLine("login.txt", Hash.hashPassword(answer.getText().toCharArray()), lineOfUsername+3);
				
				Util.replaceLine(BankState.getUsername() + "recovery.txt", confirm.getText(), 1);
				try {
					AESFileEncryption.encrypt(answer.getText().toCharArray(), BankState.getUsername(), "recovery");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SettingState.getChangePassword().dispose();
			}
		});
	}
		
	
	
}
