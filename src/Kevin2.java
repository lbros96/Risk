import java.util.LinkedList;
import java.util.Random;

// put your code here

public class Kevin2 implements Bot {
	// The public API of YourTeamName must not change
	// You cannot change any other classes
	// YourTeamName may not alter the state of the board or the player objects
	// It may only inspect the state of the board and the player objects
	// So you can use player.getNumUnits() but you can't use player.addUnits(10000), for example

	private BoardAPI board;
	private PlayerAPI player;
	Random rand;

	int maxPosition = 0;

	Kevin2 (BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;	
		player = inPlayer;
		// put your code here
		return;
	}

	public String getName () {
		String command = "";
		// put your code here
		command = "Kevin2";
		return(command);
	}

	public String getReinforcement () {
		String command = "";
		LinkedList[] list = new LinkedList[42];
		int temp = 0;

		for (int i = 0; i < 42; i++) {
			for (int j = 0; j < 42; j++) {
				if (board.isConnected(i, j) && board.getOccupier(i) == player.getId()) {
					if (list[i] == null) {
						list[i] = new LinkedList<String>();
					}
					list[i].add(j);
				}
			}
		}

		int maxSize = 0;
		for (int i = 0; i < GameData.NUM_COUNTRIES; i++) {
			if (list[i] != null) {
				if (list[i].size() > maxSize) {
					maxSize = list[i].size();
					maxPosition = i;
				}
			}
		}

		if (list[maxPosition] != null) 	{
			temp = list[maxPosition].size();

			if(maxPosition == 0){
				for(int i =0;i<42;i++){
					if(board.getOccupier(i) == player.getId())
					{
						command = GameData.COUNTRY_NAMES[i];
					}
				}
			}
			else{
				int random = (int)(Math.random()*temp);
				System.out.println("temp " + temp);
				System.out.println("random " +random);
				command = GameData.COUNTRY_NAMES[(int)list[maxPosition].get(random)];
			}
		}
		else {
			for (int i = 0; i < GameData.NUM_COUNTRIES; i++) {
				if (board.getOccupier(i) == player.getId()) {
					command = GameData.COUNTRY_NAMES[i];
				}
			}
		}


		command = command.replaceAll("\\s", "");
		command += " 1";
		return(command);
	}

	public String getPlacement (int forPlayer) {
		String command = "";
		// put your code here
		int i = 0;
		for( i=0; i<=41; i++)
		{
			if(board.getOccupier(i) == forPlayer)
			{
				for(int j = 0; j <= GameData.ADJACENT[i].length-1; j++)
				{
					if(board.getOccupier(GameData.ADJACENT[i][j]) == 0)
					{

						command = GameData.COUNTRY_NAMES[i];
					}
				}
			}

		}
		command = command.replaceAll("\\s", "");
		return command;
	}

	public String getCardExchange () {
		String command = "";
		// put your code here
		int a = 0, i = 0, c = 0, w = 0;
		int [] cards = new int[5];
		for (int j = 0; j < player.getCards().size();j++) {
			cards[j] = player.getCards().get(j).getInsigniaId();

			char temp = player.getCards().get(j).getInsigniaName().charAt(0);
			if (temp == 'I') i++;
			else if (temp == 'A') a++;
			else if (temp == 'C') c++;
			else if (temp == 'W') w++;
		}



		if (w == 0) {
			if (i >= 3) command = "III";
			else if (c >= 3) command = "CCC";
			else if (a >= 3) command = "AAA";
			else command = "ICA";
		}
		else if (w == 1) {
			if (i+w >= 3) command = "IIW";
			else if (c+w >= 3) command = "CCW";
			else if (a+w >= 3) command = "AAW";
			else {
				if (i == 1 && c > 1) command = "ICW";
				else if (i == 1 && a == 1) command = "IAW";
				else if (c == 1 && a == 1) command = "CAW";
				else command = "ICA";
			}
		}
		else if (w == 2) {
			if (i+w >= 3) command = "IWW";
			else if (c+w >= 3) command = "CWW";
			else if (a+w >= 3) command = "AWW";
			else command = "ICA";
		}
		else if (player.isForcedExchange()) {
			command = "ICA";
		}
		else command = "skip";


		return(command);
	}

