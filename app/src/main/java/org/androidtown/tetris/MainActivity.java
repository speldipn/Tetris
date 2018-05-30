package org.androidtown.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

  private final int STAGE_X_CNT = 12;
  private final int STAGE_Y_CNT = 21;
  private final int PREVIEW_X_CNT = 6;
  private final int PREVIEW_Y_CNT = 5;

  private int mapFrX = 0;
  private int mapFrY = 0;
  private int previewFrX = 0;
  private int previewFrY = 0;

  FrameLayout mapFr;
  FrameLayout previewFr;
  Stage stage;
  Thread stageThread;
  Preview preview;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mapFr = findViewById(R.id.map);
    previewFr = findViewById(R.id.preview);
    mapFr.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        mapFrX = mapFr.getWidth();
        mapFrY = mapFr.getHeight();
        previewFrX = previewFr.getWidth();
        previewFrY = previewFr.getHeight();

        run();
      }
    });
  }

  public void run() {
    runStage();
  }

  public void runStage() {
    // 스테이지 생성
    stage = new Stage(mapFr.getContext(), mapFrX / STAGE_X_CNT, mapFrY / STAGE_Y_CNT);
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

  public void rotate(View v) {

  }

  public void left(View v) {

  }

  public void right(View v) {

  }

  public void down(View v) {

  }
}
