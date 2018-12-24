//package algeo;

import java.io.File;
import java.io.InputStream;

public class Main {
	public static void main(String[] args) {

		// Preparing Stream for I/O.
		InputStream inputStream = System.in;
		InputReader in = new InputReader(inputStream);

		// Prompt menu.
		byte inType, problem, elim;
		inType = problem = elim = 0;
		System.out.println("Menu");
		do {
			System.out.println("Bentuk masukan:\n1. File\n" + "2. Prompt");
			inType = in.nextByte();
		} while (inType < 1 || inType > 2);
		do {
			System.out.println("1. Sistem Persamaan Linier\n" + "2. Interpolasi Polinom\n3. Keluar");
			problem = in.nextByte();
		} while (problem < 1 || problem > 3);
		if (problem == 3) { // Exit.
			return;
		}
		do {
			System.out.println("1. Metode eliminasi Gauss\n" + "2. Metode eliminasi Gauss-Jordan");
			elim = in.nextByte();
		} while (elim < 1 || elim > 2);

		if (inType == 1) // File.
			solveF(problem, elim, args[0]);
		else if (inType == 2) // Prompt
			solveP(in, problem, elim);
	}

	private static void solveP(InputReader in, byte problem, byte elim) {
		{ // Handling prompt input.
			
			if (problem == 1) { // Linear equations.
				System.out.print("Baris Matriks: ");
				int row = in.nextInt();
				System.out.print("Kolom Matriks: ");
				int column = in.nextInt();
				Matrix matrix = new Matrix(row, column);
				matrix.readMatrix();
				if (elim == 1) {
					matrix.gaussianSolution(false);
				} else if (elim == 2) {
					matrix.gaussJordanSolution(false);
				}
			} else if (problem == 2) { // Interpol.
				System.out.print("Jumlah titik: ");
				int row = in.nextInt();
				int column = 2;
				Matrix matrix = new Matrix(row, column);
				matrix.readMatrix();
				matrix.printTable();
				System.out.print("Masukkan x yang akan ditaksir: ");
				double x = in.nextDouble();
				
				double[] r = null;
				if (elim == 1) {
					r = matrix.gaussianInter();
					represent(r);
				} else if (elim == 2) {
					r = matrix.gaussJordanInter();
					represent(r);
				}
				double result = 0;
				for (int i = r.length - 1; i >= 0; i--) {
					result += Math.pow(x, i) * r[i];
				}
				System.out.println(result);
			}
		}
	}

	// File input solver.
	private static void solveF(byte problem, byte elim, String file) {
		{ // Handling file input.
			File f = new File(file);
			InputFile fin = new InputFile(f);
			int column = fin.countColumn(f);
			int row = fin.countRow(f);
			Matrix matrix = null;
			try {
				matrix = new Matrix(row, column);
			} catch (NegativeArraySizeException e) {
				System.out.println("The file is empty or invalid.");
				return;
			}
			fin = new InputFile(f);

			if (problem == 1) { // Linear equations.
				/*
				 * Read infile's augmented matrix into matrix.table and form matrix A and b
				 * based on this form : Ax = b
				 */
				for (int i = 0; i < matrix.row; i++) {
					for (int j = 0; j < matrix.column; j++) {
						matrix.table[i][j] = fin.nextDouble();
						if (j < matrix.column - 1) {
							matrix.A[i][j] = matrix.table[i][j];
						} else { // last column.
							matrix.b[i] = matrix.table[i][j];
						}
					}
				}

				if (elim == 1) {
					matrix.gaussianSolution(false);
				} else if (elim == 2) {
					matrix.gaussJordanSolution(false);
				}
			} else if (problem == 2) { // Interpolation problem.
				/*
				 * Read (x, y) values into matrix.table and form matrix a and b based on this
				 * form : a = [ x0, x1, x2, ... ] b = [ y0, y1, y2, ... ]
				 */
				for (int i = 0; i < matrix.row; i++) {
					for (int j = 0; j < matrix.column; j++) {
						matrix.table[i][j] = fin.nextDouble();
						if (j == 0) {
							matrix.a[i] = matrix.table[i][j];
						} else {
							matrix.b[i] = matrix.table[i][j];
						}
					}
				}
				double[] r = new double[matrix.table.length];
				if (elim == 1) {
					r = matrix.gaussianInter();
					represent(r);
				} else if (elim == 2) {
					r = matrix.gaussJordanInter();
					represent(r);
				}
				System.out.print("Masukkan x untuk mencari nilai y: ");
				InputReader in = new InputReader(System.in);
				double x = in.nextDouble();
				double result = 0;
				for (int i = r.length - 1; i >= 0; i--) {
					result += Math.pow(x, i) * r[i];
				}
				System.out.println(result);
			}
		}
	}

	private static void represent(double[] x) {
		int deg = x.length;
		for (double d : x) {
			if (d == 0) {
				deg--;
			}
		}
		for (int i = deg - 1; i >= 0; i--) {
			if (i < deg - 1) {
				if (x[i] > 0) {
					System.out.print("+");
				} else if (x[i] == 0) {
					continue;
				}
			}
			System.out.printf("%.2f(x^%d)", x[i], i);
		}
		System.out.println();
	}
}