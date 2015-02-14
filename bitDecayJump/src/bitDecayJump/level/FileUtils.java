package bitDecayJump.level;

import java.io.*;

import javax.swing.JFileChooser;

import com.google.gson.*;

public class FileUtils {

	public static String toJson(Object obj) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}

	public static String saveToFile(Object obj) {
		return saveToFile(toJson(obj));
	}

	public static String saveToFile(String json) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save As");
		fileChooser.setCurrentDirectory(new File("."));
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter writer = new FileWriter(fileChooser.getSelectedFile());
				writer.write(json);
				writer.close();
				return json;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static <T> T loadFileAs(Class<T> clazz) {
		return loadFileAs(clazz, loadFile());
	}

	public static <T> T loadFileAs(Class<T> clazz, File file) {
		return loadFileAs(clazz, loadFile(file));
	}

	public static <T> T loadFileAs(Class<T> clazz, String json) {
		return new GsonBuilder().create().fromJson(json, clazz);
	}

	public static String loadFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		// fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return loadFile(selectedFile);
		}
		return null;
	}

	public static String loadFile(File file) {
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
				return json.toString();
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
