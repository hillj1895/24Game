import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Main
{
	final static String[] combos = {"xxxxooo", "xxxoxoo", "xxxooxo", "xxoxxoo", "xxoxoxo"};
	final static String ops = "+-*/^";
	private static int[] postSolution;
	private static String solnPattern;
	
	// Practice Main Method
	public static void main(String[] args)
	{
		List<List<Integer>> allCards = cardGenerator();
		for(List<Integer> a : allCards)
		{
			int[] arr = toIntArray(a);
			System.out.print(Arrays.toString(arr) + " ");
			if(isSolution(arr))
			{
				System.out.println("Solution is: " + postfixToInfix(postSolution));
			}
			else
				System.out.println("No Solution");
		}
	}
	
	static int[] toIntArray(List<Integer> list)
	{
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
		{
			ret[i] = list.get(i);
		}
		return ret;
	}
	
	public static List<List<Integer>> cardGenerator()
	{
		int[] deck = new int[52];   // Initializes an array that will hold a deck or cards
		List<List<Integer>> cards = new ArrayList<>();   // Initializes an array that will hold the 4 cards chosen
		
		for(int n=0; n<13; n++)
		{
			for(int i=0; i<4; i++)
			{
				int k = 4*n+i;
				deck[k] = n+1;
			}
		}
		
		int i1, i2, i3, i4;
		int c1, c2, c3, c4;
		
		for(i1 = 0; i1 <= 48;  i1+= 4)
		{
			for(i2 = i1 + 1; i2 <= 49; i2 += 4)
			{
				for(i3 = i2 + 1; i3 <= 50; i3 += 4)
				{
					for(i4 = i3 + 1; i4 <= 51; i4 += 4)
					{
							c1 = deck[i1];
							c2 = deck[i2];
							c3 = deck[i3];
							c4 = deck[i4];
							cards.add(Arrays.asList(c1, c2, c3, c4));
					}
				}
			}
		}
		return cards;
	}


	/*
	 * isSolution method calls evaluate method on all permutations of the different numbers
	 * and operators
	 */
	public static boolean isSolution(int[] array)
	{
		ArrayList<ArrayList<Integer>> numPermutations = permute(array);
		List<List<Integer>> opPermutations = new ArrayList<>(64);
		permuteOperators(opPermutations, 4, 64);
		
		int[] possSoln = new int[7];
		
		for(String combo : combos)
		{
			char[] comboChars = combo.toCharArray();
			for(ArrayList<Integer> numPermArray: numPermutations)
			{
				for(List<Integer> opPermArray : opPermutations)
				{
					int locationCount = 0;
					int numCount = 0;
					int opCount = 0;
					for(char c : comboChars)
					{
						if(c == 'x')
						{
							possSoln[locationCount] = numPermArray.get(numCount);
							numCount++;
						}
						else
						{
							possSoln[locationCount] = opPermArray.get(opCount);
							opCount++;
						}
						locationCount++;
					}
					if(evaluate(possSoln))
					{
						postSolution = possSoln;
						solnPattern = combo;
						return true;
					}
				}
			}
			solnPattern = combo;
		}
		return false;
	}

	/*
	 * Evaluate method tests a postfix array and checks if the combination is
	 * equal to 24
	 */
	static boolean evaluate(int[] line)
    {
    	Stack<Float> s = new Stack<>();
    	float temp;
    	for (int c : line)
    	{
    		if (1 <= c && c <= 13)
    		{
    			s.push((float) c);
    		}
    		else
    		{
    			temp = applyOperator(s.pop(), s.pop(), c);
    			s.push(temp);
    		}
    	}
    	return (Math.abs(24 - s.peek()) < 0.001F);
    }
    
    /*
     * This method takes 2 floats and 1 integer as parameters and applies operators to the two floats
     */
    static float applyOperator(float a, float b, int c)
    {
    	if(c == 20)
    	{
    		return a + b;
    	}
    	else if(c == 21)
    	{
    		return b - a;
    	}
    	else if(c == 22)
    	{
    		return a * b;
    	}
    	else if(c == 23)
    	{
    		return b / a;
    	}
    	else
    		return 0;
    }

    
	/*
	 *  This is a parent method for the recursive permute method.
	 *  It just takes an integer array as input and returns a 2D ArrayList
	 */
	public static ArrayList<ArrayList<Integer>> permute(int[] num)
	{
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		permute(num, 0, result);
		return result;
	}
	
	
	/*
	 * This is the main recursive permute method. The method calls recursively until all of
	 * the possible combinations of the original integer array are saved in the 2D ArrayList.
	 */
	static void permute(int[] num, int start, ArrayList<ArrayList<Integer>> result)
	{ 
		if (start >= num.length)
		{
			ArrayList<Integer> item = convertArrayToList(num);
			result.add(item);
		}
	 
		for (int j = start; j <= num.length - 1; j++)
		{
			swap(num, start, j);
			permute(num, start + 1, result);
			swap(num, start, j);
		}
	}
	
	
	/*
	 * This method is used to convert the integer array that the recursive permute method
	 * takes as input into an ArrayList so that it can be store in the 2D ArrayList
	 */
	private static ArrayList<Integer> convertArrayToList(int[] array)
	{
		ArrayList<Integer> item = new ArrayList<Integer>();
		for (int h = 0; h < array.length; h++)
		{
			item.add(array[h]);
		}
		return item;
	}
	
	/*
	 * This method swaps two elements in an array using a temporary variable to save a value
	 */
	private static void swap(int[] array, int i, int j)
	{
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	/*
	 * This method permutes all of the operators and saves all of them in a 2D integer list
	 */
    static void permuteOperators(List<List<Integer>> array, int n, int total)
    {
    	int npow = n * n;
        for (int i = 0; i < total; i++)
        {
            array.add(Arrays.asList((i / npow ) + 20, ((i % npow) / n) + 20, (i % n) + 20));
        }
    }
    
    /*
     * This method helps the postfixToInfix convert the integer equivalent for each of the operators
     */
    public static String whichOp(int op)
    {
    	if(op == 20)
    	{
    		return "+";
    	}
    	else if(op == 21)
    	{
    		return "-";
    	}
    	else if(op == 22)
    	{
    		return "*";
    	}
    	else if(op == 23)
    	{
    		return "/";
    	}
    	else
    		return "Invalid Entry";
    }
    
    
    /*
     * This method converts an integer array in postfix notation to string in infix notation
     */
    public static String postfixToInfix(int[] postSoln)
    {
    	if(solnPattern == "xxxxooo")
    	{
    		return postSoln[0] + whichOp(postSoln[6]) +  "(" + postSoln[1] + whichOp(postSoln[5]) + "(" + postSoln[2] + whichOp(postSoln[4]) + postSoln[3] + "))";
    	}
    	else if(solnPattern == "xxxoxoo")
    	{
    		return postSoln[0] + whichOp(postSoln[6]) +  "((" + postSoln[1] + whichOp(postSoln[5]) + postSoln[2] + ")" + whichOp(postSoln[3]) + postSoln[4] + ")";
    	}
    	else if(solnPattern == "xxxooxo")
    	{
    		return "(" + postSoln[0] + whichOp(postSoln[4]) +  "(" + postSoln[1] + whichOp(postSoln[3])  + postSoln[2] + "))" + whichOp(postSoln[6]) + postSoln[5];
    	}
    	else if(solnPattern == "xxoxxoo")
    	{
    		return "(" + postSoln[0] + whichOp(postSoln[5])  + postSoln[1] + ")" + whichOp(postSoln[6]) + "(" + postSoln[3] + whichOp(postSoln[2]) + postSoln[4] + ")";
    	}
    	else if(solnPattern == "xxoxoxo")
    	{
    		return "((" + postSoln[0] + whichOp(postSoln[6])  + postSoln[1] + ")" + whichOp(postSoln[4]) + postSoln[3] + ")" + whichOp(postSoln[2]) + postSoln[5];
    	}
    	else
    		return "Invalid Entry.";
    }

}









