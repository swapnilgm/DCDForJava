/**
 * 
 */
package testcase3;

/**
 * @author Swapnil
 *
 */
public class NestedRegionTest {

	/**
	 * 
	 */
	public NestedRegionTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int x, y;
		x=10;	
		y=100;
	
		
		for (int j = 0; j < 10; j++) {
			y = y-15;
			if ( y > x ) { 			
				x = x+10;
			}
			else { 
				x = x - 20;
			}
			y--;
		}
	
		x=5;
		System.out.println("x =" +x);
	}

}
