package testsuite;

/**
 * Utility Class having method to test against the Control Flow graph analysis
 * 
 * @author Swapnil
 * 
 */
public class TestSuite {

	/**
	 * Computes factorial of given number recursively Note : To checkout the CFG
	 * for recursive function
	 * 
	 * @param number
	 * @return
	 */
	/*public static long computeFactorial(int number) {
		System.out.println("Method : computeFactorial("+number+")");
		long factorial;
		if (number <= 0) {
			factorial = 1;
		} else {
			factorial = computeFactorial(number - 1) * number;
		}
		System.out.println("Factoriaal is :: " + factorial);
		return factorial;
	}*/

	public static void main(String[] args){
		System.out.println("Method : main() ");
		int n=123456789;
		int sum = 0; 
		while (n > 0) {
			sum += n % 10;
			n = n / 10;
		}
		if(sum > 0) {
			sum = sum + 1;
		} else {
			sum = sum - 1;
		}
		System.out.println("Sum of digits is :"+sum);
		if(sum>0){
		try{
			Util.computeFactorial(5);
		} catch(NullPointerException e){
			System.out.println("Exception case");
		//	e.printStackTrace();
		}
		}
	}

	/**
	 * Sample method to generate similar control flow graph to analyzed in
	 * lecture
	 */
	/*	public static void sampleMethod() {
		System.out.println("Method : sampleMethod() ");
		int x=10;
		while(x>0){
			x--;
		}
		System.out.println(x);
	}
	 */

}
