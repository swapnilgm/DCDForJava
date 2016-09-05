/**
 * 
 */
package testsuite;

/**
 * @author Swapnil
 *
 */
public class Util {

	/**
	 * 
	 */
	public Util() {
		// TODO Auto-generated constructor stub
	}

	public static long computeFactorial(int number)   {
		System.out.println("Method : computeFactorial("+number+")");
		long factorial;
		if (number <= 0) {
			factorial = 1;
			throw new NullPointerException();
		} else {
			factorial = computeFactorial(number - 1) * number;
		}
		System.out.println("Factoriaal is :: " + factorial);
		return factorial;
	}
}
