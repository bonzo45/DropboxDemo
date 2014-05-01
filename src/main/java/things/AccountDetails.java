package things;

public class AccountDetails {

  private long id;
  private String name;
  private String country;
  
  public AccountDetails(long id, String name, String country) {
    this.id = id;
    this.name = name;
    this.country = country;
  }
  
  public String toJson() {
    return "{" +
        "\"id\": " + id + "," +
        "\"name\": " + name + "," +
        "\"country\": " + country +
        "}";
  }
  
}
