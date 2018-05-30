package org.androidtown.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

class Stage extends View implements Runnable {

  private final int B = 1;
  private final int X = 99;
  private int blockWidth;
  private int blockHeight;

  private boolean isRun = true;

  int map[][] = {
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, X},
    {X, X, X, X, X, X, X, X, X, X, X, X}
  };

  private Preview preview = null;


  public Stage(Context ctx, int blockWidth, int blockHeight) {
    super(ctx);
    this.blockWidth = blockWidth;
    this.blockHeight = blockHeight;
    // pain
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawMap(canvas);
  }

  public void drawMap(Canvas canvas) {
    int row = map.length;
    int col = map[0].length;
    for (int i = 0; i < row; ++i) {
      for (int j = 0; j < col; ++j) {
        int left = (j * blockWidth) + Const.GRIDWIDTH;
        int right = (left + blockWidth) - Const.GRIDWIDTH;
        int top = (i * blockHeight) + Const.GRIDWIDTH;
        int bottom = (top + blockHeight) - Const.GRIDWIDTH;
        canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.GRID));
        if (map[i][j] == X) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BORDER));
        } else if (map[i][j] == B) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLOCK));
        } else {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.EMPTY));
        }
      }
    }
  }

  @Override
  public void run() {
    while (isRun) {
      postInvalidate();
      if (preview != null) {
        preview.setBlock(Generator.getBlock());
      }
      Const.sleep(1000);
    }
  }

  public void doStop() {
    isRun = false;
  }

  public void addPreview(Preview preview) {
    this.preview = preview;
  }
}
