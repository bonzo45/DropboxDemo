package model;

public class AccountMetadata {

  private long id;
  private String name;
  private String country;

  public AccountMetadata(long id, String name, String country) {
    this.id = id;
    this.name = name;
    this.country = country;
  }

  public String toJson() {
    return "{" +
        "\"id\": " + id + "," +
        "\"name\": " + "\"" + name + "\"" + "," +
        "\"country\": " + "\"" + country + "\"" +
        "}";
  }
}