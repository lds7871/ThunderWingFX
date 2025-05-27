package com.lds7871.flyaway.A_Contorller;

import com.lds7871.flyaway.B_Show.FirstPage;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class StageController{

    public  static int WIDTH=800;//  1600  2560  1920
    public  static int HEIGHT=800;//  900   1440  1080
    public static int SS持续时间;
    public static String 模式标题;
    public  static int 自身生命值;
    public  static int 自身射速;
    public  static int 敌机初始生成间隔;
    public  static int 敌机子弹飞行速度;
    public  static int 敌机最小生成间隔;
    public  static int 敌机移动速度;
    public  static int 大招充能数量;
    public  static int BOSS生命值;
    public static int BOSS当前生命值;
    public  static int BOSS移速;
    public  static int BOSS射击间隔;
    public  static int 奖励移动速度;
    public  static int 奖励生成间隔;
    public  static int 双倍火力充能数量;
    public  static long 双倍火力持续时间;
    public  static int 背景音乐;
    public  static String 音乐文件;
    public  static int 飞机模型;
    public  static String 飞机文件;

    public static Stage stage;
    public static Scene 主界面;         //  1
    public static FirstPage firstPage;//  2

    public StageController()
    {
        读取设置数据方法();
        if (Main.无尽模式)
        {
            SS持续时间=1200;
            模式标题="file:Img/H1_2.png";
            自身生命值=999;
        }else
        {
            SS持续时间=120;
            模式标题="file:Img/H1_1.png";
        }
    }

    public void initialize() {
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);

        //主界面调用
        firstPage = new FirstPage(this);//      1
        主界面 = new Scene(firstPage, WIDTH, HEIGHT);//   2
        //主界面.getStylesheets().add(Objects.requireNonNull(getClass().getResource("C2_FirstPage.css")).toExternalForm());


        //显示主界面方法();
        开发默认首次打开界面方法();

    }//未使用


//===================数据读取方法==============================
    public void 读取设置数据方法(){
        System.out.println("\n"+"===开始读取数据===");
        try (BufferedReader reader = new BufferedReader(new FileReader("Config.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("_");
                int 数值= Integer.parseInt(parts[1]);

                switch (parts[0])
                {
                    case "自身射速" -> 自身射速 = 数值;
                    case "敌机初始生成间隔" -> 敌机初始生成间隔 = 数值;
                    case "敌机子弹飞行速度" -> 敌机子弹飞行速度 = 数值;
                    case "敌机最小生成间隔" -> 敌机最小生成间隔 = 数值;
                    case "敌机移动速度" -> 敌机移动速度 = 数值;
                    case "自身生命值" -> 自身生命值 = 数值;
                    case "大招充能数量" -> 大招充能数量 = 数值;
                    case "BOSS生命值" -> {BOSS生命值 = 数值;BOSS当前生命值=数值;}
                    case "BOSS移速" -> BOSS移速 = 数值;
                    case "BOSS射击间隔" -> BOSS射击间隔 = 数值;
                    case "奖励移动速度" -> 奖励移动速度 = 数值;
                    case "奖励生成间隔" -> 奖励生成间隔 = 数值;
                    case "双倍火力充能数量" -> 双倍火力充能数量 = 数值;
                    case "双倍火力持续时间" -> 双倍火力持续时间 = 数值;
                    case "背景音乐" -> 背景音乐 =数值;
                    case "飞机模型" -> 飞机模型 =数值;
                }

            }
            if (背景音乐==1){音乐文件="Msc/朝日の向こう側へ.mp3";}
            else if (背景音乐==2) {音乐文件="Msc/罗德行进曲.mp3";}
            else if (背景音乐==3) {音乐文件="Msc/world wAr E.mp3";}

            if (飞机模型==1){飞机文件="file:Img/plant_1.png";}
            else if (飞机模型==2) {飞机文件="file:Img/plant_4.png";}

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("===读取数据完成==="+"\n");
    }

    //！！！！！！！！！！！此项目未使用方法！！！！！！！！！！！
    //主界面显示方法=================================
    public void 显示主界面方法() {
        首页淡入动画方法(firstPage);
        stage.setScene(主界面);
        stage.show();
    }
    // 开发默认首次打开界面=================================
    public void 开发默认首次打开界面方法(){
        首页淡入动画方法(firstPage);
        stage.setScene(主界面);
        stage.show();
    }

    // 显示其他界面方法=================================
    public void 显示其他界面方法(Pane nowpane,Pane topane)
    {
        其他界面淡入动画方法(nowpane,topane);
        System.out.println("\u001B[32m"+"\n"+"从 "+nowpane+" 跳转到 "+topane+"\n"+"\u001B[0m");
    }

    // 其他界面淡入动画方法=================================
    public static void 其他界面淡入动画方法(Pane nowpane,Pane topane)
    {
        FadeTransition fo = new FadeTransition(Duration.seconds(0.5), nowpane);//当前页面
        fo.setFromValue(1);
        fo.setToValue(0);
        fo.setOnFinished(event ->
        {
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), topane);//要去的页面
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            stage.setScene(topane.getScene());//目标场景加载
            stage.show();
        });

        fo.play();

    }
    // 首页淡入动画方法=================================
    public void 首页淡入动画方法(Pane pane)
    {
        FadeTransition ft = new FadeTransition(Duration.seconds(2), pane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
    public void restartGame() {
    }

}
