import java.util.Arrays;

public class NQueens
{
	public static final int N = 8;

	// Function to check if queens already placed would attack the queen currently being placed
	private static boolean isSafe(char board[][], int r, int c)
	{
		//check if a queen exists in same column
		for (int i = 0; i < r; i++)
			if (board[i][c] == 'Q')
				return false;

		//check if a queen exists in lower left diagonal  (\)
		for (int i = r, j = c; i >= 0 && j >= 0; i--, j--)
			if (board[i][j] == 'Q')
				return false;

		//check if a queen exists in upper right diagonal  (/)
		for (int i = r, j = c; i >= 0 && j < N; i--, j++)
			if (board[i][j] == 'Q')
				return false;

		return true;
	}
    //The backtracking function
	private static void nQueen(char board[][], int r)
	{
		// if last row is reached,print solution
		if (r == N)
		{
			for (int i = 0; i < N; i++)
			{
				for (int j = 0; j < N; j++)
					System.out.print(board[i][j] + " ");
				System.out.println();
			}
			System.out.println();

			return;
		}

        //iterate through every column and try placing queen, if safe
		for (int i = 0; i < N; i++)
		{
			// check if any previous queen is atacking the queen currently being placed
			if (isSafe(board, r, i))
			{
				// place queen on current square
				board[r][i] = 'Q';

				// recur for next row
				nQueen(board, r + 1);

				// backtrack and remove queen from current square
				board[r][i] = '-';
			}
		}
	}

	public static void main(String[] args)
	{
        //the chess board
		char[][] board = new char[N][N];

		// initialize board[][] by '-'
		for (int i = 0; i < N; i++) {
			Arrays.fill(board[i], '-');
		}

		nQueen(board, 0);
	}
}
