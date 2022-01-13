package application.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.tinylog.Logger;

public class DataOutputHandler {
	private final String fileName;
	private final boolean hasTitle;
	private final BufferedWriter writer;

	public DataOutputHandler(String fileName) {
		this.fileName = fileName;
		this.hasTitle = hasTitle();
		try {
			this.writer = new BufferedWriter(new FileWriter(fileName, hasTitle));
		} catch (IOException e) {
			throw new RuntimeException("Something went wrong with" + fileName + ". Close all open windows and try again\n" + e);
		}
	}

	/*
	 * check if the file exists. if not: create one. if it does exists: check if it
	 * contains any value: if it does don't write a title
	 */
	private boolean hasTitle() {
		try {
			var reader = new BufferedReader(new FileReader(fileName));
			var line = reader.readLine();
			reader.close();
			if (line != null)
				return true;
			return false;
		} catch (IOException e) {
			Logger.info("File " + fileName + " couldn't be found\nCreating a new file instead");
			return false;
		}
	}

	/*
	 * writes a string 'as is' into a file. the string must be formatted
	 */
	public void writeData(String data, DataType type) throws IOException {
		switch (type) {
		case Title:
			if (hasTitle)
				return;
		case Data:
			Logger.info("Writing data: [ " + data + " ] to " + fileName);
			writer.write(data);
			writer.newLine();
			writer.flush();
			break;
		}
	}

	public void closeStream() throws IOException {
		Logger.info("Closing data stream");
		writer.close();
	}
}
