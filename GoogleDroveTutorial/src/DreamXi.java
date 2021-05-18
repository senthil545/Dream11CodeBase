
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class DreamXi {
	static Map<String, Integer> playerPoints;
	static double topperPoints;
	static String topper;

	private static final int NON_TOPPERS_PER_ROW = 5;

	static StringBuilder result;

	public static void createFile(String result) {
		String path = "C:\\Users\\bommu\\Desktop\\IPL21Dream11\\TEst\\dream11.github.io\\index.html";
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

		String html = "<html><body><b><pre>" + result + "</pre></b>"
				+ "<br><br><br><font color= red><b>Last Updated at " + formatter.format(new Date())
				+ "</b></font></body></html>";
		try {
			FileWriter file = new FileWriter(path);
			file.write(html);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, Exception {
		executeDream11();
	}

	public static String executeDream11() throws Exception, InterruptedException {

		result = new StringBuilder();

		playerPoints = calculatePlayerPointsFromWeb();

		Map<String, DreamTeam> dreamTeams = fetchDreamXiTeams();
		Map<String, Double> finalPoints = new HashMap<String, Double>();
		for (String team : dreamTeams.keySet()) {
			DreamTeam teamsDomain = dreamTeams.get(team);

			finalPoints.put(team, teamsDomain.finalPoints);

		}

		finalPoints = DreamTeam.sortByValue(finalPoints);

		Map<String, DreamTeam> dreamTeamsSub = new LinkedHashMap<>(NON_TOPPERS_PER_ROW);
		Map<String, Double> finalPointsSub = new LinkedHashMap<>(NON_TOPPERS_PER_ROW);

		int count = 0;
		boolean isTopper = true;
		result.append("<h3>");
		displayOnPage("", "", 60, "", false);
		displayOnPage("--------------------", "", 20, "-", false);
		result.append("<br>");
		displayOnPage("", "", 60, "", false);
		displayOnPage("Dream11 Rankings", "", 20, "", false);
		result.append("<br>");
		displayOnPage("", "", 60, "", false);
		displayOnPage("--------------------", "", 20, "-", false);
		result.append("<br>");
		int i = 1;
		result = result.append("<font color=\"DodgerBlue\">");
		double topperTempPoint = 0.0;
		for (String key : finalPoints.keySet()) {

			displayOnPage("", "", 60, "", false);
			result.append(i++);
			displayOnPage(key, finalPoints.get(key).toString(), 19, "|", false);
			if (i == 2) {
				topperTempPoint = finalPoints.get(key);
				result = result.append("</font>");
			} else {
				result = result.append("<font color=\"Red\">");
				result = result.append("&#8595 " + (topperTempPoint - finalPoints.get(key)));
				result = result.append("</font>");
			}
			result.append("<br>");

		}
		displayOnPage("", "", 60, "", false);
		displayOnPage("--------------------", "", 20, "-", false);
		result.append("<br>");
		result.append("</h3>");
		result.append("<br>");
		result.append("<br>");
		result.append("<br>");
		result.append("<br>");

		for (String key : finalPoints.keySet()) {
			dreamTeamsSub.put(key, dreamTeams.get(key));
			finalPointsSub.put(key, finalPointsSub.get(key));
			count++;
			if (count == NON_TOPPERS_PER_ROW) {
				displayNonToppers(dreamTeamsSub, finalPointsSub, isTopper);
				dreamTeamsSub.clear();
				finalPointsSub.clear();
				isTopper = false;
				count = 0;
			}
		}

		if (count > 0) {
			displayNonToppers(dreamTeamsSub, finalPointsSub, isTopper);
			count = 0;
		}
		String bgImage = "<img src=\"*****\"  height=\30%\" width=\"30%\" >";
		bgImage = bgImage.replace("*****", topper + ".jpg");
		createFile(bgImage + result.toString()
				+ "<br>Note : Nagarjuna replaced Stokes with Maxwel, hence 171 points which is already scored by Maxwell will be subtracted and 12 points which is already scored by Stokes will be added later");

		return result.toString();

	}

	private static void displayNonToppers(Map<String, DreamTeam> dreamTeams, Map<String, Double> finalPoints,
			boolean isTopper) {
		int batBalKeepCount = 0;
		displayOnPage("", "", 42, "", false);
		for (String team : finalPoints.keySet()) {

			displayOnPage(team, "", 20, "", false);

		}
		result.append("<br>");
		displayOnPage("", "", 42, "", false);
		for (String team : finalPoints.keySet()) {

			displayOnPage("--------------------", "", 20, "-", false);
		}
		result.append("<br>");
		for (int i = 0; i < 17; i++) {
			displayOnPage("", "", 42, "", false);
			for (String team : finalPoints.keySet()) {
				DreamTeam teamsDomain = dreamTeams.get(team);
				String player = teamsDomain.allPlayers.get(i);
				Double playerPoint = teamsDomain.allPlayersPoints.get(i);
				if (i == 6 || i == 5 || i == 11 || i == 12 || i == 14 || i == 16) {
					result.append("<s><font color=red>");
				}
				displayOnPage(player, playerPoint.toString(), 20, "-", false);
				if (i == 6 || i == 5 || i == 11 || i == 12 || i == 14 || i == 16) {
					result.append("</font></s>");
				}
			}
			result.append("<br>");

			if (i == 6 || i == 14 || i == 12 || i == 16) {

				displayOnPage("", "", 42, "", false);
				result.append("<font color=blue>");
				displaySubTotal(dreamTeams, finalPoints, ++batBalKeepCount);
				result.append("</font>");
				result.append("<br>");
				result.append("<br>");
			}

		}
		displayOnPage("", "", 42, "", false);
		for (String team : dreamTeams.keySet()) {
			displayOnPage("--------------------", "", 20, "-", false);
		}
		result.append("<br>");
		result.append("<font size=3>");
		displayOnPage("", "", 42, "", false);
		for (String team : finalPoints.keySet()) {
			DreamTeam teamsDomain = dreamTeams.get(team);
			if (!isTopper) {
				double diff = topperPoints - teamsDomain.finalPoints;
				displayOnPage(Double.toString(teamsDomain.finalPoints), "", 20, "(-" + Double.toString(diff) + ")",
						false);

			} else {
				topperPoints = teamsDomain.finalPoints;
				topper = teamsDomain.teamOwner;
				displayOnPage(Double.toString(teamsDomain.finalPoints), "", 20, "[TOP]", false);
				isTopper = false;
			}

		}
		result.append("</font>");
		result.append("<br>");

		displayOnPage("", "", 42, "", false);

		for (String team : dreamTeams.keySet()) {
			displayOnPage("--------------------", "", 20, "-", false);
		}
		result.append("<br>");
		result.append("<br>");
		result.append("<br>");
	}

	private static void displaySubTotal(Map<String, DreamTeam> dreamTeams, Map<String, Double> finalPoints, int i) {

		for (String team : finalPoints.keySet()) {
			DreamTeam teamsDomain = dreamTeams.get(team);
			String playerPoint = null;
			if (i == 1)
				playerPoint = "Batsmans :" + teamsDomain.batsmanPoints;
			if (i == 2)
				playerPoint = "Bowlers :" + teamsDomain.bowlerPoints;
			if (i == 3)
				playerPoint = "Keeper :" + teamsDomain.keeperPoints;
			if (i == 4)
				playerPoint = "All-Rounder :" + teamsDomain.allRounderPoints;
			displayOnPage("", playerPoint, 20, "", true);
		}
	}

	private static void displayOnPage(String key, String value, int keyMaxSize, String delim, boolean onPrefix) {
		String keyValue = key + delim + value;
		String keyDisplay = "";
		if (keyValue.length() >= keyMaxSize) {
			keyValue = keyValue.substring(keyValue.length() - keyMaxSize);
		}
		keyDisplay = new String(new char[keyMaxSize - keyValue.length()]).replace('\0', ' ');
		if (onPrefix)
			result.append(keyValue + keyDisplay);
		else
			result.append(keyDisplay + keyValue);
		result.append("|");
	}

	private static Map<String, DreamTeam> fetchDreamXiTeams() throws FileNotFoundException, IOException {
		Map<String, DreamTeam> dreamTeamsMap = new HashMap<String, DreamTeam>();

		String dreamTeams = "DreamTeams";

		BufferedReader br = new BufferedReader(new FileReader(dreamTeams));

		String st;
		DreamTeam team = null;
		Map<String, Double> batsmans = null;
		Map<String, Double> bowlers = null;
		Map<String, Double> keepers = null;
		Map<String, Double> allRounders = null;

		int count = 0;
		while ((st = br.readLine()) != null) {

			if (st.contains("Team")) {
				team = new DreamTeam();
				dreamTeamsMap.put(st.replace(" Team", ""), team);
				batsmans = new HashMap<String, Double>(7);
				bowlers = new HashMap<String, Double>(6);
				keepers = new HashMap<String, Double>(2);
				allRounders = new HashMap<String, Double>(2);
				team.teamOwner = st.replace(" Team", "");
				team.batsmans = (HashMap<String, Double>) batsmans;
				team.bowlers = (HashMap<String, Double>) bowlers;
				team.keepers = (HashMap<String, Double>) keepers;
				team.allRounders = (HashMap<String, Double>) allRounders;
				count = 0;
			} else {
				if (!st.isEmpty()) {
					String[] player = st.trim().split("\t");
					double x2x = 1;
					String playerKey = player[0];
					if (player.length > 0) {
						if (player[player.length - 1].equals("C")) {
							x2x = 2;
							playerKey = player[0] + "[C]";
						} else if (player[player.length - 1].equals("V")) {
							x2x = 1.5;
							playerKey = player[0] + "[VC]";
						}
					}

					if (count < 7) {
						batsmans.put(playerKey, x2x * getOrDefault(playerPoints, player[0]));
						count++;
					} else if (count >= 7 && count < 13) {
						bowlers.put(playerKey, x2x * getOrDefault(playerPoints, player[0]));
						count++;
					} else if (count >= 13 && count < 15) {
						keepers.put(playerKey, x2x * getOrDefault(playerPoints, player[0]));
						count++;
					} else {
						allRounders.put(playerKey, x2x * getOrDefault(playerPoints, player[0]));
						count++;
						if (count > 16)
							team.calculateTotalPoints();
					}
				}
			}

		}
		return dreamTeamsMap;
	}

	static int getOrDefault(Map<String, Integer> playerPoints, String i) {
		if (playerPoints.get(i) == null)
			return 0;
		return playerPoints.get(i);
	}

	private static Map<String, Integer> calculatePlayerPointsFromWeb() throws IOException, InterruptedException {
		String batsmanFile = "https://stats.espncricinfo.com/ci/engine/records/averages/batting.html?id=13840;type=tournament";
		String bowlerFile = "https://stats.espncricinfo.com/ci/engine/records/averages/bowling.html?id=13840;type=tournament";
		// String keeperFile =
		// "https://stats.espncricinfo.com/ci/engine/records/keeping/most_dismissals_career.html?id=13840;type=tournament";
		// String fielderFile =
		// "https://stats.espncricinfo.com/ci/engine/records/fielding/most_catches_career.html?id=13840;type=tournament";
		String runOutFile = "RunOuts.txt";

		Map<String, Integer> playerStats = new LinkedHashMap<>();

		getStatsFromWeb(batsmanFile, playerStats, 5, 1);// runs
		getStatsFromWeb(batsmanFile, playerStats, 10, 16);// 100s
		getStatsFromWeb(batsmanFile, playerStats, 11, 8);// 50s
		getStatsFromWeb(bowlerFile, playerStats, 7, 25);// wickets
		getStatsFromWeb(bowlerFile, playerStats, 12, 8);// 4wcks
		getStatsFromWeb(bowlerFile, playerStats, 13, 16);// 8wcks
		getStatsFromWeb(bowlerFile, playerStats, 5, 12);// maidens
		getStatsFromWeb(bowlerFile, playerStats, 14, 8);// catches
		getStatsFromWeb(bowlerFile, playerStats, 15, 12);// stumpings
		getStats(runOutFile, playerStats, 1, 2, 6);// runouts

		return playerStats;
	}

	private static Boolean getStatsFromWeb(String urlLink, Map<String, Integer> playerStats, int index,
			int pointsPerIndex) throws IOException {

		URL url = new URL(urlLink);

		URLConnection con = url.openConnection();
		InputStream is = con.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = null;
		int count = 0;
		String player = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.contains("<td class=\"left\" nowrap=\"nowrap\"")
					|| line.contains("<td class=\"left\" title=\"record rank:")) {
				player = line.split(">")[2].replace("</a", "");
				count = 0;
			}
			count++;
			if (count == index && player != null) {
				String playerPoint = line.replace("<td nowrap=\"nowrap\">", "").replace("</td>", "").replace("<b>", "")
						.replace("</b>", "");
				if (!playerStats.containsKey(player)) {
					playerStats.put(player, isInt(playerPoint) ? Integer.parseInt(playerPoint) * pointsPerIndex : 0);
				} else {
					playerStats.put(player, playerStats.get(player)
							+ (isInt(playerPoint) ? Integer.parseInt(playerPoint) * pointsPerIndex : 0));
				}

				count++;
			}
		}
		return true;

	}

	private static void getStats(String urlLink, Map<String, Integer> playerStats, int position, int maxTabs,
			int pointsPerIndex) throws FileNotFoundException, IOException {

		File file = new File(urlLink);

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		while ((st = br.readLine()) != null) {
			String[] player = st.split("\t");
			if (player.length == maxTabs) {
				if (!playerStats.containsKey(player[0])) {
					playerStats.put(player[0],
							isInt(player[position]) ? Integer.parseInt(player[position]) * pointsPerIndex : 0);
				} else {
					playerStats.put(player[0], playerStats.get(player[0])
							+ (isInt(player[position]) ? Integer.parseInt(player[position]) * pointsPerIndex : 0));
				}
			}
		}
	}

	private static boolean isInt(String score) {
		try {
			Integer.parseInt(score);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
