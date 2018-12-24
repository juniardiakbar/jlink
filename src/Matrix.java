//package algeo;

import java.util.Arrays;

public class Matrix {
	int row;
	int column;
	double table[][];
	double[][] A;
	double[] a;
	double[] b;

	public Matrix() {

	}

	public Matrix(int row, int column) {
		this.row = row;
		this.column = column;
		this.table = new double[row][column];
		this.A = new double[row][column - 1];
		this.a = new double[row];
		this.b = new double[row];
	}

	public void readMatrix() {
		InputReader scanner = new InputReader(System.in);
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.column; j++) {
				this.table[i][j] = scanner.nextDouble();
				if (j < this.column - 1) {
					this.A[i][j] = this.table[i][j];
					this.a[i] = this.table[i][j];
				} else { // last column.
					this.b[i] = this.table[i][j];
				}
			}
		}
	}

	public void printTable() {
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.column; j++) {
				if (j < this.column - 1) {
					System.out.printf("%.2f\t", this.table[i][j]);
				} else { // last column.
					System.out.printf("%.2f\n", this.table[i][j]);
				}
			}
		}
		System.out.println();
	}

	public double[][] gaussJordanMatrix() {
		this.table = gaussianMatrix();
//		for (int i = this.row - 1; i >= 0; i--) {
//			for (int j = 0; j < this.column - 1; j++) {
//				if (this.table[i][j] != 0) {
//					for (int k = i - 1; k >= 0; k--) {
//						double N = this.table[k][j];
//						for (int l = j; l < this.column; l++) {
//							this.table[k][l] = this.table[k][l] - N * this.table[i][l];
//						}
//					}
//					break;
//				}
//			}
//		}
		for (int i = this.table.length - 1; i >= 0; i--) {
			int core = 0;
			while (core < this.table[0].length && this.table[i][core] == 0) {
				core++;
			}
			if (core == this.table[0].length) {
				continue;
			} else {
				double alpha = this.table[i][core];
				this.table[i][this.table[0].length - 1] /= alpha;
				for (int j = core; j < this.table[0].length - 1; j++) {
					this.table[i][j] /= alpha;
					for (int k = 0; k < i; k++) {
						if (this.table[k][j] != 0) {
							this.table[k][this.table[0].length - 1] -= this.table[i][this.table[0].length - 1]
									* this.table[k][j];
						}
						this.table[k][j] -= this.table[i][j] * this.table[k][j];
					}
				}
			}
		}
		return this.table;
	}

	public double[][] gaussianMatrix() {

		// If number of equation is less than number of var.
		if (this.A.length < this.A[0].length) {
			double[][] newA = new double[this.A[0].length][this.A[0].length];
			double[] newb = new double[this.A[0].length];

			for (int i = 0; i < newA.length; i++) {
				if (i < this.A.length) {
					for (int j = 0; j < newA[0].length; j++) {
						newA[i][j] = this.A[i][j];
					}
					newb[i] = this.b[i];
				} else {
					Arrays.fill(newA[i], 0);
					newb[i] = 0;
				}
			}

			this.A = newA;
			this.b = newb;
		}

		rearrange();

		// Merge A and b into augmented matrix.
		double[][] matrix = new double[this.A.length][this.A[0].length + 1];
		this.row = this.A.length;
		this.column = this.A[0].length + 1;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (j < matrix[0].length - 1) {
					matrix[i][j] = this.A[i][j];
				} else {
					matrix[i][matrix[0].length - 1] = this.b[i];
				}
			}
		}
		return matrix;
	}

	// Gaussian elimination with partial pivoting
	public double[] gaussianSolution(boolean isInterpol) {
		int n = this.A[0].length; // n number variable.
		this.table = gaussianMatrix();

		if (hasSolution()) {
			System.out.println("It has no solution.");
			return new double[n];
		}
		if (isInterpol) {
			// back substitution
			double[] result = new double[n];
			for (int i = n - 1; i >= 0; i--) {
				double sum = 0.0;
				for (int j = i + 1; j < n; j++) {
					sum += this.A[i][j] * result[j];
				}
				result[i] = Double.isNaN((this.b[i] - sum) / this.A[i][i]) ? 0 : (this.b[i] - sum) / this.A[i][i];
			}
			if (!isInterpol) {
				for (int i = 0; i < result.length; i++) {
					System.out.printf("x[%d] = %.2f\n", i + 1, result[i]);
				}
			}

			return result;
		} else { // Singular
			return gaussJordanSolution(false);
		}
	}

	private void rearrange() {
		int coreRow = 0;
		for (int p = 0; p < this.A[0].length; p++) {
			int max = coreRow;

			for (int i = coreRow + 1; i < this.A.length; i++) {
				if (Math.abs(this.A[i][p]) > Math.abs(this.A[max][p])) {
					max = i;
				}
			}

			rowSwap(coreRow, max);
			if (this.A[coreRow][p] == 0) {
				continue;
			}

			for (int i = coreRow + 1; i < this.A.length; i++) {
				double alpha = this.A[i][p] / this.A[coreRow][p];

				this.b[i] -= alpha * this.b[coreRow];
				for (int j = p; j < this.A[i].length; j++) {
					this.A[i][j] -= alpha * this.A[coreRow][j];
				}
			}
			coreRow++;
		}
	}

	/**
	 * @param p   pivot row index
	 * @param max to be swapped with p
	 */
	private void rowSwap(int p, int max) {
		double[] temp = this.A[p];
		this.A[p] = this.A[max];
		this.A[max] = temp;
		double t = this.b[p];
		this.b[p] = this.b[max];
		this.b[max] = t;
	}

	public double[][] interpolasi() {
		double[][] result = new double[this.a.length][this.a.length];
		for (int i = 0; i < this.a.length; i++) {
			for (int j = 0; j < this.a.length; j++) {
				result[i][j] = Math.pow(this.a[i], j);
			}
		}
		return result;
	}

	public double[] gaussianInter() {
		this.A = this.interpolasi();
		return this.gaussianSolution(true);
	}

	public double[] gaussJordanSolution(boolean isInterpol) {
		int n = this.A[0].length; // n number variable.
		this.table = gaussJordanMatrix();
		if (hasSolution()) {
			System.out.println("It has no solution.");
			return new double[n];
		} 
		if (isInterpol) {
			// back substitution
			double[] result = new double[n];
			for (int i = n - 1; i >= 0; i--) {
				double sum = 0.0;
				for (int j = i + 1; j < n; j++) {
					sum += this.A[i][j] * result[j];
				}
				result[i] = Double.isNaN((this.b[i] - sum) / this.A[i][i]) ? 0 : (this.b[i] - sum) / this.A[i][i];
			}
			if (!isInterpol) {
				for (int i = 0; i < result.length; i++) {
					System.out.printf("x[%d] = %.2f\n", i + 1, result[i]);
				}
			}

			return result;
		}
		else {
//		if (!isSingular()) {
//			// back substitution
//			double[] result = new double[n];
//			for (int i = n - 1; i >= 0; i--) {
//				double sum = 0.0;
//				for (int j = i + 1; j < n; j++) {
//					sum += this.A[i][j] * result[j];
//				}
//				result[i] = (this.b[i] - sum) / this.A[i][i];
//			}
//			return result;
//		} else { // Singular
//			double[] result = new double[n];
//			for (int i = n - 1; i >= 0; i--) {
//				double sum = 0.0;
//				for (int j = i + 1; j < n; j++) {
//					sum += this.A[i][j] * result[j];
//				}
//				result[i] = Double.isNaN((this.b[i] - sum) / this.A[i][i]) ? 0 : (this.b[i] - sum) / this.A[i][i];
//			}
//			return result;
			for (int i = 0; i < this.table[0].length - 1; i++) {
				System.out.printf("x[%d] = ", i + 1);
				boolean flag = false;
				int last = 0;
				boolean printed = false;
				for (int j = this.table[0].length - 1; j > i; j--) {
					if (j < this.table[0].length - 1 && this.table[j][j] == 0) {
						if (this.table[i][j] != 0) {
							flag = true;
							if (this.table[i][this.table[0].length - 1] != 0) {
								System.out.print(" + ");
							}
							if (this.table[i][j] == 1) {
								System.out.printf("-x[%d]", j + 1);
							} else if (this.table[i][j] == -1) {
								System.out.printf("x[%d]", j + 1);
							} else {
								System.out.printf("%.2fx[%d]", -this.table[i][j], j + 1);
							}
						}
					} else if (this.table[i][j] != 0) {
						printed = true;
						System.out.printf("%.2f", this.table[i][j]);
					}
					last = j;
				}
				if (!flag) {
					if (this.table[last - 1][last - 1] == 0) {
						System.out.printf("x[%d]", i + 1);
					} else if (!printed) {
						System.out.printf("%.2f", this.table[i][this.table[0].length - 1]);
					}
				}
				System.out.println();
			}
			return new double[n];
		}
	}

	private boolean hasSolution() {
		boolean noSolution = false;
		for (int i = 0; i < this.table.length; i++) {
			noSolution = true;
			for (int j = 0; j < this.table[0].length - 1; j++) {
				if (this.table[i][j] != 0) {
					noSolution = false;
				}
			}
			if (noSolution && this.table[i][this.table[0].length - 1] != 0) {
				noSolution = true;
				System.out.println(i);
				break;
			} else {
				noSolution = false;
			}
		}
		return noSolution;
	}

	public double[] gaussJordanInter() {
		this.A = this.interpolasi();
		return this.gaussJordanSolution(true);
	}
}
