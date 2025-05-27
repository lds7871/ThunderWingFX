package com.lds7871.flyaway.B_Show;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.lds7871.flyaway.A_Contorller.Main;
import com.lds7871.flyaway.C_LGUI.ConfigReader;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Modality;
import com.almasb.fxgl.physics.CollisionHandler;
import com.lds7871.flyaway.A_Contorller.StageController;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.lds7871.flyaway.A_Contorller.StageController.*;
import static com.lds7871.flyaway.A_Contorller.Main.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import static javafx.scene.paint.Color.GOLD;

//数据读取 自身射速
public class FirstPage extends Pane {

    public static int S持续时间=SS持续时间;
    //常量 变量
    private static boolean playerAlive = true;
    public static boolean boss子弹发射启用;
    public static boolean 双倍火力启用 = false;
    private boolean 方形物体生成器运行中 = false;
    public static boolean 双倍火力充能启用=false;
    private boolean isPaused = false;
    public static int 大招充能进度=-大招充能数量;
    private static int 大招释放判断=0;
    private long 双倍火力结束时间 = 0;
    public static int 目前双倍火力数值=0;
    public static int 星星获取数量=0;
    public List<Entity> 活跃敌机列表 = new ArrayList<>();
    private List<Timeline> activeTimelines = new ArrayList<>();
    private List<AnimationTimer> activeAnimationTimers = new ArrayList<>();
    private double[] 生成间隔 = {敌机初始生成间隔};
    //控件
    private ImageView 背景1;
    private ImageView 背景2;
    private ImageView 分割栏;
    private ImageView 挑战模式H1;
    private static ImageView goImageView;
    private static ImageView 爆炸ImageView;
    private static Text 消灭敌机显示;
    private static Text 大招充能显示;
    private static Text 大招充能文本;
    private static Text 当前分数文本;private static Text 当前分数显示;
    private static Text 双倍火力文本;private static Text 双倍火力显示;
    private static Text 生命值文本显示;private static Text 生命值数值显示;
    private static Text 星星文本显示;private static Text 星星数值显示;
    private static Text 时间显示;int S时间显示=S持续时间;
    private static Entity 飞机;
    private MediaPlayer 背景音乐播放器;
    private MediaPlayer 射击音效播放器;
    private static BooleanProperty isShooting = new SimpleBooleanProperty(false);
    private StageController controller;

