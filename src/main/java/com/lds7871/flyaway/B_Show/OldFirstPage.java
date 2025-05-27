package com.lds7871.flyaway.B_Show;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.lds7871.flyaway.A_Contorller.StageController;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.Random;
import static com.lds7871.flyaway.A_Contorller.StageController.自身射速;

public class OldFirstPage extends Pane {
    //static常量
    private ImageView 背景1;
    private ImageView 背景2;
    private Entity 飞机;
    private BooleanProperty isShooting = new SimpleBooleanProperty(false);
    private StageController controller;

    public enum EntityType {
        飞机, BLACK_SQUARE, BULLET
    }

    public OldFirstPage(StageController controller) {
        this.controller = controller;

        // 初始化背景图片
//        {
//            Image 背景1Image = new Image("file:Img/background.jpg");
//            背景1 = new ImageView(背景1Image);
//            this.getChildren().add(背景1);
//            背景2 = new ImageView(背景1Image);
//            this.getChildren().add(背景2);
//        }

        // 创建飞机
        {
            Image 飞机Image = new Image("file:Img/plant_1.png");
            ImageView 飞机ImageView = new ImageView(飞机Image);
            飞机ImageView.setFitWidth(40);
            飞机ImageView.setFitHeight(60);
            飞机 = FXGL.entityBuilder().at(220, 600)
                    .view(飞机ImageView)
                    .type(EntityType.飞机)
                    .buildAndAttach();
            // 确保黑色方块在最上层
            飞机.getViewComponent().getChildren().forEach(Node::toFront);
            this.getChildren().add(飞机.getViewComponent().getParent());
        }

       // 开始界面按钮位置总控();
       // 背景向下移动();
        飞机控制();
        发射子弹();
        生成敌机();

        // 添加碰撞处理器 - 这里是关键部分
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.BLACK_SQUARE) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity blackSquare) {
                System.out.println("检测到子弹与敌机碰撞!");
                bullet.removeFromWorld();
                blackSquare.removeFromWorld();

                // 可选：生成新的敌机
                生成敌机();
            }
        });
    }

    //============开始界面按钮位置总控=============
    public void 开始界面按钮位置总控() {
        背景1.setLayoutX(0);
        背景1.setLayoutY(0);//向下
        背景2.setLayoutX(0);
        背景2.setLayoutY(-790);//向下
    }

    //============背景向下移动=============
    public void 背景向下移动() {
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
    private void 飞机控制() {
        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (移动区域(飞机.getX(), 飞机.getY() - 3)) {
                    飞机.setY(飞机.getY() - 2);
                }
            }
        }, KeyCode.W);
        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (移动区域(飞机.getX(), 飞机.getY() + 3)) {
                    飞机.setY(飞机.getY() + 2);
                }
            }
        }, KeyCode.S);
        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (移动区域(飞机.getX() - 3, 飞机.getY())) {
                    飞机.setX(飞机.getX() - 2);
                }
            }
        }, KeyCode.A);
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                if (移动区域(飞机.getX() + 3, 飞机.getY())) {
                    飞机.setX(飞机.getX() + 2);
                }
            }
        }, KeyCode.D);
    }

    private boolean 移动区域(double x, double y) {
        return (x >= 0 && x <= 440) && (y >= 100 && y <= 750);
    }

    //============射出子弹====================
    private void 发射子弹() {
        FXGL.getInput().addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin() {
                isShooting.set(true);
                startShooting();
            }

            @Override
            protected void onActionEnd() {
                isShooting.set(false);
            }
        }, KeyCode.SPACE);
    }

    private void startShooting() {
        ReadOnlyBooleanProperty shootingCondition = isShooting;
        FXGL.getGameTimer().runAtIntervalWhile(() -> {
            if (isShooting.get()) {
                shootBullet();
            }
        }, Duration.millis(自身射速), shootingCondition);
    }

    private void shootBullet() {
        Image 子弹Image = new Image("file:Img/B_1.png");
        ImageView 子弹ImageView = new ImageView(子弹Image);
        子弹ImageView.setFitWidth(15);
        子弹ImageView.setFitHeight(30);

        // 修改这里：使用BULLET而不是bullet
        Entity bullet = FXGL.entityBuilder()
                .at(飞机.getX() + 12, 飞机.getY() - 10)
                .view(子弹ImageView)
                .with(new CollidableComponent(true))
                .type(EntityType.BULLET)  // 使用枚举中的BULLET
                .buildAndAttach();

        this.getChildren().add(bullet.getViewComponent().getParent());

        // 子弹移动
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), bullet.getViewComponent().getChildren().get(0));
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.play();

        FXGL.getGameTimer().runAtInterval(() -> {
            bullet.setY(bullet.getY() - 5);
            if (bullet.getY() <= 10) {
                bullet.removeFromWorld();
            }
        }, Duration.millis(10));
    }

    //==========生成敌机=====================
    private void 生成敌机() {
        Random random = new Random();
        int x = (int) (random.nextDouble() * 440);
        int y = 100;

        // 生成敌机并确保使用了正确的类型和碰撞组件
        Entity blackSquare = FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(20, 20, Color.BLACK))
                .with(new CollidableComponent(true))
                .type(EntityType.BLACK_SQUARE)  // 确保使用枚举中的BLACK_SQUARE
                .buildAndAttach();

        this.getChildren().add(blackSquare.getViewComponent().getParent());

        // 添加敌机移动逻辑
        FXGL.getGameTimer().runAtInterval(() -> {
            blackSquare.setY(blackSquare.getY() + 2);

            // 如果敌机飞出屏幕底部，移除它
            if (blackSquare.getY() >= 800) {
                blackSquare.removeFromWorld();
            }
        }, Duration.millis(30));
    }
}