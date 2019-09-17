package panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class DBAMainPanelButtons {
	JButton delete_colab;
	JButton delete_approved_grant;
	JButton insert_open_grant;
	JButton insert_approved_grant;
	
	JButton update_lm_educ;
	JButton update_lm_name;
	JButton update_open_grant;
	
	public DBAMainPanelButtons() {
		
	}
	
	public JButton getDelete_colab() {
		return delete_colab;
	}
	public JButton getDelete_approved_grant() {
		return delete_approved_grant;
	}
	public JButton getInsert_open_grant() {
		return insert_open_grant;
	}
	public JButton getInsert_approved_grant() {
		return insert_approved_grant;
	}
	public JButton getUpdate_lm_educ() {
		return update_lm_educ;
	}
	public JButton getUpdate_lm_name() {
		return update_lm_name;
	}
	public JButton getUpdate_open_grant() {
		return update_open_grant;
	}
	
}
