package com.ffeichta.tictactoe.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Class that shows information about the application in a Dialog
 * 
 * @author Fabian
 * 
 */
public class InfoBox extends JDialog {

	private JLabel infoLabel;
	private JButton close;

	public InfoBox() {
		setTitle("About");
		setSize(282, 201);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setModal(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		Image image = Toolkit.getDefaultToolkit().getImage(
				getClass()
						.getResource("/com/ffeichta/tictactoe/images/app.png"));
		setIconImage(image);

		String msg = "<html><body><b>Tic Tac Toe</b><br>Version 1.0 "
				+ "(02.07.15)<br><br>Fabian Feichter, TFO \"Max Valier\", "
				+ "Bozen<br><br>Web: www.ffeichta.com<br>E-Mail: "
				+ "fabian.feichter@ffeichta.com</body></html>";
		infoLabel = new JLabel(msg);
		infoLabel.setBounds(10, 11, 241, 116);
		getContentPane().add(infoLabel);

		close = new JButton("Close");
		close.setBounds(162, 128, 89, 23);
		close.setFocusPainted(false);
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(close);
	}
}
