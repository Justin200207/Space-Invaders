import java.awt.*;
public class Shield{
  int[][] y_pos = new int[4][4];
  int[][] x_pos = new int[4][4];
  int[][] health = new int[4][4];
  public Shield(int x, int y){
    for(int i = 0; i<4; i++){
      for(int o = 0; o<4; o++){
        x_pos[i][o] = x+(o*20);
        y_pos[i][o] = y+(i*20);
        //System.out.println("x "+x_pos[i][o]+" y "+y_pos[i][o]+" i "+i+" o "+o+" xO "+x+" yO "+y);
        health[i][o] = 3;
      }
    }
  }
  public int getX_pos(int i, int o){
    return x_pos[i][o];
  }
  public int getY_pos(int i,int o){
    return y_pos[i][o];
  }
  public int getHealth(int i, int o){
    return health[i][o];
  }
  public void hit(int i, int o){
    health[i][o]--;
  }
  public Rectangle getBounds(int i, int o){
    return new Rectangle(x_pos[i][o], y_pos[i][o], 20, 20);
  }
}