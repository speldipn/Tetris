package org.androidtown.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class Preview extends View {

  private final int X = 99;
  private int blockWidth;
  private int blockHeight;

  private boolean isRun = true;

  int preview[][] = {
    {X, X, X, X, X, X},
    {X, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, X},
    {X, X, X, X, X, X}
  };

  public void setBlock(int[][] block) {
    int row = block.length;
    int col = block[0].length;
    for (int i = 0; i < row; ++i) {
      for (int j = 0; j < col; ++j) {
        preview[i + 1][j + 1] = block[i][j];
      }
    }
  }

  public Preview(Context context, int width, int height) {
    super(context);
    this.blockWidth = width;
    this.blockHeight = height;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    drawPreview(canvas);
  }

  public void drawPreview(Canvas canvas) {
    int row = preview.length;
    int col = preview[0].length;
    for (int i = 0; i < row; ++i) {
      for (int j = 0; j < col; ++j) {
        int left = (j * blockWidth) + Const.GRIDWIDTH;
        int right = (left + blockWidth) - Const.GRIDWIDTH;
        int top = (i * blockHeight) + Const.GRIDWIDTH;
        int bottom = (top + blockHeight) - Const.GRIDWIDTH;
        canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.GRID));
        if (preview[i][j] == X) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BORDER));
        } else if (preview[i][j] == 1) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_O));
        } else if (preview[i][j] == 2) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_I));
        } else if (preview[i][j] == 3) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_S));
        } else if (preview[i][j] == 4) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_Z));
        } else if (preview[i][j] == 5) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_L));
        } else if (preview[i][j] == 6) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_J));
        } else if (preview[i][j] == 7) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_T));
        } else {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.EMPTY));
        }
      }
    }
  }
}
