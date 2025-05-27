package com.lds7871.flyaway.A_Contorller;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.lds7871.flyaway.B_Show.FirstPage;
import com.lds7871.flyaway.B_Show.FirstPage.EntityType;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import static com.lds7871.flyaway.A_Contorller.StageController.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;
import static com.lds7871.flyaway.A_Contorller.StageController.HEIGHT;
import static com.lds7871.flyaway.A_Contorller.StageController.WIDTH;
import static com.lds7871.flyaway.B_Show.FirstPage.*;

public class Main extends GameApplication {
    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    private FirstPage firstPage;
    private StageController controller;
    public static int 摧毁敌机架次=0;
    public static int 当前分数数值=0;
    public static boolean 无尽模式=false;

    public static void main(String[] args)
    {
        launch(args);
    }
    public static void startMain(String[] args)
    {
        System.out.println("=====================");
        System.out.println("==     挑战模式     ==");
        System.out.println("=====================");
        launch(args);

    }
    public static void startMain2(String[] args)
    {
        无尽模式=true;
        System.out.println("=====================");
        System.out.println("==     无尽模式     ==");
        System.out.println("=====================");
        launch(args);

    }


    @Override
    protected void initSettings(GameSettings settings)
    {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("LDS--FlyAway");
        if (无尽模式){settings.setVersion("无尽模式");}
        else settings.setVersion("挑战模式");
    }
    @Override
    protected void initGame()// 初始化
    {
        controller = new StageController();
        firstPage = new FirstPage(controller);
        FXGL.getGameScene().addUINode(firstPage);
    }
    @Override
    protected void initPhysics()//碰撞处理区
    {
        // ============碰撞处理设置============
        //-----自身子弹-----敌方飞机-----
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.敌机)
        {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity blackSquare) {
                当前分数数值+=20;
                firstPage.当前分数值显示();
                摧毁敌机架次++;
                大招充能进度++;
                if(目前双倍火力数值<双倍火力充能数量&& !双倍火力启用){目前双倍火力数值++;firstPage.双倍火力充能显示();}
                if (目前双倍火力数值==双倍火力充能数量){firstPage.双倍火力充能完毕显示();双倍火力充能启用=true;}
                if (摧毁敌机架次<大招充能数量){firstPage.大招充能进度显示();}
                if (摧毁敌机架次==大招充能数量){firstPage.大招充能进度完成显示();}
                firstPage.更新消灭敌机显示();
                System.out.println(YELLOW+"摧毁敌机架次:  "+摧毁敌机架次+RESET);
                bullet.removeFromWorld();
                firstPage.从活跃列表中移除敌机(blackSquare);
                // 修改敌机的图片
                Image 损毁敌机Image = new Image("file:Img/ep_2.png");
                ImageView 损毁敌机ImageView = new ImageView(损毁敌机Image);
                损毁敌机ImageView.setFitWidth(40);
                损毁敌机ImageView.setFitHeight(60);
                blackSquare.getViewComponent().clearChildren();
                blackSquare.getViewComponent().addChild(损毁敌机ImageView);

                // 延迟150毫秒后移除敌机
                FXGL.getGameTimer().runOnceAfter(() -> {
                    //blackSquare.removeFromWorld();
                    blackSquare.removeComponent(CollidableComponent.class);
                    Image 损毁敌机2Image = new Image("file:Img/ep_3.png");
                    ImageView 损毁敌机2ImageView = new ImageView(损毁敌机2Image);
                    损毁敌机2ImageView.setFitWidth(40);
                    损毁敌机2ImageView.setFitHeight(60);
                    blackSquare.getViewComponent().clearChildren();
                    blackSquare.getViewComponent().addChild(损毁敌机2ImageView);
                }, javafx.util.Duration.millis(150));

                FXGL.getGameTimer().runOnceAfter(blackSquare::removeFromWorld, javafx.util.Duration.millis(300));

            }
        });

        //-----自身飞机-----敌方子弹-----
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.飞机,EntityType.SQUARE)
        {
            @Override
            protected void onCollisionBegin(Entity player, Entity square) {
                System.out.println(RED+"检测到碰撞: 飞机 + 敌方子弹"+RESET);
                if (无尽模式){}
                else
                {
                    自身生命值--;
                    firstPage.生命值显示();
                }
                当前分数数值-=40;
                firstPage.当前分数值显示();
                firstPage.自身中弹音效();
                if (自身生命值==0)//生命值为0触发
                {
                    firstPage.生命值为0();

                    // 修改敌机的图片
                    Image 损毁Image = new Image("file:Img/plant_2.png");
                    ImageView 损毁ImageView = new ImageView(损毁Image);
                    损毁ImageView.setFitWidth(40);
                    损毁ImageView.setFitHeight(60);
                    player.getViewComponent().clearChildren();
                    player.getViewComponent().addChild(损毁ImageView);

                    FXGL.getGameTimer().runOnceAfter(() -> {
                        //blackSquare.removeFromWorld();
                        player.removeComponent(CollidableComponent.class);
                        Image 损毁2Image = new Image("file:Img/plant_3.png");
                        ImageView 损毁2ImageView = new ImageView(损毁2Image);
                        损毁2ImageView.setFitWidth(40);
                        损毁2ImageView.setFitHeight(60);
                        player.getViewComponent().clearChildren();
                        player.getViewComponent().addChild(损毁2ImageView);
                    }, javafx.util.Duration.millis(150));

                    FXGL.getGameTimer().runOnceAfter(player::removeFromWorld, javafx.util.Duration.millis(300));
                }
                // 移除子弹
                square.removeFromWorld();
            }
        });

        //-----自身子弹-----BOSS-----
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET,EntityType.BOSSP)
        {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity bossp) {
                System.out.println(YELLOW+"检测到碰撞: 子弹 + BOSS"+RESET);
                BOSS当前生命值--;
                bullet.removeFromWorld();
                System.out.println(BOSS当前生命值);
                if (BOSS当前生命值<=0)
                {
                    当前分数数值+=300;
                    firstPage.当前分数值显示();
                    // 修改BOSS的图片
                    bossp.removeComponent(CollidableComponent.class);
                    Image BOSS损毁Image = new Image("file:Img/BOSS2.png");
                    ImageView BOSS损毁ImageView = new ImageView(BOSS损毁Image);
                    bossp.getViewComponent().clearChildren();
                    bossp.getViewComponent().addChild(BOSS损毁ImageView);

                    FXGL.getGameTimer().runOnceAfter(() -> {
                        Image BOSS损毁2Image = new Image("file:Img/BOSS3.png");
                        ImageView BOSS损毁2ImageView = new ImageView(BOSS损毁2Image);
                        BOSS损毁2ImageView.setFitWidth(40);
                        BOSS损毁2ImageView.setFitHeight(60);
                        bossp.getViewComponent().clearChildren();
                        bossp.getViewComponent().addChild(BOSS损毁2ImageView);
                    }, javafx.util.Duration.millis(150));
                    boss子弹发射启用=false;
                    FXGL.getGameTimer().runOnceAfter(bossp::removeFromWorld, javafx.util.Duration.millis(300));
                    FXGL.getGameTimer().runOnceAfter(() -> BOSS当前生命值=BOSS生命值,javafx.util.Duration.millis(400));
                }

            }
        });

        //-----自身飞机-----Star-----
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.飞机,EntityType.STAR)
        {
            @Override
            protected void onCollisionBegin(Entity plant, Entity star) {
                System.out.println(GREEN+"检测到碰撞: 飞机 + STAR"+RESET);
                star.removeFromWorld();
                星星获取数量++;
                firstPage.星星数值更新显示();
                当前分数数值+=100;
                firstPage.当前分数值显示();
            }
        });

    }
    @Override
    protected void onUpdate(double tpf) {
        // 更新逻辑
    }


}