package cs601.webmail.entities;

public class User {
	private String name;
	private String pwd;
    private int id;
	public User(String name, String pwd) {
		this.name = name;
		this.pwd = pwd;
	}
	public String getName() { return name; }
	public String getPwd() { return pwd; }
	public String toString() { return name+":"+pwd; }
}
