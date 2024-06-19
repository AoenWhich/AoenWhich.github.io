package tankgame;

import java.util.Vector;

public class Hero extends Tank{

    //定义一个Shot对象，表示一个射击线程
    Shot shot = null;
    //使坦克可以发射多颗子弹
    Vector<Shot> shots = new Vector<>();

    public Hero(int x, int y) {
        super(x, y);
    }

    //射击
    public void shotEnemyTank(){

        if (shots.size() == 5){
            return;
        }
        switch (getDirect()){//hero的方向
            case 0://上
                shot = new Shot(getX() + 20,getY(),0);
                break;
            case 1://右
                shot = new Shot(getX() + 60,getY() + 20,1);
                break;
            case 2://下
                shot = new Shot(getX() + 20,getY() + 60,2);
                break;
            case 3://左
                shot = new Shot(getX(),getY() + 20,3);
                break;
        }
        //将新创建的shot放入到shots集合中
        shots.add(shot);
        //启动shot线程
        new Thread(shot).start();

    }
}


