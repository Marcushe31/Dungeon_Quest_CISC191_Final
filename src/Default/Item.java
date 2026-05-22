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
 * Purpose: The responsibility of Item is ...
 *
 * Item is-a ...
 * Item is ...
 */
public class Item
{
	private String itemType;
	private int effect;
	
	public Item(String itemType, int effect)
	{
		this.itemType = itemType;
		this.effect = effect;
	}
	
	public String getItemType()
	{
		return itemType;
	}
	
	public int getEffect()
	{
		return effect;
	}
}
