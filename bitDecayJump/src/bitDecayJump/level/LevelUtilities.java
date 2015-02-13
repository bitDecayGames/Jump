package bitDecayJump.level;

import java.io.*;

import javax.swing.JFileChooser;

import com.google.gson.GsonBuilder;

public class LevelUtilities {
	public static Level loadLevel() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		// fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return loadLevel(selectedFile);
		}
		return null;
	}

	public static Level loadLevel(String fileName) {
		return loadLevel(new File(fileName));
	}

	public static Level loadLevel(File file) {
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
				return levelFromJson(json.toString());
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

	private static Level levelFromJson(String json) {
		return new GsonBuilder().create().fromJson(json, Level.class);
	}
}
