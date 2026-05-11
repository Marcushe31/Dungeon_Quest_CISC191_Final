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
 * Purpose: The reponsibility of BattleController is ...
 *
 * BattleController is-a ...
 * BattleController is ...
 */
public class BattleController
{
	private Battle battle;
	
	public BattleController(Battle battle) {
		this.battle = battle;
	}
		
	public void handlePlayerAction(String action) {
		battle.takeTurn(action);
	}
	
	public void handleEnemyTurn() {
		battle.takeTurn("attack");
	}
	

	
	
}










