package bitDecayJump.ui;

import java.awt.event.*;

import javax.swing.*;

public class OptionsUI extends JPanel {
	OptionsUICallback callback;

	public OptionsUI(OptionsUICallback callback) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.callback = callback;

		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Select");
			}
		});
		add(btnSelect);

		add(Box.createVerticalStrut(10));

		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Create");
			}
		});
		add(btnCreate);

		add(Box.createVerticalStrut(10));

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Delete");
			}
		});
		add(btnDelete);

		add(Box.createVerticalStrut(10));

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Save");
			}
		});
		add(btnSave);

		add(Box.createVerticalStrut(10));

		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Load");
			}
		});
		add(btnLoad);
	}
}
