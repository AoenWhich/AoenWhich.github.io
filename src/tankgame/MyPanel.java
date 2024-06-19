package tankgame;

import sun.awt.image.ToolkitImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
//为了监听键盘事件，实现KeyListener
//为了让Panel不停的重绘子弹，需要将MyPanel实现Runable，当做一个线程使用
public class MyPanel extends JPanel implements KeyListener ,Runnable{
    //定义敌人坦克
    Vector<EnemyTank> enemyTanks = new Vector<>();
    int EnemyTankSize = 3;

    //定义我的坦克
    Hero hero = null;

    //定义一个Vector，用于存放炸弹
    Vector<Bomb> bombs = new Vector<>();
    //定义三张炸弹图片，显示爆炸效果
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;

    public MyPanel(){

        //设置我的坦克
        hero = new Hero(500,400);//初始坐标
        hero.setSpeed(3);//速度

        //设置敌人坦克
        for(int i = 0 ; i < EnemyTankSize ; i++){
            //创建一个敌人坦克
            EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);
            //设置方向
            enemyTank.setDirect(2);
            //启动敌人坦克线程
            new Thread(enemyTank).start();
            //给敌人坦克加子弹
            Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
            enemyTank.shots.add(shot);
            new Thread(shot).start();//启动线程
            //加入集合
            enemyTanks.add(enemyTank);
        }

        //初始化图片对象
        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //画出面板
        g.fillRect(0,0,1000,750);//填充矩形

        //画出自己坦克
        drawTank(hero.getX(),hero.getY(),g,hero.getDirect(),1);

        //画出自己坦克射击的子弹
        for (int i = 0; i < hero.shots.size(); i++) {
            Shot shot = hero.shots.get(i);
            if (shot != null && shot.isLive != false) {
                g.draw3DRect(shot.x, shot.y, 1, 1, false);
            }else {//如果该shot对象已经无效，就从shots集合中拿掉
                hero.shots.remove(shot);
            }
        }
        //如果bombs集合中有对象，就画出
        for (int i = 0; i < bombs.size(); i++) {
            //取出炸弹
            Bomb bomb = bombs.get(i);
            //根据当前bomb对象的life值画出相应的图片
            if (bomb.life > 6) {
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 3) {
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            //让炸弹生命值减少
            bomb.lifeDown();
            //如果bomb life为0，就从bombs集合中删除
            if (bomb.life == 0){
                bombs.remove(bomb);
            }
        }
        //画出敌人坦克,遍历Vector
        for (int i = 0 ; i < enemyTanks.size() ; i++){
            //从Vector中取出坦克
            EnemyTank enemyTank = enemyTanks.get(i);
            //判断当前坦克是否存活
            if (enemyTank.isLive) {//当敌人坦克存活，才画出坦克
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 0);
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    //取出子弹
                    Shot shot = enemyTank.shots.get(j);
                    //绘制
                    if (shot.isLive) {
                        g.draw3DRect(shot.x, shot.y, 1, 1, false);
                    } else {
                        enemyTank.shots.remove(shot);
                    }
                }
            }
        }


    }

    //画坦克的方法
    /**
     *
     * @param x 坦克的左上角X坐标
     * @param y 坦克的左上角Y坐标
     * @param g 画笔
     * @param direction 坦克方向（上下左右）
     * @param type 坦克类型
     */
    public void drawTank(int x, int y, Graphics g,int direction,int type){
        switch (type){
            case 0://敌方坦克
                g.setColor(Color.yellow);
                break;
            case 1://我方坦克
                g.setColor(Color.cyan);
                break;
        }

        switch (direction){
            case 0://表示向上
                g.fill3DRect(x,y,10,60,false);//坦克左轮子
                g.fill3DRect(x + 30,y,10,60,false);//坦克右轮子
                g.fill3DRect(x + 10,y + 10,20,40,false);//车身
                g.fillOval(x + 10,y + 20 ,20,20);
                g.drawLine(x + 20,y + 30,x + 20, y);
                break;
            case 1://表示向右
                g.fill3DRect(x,y,60,10,false);//坦克左轮子
                g.fill3DRect(x ,y + 30,60,10,false);//坦克右轮子
                g.fill3DRect(x + 10,y + 10,40,20,false);//车身
                g.fillOval(x + 20,y + 10 ,20,20);
                g.drawLine(x + 30,y + 20,x + 60, y + 20);
                break;
            case 2://表示向下
                g.fill3DRect(x,y,10,60,false);//坦克左轮子
                g.fill3DRect(x + 30,y,10,60,false);//坦克右轮子
                g.fill3DRect(x + 10,y + 10,20,40,false);//车身
                g.fillOval(x + 10,y + 20 ,20,20);
                g.drawLine(x + 20,y + 30,x + 20, y + 60);
                break;
            case 3://表示向左
                g.fill3DRect(x,y,60,10,false);//坦克左轮子
                g.fill3DRect(x ,y + 30,60,10,false);//坦克右轮子
                g.fill3DRect(x + 10,y + 10,40,20,false);//车身
                g.fillOval(x + 20,y + 10 ,20,20);
                g.drawLine(x + 30,y + 20,x , y + 20);
                break;
        }
    }

    //编写方法，判断我方子弹是否击中敌人坦克
    public void hitTank(Shot s,EnemyTank enemyTank){
        //判断s击中坦克
        switch (enemyTank.getDirect()){
            case 0:
            case 2:
                if (s.x > enemyTank.getX() && s.x < enemyTank.getX() + 40
                        && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 60){
                    s.isLive = false;
                    enemyTank.isLive = false;
                    //子弹击中敌人坦克后，enemyTank从集合中去掉
                    enemyTanks.remove(enemyTank);
                    //创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1:
            case 3:
                if (s.x > enemyTank.getX() && s.x < enemyTank.getX() + 60
                        && s.y > enemyTank.getY() && s.y < enemyTank.getY() + 40){
                    s.isLive = false;
                    enemyTank.isLive = false;
                    //子弹击中敌人坦克后，enemyTank从集合中去掉
                    enemyTanks.remove(enemyTank);
                    //创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W){
            hero.setDirect(0);
            if (hero.getY() > 0) {
                hero.moveup();
            }
        }else if (e.getKeyCode() == KeyEvent.VK_D){
            hero.setDirect(1);
            if (hero.getX() + 60 < 1000) {
                hero.moveRight();
            }
        }else if (e.getKeyCode() == KeyEvent.VK_S){
            hero.setDirect(2);
            if (hero.getY() + 60 < 750) {
                hero.moveDown();
            }
        }else if (e.getKeyCode() == KeyEvent.VK_A){
            hero.setDirect(3);
            if (hero.getX() > 0) {
                hero.moveLeft();
            }
        }
        //如果用户按下J键，发射子弹
        if (e.getKeyCode() == KeyEvent.VK_J){
            //判断hero的子弹是否消亡
//            if (hero.shot == null || !hero.shot.isLive) {
//                hero.shotEnemyTank();
//            }
            hero.shotEnemyTank();
        }
        //重绘面板
        this.repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {//每隔100毫秒刷新绘图区域

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //判断是否击中了坦克
            if (hero.shot != null && hero.shot.isLive){//当我的子弹存活
                //遍历敌人所有坦克
                for (int i = 0; i < enemyTanks.size(); i++){
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hitTank(hero.shot,enemyTank);
                }
            }
            this.repaint();
        }
    }
}
