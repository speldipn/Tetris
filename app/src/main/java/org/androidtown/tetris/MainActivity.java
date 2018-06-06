package org.androidtown.tetris;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


  private final int STAGE_X_CNT = 12;
  private final int STAGE_Y_CNT = 21;
  private final int PREVIEW_X_CNT = 6;
  private final int PREVIEW_Y_CNT = 6;

  private int mapFrX = 0;
  private int mapFrY = 0;
  private int previewFrX = 0;
  private int previewFrY = 0;
  private boolean isDone = false;

  private int gameSpeed = 500;
  private int stageLevel = 1;

  private FrameLayout popUp;

  TextView scoreView;

  FrameLayout mapFr;
  FrameLayout previewFr;
  Stage stage;
  Thread stageThread;
  MainThread mainTask;
  Thread mainThread;
  Preview preview;
  TextView stageLvLabel;

  ViewTreeObserver vto;

  class MainThread implements Runnable {
    private boolean isRun = true;
    private boolean isPause = false;

    @Override
    public void run() {
      Handler handler = MainActivity.this.handler;
      while (isRun) {
        if (this.isPause) {
          try {
            Thread.sleep(1000);
            continue;
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        if (stage != null) {
          stage.moveDown();
          handler.sendEmptyMessage(SCORE);
        }
        try {
          Thread.sleep(gameSpeed);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    public void doPause(boolean enable) {
      if (enable) {
        this.isPause = true;
      } else {
        this.isPause = false;
      }
    }

    public void doStop() {
      this.isRun = false;
    }
  }

  public static final int SCORE = 1;
  public static final int NEXT = 2;
  public static final int END = 3;
  public static final int STAGE_LEVEL = 4;
  public static final int START = 5;
  public static final int STOP = 6;

  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == SCORE) {
        if (stage != null) {
          int score  = stage.getScore();
          scoreView.setText(score + "점");
          if(score >= 500) {
            handler.sendEmptyMessage(NEXT);
          }
        }
      } else if (msg.what == NEXT) {
        mainTask.doPause(true);
        popUp.setVisibility(View.VISIBLE);
        btnShowAll(false);
      } else if (msg.what == END) {
        Toast.makeText(MainActivity.this, "게임을 종료합니다", Toast.LENGTH_SHORT).show();
        finish();
      } else if(msg.what == STAGE_LEVEL) {
        int stageLv = msg.arg1;
        stageLvLabel.setText("Stage " + stageLv);
      } else if(msg.what == START) {
        mainTask.doPause(false);
      } else if(msg.what == STOP) {
        mainTask.doPause(true);
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    popUp = findViewById(R.id.popup);
    mapFr = findViewById(R.id.map);
    previewFr = findViewById(R.id.preview);
    scoreView = findViewById(R.id.score);
    stageLvLabel = findViewById(R.id.stageLvLabel);
    stageLvLabel.setText("Stage " + stageLevel);

    vto = mapFr.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        mapFrX = mapFr.getWidth();
        mapFrY = mapFr.getHeight();
        previewFrX = previewFr.getWidth();
        previewFrY = previewFr.getHeight();

        // 등록된 리스너 제거(제거하지 않으면 반복 호출됨)
        if (Build.VERSION.SDK_INT > 16) {
          mapFr.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
          mapFr.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }

        // 프로그램 동작
        stageRun();
      }
    });
  }

  public void stageRun() {
    runStage();
    mainTask = new MainThread();
    mainThread = new Thread(mainTask);
    mainThread.start();
  }

  public void runStage() {
    // 스테이지 생성
    stage = new Stage(mapFr.getContext(), mapFrX / STAGE_X_CNT, mapFrY / STAGE_Y_CNT, this.handler);
    mapFr.addView(stage);

    // 프리뷰 생성
    preview = new Preview(previewFr.getContext(), previewFrX / PREVIEW_X_CNT, previewFrY / PREVIEW_Y_CNT);
    previewFr.addView(preview);

    stage.addPreview(preview);

    // 스레드 실행
    stageThread = new Thread(stage);
    stageThread.start();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    try {
      mainTask.doStop();
      stage.doStop();
      stageThread.join();
      mainThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      Log.d(Const.TAG, "STAGE thread join.");
    }
  }

  public void command(View v) {
    if (stage != null) {
      switch (v.getId()) {
        case R.id.btnRotate:
          stage.moveRotate();
          break;
        case R.id.btnLeft:
          stage.moveLeft();
          break;
        case R.id.btnRight:
          stage.moveRight();
          break;
        case R.id.btnDown:
          stage.moveDown();
          break;
        case R.id.btnDownFast:
          stage.moveDownFast();
          break;
      }
    }
  }

  public void next(View v) {
    if (gameSpeed > 100) {
      gameSpeed -= 100;
      btnShowAll(true);
      popUp.setVisibility(View.GONE);
      /* --- */
      stage.setScore(0);
      stage.mapInit();
      /* ---- */
      Message msg = new Message();
      msg.what = STAGE_LEVEL;
      msg.arg1 = (++stageLevel);
      handler.sendMessage(msg);
      /* --- */
      mainTask.doPause(false);
    } else {
      handler.sendEmptyMessage(END);
    }
  }

  public void btnShowAll(boolean yes) {
    if (yes) {
      findViewById(R.id.btnRotate).setVisibility(View.VISIBLE);
      findViewById(R.id.btnDown).setVisibility(View.VISIBLE);
      findViewById(R.id.btnLeft).setVisibility(View.VISIBLE);
      findViewById(R.id.btnRight).setVisibility(View.VISIBLE);
      findViewById(R.id.btnDownFast).setVisibility(View.VISIBLE);
    } else {
      findViewById(R.id.btnRotate).setVisibility(View.INVISIBLE);
      findViewById(R.id.btnDown).setVisibility(View.INVISIBLE);
      findViewById(R.id.btnLeft).setVisibility(View.INVISIBLE);
      findViewById(R.id.btnRight).setVisibility(View.INVISIBLE);
      findViewById(R.id.btnDownFast).setVisibility(View.INVISIBLE);
    }
  }
}
