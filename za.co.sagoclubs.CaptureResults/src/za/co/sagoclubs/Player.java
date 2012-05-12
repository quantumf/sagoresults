package za.co.sagoclubs;

public class Player implements Comparable {
	
	private String id;
	private String name;
	
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
