package org.androidtown.tetris;

import android.graphics.Color;
import android.graphics.Paint;

public class Const {

  public static final String TAG = "SPELDIPN";

  public static final int GRIDWIDTH = 1;

  static Paint paint = new Paint();
  public enum PType {BLOCK, BORDER, FIXED, EMPTY, GRID}

  static Paint getPaint(PType type) {
    switch (type) {
      case EMPTY:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        break;
      case BORDER:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        break;
      case GRID:
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(GRIDWIDTH);
        paint.setColor(Color.GRAY);
        break;
      case BLOCK:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        break;
      case FIXED:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        break;
      default:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
    }
    return paint;
  }
}
