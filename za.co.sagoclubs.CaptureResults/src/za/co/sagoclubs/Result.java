package za.co.sagoclubs;

enum ResultState {
	Enter, Confirm, Undo, Complete;
}

public class Result {

	public static Player white;
	public static Player black;
	public static String weight;
	public static String result; //B, W, D
	public static String komi;
	public static String handicap; //minimum 1
	public static String day;
	public static String month;
	public static String year;
	public static String notes;

	public static ResultState resultState;
	
	public static Player logfile;
	
	public static void setWhite(Player white) {
		Result.white = white;
	}
	
	public static void setBlack(Player black) {
		Result.black = black;
	}
	
	public static void setLogFile(Player log) {
		Result.logfile = log;
	}

	public static void setKomi(String komi) {
		Result.komi = komi;
	}
	
	public static void setWeight(String weight) {
		Result.weight = weight;
	}
	
	public static void setResultState(ResultState r) {
		Result.resultState = r;
	}

	public static String constructResultUri() {
		String uri ="http://rank.sagoclubs.co.za/loggame.cgi?";
		uri = uri + "whitename="+Result.white.getId();
		uri = uri + "&blackname="+Result.black.getId();
		uri = uri + "&GSF="+Result.weight;
		uri = uri + "&result="+Result.result;
		uri = uri + "&komi="+Result.komi;
		uri = uri + "&handicap="+Result.handicap;
		uri = uri + "&day="+Result.day;
		uri = uri + "&month="+Result.month;
		uri = uri + "&year="+Result.year;
		uri = uri + "&notes="+Result.notes;
		return uri;
	}
	
	public static String constructUndoUri() {
		String uri ="http://rank.sagoclubs.co.za/undo.cgi?";
		uri += "a="+white.getId();
		uri += "&b="+black.getId();
		return uri;
	}
		
}
