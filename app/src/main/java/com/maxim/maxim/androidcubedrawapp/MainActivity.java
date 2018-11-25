package com.maxim.maxim.androidcubedrawapp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AbsoluteLayout;

// главная активность
public class MainActivity extends AppCompatActivity {
    // координаты куба
    int cube_x = 100;
    int cube_y = 20;
    // размер куба
    int cube_size = 50;
    // скорость движения
    int speed = 5;

    // размер игрового поля
    int screen_width;
    int screen_height;

    // игровой Canvas
    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // запрет поворота экрана
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // создание игрового Canvas
        drawView = new DrawView(this);

        setContentView(R.layout.activity_main);

        // получаем элемент для хранения Canvas
        AbsoluteLayout gameLayout = (AbsoluteLayout) findViewById(R.id.game);
        // засовываем Canvas в элемент
        gameLayout.addView(drawView);

        // объект для получения размера экрана
        Display display = getWindowManager().getDefaultDisplay();
        // ширина игрвого поля
        screen_width = display.getWidth();
        // высота игрового поля
        screen_height = display.getHeight() - 300;

        // запускаем циклический вызов функции
        makeWorkingMyInterval();
    }

    // функция, вызывающаяся циклически
    private void makeWorkingMyInterval(){
        final Handler myHandler = new Handler();
        myHandler.post (new Runnable(){
            @Override
            public void run(){
                // изменяем положение
                cube_y += speed;
                // передаём Canvas обновлённые свойства куба
                drawView.setCubeParams(cube_x, cube_y, cube_size);
                // контролируем подход к границе
                if(screen_height < cube_y + cube_size) {
                    speed *= -1;
                }
                // контролируем подход к границе
                if(0 > cube_y) {
                    speed *= -1;
                }
                // перерисовываем содержимое Canvas
                drawView.invalidate();
                // ждём определённое время
                myHandler.postDelayed(this, 50);
            }
        });
    }

    // при нажатии на кнопку изменения скорости
    public void changeSpeed(View view) {
        speed = (int)(speed * 1.25);
    }

    // при нажатии на кнопку выхода из приложения
    public void exitApp(View view) {
        finish();
    }

    // класс для работы с Canvas
    class DrawView extends View {
        // объект для реализации рисования
        Paint p;

        // начальные свойства куба
        int cube_x = 100;
        int cube_y = 20;
        int cube_size = 50;

        // метод для обновления параметров куба
        public void setCubeParams(int xx, int yy, int ss) {
            cube_x = xx;
            cube_y = yy;
            cube_size = ss;
        }

        public DrawView(Context context) {
            super(context);
            p = new Paint();
        }

        // метод для отрисовки куба
        public void drawCube(int xx, int yy, int ss, Canvas canvas) {
            p.setColor(Color.WHITE);
            canvas.drawRect(xx, yy, xx + ss, yy + ss, p);
        }

        // метод для отрисовки всего содержимого
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.GRAY);
            drawCube(cube_x, cube_y, cube_size, canvas);
        }
    }
}

