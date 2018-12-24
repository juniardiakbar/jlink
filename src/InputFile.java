//package algeo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

class InputFile {
	public BufferedReader reader;
	public StringTokenizer tokenizer;

	public InputFile(File file) {
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("Tidak ditemukan");
		}
		tokenizer = null;
	}

	public String next() {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(reader.readLine());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return tokenizer.nextToken();
	}
	
//	public String[] nextAll(int row) {
//		String[] s = new String[row];
//		try {
//			String temp = reader.readLine();
//			for (int i = 0;temp != null;i++) {
//				s[i] = 
//			}
//		} catch (FileNotFoundException e) {
//			throw new RuntimeException(e);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	public boolean hasNext(File file) {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			try {
				String line = reader.readLine();
				if (line == null) {
					return false;
				}
				tokenizer = new StringTokenizer(line);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	public int nextInt() {
		return Integer.parseInt(next());
	}
	
	public double nextDouble() {
		return Double.parseDouble(next());
	}

	public int countColumn(File file) {
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (hasNext(file)) {
			return tokenizer.countTokens();
		} else {
			return 0;
		}
	}

	public int countRow(File file) {
		int count = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			while (reader.readLine() != null)
				count++;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return count;
	}
}