package za.co.sagoclubs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import static za.co.sagoclubs.Constants.TAG;

public class InternetActions {

	private static Player[] playerData = null;
	private static Player[] playerRatingData = null;
	private static String username="";
	private static String password="";
	
	public static void setUsername(String name) {
        Log.d(TAG, "Set username to " +name);
		InternetActions.username = name;
		playerData = null;
	}
	
	public static void setPassword(String pw) {
        Log.d(TAG, "Set password to " + pw);
		InternetActions.password = pw;		
		playerData = null;
	}

	public static Player[] getPlayerArray() {
    	if (playerData!=null) {
    		return playerData;
    	}
    	ArrayList<Player> list = new ArrayList<Player>();
    	for (String item:getRawPlayerList()) {
    		if (item.startsWith("s/,")) {
	    		String[] parts = item.split(",");
	    		String id = parts[1];
	    		String name = parts[3];
	    		list.add(new Player(id, name));
    		}
    	}
    	
    	Player[] template = new Player[]{};
    	playerData = list.toArray(template);
    	Arrays.sort(playerData);
    	return playerData;
    }

	private static List<Player> getTestPlayers() {
		List<Player> list = new ArrayList<Player>();
		Player p = new Player("victor", "Victor Chow");p.setRank("7d");p.setIndex("345");
		list.add(p);
		p = new Player("bengo", "Ben Gale");p.setRank("3d");p.setIndex("34");
		list.add(p);
		p = new Player("andrew", "Andrew Davies");p.setRank("3d");p.setIndex("622");
		list.add(p);
		p = new Player("sam", "Sam Scott");p.setRank("2d");p.setIndex("876");
		list.add(p);
		p = new Player("andre", "Andre Connell");p.setRank("2d");p.setIndex("-80");
		list.add(p);
		p = new Player("chris", "Chris Welsh");p.setRank("1d");p.setIndex("219");
		list.add(p);
		p = new Player("lloyd", "Lloyd Rubidge");p.setRank("1k");p.setIndex("-322");
		list.add(p);
		p = new Player("francois", "Francois van Niekerk");p.setRank("4k");p.setIndex("-33");
		list.add(p);
		p = new Player("paul", "Paul Steyn");p.setRank("5k");p.setIndex("0");
		list.add(p);
		p = new Player("stephen", "Stephen Martindale");p.setRank("10k");p.setIndex("909");
		list.add(p);
		p = new Player("rory", "Rory Shea");p.setRank("15k");p.setIndex("-995");
		list.add(p);
		return list;
	}
	
	public static Player[] getPlayerRatingsArray() {
		return getPlayerRatingsArray(PlayerSortOrder.SORT_BY_NAME);
	}

	public static Player[] getPlayerRatingsArray(PlayerSortOrder order) {
    	if (playerRatingData!=null) {
    		return playerRatingData;
    	}
//    	List<Player> list = getTestPlayers();
    	List<Player> list = getRawPlayerRatingsList();
    	Player[] template = new Player[]{};
    	playerRatingData = list.toArray(template);
    	return playerRatingData;
    }

	public static Player[] getFavouritePlayers(SharedPreferences preferences) {
		String save = preferences.getString("favourite_players", "");
		List<String> items = Arrays.asList(save.split(","));
		Player[] allPlayers = getPlayerArray();
		List<Player> list = new ArrayList<Player>();
		for (Player player: allPlayers) {
			if (items.contains(player.getId())) {
				list.add(player);
			}
		}
    	Player[] template = new Player[]{};
    	Player[] result = list.toArray(template);
    	return result;
	}
	
    public static void executeUrl(String url) {
    	openConnection(url);
    }

    public static String openPage(String url) {
    	HttpURLConnection c = openConnection(url);
        BufferedReader reader = null;
        String result = ""; 
        try {
            reader = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"), 8192);
            for (String line; (line = reader.readLine()) != null;) {
            	result = result + line + "\n";
            }
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
        } catch(FileNotFoundException fnfe)
        {
        	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(null);

        	dlgAlert.setMessage("Unable to open connection to server. Please check the user name and password configured in Settings.");
        	dlgAlert.setTitle("Connection Failure");
        	dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
            c.disconnect();
        }
        return result;
    }

    public static String getPreBlock(String url) {
    	HttpURLConnection c = openConnection(url);
        BufferedReader reader = null;
        String result= "";
        StringBuffer work = new StringBuffer(); 
        try {
            reader = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"), 8192);
            for (String line; (line = reader.readLine()) != null;) {
            	work.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
            c.disconnect();
        }
        result = work.toString();
        int index = result.toLowerCase().indexOf("<pre>");
        if (index>0) {
        	result = result.substring(index+6);
        	index = result.toLowerCase().indexOf("</pre>");
            if (index>0) {
            	result = result.substring(0,index);
            }
        }
        return result;
    }
    
    private static HttpURLConnection openConnection(String url) {
        HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			Log.d(TAG, "openConnection: url="+url+", username="+username+", password="+password);
			String basicAuth = "Basic " + new String(Base64.encode((username+":"+password).getBytes(),Base64.NO_WRAP ));
			c.setRequestProperty ("Authorization", basicAuth);
			c.connect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
    }
    
    private static HttpURLConnection openUnsecuredConnection(String url) {
        HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.connect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
    }
    
    private static List<String> getRawPlayerList() {
        HttpURLConnection c = openConnection("http://rank.sagoclubs.co.za/sed_script");
        BufferedReader reader = null;
        ArrayList<String> list = new ArrayList<String>(); 
        try {
            reader = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"), 8192);
            for (String line; (line = reader.readLine()) != null;) {
            	list.add(line.trim());
            }
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
            c.disconnect();
        }
        return list;
    }
    
    private static List<Player> getRawPlayerRatingsList() {
        HttpURLConnection c = openUnsecuredConnection("http://www.sagoclubs.co.za/player-ratings/");
        BufferedReader reader = null;
    	List<Player> list = new ArrayList<Player>();
        try {
            reader = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"), 8192);
            for (String line; (line = reader.readLine()) != null;) {
            	if (line.startsWith("<td><a href='/wp-content/playerrank.php?id=")) {
            		Player player = getPlayerFromRatingLine(line);
                	player.setRank(stripTD(reader.readLine().trim()));
                	player.setIndex(stripTD(reader.readLine().trim()));
                	player.setLastPlayedDate(stripTD(reader.readLine().trim()));
                	list.add(player);
            	}
            }
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
            c.disconnect();
        }
    	return list;
    }
    
    private static String stripTD(String line) {
    	String result = line.substring(4, line.indexOf("</td>"));
    	return result;
    }
    
    private static Player getPlayerFromRatingLine(String line) {
    	String id = line.substring(43, line.indexOf("'",43));
    	int nameIndexStart = line.indexOf("<img src='/wp-content/archive.gif' alt='' /></a>", 43)+48;
    	String name = line.substring(nameIndexStart, line.indexOf("</td>", nameIndexStart));
    	Player result = new Player(id, name);
    	return result;
    }
    
}
