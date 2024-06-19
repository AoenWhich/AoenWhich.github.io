package tankgame;

public class Shot implements Runnable {
    int x;//子弹的X坐标
    int y;//子弹的Y坐标
    int direct;//子弹的方向
    int speed = 2;//子弹速度
    boolean isLive = true;//子弹是否存活

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    @Override
    public void run() {
        while (true){
            //休眠50毫秒
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (direct){
                case 0://上
                    y -= speed;
                    break;
                case 1://右
                    x += speed;
                    break;
                case 2:
                    y += speed;
                    break;
                case 3:
                    x -= speed;
                    break;
            }
            //System.out.println("子弹X坐标：" + x + "子弹Y坐标：" + y);
            if (!(x >= 0 && x <= 1000 && y >= 0 && y <= 750 && isLive)){
                isLive = false;
                break;
            }
        }
    }
}
