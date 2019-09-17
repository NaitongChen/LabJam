import java.awt.CardLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import panels.CollaboratorMainPanel;
import panels.Constant;
import panels.LogIn;
import panels.ResearcherMainPanel;

public class LabJam {
	JFrame frame;
	JPanel mainPanel;
	JButton button2;
	CardLayout cl;
	Connection con;
	
	public LabJam() {
		
		frame = new JFrame("LabJAM");
		mainPanel = new JPanel();
		button2 = new JButton("switch to 1");
		cl = new CardLayout();
		connect("ora_q7l0b", "a21539151");
		mainPanel.setLayout(cl);
		LogIn login = new LogIn(cl, mainPanel,con);

		mainPanel.add(login, Constant.LOGIN);
		
		frame.add(mainPanel);
		frame.setPreferredSize(new Dimension(450, 600));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		cl.show(mainPanel, Constant.LOGIN);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LabJam();
			}
		});
	}
	
	private void connect(String username, String password) {
		
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}
		
		String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug"; 
		
		try {
			con = DriverManager.getConnection(connectURL,username,password);
			con.setAutoCommit(false);
			System.out.println("\nConnected to Oracle!");
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
	    }
    }
}
