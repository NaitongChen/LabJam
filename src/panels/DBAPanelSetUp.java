package panels;

import javax.swing.JComboBox;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.proteanit.sql.DbUtils;
import queries.QueryBuilder;


public class DBAPanelSetUp {
	
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBoxOptions;
	private JComboBox<String> openGrantOpenBox;
	private JLabel lblNewLabel;
	private JButton btnLogout;
	private JButton btnSeeMyProjects;
	private JTable table;
	private Connection con;
	
	public DBAPanelSetUp(Connection con, CardLayout cl, JPanel mainPanel ) {
		this.con = con;
		comboBoxOptions = instantiate_researcher_subcombo_box();
		lblNewLabel = instantiate_labels();
		btnLogout = instantiate_btn_log_out( cl,  mainPanel );
		btnSeeMyProjects = instantiate_btn_see_proj();
		openGrantOpenBox = instantiate_open_grant_subcombo_box();
		comboBox = instantiate_combo_box();
		System.out.println("DBAPanelSetUp all instantiated. ");
	}
	
	/* Getter methods */
	
	public JButton get_btn_log_out() {
		return btnLogout;
	}
	
	public JLabel get_label() {
		return lblNewLabel;
	}
	
	public JComboBox<String> get_researcher_subcombo() {
		return comboBoxOptions;
	}
	
	public JComboBox<String> get_combo_box() {
		return comboBox;
	}
	
	public JComboBox<String> get_grant_combo_box() {
		return openGrantOpenBox;
	}
	
	public JButton get_btn_see_proj() {
		return btnSeeMyProjects;
	}
	
	private JButton instantiate_btn_log_out(CardLayout cl, JPanel mainPanel ) {
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryBuilder.reset();
				mainPanel.add(new LogIn(cl, mainPanel, con), Constant.LOGIN);
				cl.show(mainPanel, Constant.LOGIN);
			}
		});
		return btnLogout;
	}
	
	private JButton instantiate_btn_see_proj() {
		JButton btnSeeMyProjects = new JButton("See all Projects");
		btnSeeMyProjects.setBounds(320, 86, 115, 29);
		
		return btnSeeMyProjects;
	}
	
	private JLabel instantiate_labels() {
		JLabel lblNewLabel = new JLabel("Hello, dear DBA" );
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		lblNewLabel.setBounds(15, 15, 290, 30);
		return lblNewLabel;
	}
	
	private JComboBox<String> instantiate_combo_box() {
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("");
		comboBox.addItem("Display all labs");
		comboBox.addItem("Display all lab members");
		comboBox.addItem("Display all projects");


		/** Following are DBAdmin related tasks */

		comboBox.addItem("See all open grants");
		comboBox.addItem("See all approved grants");
		comboBox.addItem("Update lab member name");
		comboBox.addItem("Update lab member education");
		comboBox.addItem("Delete collaborator");
		comboBox.addItem("Remove all approved grants from open grants");
		comboBox.addItem("Update specific open grant");
		comboBox.addItem("Insert open grant");
		comboBox.addItem("Insert approved grant");
		
		return comboBox;
	}
	
	private JComboBox<String> instantiate_open_grant_subcombo_box() {
		Statement stmt;
		ResultSet rs;
		String query;
		String name;
		openGrantOpenBox = new JComboBox<String>();
		query = DBAPanelConstants.getAllOpenGrants();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				name = rs.getString("name");
				openGrantOpenBox.addItem(name);
				
			}
			stmt.close();
		} 
			
			catch (SQLException e) {
				System.out.println("sql error thrown ");
			e.printStackTrace();
		}
			

		return openGrantOpenBox;
	}
	
	private JComboBox<String> instantiate_researcher_subcombo_box() {
		
		JComboBox<String>  comboBoxOptions = new JComboBox<String>();
		comboBoxOptions.addItem("All");
		comboBoxOptions.addItem("Researcher");
		comboBoxOptions.addItem("Lab Manager");
		comboBoxOptions.addItem("PI");
		
		return comboBoxOptions;
	}
	
}