	public String getBattle () {
		LinkedList[] list = new LinkedList[42];

		String command = "";
		// put your code here
		int attackingCountry = 0;
		int defendingCountry = 0;
		int australia = 0;
		int sAmerica = 0;
		int europe = 0;
		int nAmerica = 0;
		int asia = 0;
		int africa = 0;

		// Groups connected territories into linked list
		for (int i = 0; i < 42; i++) {
			for (int j = 0; j < 42; j++) {
				if (board.isConnected(i, j) && board.getOccupier(i) == player.getId()) {
					if (list[i] == null) {
						list[i] = new LinkedList<String>();
					}
					list[i].add(j);
				}
			}
		}

		int i = 0;


		//Find how many countries bot owns in Australia
		for(i = 0; i < GameData.CONTINENT_COUNTRIES[3].length; i++){
			if(board.getOccupier(GameData.CONTINENT_COUNTRIES[3][i]) == player.getId()){
				australia++;
			}
		}

		//Find how many countries bot owns in sAmerica		
		for(i = 0; i < GameData.CONTINENT_COUNTRIES[4].length; i++){
			if(board.getOccupier(GameData.CONTINENT_COUNTRIES[4][i]) == player.getId()){
				sAmerica++;
			}
		}

		if (australia == 2 || australia == 3) {
			// finds country to attack with
			for ( i = 0; i < GameData.CONTINENT_COUNTRIES[3].length; i++) {
				attackingCountry = GameData.CONTINENT_COUNTRIES[3][i];
				if (board.getOccupier(attackingCountry) == player.getId() && board.getNumUnits(attackingCountry) > 2) {

					command += GameData.COUNTRY_NAMES[attackingCountry].replaceAll("\\s", "");
					break;
				}
			}
			// finds country to attack
			for (int j = 0; j < GameData.CONTINENT_COUNTRIES[3].length; j++) {
				defendingCountry = GameData.CONTINENT_COUNTRIES[3][j];
				if (board.getOccupier(defendingCountry) != player.getId() && board.isAdjacent(attackingCountry, defendingCountry)) {
					command += " " + GameData.COUNTRY_NAMES[defendingCountry].replaceAll("\\s", "");
					break;
				}
			}

			if (board.getNumUnits(attackingCountry) > 3 && command.length() > GameData.COUNTRY_NAMES[attackingCountry].length()+1) command += " 3";
			else if (board.getNumUnits(attackingCountry) > 2 && command.length() > GameData.COUNTRY_NAMES[attackingCountry].length()+1) command += " 2";
			else command = "skip";

		}
		else if (sAmerica == 2 || sAmerica == 3) {
			// finds country to attack with
			for (i = 0; i < GameData.CONTINENT_COUNTRIES[4].length; i++) {
				attackingCountry = GameData.CONTINENT_COUNTRIES[4][i];
				if (board.getOccupier(attackingCountry) == player.getId() && board.getNumUnits(attackingCountry) > 2) {

					command += GameData.COUNTRY_NAMES[attackingCountry].replaceAll("\\s", "");
					System.out.println(command);
					break;
				}
			}
			// finds country to attack
			for (int j = 0; j < GameData.CONTINENT_COUNTRIES[4].length; j++) {
				defendingCountry = GameData.CONTINENT_COUNTRIES[4][j];
				if (board.getOccupier(defendingCountry) != player.getId() && board.isAdjacent(attackingCountry, defendingCountry)) {
					command += " " + GameData.COUNTRY_NAMES[defendingCountry].replaceAll("\\s", "");
					System.out.println(command);
					break;
				}
			}

			if (board.getNumUnits(attackingCountry) > 3 && command.length() > GameData.COUNTRY_NAMES[attackingCountry].length()+1) command += " 3";
			else if (board.getNumUnits(attackingCountry) > 2 && command.length() > GameData.COUNTRY_NAMES[attackingCountry].length()+1) command += " 2";
			else if(board.getNumUnits(attackingCountry) > 2)command += " 2";
			else command = "skip";

		}


		else if (list[maxPosition] != null){
			// finds country to attack with
			outerLoop:
				for (i = 0; i < list[maxPosition].size(); i++) {
					// checks its adjacent countries, if they are friendlies or not
					for (int j = 0; j < GameData.ADJACENT[(int)list[maxPosition].get(i)].length; j++) {
						if (board.getNumUnits((int)list[maxPosition].get(i)) > 2 && board.getOccupier(GameData.ADJACENT[(int)list[maxPosition].get(i)][j]) != player.getId()) {
							attackingCountry = (int)list[maxPosition].get(i);
							command = GameData.COUNTRY_NAMES[attackingCountry].replaceAll("\\s", "");
							break outerLoop;
						}
					}
				}
		// finds country to attack
		for (int j = 0; j < GameData.NUM_COUNTRIES; j++) {
			if (board.getOccupier(j) != player.getId() && board.isAdjacent(attackingCountry, j)) {
				command += " " + GameData.COUNTRY_NAMES[j].replaceAll("\\s", "");
				break;
			}
		}

		// Chooses how many units to attack with
		if (board.getNumUnits(attackingCountry) > 3 && command.length() > GameData.COUNTRY_NAMES[attackingCountry].length()+2) command += " 3";
		else if (board.getNumUnits(attackingCountry) > 2 && command.length() > GameData.COUNTRY_NAMES[attackingCountry].length()+2) command += " 2";
		else command = "skip";

		}
		else command = "skip";


		return(command);
	}

