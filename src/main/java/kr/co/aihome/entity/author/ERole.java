package kr.co.aihome.entity.author;

public enum ERole {
  ROLE_ADMIN("어드민"),
  ROLE_USER("사용자");
//  ROLE_MODERATOR

  private final String desc;
  ERole(String desc) {
    this.desc = desc;
  }
  public String getDesc() {
    return desc;
  }
}
