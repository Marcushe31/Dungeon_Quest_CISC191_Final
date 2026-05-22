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
* Version: 2026-05-20
*/
package Default;

/**
 * Purpose: The responsibility of Door is ...
 *
 * Door is-a ...
 * Door is ...
 */
public class Door
{
	private String eventType;
	private Enemy enemy;
	private Item item;

	public Door(String eventType,
			Enemy enemy,
			Item item)
	{
		this.eventType = eventType;
		this.enemy = enemy;
		this.item = item;
	}

	public String getEventType()
	{
		return eventType;
	}

	public Enemy getEnemy()
	{
		return enemy;
	}

	public Item getItem()
	{
		return item;
	}
}