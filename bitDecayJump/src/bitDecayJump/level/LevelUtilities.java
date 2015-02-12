package bitDecayJump.level;

import java.io.*;

import javax.swing.JFileChooser;

public class LevelUtilities {
	public static LevelBuilder loadLevel() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		// fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return loadLevel(selectedFile);
		}
		return null;
	}

	public static LevelBuilder loadLevel(String fileName) {
		return loadLevel(new File(fileName));
	}

	public static LevelBuilder loadLevel(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			StringBuffer json = new StringBuffer();
			String line = reader.readLine();
			while (line != null) {
				json.append(line);
				line = reader.readLine();
			}
			if (json.length() > 0) {
				return new LevelBuilder(json.toString());
			} else {
				System.out.println("File was empty. Could not load.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
}
