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
 * Purpose: The reponsibility of Fireball is ...
 *
 * Fireball is-a ...
 * Fireball is ...
 */
public class Fireball extends Skill
{
	private static final int DAMAGE = 45;
	
	public Fireball() {
		super("Fireball", 20 );
	}
	
	@Override
	public void activate(Player user, Enemy target) {
		target.takeDamage(DAMAGE);
	}
	
}



