	public String getDefence (int countryId) {
		String command = "";
		// put your code here
		if(board.getNumUnits(countryId)>=2){
			command = "2";
		}
		else{
			command = "1";
		}
		return(command);
	}

	public String getMoveIn (int attackCountryId) {
		String command = "";
		int enemyBorder = 0;
		int friendlyBorder = 0;

		for(int i = 0;i< GameData.ADJACENT[attackCountryId].length;i++){
			if(board.getOccupier(GameData.ADJACENT[attackCountryId][i]) == 0){
				enemyBorder++;
			}
			else if(board.getOccupier(GameData.ADJACENT[attackCountryId][i]) == 1){
				friendlyBorder++;
			}	
		}


		if(enemyBorder >= friendlyBorder){
			command = "0";
		}

		else{
			command = (""+board.getNumUnits(attackCountryId)/2);
		}

		return(command);
	}


	public String getFortify () {
		LinkedList[] list = new LinkedList[42];	
		String command = "";

		for (int i = 0; i < 42; i++) {
			for (int j = 0; j < 42; j++) {
				if (board.isConnected(i, j) && board.getOccupier(i) == player.getId()) {
					if (list[i] == null) {
						list[i] = new LinkedList<String>();
					}
					list[i].add(j);
				}
			}
		}

		int maxSize = 0;
		int maxPosition = 0;
		for (int i = 0; i < GameData.NUM_COUNTRIES; i++) {
			if (list[i] != null) {
				if (list[i].size() > maxSize) {
					maxSize = list[i].size();
					maxPosition = i;
				}
			}
		}



		boolean[] bordersFriendlies = new boolean [42];
		for (int i = 0; i < 42; i++) {
			int numBorder = 0;
			for (int j = 0; j < GameData.ADJACENT[i].length; j++) {
				if (board.getOccupier(GameData.ADJACENT[i][j]) == player.getId()) {
					numBorder++;
				}
			}
			if (numBorder == GameData.ADJACENT[i].length) {
				bordersFriendlies[i] = true;
			}
			else bordersFriendlies[i] = false;
		}

		// Check whether the country is eligible
		boolean checkFortify = false;
		for (int i = 0; i < GameData.NUM_COUNTRIES; i++) {
			if (bordersFriendlies[i] == true) {
				checkFortify = true;
			}
		}

		if (checkFortify == true) {
			int country = 0; 			
			int pos = 0;
			int temp = 0;

			int numOfEnemies = 0;
			int posOfHighestEnemies = 0;
			temp = -1;

			for (int i = 0; i < list[maxPosition].size(); i++) {
				int countryMax = (int)list[maxPosition].get(i);

				for (int j = 0; j < GameData.ADJACENT[country].length; j++) {
					if (board.getOccupier((int)GameData.ADJACENT[country][j]) != player.getId()) {
						numOfEnemies++;
					}
				}
				if (numOfEnemies > temp) {
					temp = numOfEnemies;
					posOfHighestEnemies = country;
				}
				if (list[maxPosition].isEmpty()) {
					posOfHighestEnemies = -1;
				}
			}


			int highestAdj = posOfHighestEnemies;

			for (int i = 0; i < list[maxPosition].size(); i++) {
				country = (int)list[maxPosition].get(i);
				int numUnits = board.getNumUnits(country);
				if (numUnits > temp && bordersFriendlies[country]) {
					temp = numUnits;
					pos = country;
				}
			}

			if (board.getOccupier(pos) == player.getId()) command += GameData.COUNTRY_NAMES[pos].replaceAll("\\s", "");
			else command = "skip";		
			if (board.getOccupier(highestAdj) == player.getId()) command += " " + GameData.COUNTRY_NAMES[highestAdj].replaceAll("\\s", "");
			else command = "skip";			
			if (board.getNumUnits(pos) > 1) command += " " + (board.getNumUnits(pos)-1); 
			else command = "skip";
			
			if(command.contains("skip"))command = "skip";

		}
		else command = "skip";

		return(command);

	}



}