    public enum EntityType {
        飞机, 敌机, BULLET,SQUARE,BOSSP,STAR
    }
//===========================           主函数              ============================
    public FirstPage(StageController controller){
        this.controller = controller;
        //创建物体
        {
            // 初始化背景图片
            {
                Image 背景1Image = new Image("file:Img/background.jpg");
                背景1 = new ImageView(背景1Image);
                this.getChildren().add(背景1);
                背景2 = new ImageView(背景1Image);
                this.getChildren().add(背景2);
            }
            //创建飞机
            {
                Image 飞机Image = new Image(飞机文件);
                ImageView 飞机ImageView = new ImageView(飞机Image);
                飞机ImageView.setFitWidth(40);
                飞机ImageView.setFitHeight(60);
                飞机 = FXGL.entityBuilder().at(220, 600)
                        .viewWithBBox(飞机ImageView)
                        .with(new CollidableComponent(true))
                        .type(EntityType.飞机)
                        .buildAndAttach();
                // 确保飞机显示在前面
                飞机.getViewComponent().getChildren().forEach(Node::toFront);
                this.getChildren().add(飞机.getViewComponent().getParent());
            }
            //创建分割栏
            {
                Image 分割栏Image = new Image("file:Img/分割栏.jpg");
                分割栏 = new ImageView(分割栏Image);
                分割栏.setFitHeight(800);
                this.getChildren().add(分割栏);
            }
            //创建H1
            {
                Image 挑战模式H1Image = new Image(模式标题);
                挑战模式H1 = new ImageView(挑战模式H1Image);
                this.getChildren().add(挑战模式H1);
            }
            //消灭敌机显示
            {
                消灭敌机显示 = new Text();
                消灭敌机显示.setText("消灭敌机: "+摧毁敌机架次);
                消灭敌机显示.setFont(Font.font("Noto Sans", FontWeight.BOLD, 30));
                this.getChildren().add(消灭敌机显示);
            }
            //大招充能文本
            {
                大招充能文本 = new Text();
                大招充能文本.setText("绝招充能进度: ");
                大招充能文本.setFont(Font.font("Noto Sans", FontWeight.BOLD, 30));
                this.getChildren().add(大招充能文本);
            }
            //大招充能显示
            {
                大招充能显示 = new Text();
                大招充能显示.setText("未充能");
                大招充能显示.setFont(Font.font("Noto Sans", FontWeight.BOLD, 30));
                this.getChildren().add(大招充能显示);
            }
            //当前分数文本
            {
                当前分数文本 = new Text();
                当前分数文本.setText("当前获得分数: ");
                当前分数文本.setFont(Font.font("Noto Sans", FontWeight.BOLD, 30));
                this.getChildren().add(当前分数文本);
            }
            //当前分数显示
            {
                当前分数显示 = new Text();
                当前分数显示.setText(当前分数数值+" 分");
                当前分数显示.setFont(Font.font("Noto Sans", FontWeight.BOLD, 30));
                this.getChildren().add(当前分数显示);
            }
            //双倍火力文本
            {
                双倍火力文本 = new Text();
                双倍火力文本.setText("双倍火力充能: ");
                双倍火力文本.setFont(Font.font("Noto Sans", FontWeight.BOLD, 30));
                this.getChildren().add(双倍火力文本);
            }
            //双倍火力显示
            {
                双倍火力显示 = new Text();
                双倍火力显示.setText(目前双倍火力数值+"/"+双倍火力充能数量);
                双倍火力显示.setFont(Font.font("Noto Sans", FontWeight.BOLD, 30));
                this.getChildren().add(双倍火力显示);
            }
            //生命值文本显示
            {
                生命值文本显示 = new Text();
                生命值文本显示.setText("♥");
                生命值文本显示.setFont(Font.font("Noto Sans", FontWeight.BOLD, 60));
                生命值文本显示.setFill(Color.RED);
                this.getChildren().add(生命值文本显示);
            }
            //生命值数值显示
            {
                生命值数值显示 = new Text();
                if (无尽模式) {生命值数值显示.setText("∞");}else
                {生命值数值显示.setText(String.valueOf(自身生命值));}
                生命值数值显示.setFont(Font.font("Noto Sans", FontWeight.BOLD, 40));
                this.getChildren().add(生命值数值显示);
            }
            //星星文本显示
            {
                星星文本显示 = new Text();
                星星文本显示.setText("✭");
                星星文本显示.setFont(Font.font("Noto Sans", FontWeight.BOLD, 60));
                星星文本显示.setFill(Color.GREEN);
                this.getChildren().add(星星文本显示);
            }
            //星星数值显示
            {
                星星数值显示 = new Text();
                星星数值显示.setText(String.valueOf(星星获取数量));
                星星数值显示.setFont(Font.font("Noto Sans", FontWeight.BOLD, 40));
                this.getChildren().add(星星数值显示);
            }
            //时间
            {
                时间显示 = new Text();
                时间显示.setText("时间: "+S时间显示);
                时间显示.setFont(Font.font("Noto Sans", FontWeight.BOLD, 35));
                if (!无尽模式)this.getChildren().add(时间显示);
            }
            //无尽模式关闭游戏窗口
            if (无尽模式)
            {
                Button 无尽Button = new Button("结束游戏");
                无尽Button.setPrefWidth(150);
                无尽Button.setPrefHeight(60);
                无尽Button.setLayoutX(570);
                无尽Button.setLayoutY(620);
                //Css
                {
                    无尽Button.setStyle(
                            "-fx-background-color: white;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-font-size: 20px;" +
                                    "-fx-border-radius: 10px;" +
                                    "-fx-border-color: blue;"
                    );
                    // 设置按钮按下事件
                    无尽Button.setOnMousePressed(event -> {
                        无尽Button.setStyle(
                                "-fx-background-color: white;" +
                                        "-fx-font-size: 22px;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-border-radius: 10px;" +
                                        "-fx-border-color: red;"
                        );
                    });

                    // 设置按钮弹起事件
                    无尽Button.setOnMouseReleased(event -> {
                        无尽Button.setStyle(
                                "-fx-background-color: white;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-font-size: 20px;" +
                                        "-fx-border-radius: 10px;" +
                                        "-fx-border-color: blue;"
                        );
                    });
                }
                this.getChildren().add(无尽Button);
                无尽Button.setOnAction(event ->
                {
                    成功音效();
                    //消除发射键
                    playerAlive = false;
                    isShooting.set(false);
                    FXGL.getGameTimer().clear();
                    FXGL.getGameTimer().runOnceAfter(this::显示Su图片, Duration.seconds(0));
                    FXGL.getGameTimer().runOnceAfter(this::成功小窗, Duration.seconds(3));
                });
            }
        }

        开始界面按钮位置总控();
        背景向下移动();
        音乐启动器();
        更新时间显示();

        飞机控制();//本体
        发射子弹();
        大招触发();
        设置双倍火力触发();

        //对方
        if (无尽模式){FXGL.getGameTimer().runAtInterval(this::生成boss, Duration.seconds(35));}
        else FXGL.getGameTimer().runAtInterval(this::生成boss, Duration.seconds(35),2);
        开始敌机生成();
        开始敌方子弹生成();
        FXGL.getGameTimer().runOnceAfter(this::开始奖励生成, Duration.seconds(15));

    }

