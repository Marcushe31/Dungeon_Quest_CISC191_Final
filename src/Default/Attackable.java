/**
* Lead Author(s):
* @author Patrick Tran
* @author Marcus Hernandez
*
* Other Contributors:
*
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
*
*
* Version: 2026-05-04
*/
package Default;

/**
 * Purpose: The reponsibility of Attackable is ...
 *
 * Attackable is-a ...
 * Attackable is ...
 */
public interface Attackable
{
	/**
	 * 
	 * Purpose: Marks that this creation has taken an attack action
	 */
	void attack();
	
	/**
	 * 
	 * Purpose: Applies incoming damage to this creation
	 * @param damage amount of damage to apply
	 */
	void receivedDamage(int damage);
	
	/**
	 *
	 * Purpose: Returns this creation's base attack damage
	 * 
	 * @return attack damage
	 */
	int getDamage();
	
	/**
	 *
	 * Purpose: Returns this creation's current health
	 * 
	 * @return current health
	 */
	int getHealth();
}
