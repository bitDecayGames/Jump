package com.bitdecay.jump.level;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.io.*;

import javax.swing.*;

import com.bitdecay.jump.json.JumperStateDeserializer;
import com.bitdecay.jump.state.JumperState;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class FileUtils {

	private static String nextSaveDir = null;

	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		SimpleModule module = new SimpleModule();
		module.addDeserializer(JumperState.class, new JumperStateDeserializer());
		mapper.registerModule(module);
		//		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}

	public static String toJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//		return gson.toJson(obj);
	}

	public static String saveToFile(Object obj) {
		return saveToFile(toJson(obj));
	}

	public static String saveToFile(Object obj, String dir) {
		nextSaveDir = dir;
		return saveToFile(obj);
	}

	public static String saveToFile(String json) {
		JFileChooser fileChooser = new JFileChooser(nextSaveDir) {
			@Override
			protected JDialog createDialog(Component parent) throws HeadlessException {
				JDialog dialog = super.createDialog(parent);
				dialog.setAlwaysOnTop(true);
				dialog.setModalityType(ModalityType.APPLICATION_MODAL);
				dialog.setModal(true);
				return dialog;
			}
		};
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle("Save As");
		fileChooser.setApproveButtonText("Save");
		fileChooser.setCurrentDirectory(new File("."));
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			nextSaveDir = fileChooser.getSelectedFile().getParent();
			try {
				FileWriter writer = new FileWriter(fileChooser.getSelectedFile());
				writer.write(json);
				writer.close();
				return json;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Got something else back...");
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
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		//		return new GsonBuilder().create().fromJson(json, clazz);
	}

	public static String loadFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("Load");
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
