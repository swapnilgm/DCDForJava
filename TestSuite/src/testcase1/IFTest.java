/**
 * 
 */
package testcase1;

/**
 * @author Swapnil
 *
 */
public class IFTest {

	/**
	 * 
	 */
	public IFTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double a =Math.random();
		if(a<100){
			System.out.println("Found a :: "+ a);
		}else{
			System.out.println("Unsuccessful attempt");
		}
	}

}
