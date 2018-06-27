import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SequenceAlign {
	public SequenceAlign() {
	}
	static final private List<ValuePair> list = new ArrayList<ValuePair>();
	private int k = 0;
	public int[][] getArray(String x, String y) {
		int xSize = x.length();
		int ySize = y.length();
		int[][] arr = new int[xSize + 1][ySize + 1];
		for (int i = ySize; i >= 0; i--) {
			arr[xSize][i] = 2 * (ySize - i);
		}
		for (int j = xSize; j >= 0; j--) {
		arr[j][ySize] = 2 * (xSize - j);
		}
		int penalty = 0;
		for (int i = xSize - 1; i >= 0; i--) {
			for (int j = ySize - 1; j >= 0; j--) {
				if (x.charAt(i) == y.charAt(j)) {
					penalty = 0;
				} else {
					penalty = 1;
				}
				arr[i][j] = Math.min(arr[i + 1][j + 1] + penalty, Math.min(arr[i + 1][j] + 2, arr[i][j + 1] + 2));
			}
		}
		return arr;
	}
	private int rowDefalut = 0;
	private int colDefalut = 0;
	public  List<ValuePair> getBackTrack(int arr[][], String x, String y, int tag) {
		if (tag == 0) {
			list.add(new ValuePair(0, 0));
		} else if (tag == 1) {
			rowDefalut++;
			colDefalut++;
			list.add(new ValuePair(rowDefalut, colDefalut));
		} else if (tag == 2) {
			rowDefalut++;
			list.add(new ValuePair(rowDefalut, colDefalut));
		} else if (tag == 3) {
			colDefalut++;
			list.add(new ValuePair(rowDefalut, colDefalut));
		}
		if (arr.length > 1 && arr[0].length > 1) {
			int temp = arr[0][0];
			int penalty = 0;
			if (x.charAt(0) == y.charAt(0)) {
				penalty = 0;
			} else {
				penalty = 1;
			}
			if (temp == arr[0 + 1][0 + 1] + penalty) {
				String SubRowString = x.substring(1);
				String SubColString = y.substring(1);
				getBackTrack(getSubArray(arr, SubRowString, SubColString, 1, 1), SubRowString, SubColString, 1);
			} else if (temp == arr[0][0 + 1] + 2) {
				String SubRowString = x.substring(0);
				String SubColString = y.substring(1);
				getBackTrack(getSubArray(arr, SubRowString, SubColString, 0, 1), SubRowString, SubColString, 2);
			} else if (temp == arr[0 + 1][0] + 2) {
				String SubRowString = x.substring(1);
				String SubColString = y.substring(0);
				getBackTrack(getSubArray(arr, SubRowString, SubColString, 1, 0), SubRowString, SubColString, 3);
			}

		}
		return list;
	}
	public int[][] getSubArray(int arr[][], String rowSubString, String colSubString, int rowStart, int colStart) {
		int SubrowLength = rowSubString.length();
		int SubcolLength = colSubString.length();
		int[][] subArr = new int[SubrowLength + 1][SubcolLength + 1];
		for (int i = 0; i <= SubrowLength; i++) {
			for (int j = 0; j <= SubcolLength; j++) {
				subArr[i][j] = arr[rowStart + i][colStart + j];
			}
		}
		return subArr;
	}
	public char[][] getAlignmentSequence(String rowString, String colString, List<ValuePair> list) {
		int length = Math.max(rowString.length(), colString.length());
		char[][] sequArr = new char[2][length+1];
		int ifirst = list.get(0).getRow();
		int jfirst = list.get(0).getCol();
		sequArr[0][0] = rowString.charAt(ifirst);
		sequArr[1][0] = colString.charAt(jfirst);
		for (int i = 1; i < list.size()-1; i++) {
			int iIn = list.get(i).getRow();
			int jIn = list.get(i).getCol();
			if ((iIn != list.get(i - 1).getRow()) && (jIn != list.get(i - 1).getCol())) {
				sequArr[0][i] = rowString.charAt(jIn);
				sequArr[1][i] = colString.charAt(iIn);
			} else if ((iIn == list.get(i - 1).getRow()) && (jIn != list.get(i - 1).getCol())) {
				sequArr[0][i] = rowString.charAt(jIn);
				sequArr[1][i] = ' ';
			} else if ((iIn != list.get(i - 1).getRow()) && (jIn == list.get(i - 1).getCol())) {
				sequArr[0][i] = ' ';
				sequArr[1][i] = colString.charAt(iIn);
			}
			
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < length; j++) {
				System.out.print(sequArr[i][j] + " ");
			}
			System.out.println();
		}
		return sequArr;
	}
	public static void main(String[] args) throws IOException {
		//String String1 = "AACAGTTACC";
		//String String2 = "TAAGGTCA";
		System.out.println("请输入第一个序列：");
		BufferedReader buf1 = new BufferedReader (new InputStreamReader(System.in));
		String String1 = buf1.readLine();
		System.out.println("请输入第二个序列：");
		BufferedReader buf2 = new BufferedReader (new InputStreamReader(System.in)); 
		String String2 = buf2.readLine();
		System.out.println("排列后的结果为：");
		SequenceAlign sequence = new SequenceAlign();
		int[][] costArray = sequence.getArray(String1, String2);
		 List<ValuePair> list = sequence.getBackTrack(costArray, String1, String2, 0);
		char[][] sequArr = sequence.getAlignmentSequence(String1, String2, list);
	}
	class ValuePair {
		int row;
		int col;
		public ValuePair(int row, int col) {
			this.row = row;
			this.col = col;
		}
		public int getRow() {
			return row;
		}
		public int getCol() {
			return col;
		}
	}
}
