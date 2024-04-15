package ro.ase.acs.classes;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class Utils {

	public static void matchDayReport(List<HandballMatch> matches, String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			for(HandballMatch match : matches) {
				bw.write(match.getHomeTeam() + " ");
				bw.write(Integer.toString(match.getGoalsHomeTeam()));
				bw.write(" - ");
				bw.write(Integer.toString(match.getGoalsAwayTeam()));
				bw.write(" " + match.getAwayTeam());
				bw.write(System.lineSeparator());
			} 
			bw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<HandballMatch> readFromCSV(String filename) {
		List<HandballMatch> matches = new ArrayList<>();
		try {
			FileInputStream fos = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fos);
			BufferedReader br = new BufferedReader(isr);

			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				HandballMatch matchRead = new HandballMatch(values[0].trim(), values[1].trim(),
						Integer.parseInt(values[2].trim()), Integer.parseInt(values[3].trim()));
				matches.add(matchRead);
				}
			br.close();
			//return matches;
			} catch (IOException e) {
				System.err.println("Eroare la citirea fișierului CSV: " + e.getMessage());
				e.printStackTrace();

			}
		return matches;
	}
	
	public static int secretInfo(String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
			DataInputStream dis = new DataInputStream(fis);
			dis.skipBytes(12);
			int secret = dis.readInt();
			dis.close();
			fis.close();
			return secret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	public static void serialize(List<HandballMatch> matches, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(matches.size());
			for(HandballMatch match : matches){
				dos.writeUTF(match.getHomeTeam());
				dos.writeUTF(match.getAwayTeam());
				dos.writeInt(match.getGoalsHomeTeam());
				dos.writeInt(match.getGoalsAwayTeam());
			}
			dos.close();
			fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
	
	public static List<HandballMatch> deserialize(String filename) {
		List<HandballMatch> list = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filename);
			DataInputStream dis = new DataInputStream(fis);
			int numberOfElements = dis.readInt();
			for(int i = 0; i < numberOfElements; i++){
				String home = dis.readUTF();
				String away = dis.readUTF();
				int goalsHome = dis.readInt();
				int goalsAway = dis.readInt();
				HandballMatch match = new HandballMatch(home, away, goalsHome, goalsAway);
				list.add(match);
			}
			dis.close();
			fis.close();

        } catch (IOException e) {
			throw new RuntimeException(e);
        }
		return list;
    }
	
	public static void writeHeader(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write("NO, TEAM, PTS, GF, GA, GD");
			bw.write(System.lineSeparator());
			bw.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	public static void writePoints(String filename, List<HandballMatch> matches) {
		Map<String, Integer> centralizator_puncte = new HashMap<String, Integer>();
		for(HandballMatch match : matches){
			centralizator_puncte.putIfAbsent(match.getHomeTeam(), 0);
			centralizator_puncte.putIfAbsent(match.getAwayTeam(), 0);
			//gazdele castiga
			if(match.getGoalsHomeTeam() > match.getGoalsAwayTeam()){
				centralizator_puncte.put(match.getHomeTeam(), centralizator_puncte.get(match.getHomeTeam()) + 3);
			}
			//egal
			else if (match.getGoalsHomeTeam() == match.getGoalsAwayTeam()){
				centralizator_puncte.put(match.getHomeTeam(), centralizator_puncte.get(match.getHomeTeam()) + 1);
				centralizator_puncte.put(match.getAwayTeam(), centralizator_puncte.get(match.getAwayTeam()) + 1);
			}
			//oaspetii castiga
			else {
				centralizator_puncte.put(match.getAwayTeam(), centralizator_puncte.get(match.getAwayTeam()) + 3);
			}
		}

		List<Map.Entry<String, Integer>> sortedEntries = centralizator_puncte.entrySet().stream().
				sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toList());


        try(FileOutputStream fos = new FileOutputStream(filename, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw)){

			int position = 1;
			for(Map.Entry<String, Integer> team : sortedEntries){
				bw.write(position + ", " + team.getKey() + ", " + team.getValue() + System.lineSeparator());
				position++;
			}
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
	
	public static void writePointsAndGoals(String filename, List<HandballMatch> matches) {
		Map<String, int[]> teamStats = new HashMap<>();

		for (HandballMatch match : matches) {
			teamStats.putIfAbsent(match.getHomeTeam(), new int[4]); // [Puncte, GF, GA, GD]
			teamStats.putIfAbsent(match.getAwayTeam(), new int[4]);

			int[] homeStats = teamStats.get(match.getHomeTeam());
			int[] awayStats = teamStats.get(match.getAwayTeam());

			homeStats[1] += match.getGoalsHomeTeam();
			homeStats[2] += match.getGoalsAwayTeam();
			awayStats[1] += match.getGoalsAwayTeam();
			awayStats[2] += match.getGoalsHomeTeam();

			if (match.getGoalsHomeTeam() > match.getGoalsAwayTeam()) {
				homeStats[0] += 3;
			} else if (match.getGoalsHomeTeam() < match.getGoalsAwayTeam()) {
				awayStats[0] += 3;
			} else {
				homeStats[0] += 1;
				awayStats[0] += 1;
			}

			homeStats[3] = homeStats[1] - homeStats[2];
			awayStats[3] = awayStats[1] - awayStats[2];
		}

		List<Map.Entry<String, int[]>> sortedTeams = new ArrayList<>(teamStats.entrySet());
		sortedTeams.sort((e1, e2) -> {
			int comparePoints = Integer.compare(e2.getValue()[0], e1.getValue()[0]);
			if (comparePoints == 0) {
				return Integer.compare(e2.getValue()[3], e1.getValue()[3]);
			}
			return comparePoints;
		});


		try (FileWriter writer = new FileWriter(filename)) {
			writer.write("TEAM, PTS, GF, GA, GD\n");
			for (Map.Entry<String, int[]> entry : sortedTeams) {
				int[] stats = entry.getValue();
				writer.write(String.format("%s, %d, %d, %d, %d\n", entry.getKey(), stats[0], stats[1], stats[2], stats[3]));
			}
		} catch (IOException e) {
			throw new RuntimeException("Eroare la scrierea în fișier: " + e.getMessage(), e);
		}
	}

	public static void leagueTable(String filename, List<HandballMatch> matches) {
		Map<String, int[]> teamStats = new HashMap<>();

		for (HandballMatch match : matches) {
			teamStats.putIfAbsent(match.getHomeTeam(), new int[4]); // [Puncte, GF, GA, GD]
			teamStats.putIfAbsent(match.getAwayTeam(), new int[4]);

			int[] homeStats = teamStats.get(match.getHomeTeam());
			int[] awayStats = teamStats.get(match.getAwayTeam());

			homeStats[1] += match.getGoalsHomeTeam();
			homeStats[2] += match.getGoalsAwayTeam();
			awayStats[1] += match.getGoalsAwayTeam();
			awayStats[2] += match.getGoalsHomeTeam();

			if (match.getGoalsHomeTeam() > match.getGoalsAwayTeam()) {
				homeStats[0] += 3;
			} else if (match.getGoalsHomeTeam() < match.getGoalsAwayTeam()) {
				awayStats[0] += 3;
			} else {
				homeStats[0] += 1;
				awayStats[0] += 1;
			}

			homeStats[3] = homeStats[1] - homeStats[2];
			awayStats[3] = awayStats[1] - awayStats[2];
		}

		List<Map.Entry<String, int[]>> sortedTeams = new ArrayList<>(teamStats.entrySet());
		sortedTeams.sort((e1, e2) -> {
			int comparePoints = Integer.compare(e2.getValue()[0], e1.getValue()[0]);
			if (comparePoints == 0) {
				return Integer.compare(e2.getValue()[3], e1.getValue()[3]);
			}

			return comparePoints;
		});


		try (FileWriter writer = new FileWriter(filename)) {
			for (Map.Entry<String, int[]> entry : sortedTeams) {
				int[] stats = entry.getValue();
				writer.write(String.format("%s, %d, %d, %d, %d\n", entry.getKey(), stats[0], stats[1], stats[2], stats[3]));
			}
		} catch (IOException e) {
			throw new RuntimeException("Eroare la scrierea în fișier: " + e.getMessage(), e);
		}
	}
	
	public static void specialLeagueTable(String filename, List<HandballMatch> matches) {
		Map<String, int[]> teamsScores = new HashMap<>();

		for(HandballMatch match: matches) {
			int[] homeStats = {0, 0, 0, 0};
			int[] awayStats = {0, 0, 0, 0};

			if(match.getGoalsHomeTeam() > match.getGoalsAwayTeam()){
				homeStats[0] += 3;
			}
			else if(match.getGoalsHomeTeam() < match.getGoalsAwayTeam()){
				awayStats[0] += 3;
			}
			else{
				homeStats[0] += 1;
				awayStats[0] += 1;
			}

			homeStats[1] += match.getGoalsHomeTeam();
			homeStats[2] += match.getGoalsAwayTeam();
			awayStats[1] += match.getGoalsAwayTeam();
			awayStats[2] += match.getGoalsHomeTeam();

			homeStats[3] = homeStats[1] - homeStats[2];
			awayStats[3] = awayStats[1] - awayStats[2];

			teamsScores.put(match.getHomeTeam(), homeStats);
			teamsScores.put(match.getAwayTeam(), awayStats);
		}

		List<Team> ranking = new ArrayList<>();
		for(String team : teamsScores.keySet()){
			int[] stats = teamsScores.get(team);
			Team rankingTeam = new Team(team, stats[0], stats[1], stats[2], stats[3]);
			ranking.add(rankingTeam);
		}

		List<Team> finalRanking = ranking.stream().sorted().collect(Collectors.toList());

        try(FileOutputStream fos = new FileOutputStream(filename);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw)){
			for(Team team : finalRanking){
				bw.write( team.getPosition() + ", " + team.getName() + ", " + team.getPoints() + ", " +
						team.getGF() + ", " + team.getGA() + ", " + team.getGD() + System.lineSeparator());
				team.setPosition();
			}
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
