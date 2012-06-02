package za.co.sagoclubs;

public class Player implements Comparable {
	
	private String id;
	private String name;
	private String rank;
	private String index;
	private String lastPlayedDate;
	
	public Player(String id, String name) {
		setId(id);
		setName(name);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getLastPlayedDate() {
		return lastPlayedDate;
	}

	public void setLastPlayedDate(String lastPlayedDate) {
		this.lastPlayedDate = lastPlayedDate;
	}

	public String toString() {
    	return name;
    }

	@Override
	public int compareTo(Object arg0) {
		if (arg0 instanceof Player) {
			Player compare = (Player)arg0;
		    return this.name.compareTo(compare.name);
		}
		return 0;
	}
	
}
