package panels;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.proteanit.sql.DbUtils;
import javax.swing.JTextField;

public class Tab2 extends JPanel {
	private JTextField nid_field;
	private JTextField name_field;
	private JTextField major_field;

	/**
	 * Create the panel.
	 */
	public Tab2(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		
		JButton switchButton = new JButton("Switch to 1");
		switchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(mainPanel, "1");
			}
		});
		switchButton.setBounds(78, 70, 115, 29);
		add(switchButton);
		
		nid_field = new JTextField();
		nid_field.setBounds(68, 115, 146, 26);
		add(nid_field);
		nid_field.setColumns(10);
		
		name_field = new JTextField();
		name_field.setBounds(68, 157, 146, 26);
		add(name_field);
		name_field.setColumns(10);
		
		major_field = new JTextField();
		major_field.setBounds(68, 202, 146, 26);
		add(major_field);
		major_field.setColumns(10);
		
		JButton btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nid = nid_field.getText();
				String name = name_field.getText();
				String major = major_field.getText();
				if (!nid.isEmpty() && !name.isEmpty() && !major.isEmpty()) {
					insert(con, nid, name, major);
				}
			}
		});
		btnInsert.setBounds(78, 255, 115, 29);
		add(btnInsert);
	}
	
	private void insert(Connection con, String nid, String name, String major) {
		int nid_int = Integer.parseInt(nid);
		PreparedStatement ps;
		
		try{
			ps = con.prepareStatement("INSERT INTO tab1 VALUES (?,?,?)");
			ps.setInt(1, nid_int);
		    ps.setString(2, name);
		    ps.setString(3, major);

		    ps.executeUpdate();

		    // commit work 
		    con.commit();

		    ps.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
