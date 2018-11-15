package panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JTabbedPane;

public class Tab1 extends JPanel {
	private JTable table;

	/**
	 * Create the panel.
	 */
	public Tab1(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		
		JButton switchButton = new JButton("Switch to 2");
		switchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(mainPanel, "2");
			}
		});
		switchButton.setBounds(0, 0, 115, 29);
		add(switchButton);
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectAll(con);
			}
		});
		refreshButton.setBounds(130, 0, 115, 29);
		add(refreshButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(65, 111, 262, 116);
		add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JButton btnOtherTable = new JButton("Change Query");
		btnOtherTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectSome(con);
			}
		});
		btnOtherTable.setBounds(36, 55, 156, 29);
		add(btnOtherTable);
		
		JButton btnChangePane = new JButton("Toggle Visibility");
		btnChangePane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPane.setVisible(!scrollPane.isVisible());
			}
		});
		btnChangePane.setBounds(212, 55, 156, 29);
		add(btnChangePane);
		
		selectAll(con);
	}
	
	private void selectAll(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		   
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM tab1");
			table.setModel(DbUtils.resultSetToTableModel(rs));
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}
	
	private void selectSome(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		   
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT NID, NAME FROM tab1");
			table.setModel(DbUtils.resultSetToTableModel(rs));
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}
}