    // ============碰撞处理设置============
          /*  已经移至Main.java  */
    //============开始界面按钮位置总控=============
    public void 开始界面按钮位置总控()
    {
        背景1.setLayoutX(0);//向右
        背景1.setLayoutY(0);//向下
        背景2.setLayoutX(0);
        背景2.setLayoutY(-790);//向下
        分割栏.setLayoutX(478);
        分割栏.setLayoutY(0);
        挑战模式H1.setX(550); // 设置X坐标
        挑战模式H1.setY(10); // 设置Y坐标
        消灭敌机显示.setX(570);
        消灭敌机显示.setY(200);
        大招充能文本.setX(550);大招充能显示.setX(600);
        大招充能文本.setY(300);大招充能显示.setY(350);
        当前分数文本.setX(550);当前分数显示.setX(550);
        当前分数文本.setY(550);当前分数显示.setY(600);
        双倍火力文本.setX(550);双倍火力显示.setX(600);
        双倍火力文本.setY(430);双倍火力显示.setY(480);
        生命值文本显示.setX(520);生命值数值显示.setX(570);
        生命值文本显示.setY(760);生命值数值显示.setY(760);
        星星文本显示.setX(600);星星数值显示.setX(660);
        星星文本显示.setY(760);星星数值显示.setY(760);
        时间显示.setX(520);时间显示.setY(680);
    }
    //============背景向下移动=============
    public void 背景向下移动()
    {
        TranslateTransition 向下匀速移动 = new TranslateTransition();
        TranslateTransition 向下匀速移动2 = new TranslateTransition();
        向下匀速移动.setNode(背景1);
        向下匀速移动2.setNode(背景2);
        向下匀速移动.setDuration(Duration.millis(50)); // 每50毫秒
        向下匀速移动2.setDuration(Duration.millis(50));
        向下匀速移动.setCycleCount(1);
        向下匀速移动2.setCycleCount(1);
        // 设置移动完成后的操作
        向下匀速移动.setOnFinished(event ->
        {
            if (背景1.getLayoutY() >= 790) {
                背景1.setLayoutY(-790);
            } // 重置位置
            else {
                背景1.setLayoutY(背景1.getLayoutY() + 2);
            }// 继续移动
            向下匀速移动.play();
        });
        向下匀速移动2.setOnFinished(event ->
        {
            if (背景2.getLayoutY() >= 790) {
                背景2.setLayoutY(-790);
            } // 重置位置
            else {
                背景2.setLayoutY(背景2.getLayoutY() + 2);
            }// 继续移动
            向下匀速移动2.play();
        });
        // 启动TranslateTransition
        向下匀速移动.play();
        向下匀速移动2.play();
    }
    //============移动区域和飞机控制==============
    private void 飞机控制()
    {
        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (移动区域(飞机.getX(), 飞机.getY() - 3)) {
                    飞机.translateY(-2);
                }
            }
        }, KeyCode.W);
        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (移动区域(飞机.getX(), 飞机.getY() + 3)) {
                    飞机.translateY(2);
                }
            }
        }, KeyCode.S);
        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (移动区域(飞机.getX() - 3, 飞机.getY())) {
                    飞机.translateX(-2);
                }
            }
        }, KeyCode.A);
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (移动区域(飞机.getX() + 3, 飞机.getY())) {
                    飞机.translateX(2);
                }
            }
        }, KeyCode.D);
    }
    private boolean 移动区域(double x, double y)
    {
        return (x >= 0 && x <= 440) && (y >= 100 && y <= 750);
    }
    //============射出子弹====================
    private void 发射子弹()
    {
        FXGL.getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {
                if (playerAlive) {
                    isShooting.set(true);
                    startShooting();
                    播放射击音效();
                }
            }
            @Override
            protected void onActionEnd() {
                isShooting.set(false);
                停止射击音效();
            }
        }, KeyCode.J);

    }
    private  void startShooting()
    {
        ReadOnlyBooleanProperty shootingCondition = isShooting;
        FXGL.getGameTimer().runAtIntervalWhile(() -> {
            if (isShooting.get()) {
                shootBullet();
            }
        }, Duration.millis(自身射速), shootingCondition);
    }
    private  void shootBullet()
    {
        if (双倍火力启用) {
            // 左侧子弹
            双倍火力(5);
            // 右侧子弹
            双倍火力(20);
        } else {
            // 原有的单发子弹逻辑
            Image 子弹Image = new Image("file:Img/B_1.png");
            ImageView 子弹ImageView = new ImageView(子弹Image);
            子弹ImageView.setFitWidth(15);
            子弹ImageView.setFitHeight(30);

            Entity bullet = FXGL.entityBuilder()
                    .at(飞机.getX() + 12, 飞机.getY() - 10)
                    .viewWithBBox(子弹ImageView)
                    .with(new CollidableComponent(true))
                    .type(EntityType.BULLET)
                    .buildAndAttach();

            this.getChildren().add(bullet.getViewComponent().getParent());

            // 子弹移动
            FXGL.getGameTimer().runAtInterval(() -> {
                bullet.translateY(-5);
                if (bullet.getY() <= 10) {
                    bullet.removeFromWorld();
                }
            }, Duration.millis(10));
        }
    }
    //==========生成敌机=====================
    public void 生成敌机()
    {
        Random random = new Random();
        int x = (int) (random.nextDouble() * 440);
        int y = -50;

        Image ep1Image = new Image("file:Img/ep_1.png");
        ImageView ep1ImageView = new ImageView(ep1Image);
        ep1ImageView.setFitWidth(40);
        ep1ImageView.setFitHeight(60);

        Entity 敌机 = FXGL.entityBuilder()
                .at(x, y)
                .viewWithBBox(ep1ImageView) // 使用viewWithBBox自动创建碰撞边界
                .with(new CollidableComponent(true))
                .type(EntityType.敌机)
                .buildAndAttach();

        活跃敌机列表.add(敌机);//添加到了列表
        this.getChildren().add(敌机.getViewComponent().getParent());
        // 添加敌机移动逻辑
        FXGL.getGameTimer().runAtInterval(() -> {
            敌机.translateY(敌机移动速度); // 使用translate而非setY以确保碰撞盒也移动

            // 如果敌机飞出屏幕底部，移除它
            if (敌机.getY() >= 800) {
                活跃敌机列表.remove(敌机);
                敌机.removeFromWorld();
            }
        }, Duration.millis(40));
    }
    public void 开始敌机生成()
    {

        final long 开始时间 = System.currentTimeMillis();
        final long 持续时间 = S持续时间* 1000L-10000; // 2分钟（毫秒）
        // 延迟5秒后开始生成敌机
        FXGL.getGameTimer().runOnceAfter(() -> {
            生成敌机计时器(生成间隔, 开始时间, 持续时间);// 创建一个递归调用的方法，用于按照不断缩短的时间间隔生成敌机
        }, Duration.seconds(10));
    }
    private void 生成敌机计时器(double[] 生成间隔, long 开始时间, long 持续时间)
    {
        long 当前时间 = System.currentTimeMillis();
        // 检查是否超过了2分钟限制
        if (当前时间 - 开始时间 < 持续时间)
        {
            生成敌机();
            生成间隔[0] = Math.max(敌机最小生成间隔, 生成间隔[0] - 30);
                                                                  System.out.println("生成间隔  "+生成间隔[0]);
            // 调整下一次生成的时间间隔
            FXGL.getGameTimer().runOnceAfter(() -> {
                生成敌机计时器(生成间隔, 开始时间, 持续时间);
            }, Duration.millis(生成间隔[0]));
        } else {
            // 两分钟结束，停止生成
            System.out.println("两分钟敌机生成已结束");
        }
    }
    public void 从活跃列表中移除敌机(Entity 敌机) {
        活跃敌机列表.remove(敌机);
        爆炸音效();
    }
    //===========生成BOSS=======================
    public void 生成boss()
    {
        Image BOSSImage = new Image("file:Img/BOSS.png");
        ImageView BOSSImageView = new ImageView(BOSSImage);

        Entity BOSS = FXGL.entityBuilder()
                .at(-200, 100)
                .viewWithBBox(BOSSImageView) // 使用viewWithBBox自动创建碰撞边界
                .with(new CollidableComponent(true))
                .type(EntityType.BOSSP)
                .buildAndAttach();

        final boolean[] movingRight = {true};// 标记移动方向
        final boolean[] enteredMainArea = {false};  // 标记是否已进入主要活动区域
        boss子弹发射启用 = true;
        //左右移动
        FXGL.getGameTimer().runAtInterval(() -> {
            double currentX = BOSS.getX();
                    if (!enteredMainArea[0])
                    {
                        BOSS.translateX(BOSS移速);
                        if (currentX >= 0)
                        {enteredMainArea[0] = true;}
                    }
                    else
                    {
                        if (currentX >= 270) {movingRight[0] = false;}
                        if (movingRight[0]) {BOSS.translateX(BOSS移速);}
                        else {BOSS.translateX(-BOSS移速);}
                    }
        }, Duration.millis(40));
        // BOSS发射子弹
        FXGL.getGameTimer().runAtInterval(() -> {
            if (boss子弹发射启用) { // 检查是否启用子弹发射
                double currentX = BOSS.getX();
                BOSS子弹(currentX-30);
                BOSS子弹(currentX+30);
            }
        }, Duration.millis(BOSS射击间隔));

        this.getChildren().add(BOSS.getViewComponent().getParent());
    }
    public void BOSS子弹(double currentX)
    {
        Image 敌方子弹Image = new Image("file:Img/B_2.png");
        ImageView  敌方子弹ImageView = new ImageView( 敌方子弹Image);

        // 创建方形物体实体
        Entity 敌方子弹 = FXGL.entityBuilder()
                .at(currentX+100, 170)
                .viewWithBBox( 敌方子弹ImageView)
                .with(new CollidableComponent(true))
                .type(EntityType.SQUARE)
                .buildAndAttach();
        this.getChildren().add(敌方子弹.getViewComponent().getParent());
        // 方形物体向下移动逻辑
        FXGL.getGameTimer().runAtInterval(() ->
        {
            敌方子弹.translateY(敌机子弹飞行速度);//子弹移速
            // 如果方形物体飞出屏幕底部，移除它
            if (敌方子弹.getY() >= 800)
            {
                敌方子弹.removeFromWorld();
            }
        }, Duration.millis(30));
    }
    //===========生成敌方子弹=======================
    public void 开始敌方子弹生成()
    {
        if (方形物体生成器运行中) return; // 防止多次启动
        方形物体生成器运行中 = true;
        // 响应时间生成一个方形物体
        FXGL.getGameTimer().runAtInterval(this::生成敌方子弹, Duration.millis(Math.max(生成间隔[0]+1500,500)));

    }
    public void 生成敌方子弹()
    {
        if (活跃敌机列表.isEmpty()) {return;}

        for (Entity 敌机 : new ArrayList<>(活跃敌机列表)) {

            double x = 敌机.getX();
            double y = 敌机.getY();

            Image 敌方子弹Image = new Image("file:Img/B_2.png");
            ImageView  敌方子弹ImageView = new ImageView( 敌方子弹Image);
//            敌方子弹ImageView.setFitWidth(15);
//            敌方子弹ImageView.setFitHeight(30);
        // 创建方形物体实体
        Entity 敌方子弹 = FXGL.entityBuilder()
                .at(x+15, y+40)
                .viewWithBBox( 敌方子弹ImageView)
                .with(new CollidableComponent(true))
                .type(EntityType.SQUARE)
                .buildAndAttach();
        this.getChildren().add(敌方子弹.getViewComponent().getParent());
        // 方形物体向下移动逻辑
        FXGL.getGameTimer().runAtInterval(() ->
        {
            敌方子弹.translateY(敌机子弹飞行速度);//子弹移速
            // 如果方形物体飞出屏幕底部，移除它
            if (敌方子弹.getY() >= 800)
            {
                敌方子弹.removeFromWorld();
            }
        }, Duration.millis(30));
    }
    }
    //============生成奖励=========================
    public void 生成加分奖励()
    {
        Random random = new Random();
        int x = (int) (random.nextDouble() * 440);
        int y = -50;

        Image StarImage = new Image("file:Img/Star.png");
        ImageView StarImageView = new ImageView(StarImage);
        StarImageView.setFitWidth(30);
        StarImageView.setFitHeight(30);

        Entity 星星 = FXGL.entityBuilder()
                .at(x, y)
                .viewWithBBox(StarImageView) // 使用viewWithBBox自动创建碰撞边界
                .with(new CollidableComponent(true))
                .type(EntityType.STAR)
                .buildAndAttach();

        this.getChildren().add(星星.getViewComponent().getParent());

        FXGL.getGameTimer().runAtInterval(() -> {
            星星.translateY(奖励移动速度);
            if (星星.getY() >= 800) {
                星星.removeFromWorld();
            }
        }, Duration.millis(30));
    }
    public void 开始奖励生成() {
        final long 开始时间 = System.currentTimeMillis();
        final long 持续时间 = 11 * 10 * 1000; // 110（秒）
        final long 奖励生成间隔this = 奖励生成间隔 * 1000L;
        奖励生成计时器(开始时间, 持续时间, 奖励生成间隔this);
    }
    private void 奖励生成计时器(long 开始时间, long 持续时间, long 奖励生成间隔this)
    {
        long 当前时间 = System.currentTimeMillis();

        if (当前时间 - 开始时间 < 持续时间) {
            生成加分奖励();
            System.out.println("生成一个奖励星星");
            FXGL.getGameTimer().runOnceAfter(() -> {
                奖励生成计时器(开始时间, 持续时间, 奖励生成间隔this);
            }, Duration.millis(奖励生成间隔this));
        }
    }
    //============双倍火力=========================
    public void 双倍火力(int Xset)
    {
        Image 子弹Image = new Image("file:Img/B_1.png");
        ImageView 子弹ImageView = new ImageView(子弹Image);
        子弹ImageView.setFitWidth(15);
        子弹ImageView.setFitHeight(30);

        Entity bullet = FXGL.entityBuilder()
                .at(飞机.getX() + Xset, 飞机.getY() - 10)
                .viewWithBBox(子弹ImageView) // 使用viewWithBBox自动创建碰撞边界
                .with(new CollidableComponent(true))
                .type(EntityType.BULLET)
                .buildAndAttach();
        this.getChildren().add(bullet.getViewComponent().getParent());
        // 子弹移动
        FXGL.getGameTimer().runAtInterval(() -> {
            bullet.translateY(-5); // 使用translate而非setY以确保碰撞盒也移动
            if (bullet.getY() <= 10) {
                bullet.removeFromWorld();
            }
        }, Duration.millis(10));
    }
    private void 设置双倍火力触发()
    {
        FXGL.getInput().addAction(new UserAction("Double Firepower") {
            @Override
            protected void onActionBegin() {
                if (playerAlive&&双倍火力充能启用)
                {
                    双倍火力启用 = true;
                    双倍火力结束时间 = System.currentTimeMillis() + (双倍火力持续时间*1000);
                    双倍火力充能启用=false;
                    目前双倍火力数值=0;
                    双倍火力释放显示();
                    技能释放音效();
                }else {拒绝音效();}
            }
        }, KeyCode.K);

        // Check timer to disable double firepower after 5 seconds
        FXGL.getGameTimer().runAtInterval(() -> {
            if (双倍火力启用 && System.currentTimeMillis() >= 双倍火力结束时间) {
                双倍火力启用 = false;
                目前双倍火力数值=0;
            }
        }, Duration.millis(100));
    }
    //==============大招释放====================
    public void 移除所有敌机并添加爆炸效果()
    {
        for (Entity 敌机 : new ArrayList<>(活跃敌机列表)) {
            敌机.removeFromWorld();
            摧毁敌机架次++;
        }
        Image 爆炸Image = new Image("file:Img/plant_2.png");
        爆炸ImageView = new ImageView(爆炸Image);
        爆炸ImageView.setFitWidth(700);
        爆炸ImageView.setFitHeight(800);
        爆炸ImageView.setX(-150);
        爆炸ImageView.setY(0);
        this.getChildren().add(爆炸ImageView);
        FXGL.getGameTimer().runOnceAfter(() -> {
            // 创建第二张爆炸图片
            Image 爆炸Image2 = new Image("file:Img/plant_3.png");
            爆炸ImageView.setImage(爆炸Image2);
        }, Duration.seconds(0.3));
        // 设置爆炸效果在屏幕上显示的时间
        FXGL.getGameTimer().runOnceAfter(() -> {
            this.getChildren().remove(爆炸ImageView);
        }, Duration.seconds(0.6));
        活跃敌机列表.clear();
        更新消灭敌机显示();
    }
    private void 大招触发()
    {
        FXGL.getInput().addAction(new UserAction("Remove All Enemies") {
            @Override
            protected void onActionBegin() {
                大招音效();
                // 检查是否已经达到摧毁敌机的条件
                if (大招充能进度 >= 0&&大招释放判断==0)
                {
                    大招释放判断++;
                    大招充能进度-=大招充能数量;
                    大招充能显示.setText("已释放");
                    大招充能显示.setFill(Color.ORANGE);
                    移除所有敌机并添加爆炸效果();
                }else {拒绝音效();}
            }
        }, KeyCode.L);
    }
    //==============生命值耗尽====================
    public  void 生命值为0()
    {
        失败音效();
        //消除发射键
        playerAlive = false;
        isShooting.set(false);
        //战斗结束图片
        FXGL.getGameTimer().runOnceAfter(this::显示GO图片, Duration.seconds(1));
        //关闭音乐
        if (背景音乐播放器 != null) {背景音乐播放器.stop();}
        //
        结束小窗();
    }
    private void 显示GO图片()
    {
        背景音乐播放器.stop();
        Image goImage = new Image("file:Img/GO.png");
        goImageView = new ImageView(goImage);
        goImageView.setX((100- goImageView.getFitWidth()) / 2 );
        goImageView.setY((600 - goImageView.getFitHeight()) / 2);
        this.getChildren().add(goImageView);
        goImageView.toFront();
    }
    private void 显示Su图片()
    {
        背景音乐播放器.stop();
        Image goImage = new Image("file:Img/Success.png");
        goImageView = new ImageView(goImage);
        goImageView.setX((100- goImageView.getFitWidth()) / 2 );
        goImageView.setY((600 - goImageView.getFitHeight()) / 2);
        this.getChildren().add(goImageView);
        goImageView.toFront();
    }

    //===============右侧信息栏更新===================
    public void 更新消灭敌机显示() {消灭敌机显示.setText("消灭敌机: "+摧毁敌机架次);}
    public void 大招充能进度显示() {大招充能显示.setText(摧毁敌机架次+"/"+大招充能数量);}
    public void 大招充能进度完成显示() {大招充能显示.setText("已就绪");大招充能显示.setFill(Color.GREEN);}
    public void 当前分数值显示() {当前分数显示.setText(当前分数数值+" 分");当前分数显示.setFill(Color.GRAY);}
    public void 双倍火力充能显示(){双倍火力显示.setText(目前双倍火力数值+"/"+双倍火力充能数量);双倍火力显示.setFill(Color.GRAY);}
    public void 双倍火力充能完毕显示(){双倍火力显示.setText("充能完毕");双倍火力显示.setFill(Color.GREEN);}
    public void 双倍火力释放显示(){双倍火力显示.setText("使用中");双倍火力显示.setFill(Color.DARKRED);}
    public void 生命值显示(){ 生命值数值显示.setText(String.valueOf(自身生命值));}
    public void 星星数值更新显示(){ 星星数值显示.setText(String.valueOf(星星获取数量));}
    private void 更新时间显示() {
        FXGL.getGameTimer().runAtInterval(() ->
        {
            if (S时间显示 > 0)
            {
                S时间显示--;
                时间显示.setText("时间: " + S时间显示);
            }
            if(S时间显示 ==0)
            {
                成功音效();
                //消除发射键
                playerAlive = false;
                isShooting.set(false);
                FXGL.getGameTimer().clear();
                FXGL.getGameTimer().runOnceAfter(this::显示Su图片, Duration.seconds(0));
                FXGL.getGameTimer().runOnceAfter(this::成功小窗, Duration.seconds(3));
            }
        }, Duration.seconds(1));
    }
    //=================声音效果====================
    private void 音乐启动器()
    {
        try {
            // 调整音乐文件路径
            String musicPath = 音乐文件;
            Media backgroundMusic = new Media(new File(musicPath).toURI().toString());// 创建媒体对象
            背景音乐播放器 = new MediaPlayer(backgroundMusic);// 创建媒体播放器
            背景音乐播放器.setCycleCount(MediaPlayer.INDEFINITE); // 设置音乐无限循环
            背景音乐播放器.setVolume(0.05);// 设置音量（0.0 到 1.0 之间）
            if (背景音乐==2){背景音乐播放器.setVolume(0.02);}
            背景音乐播放器.play();// 开始播放音乐
        } catch (Exception e) {System.out.println("加载背景音乐时出错: " + e.getMessage());}
    }
    public void 爆炸音效()
    {
        try {
            String explosionSoundPath = "Msc/Boom.mp3";
            Media explosionSound = new Media(new File(explosionSoundPath).toURI().toString());
            MediaPlayer explosionPlayer = new MediaPlayer(explosionSound);
            explosionPlayer.setVolume(0.05);
            explosionPlayer.play();
        } catch (Exception e) {
            System.out.println("加载爆炸音效时出错: " + e.getMessage());
        }
    }
    public void 大招音效()
    {
        try {
            String explosionSoundPath = "Msc/大爆炸.mp3";
            Media explosionSound = new Media(new File(explosionSoundPath).toURI().toString());
            MediaPlayer explosionPlayer = new MediaPlayer(explosionSound);
            explosionPlayer.setVolume(0.05);
            explosionPlayer.play();
        } catch (Exception e) {
            System.out.println("加载爆炸音效时出错: " + e.getMessage());
        }
    }
    public void 自身中弹音效()
    {
        try {
            String explosionSoundPath = "Msc/自身被击中.mp3";
            Media explosionSound = new Media(new File(explosionSoundPath).toURI().toString());
            MediaPlayer explosionPlayer = new MediaPlayer(explosionSound);
            explosionPlayer.setVolume(0.05);
            explosionPlayer.play();
        } catch (Exception e) {
            System.out.println("加载爆炸音效时出错: " + e.getMessage());
        }
    }
    public void 技能释放音效()
    {
        try {
            String explosionSoundPath = "Msc/释放.mp3";
            Media explosionSound = new Media(new File(explosionSoundPath).toURI().toString());
            MediaPlayer explosionPlayer = new MediaPlayer(explosionSound);
            explosionPlayer.setVolume(0.8);
            explosionPlayer.play();
        } catch (Exception e) {
            System.out.println("加载技能释放音效时出错: " + e.getMessage());
        }
    }
    private void 播放射击音效()
    {

        try {
            String 射击音效路径 = "Msc/laser.mp3";
            Media 射击音效 = new Media(new File(射击音效路径).toURI().toString());
            射击音效播放器 = new MediaPlayer(射击音效);
            射击音效播放器.setCycleCount(MediaPlayer.INDEFINITE);
            射击音效播放器.setVolume(0.05);
            射击音效播放器.play();
        } catch (Exception e)
        {
            System.out.println("加载射击音效时出错: " + e.getMessage());
        }
    }
    private void 停止射击音效()
    {
        if (射击音效播放器 != null) {
            射击音效播放器.stop();
        }
    }
    public void 失败音效()
    {
        try {
            String explosionSoundPath = "Msc/失败.mp3";
            Media explosionSound = new Media(new File(explosionSoundPath).toURI().toString());
            MediaPlayer explosionPlayer = new MediaPlayer(explosionSound);
            explosionPlayer.setVolume(0.05);
            explosionPlayer.play();
        } catch (Exception e) {
            System.out.println("加载爆炸音效时出错: " + e.getMessage());
        }
    }
    public void 成功音效()
    {
        try {
            String explosionSoundPath = "Msc/成功.mp3";
            Media explosionSound = new Media(new File(explosionSoundPath).toURI().toString());
            MediaPlayer explosionPlayer = new MediaPlayer(explosionSound);
            explosionPlayer.setVolume(0.05);
            explosionPlayer.play();
        } catch (Exception e) {
            System.out.println("加载爆炸音效时出错: " + e.getMessage());
        }
    }
    public void 拒绝音效()
    {
        try {
            String explosionSoundPath = "Msc/wrong-47985.mp3";
            Media explosionSound = new Media(new File(explosionSoundPath).toURI().toString());
            MediaPlayer explosionPlayer = new MediaPlayer(explosionSound);
            explosionPlayer.setVolume(0.05);
            explosionPlayer.play();
        } catch (Exception e) {
            System.out.println("加载爆炸音效时出错: " + e.getMessage());
        }
    }
    //=================额外窗口====================
    public void 结束小窗()
    {
        Platform.runLater(() -> {
            Stage gameOverStage = new Stage();
            gameOverStage.initStyle(StageStyle.UNDECORATED);
            gameOverStage.initModality(Modality.APPLICATION_MODAL);
            gameOverStage.setWidth(200);
            gameOverStage.setHeight(100);

            Text gameOverText = new Text("分数: "+当前分数数值);
            gameOverText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Button exitButton = new Button("关闭程序");
            exitButton.setOnAction(e -> {
                gameOverStage.close();
                Platform.exit();
            });

            VBox layout = new VBox(10);
            layout.setAlignment(Pos.CENTER);
            layout.getChildren().addAll(gameOverText, exitButton);
            Scene scene = new Scene(layout, 200, 100);
            gameOverStage.setScene(scene);
            // Center the window on the screen
            gameOverStage.centerOnScreen();
            gameOverStage.show();
        });
    }
    public void 成功小窗()
    {

        Platform.runLater(() -> {
            Stage gameOverStage = new Stage();
            gameOverStage.initStyle(StageStyle.UNDECORATED);
            gameOverStage.initModality(Modality.APPLICATION_MODAL);
            gameOverStage.setWidth(200);
            gameOverStage.setHeight(100);

            Text gameOverText1 = new Text("游戏结束");
            gameOverText1.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Text gameOverText = new Text("分数: "+当前分数数值);
            gameOverText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Button exitButton = new Button("关闭程序");
            exitButton.setOnAction(e -> {
                gameOverStage.close();
                Platform.exit();
            });

            VBox layout = new VBox(10);
            layout.setAlignment(Pos.CENTER);
            layout.getChildren().addAll(gameOverText1,gameOverText, exitButton);
            Scene scene = new Scene(layout, 200, 100);
            gameOverStage.setScene(scene);
            gameOverStage.centerOnScreen();
            gameOverStage.show();
        });
    }


}