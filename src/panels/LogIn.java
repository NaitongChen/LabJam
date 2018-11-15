package panels;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import queries.QueryBuilder;

import java.awt.CardLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class LogIn extends JPanel {
	private JTextField textField;
	private JLabel IDlabel;
	private String id;
	private JButton LoginButton;

	/**
	 * Create the panel.
	 */
	public LogIn(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		JLabel lblIAmA = new JLabel("I am a");
		lblIAmA.setFont(new Font("Arial", Font.PLAIN, 20));
		lblIAmA.setBounds(195, 15, 60, 30);
		add(lblIAmA);
		
		JButton btnResearcher = new JButton("Researcher");
		btnResearcher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IDlabel.setText("My Researcher ID is:");
				IDlabel.setVisible(true);
				LoginButton.setVisible(true);
				textField.setVisible(true);
			}
		});
		btnResearcher.setBounds(15, 50, 130, 30);
		add(btnResearcher);
		
		JButton btnCollaborator = new JButton("Collaborator");
		btnCollaborator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IDlabel.setText("My Collaborator ID is:");
				IDlabel.setVisible(true);
				LoginButton.setVisible(true);
				textField.setVisible(true);
			}
		});
		btnCollaborator.setBounds(305, 50, 130, 30);
		add(btnCollaborator);
		
		JButton btnLabManager = new JButton("Lab Manager");
		btnLabManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IDlabel.setText("My Lab Manager ID is:");
				IDlabel.setVisible(true);
				LoginButton.setVisible(true);
				textField.setVisible(true);
			}
		});
		btnLabManager.setBounds(60, 105, 130, 30);
		add(btnLabManager);
		
		JButton btnPi = new JButton("PI");
		btnPi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IDlabel.setText("My PI ID is:");
				IDlabel.setVisible(true);
				LoginButton.setVisible(true);
				textField.setVisible(true);
			}
		});
		btnPi.setBounds(230, 105, 130, 30);
		add(btnPi);
		
		JButton btnDba = new JButton("DBA");
		btnDba.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IDlabel.setText("My DBA ID is:");
				IDlabel.setVisible(true);
				LoginButton.setVisible(true);
				textField.setVisible(true);
			}
		});
		btnDba.setBounds(160, 50, 130, 30);
		add(btnDba);
		
		textField = new JTextField();
		textField.setBounds(130, 200, 146, 26);
		add(textField);
		textField.setColumns(10);
		
		IDlabel = new JLabel("My ID is:");
		IDlabel.setFont(new Font("Arial", Font.PLAIN, 20));
		IDlabel.setBounds(60, 160, 300, 20);
		add(IDlabel);
		
		LoginButton = new JButton("Login");
		LoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login(mainPanel, cl, con);
			}
		});
		LoginButton.setBounds(230, 255, 130, 30);
		add(LoginButton);
		
		
		// set initial visibility
		LoginButton.setVisible(false);
		IDlabel.setVisible(false);
		textField.setVisible(false);
	}
	
	private void login(JPanel mainPanel, CardLayout cl, Connection con) {
		id = textField.getText();
		switch (IDlabel.getText()) {
		case "My Researcher ID is:":
			QueryBuilder.setResearcherID(id);
			ResearcherMainPanel researcherMainPanel = new ResearcherMainPanel(cl, mainPanel, con);
			mainPanel.add(researcherMainPanel, Constant.RESEARCHERMAIN);
			mainPanel.revalidate();
			cl.show(mainPanel, Constant.RESEARCHERMAIN);
		case "My PI ID is:":
			QueryBuilder.setResearcherID(id);
			PIMainPanel piMainPanel = new PIMainPanel(cl, mainPanel, con);
			mainPanel.add(piMainPanel, Constant.PIMAIN);
			mainPanel.revalidate();
			cl.show(mainPanel, Constant.PIMAIN);
		case "My Collaborator ID is:":
			QueryBuilder.setResearcherID(id);
			CollaboratorMainPanel collaboratorMainPanel = new CollaboratorMainPanel(cl, mainPanel, con);
			mainPanel.add(collaboratorMainPanel, Constant.COLLABORATORMAIN);
			mainPanel.revalidate();
			cl.show(mainPanel, Constant.COLLABORATORMAIN);
		case "My DBA ID is:":
			QueryBuilder.setResearcherID(id);
			DBAMainPanel dbaMainPanel = new DBAMainPanel(cl, mainPanel, con);
			mainPanel.add(dbaMainPanel, Constant.DBAMAIN);
			mainPanel.revalidate();
			cl.show(mainPanel, Constant.DBAMAIN);
		case "My Lab Manager ID is:":
			QueryBuilder.setResearcherID(id);
			LabManagerMainPanel labManagerMainPanel = new LabManagerMainPanel(cl, mainPanel, con);
			mainPanel.add(labManagerMainPanel, Constant.LABMANAGERMAIN);
			mainPanel.revalidate();
			cl.show(mainPanel, Constant.LABMANAGERMAIN);
		}
	}
}
