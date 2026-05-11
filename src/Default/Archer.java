/**
* Lead Author(s):
* @author marcu_n7cwnq7; student ID
* @author Full name; student ID
* <<Add additional lead authors here>>
*
* Other Contributors:
* Full name; student ID or contact information if not in class
* <<Add additional contributors (mentors, tutors, friends) here, with contact information>>
*
* References:
* Morelli, R., & Walde, R. (2016).
* Java, Java, Java: Object-Oriented Problem Solving
* https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
*
* <<Add more references here>>
*
* Version: 2026-05-11
*/
package Default;

/**
 * Purpose: The reponsibility of Archer is ...
 *
 * Archer is-a ...
 * Archer is ...
 */
public class Archer extends Player
{
	public Archer()
	{
		// health mana stamina damage
		super("Archer", 100, 50, 70, 15);
		addSkill(new DoubleShot());
	}

}
