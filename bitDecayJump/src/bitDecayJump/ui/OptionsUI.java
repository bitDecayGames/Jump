package bitDecayJump.ui;

import java.awt.event.*;

import javax.swing.*;

public class OptionsUI extends JPanel {
	private static final long serialVersionUID = 1L;
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

		add(Box.createVerticalStrut(25));

		JButton btnSetPlayer = new JButton("Set Test Player");
		btnSetPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Set Player");
			}
		});
		add(btnSetPlayer);

		add(Box.createVerticalStrut(10));

		JButton btnSavePlayer = new JButton("Save Player Props");
		btnSavePlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Save Player Props");
			}
		});
		add(btnSavePlayer);

		add(Box.createVerticalStrut(10));

		JButton btnLoadPlayer = new JButton("Load Player Props");
		btnLoadPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Load Player Props");
			}
		});
		add(btnLoadPlayer);

		add(Box.createVerticalStrut(10));

		JButton btnSetSpawn = new JButton("Set Spawn");
		btnSetSpawn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Spawn");
			}
		});
		add(btnSetSpawn);

		add(Box.createVerticalStrut(50));

		JButton btnSave = new JButton("Save Level");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Save");
			}
		});
		add(btnSave);

		add(Box.createVerticalStrut(10));

		JButton btnLoad = new JButton("Load Level");
		btnLoad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				callback.setMode("Load");
			}
		});
		add(btnLoad);
	}
}
