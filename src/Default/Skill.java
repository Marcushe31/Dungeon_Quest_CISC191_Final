/**
* Lead Author(s):
* @author patri; student ID
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
* Version: 2026-05-06
*/
package Default;

/**
 * Purpose: The reponsibility of Skill is ...
 *
 * Skill is-a ...
 * Skill is ...
 */
public abstract class Skill
{
	protected String name;
	protected int cost;
	
	public Skill(String name, int cost) {
		this.name = name;
		this.cost = cost;
	}
	
	public abstract void activate(Player user, Enemy target);
	
	public String getName() {
		return name;
	}
	
	public int getCost() {
		return cost;
	}
	
	
	
}
