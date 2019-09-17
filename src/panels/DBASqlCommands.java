package panels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import queries.QueryBuilder;

public class DBASqlCommands {
	
	private Connection con; 
	
	public DBASqlCommands(Connection con) {
		this.con = con;
	}
	
	public void delete_approved_grant() {
		PreparedStatement ps;

		try {
			ps = con.prepareStatement(DBAPanelConstants.deleteApprovedGrant());
			con.commit();
			
			int rowCount = ps.executeUpdate();
			System.out.println(rowCount + " got deleted.");
			
			ps.close();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
		}
	}
	
	public void delete_collaborator(String colab_id) {
		PreparedStatement ps;

		try {
			ps = con.prepareStatement(DBAPanelConstants.deleteCollaborator());
			ps.setString(1, colab_id);

			int rowCount = ps.executeUpdate();
			System.out.println(rowCount +" got deleted.");

			con.commit();

			ps.close();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
		}
	}
	
	
	public void update_lab_member(String actionString, String field_to_change, int lab_member_id) {
		PreparedStatement ps = null;
		try {
			switch (actionString) {
			case DBAPanelConstants.UPDATE_LAB_MEM_NAME:
				ps = con.prepareStatement(DBAPanelConstants.updateLabMemName());
				ps.setString(1, field_to_change);
				ps.setInt(2, lab_member_id);
				
				con.commit();
				break;
			case DBAPanelConstants.UPDATE_LAB_MEM_EDUC:
				ps = con.prepareStatement(DBAPanelConstants.updateLabMemEduc());
				ps.setString(1, field_to_change);
				ps.setInt(2, lab_member_id);
				con.commit();
				
				break;
			}
			int rowCount = ps.executeUpdate();
			System.out.println(rowCount + " row got updated.");
			ps.close();

		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
		}
	}
	
	
	
	public void insert_open_grant(String name, String amout_input, String date, String status) {
		System.out.println("insert_open_grant is called.");
		try {
			PreparedStatement ps = null;
			ps = con.prepareStatement("INSERT INTO Applies_OpenGrant_Date VALUES (?,?,?, ?)");
			
			ps.setString(1, name);
			ps.setString(2, amout_input);
			ps.setDate(3, new java.sql.Date(new java.util.Date().getTime())); 
			ps.setString(4, status); 
			int rowCount = ps.executeUpdate();
			System.out.println(rowCount + " row got added.");
			ps.close();
			con.commit();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void insert_approved_grant(String name, String amount, String third_output) {
		System.out.println("insert_approved_grant is called.");
		try {
			PreparedStatement ps = null;
			ps = con.prepareStatement("INSERT INTO Fund_ApprovedGrant VALUES (?,?,?)");
			ps.setString(1, name);
			ps.setString(2, amount);
			ps.setString(3, third_output); 
			int rowCount = ps.executeUpdate();
			System.out.println(rowCount + " row got added.");
			ps.close();
	
			con.commit();
	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
		
		public void update_open_grant_status(String name, String amout_input, String status) {
			System.out.println("insert_open_grant is called.");
			try {
				PreparedStatement ps = null;
				ps = con.prepareStatement("UPDATE Applies_OpenGrant_Date set status = ? where name = ? and amount = ?");
				
				ps.setString(2, name);
				ps.setString(3, amout_input);
				ps.setString(1, status); 
				int rowCount = ps.executeUpdate();
				System.out.println(rowCount + " row got added.");
				ps.close();
				con.commit();

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	
}
