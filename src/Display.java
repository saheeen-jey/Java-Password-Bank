
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Display {

	private static JFrame frame;
	
	private static boolean running = false;
	public final static int BANK_STATE_NUMBER = 0;
	public final static int LOGIN_STATE_NUMBER = 1;
	public final static int REGISTER_STATE_NUMBER = 2;
	public final static int SETTING_STATE_NUMBER = 3;
	public final static int FORGOT_PASSWORD_NUMBER = 4;
	
	private String title;
	private static int width, height;
	//States
	private static JPanel container = new JPanel();
	private static CardLayout cl = new CardLayout();
	private static State[] states;
	private static String username;
	private static boolean thereIsAUserActive = false;
	
	
	public Display(String title, int width, int height){
		if(running)
			frame.dispose();
		running = true;
		
		this.title = title;
		Display.width = width;
		Display.height = height;
		
		states = new State[5];
		
	
		// States bank and settings are null as they are dependent on username.
		states[0] = null;
		states[1] = new LoginState();
		states[2] = new RegisterState();
		states[3] = null;
		states[4] = null;
		
		username = "";
		
		container.setLayout(cl);
		//instantiate every state except for bank and personal settings.
		for(int i =1; i < states.length -2; i++)
			container.add(states[i].getJPanel(),"" + i);
		

		container.setPreferredSize(new Dimension(width, height));
		container.setMaximumSize(new Dimension(width, height));
		container.setMinimumSize(new Dimension(width, height));
		createDisplay();
		
		
	}
	
	public static void setCardLayout(int i) {
		//Clear text fields by creating a new instance of the state and adding it to the frame.
		if(i == 2) {
			//container.remove(2);
			states[2] = new RegisterState();
			container.add(states[2].getJPanel(),"" + REGISTER_STATE_NUMBER);
		}
		
		//Create bank state and setting state based off of user that is entered.
		if(i == 0)
		{
			thereIsAUserActive = true;
			states[0] = new BankState(username);
			container.add(states[0].getJPanel(),"" +BANK_STATE_NUMBER);
			states[3] = new SettingState(username);
			container.add(states[3].getJPanel(),"" + SETTING_STATE_NUMBER);

		}		
		
		if(i == 4) {
			
			states[4] = new ForgotPasswordState(username);
			container.add(states[4].getJPanel(),"" + FORGOT_PASSWORD_NUMBER);
		}
		
		if(i == 1) {
			thereIsAUserActive = false;
			container.removeAll();
			states[1] = null;
			states[2] = null;
			states[0] = null;
			states[3] = null;
			
			states[1] = new LoginState();
			states[2] = new RegisterState();
			
			container.setLayout(cl);
			//instantiate every state except for bank and personal settings.
			for(int j =1; j < states.length -2; j++)
				container.add(states[i].getJPanel(),"" + i);
		}
		//Display card in card layout.
		cl.show(container, "" + i);
	}
	
	
	private void createDisplay(){
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.add(container);
		frame.pack();
		
		//https://stackoverflow.com/questions/16372241/run-function-on-jframe-close
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		    	if(!thereIsAUserActive) {
		    		frame.dispose();
			        System.exit(0);
		    	}
		    		
		    	System.gc();
		        if(!BankState.getUsername().equals("")) {
		        	if(Util.fileExists(BankState.getUsername() + ".txt")) {
		        		try {
							AESFileEncryption.encrypt(BankState.getKey(), BankState.getUsername(), "");
							AESFileEncryption.encrypt(BankState.getKey(), BankState.getUsername(), "hashes");
							AESFileEncryption.encrypt(BankState.getKey(), BankState.getUsername(), "settings");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
		        }
		        
		    	
		        frame.dispose();
		        System.exit(0);
		    }
		});
	}
	
	

	public static void setCurrentUser(String name) {
		username = name;
	}
	
	public static String  getCurrentUser() {
		return username;
	}
	
	public static JFrame getFrame(){
		return frame;
	}
	public static State getState(int i){
		return states[i];
	}
	
	public static int getWindowWidth() {
		return width;
	}
	
	public static int getWindowHeight() {
		return height;
	}
	
	

	//https://stackoverflow.com/questions/7080205/popup-message-boxes
	public static void warningBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "" + titleBar, JOptionPane.WARNING_MESSAGE);
    }
	
	public static void errorBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "" + titleBar, JOptionPane.ERROR_MESSAGE);
    }
	
	public static void informationBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "" + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
	
	
	
	
}