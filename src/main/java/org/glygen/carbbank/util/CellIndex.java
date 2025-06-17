package org.glygen.carbbank.util;

public class CellIndex {
	int row;
	int col;
	
	public CellIndex(int i, int j) {
		this.row = i;
		this.col = j;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	
	
}
