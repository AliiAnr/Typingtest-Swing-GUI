public class UserScore {
   private String username;
   private int high_score;

   UserScore(String username, int high_score) {
      this.username = username;
      this.high_score = high_score;
   }
   
   public String getUsername() {
      return this.username;
   }
   
   public int getHighScore() {
      return this.high_score;
   }
   
   public void setUsername(String username) {
      this.username = username;
   }
   
   public void setHighScore(int high_score) {
      this.high_score = high_score;
   }
}
