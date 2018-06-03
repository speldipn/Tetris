package org.androidtown.tetris;

import android.graphics.Color;
import android.graphics.Paint;

public class Const {

  public static final String TAG = "SPELDIPN";

  public static final int GRIDWIDTH = 1;

  static Paint paint = new Paint();

  public enum PType {
    BLOCK, BORDER, FIXED, EMPTY, DONE, GRID,
    BLK_S, BLK_O, BLK_T, BLK_J, BLK_I, BLK_Z, BLK_L
  }

  static Paint getPaint(PType type) {
    switch (type) {
      case EMPTY:
        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.GRAY);
        paint.setColor(Color.rgb(46, 46, 46));
        break;
      case BORDER:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(219, 112, 147)); // pale violet
        break;
      case GRID:
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(GRIDWIDTH);
        paint.setColor(Color.rgb(	255, 255, 255));
        break;
      case BLOCK:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        break;
      case FIXED:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        break;
      case DONE:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(100, 0, 100));
        break;
      case BLK_O:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        break;
      case BLK_J:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        break;
      case BLK_S:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        break;
      case BLK_Z:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.CYAN);
        break;
      case BLK_L:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.MAGENTA);
        break;
      case BLK_I:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        break;
      case BLK_T:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(255, 165, 0)); // Orange
        break;
      default:
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
    }
    return paint;
  }
}
