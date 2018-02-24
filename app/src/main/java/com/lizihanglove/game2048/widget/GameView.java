package com.lizihanglove.game2048.widget;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.lizihanglove.game2048.R;
import com.lizihanglove.game2048.activity.MainActivity;
import com.lizihanglove.game2048.util.Constant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameView extends LinearLayout {

    private boolean isVibrate;
    private boolean isPlayMusic;

    private SoundPool soudPool;
    private HashMap<Integer, Integer> spMap;

    public GameView(Context context) {
        super(context);

        initGameView();
        initSoundPool();


    }

    private void initSoundPool() {

        soudPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        spMap = new HashMap<Integer, Integer>();
        spMap.put(1, soudPool.load(getContext(), R.raw.swipe_left, 1));
        spMap.put(2, soudPool.load(getContext(), R.raw.swipe_right, 1));
        spMap.put(3, soudPool.load(getContext(), R.raw.swipe_up, 1));
        spMap.put(4, soudPool.load(getContext(), R.raw.swipe_down, 1));
        spMap.put(5, soudPool.load(getContext(), R.raw.complete, 1));
    }

    private void playSound(int sound, int number) {
        isPlayMusic = getContext().
                getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE).
                getBoolean(Constant.IS_PLAY_MUSIC, false);
        if (isPlayMusic) {
            AudioManager am = (AudioManager) getContext().getSystemService(
                    Context.AUDIO_SERVICE);
            float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float audioCurrentVolumn = am
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
            soudPool.play(spMap.get(sound), volumnRatio, volumnRatio, 1, number, 1);
        }

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initGameView();
        initSoundPool();
    }

    private void initGameView() {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xffbbada0);
        setOnTouchListener(new OnTouchListener() {

            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;


                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -8) {
                                swipeLeft();
                            } else if (offsetX > 8) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -8) {
                                swipeUp();
                            } else if (offsetY > 8) {
                                swipeDown();
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Constant.CARD_WIDTH = (Math.min(w, h) - 10) / Constant.LINES;

        addCards(Constant.CARD_WIDTH, Constant.CARD_WIDTH);

        startGame();
    }

    private void addCards(int cardWidth, int cardHeight) {

        CardView c;

        LinearLayout line;
        LayoutParams lineLp;

        for (int y = 0; y < Constant.LINES; y++) {
            line = new LinearLayout(getContext());
            lineLp = new LayoutParams(-1, cardHeight);
            addView(line, lineLp);

            for (int x = 0; x < Constant.LINES; x++) {
                c = new CardView(getContext());
                line.addView(c, cardWidth, cardHeight);

                cardsMap[x][y] = c;
            }
        }
    }

    public void startGame() {

        MainActivity aty = MainActivity.getMainActivity();
        aty.clearScore();
        aty.showBestScore(aty.getLastBestScore());

        for (int y = 0; y < Constant.LINES; y++) {
            for (int x = 0; x < Constant.LINES; x++) {
                cardsMap[x][y].setNum(0);
            }
        }

        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum() {

        emptyPoints.clear();

        for (int y = 0; y < Constant.LINES; y++) {
            for (int x = 0; x < Constant.LINES; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        if (emptyPoints.size() > 0) {

            Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
            cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

            MainActivity.getMainActivity().getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
        }
    }


    private void swipeLeft() {
        playSound(1, 1);
        boolean merge = false;

        for (int y = 0; y < Constant.LINES; y++) {
            for (int x = 0; x < Constant.LINES; x++) {

                for (int x1 = x + 1; x1 < Constant.LINES; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {

                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;
                            merge = true;

                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }

        vibrate();

    }

    private void swipeRight() {
        playSound(2, 1);
        boolean merge = false;

        for (int y = 0; y < Constant.LINES; y++) {
            for (int x = Constant.LINES - 1; x >= 0; x--) {

                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }

        vibrate();
    }

    private void swipeUp() {
        playSound(3, 1);
        boolean merge = false;

        for (int x = 0; x < Constant.LINES; x++) {
            for (int y = 0; y < Constant.LINES; y++) {

                for (int y1 = y + 1; y1 < Constant.LINES; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;

                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;

                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }

        vibrate();
    }

    private void swipeDown() {
        playSound(4, 1);
        boolean merge = false;

        for (int x = 0; x < Constant.LINES; x++) {
            for (int y = Constant.LINES - 1; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }

        vibrate();
    }

    private void checkComplete() {

        boolean complete = true;

        ALL:
        for (int y = 0; y < Constant.LINES; y++) {
            for (int x = 0; x < Constant.LINES; x++) {
                if (cardsMap[x][y].getNum() == 0 ||
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) ||
                        (x < Constant.LINES - 1 && cardsMap[x][y].equals(cardsMap[x + 1][y])) ||
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) ||
                        (y < Constant.LINES - 1 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {

                    complete = false;
                    break ALL;
                }
            }
        }

        if (complete) {
            playSound(5, 1);
            alertDialg();

        }

    }

    private void alertDialg() {
        DiyDialog.Builder builder = new DiyDialog.Builder(MainActivity.getMainActivity());
        builder.setTitle("游戏结束")
                .setMessage("游戏结束,是否重新开始")
                .setPositiveButton("开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startGame();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MainActivity.getMainActivity().finish();
                    }
                })
                .create()
                .show();

    }

    private CardView[][] cardsMap = new CardView[Constant.LINES][Constant.LINES];
    private List<Point> emptyPoints = new ArrayList<Point>();


    public void vibrate() {
        isVibrate = getContext().
                getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE).
                getBoolean(Constant.IS_VIBRATE, false);
        if (isVibrate) {
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(50);
        }

    }


}
