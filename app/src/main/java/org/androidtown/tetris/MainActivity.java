package org.androidtown.tetris;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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

  TextView scoreView;

  FrameLayout mapFr;
  FrameLayout previewFr;
  Stage stage;
  Thread stageThread;
  Preview preview;

  ViewTreeObserver vto;

  public static final int SCORE = 1;
  public static final int END = 2;

  final Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if(msg.what == SCORE) {
        if(stage != null) {
          scoreView.setText(stage.getScore() + "점");
        }
      } else if(msg.what == END) {
        Toast.makeText(MainActivity.this, "게임이 끝났습니다", Toast.LENGTH_LONG).show();
        finish();
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mapFr = findViewById(R.id.map);
    previewFr = findViewById(R.id.preview);
    scoreView = findViewById(R.id.score);

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
    new Thread() {
      @Override
      public void run() {
        Handler handler = MainActivity.this.handler;
        while(true) {
          if(stage != null) {
            stage.moveDown();
            handler.sendEmptyMessage(SCORE);
          }
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }.start();
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
    stage.doStop();
    try {
      stageThread.join();
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
      }
    }
  }
}
