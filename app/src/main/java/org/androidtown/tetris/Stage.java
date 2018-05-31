package org.androidtown.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

import java.security.CodeSigner;

class Stage extends View implements Runnable {

  private final int B = 1;
  private final int F = 2;
  private final int X = 99;
  private int blockWidth;
  private int blockHeight;
  private int curX;
  private int curY;

  private final int START_X_IDX = 1;
  private final int END_X_IDX = 10;
  private final int START_Y_IDX = 0;
  private final int END_Y_IDX = 22;

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
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawMap(canvas);
  }

  public void drawMap(Canvas canvas) {
    int row = map.length;
    int col = map[0].length;
    for (int i = 3; i < row; ++i) {
      for (int j = 0; j < col; ++j) {
        int left = (j * blockWidth) + Const.GRIDWIDTH;
        int right = (left + blockWidth) - Const.GRIDWIDTH;
        int top = ((i - 3) * blockHeight) + Const.GRIDWIDTH;
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

  public boolean isReady() {
    boolean moving = false;
    for (int i = 0; i < map.length; ++i) {
      for (int j = 0; j < map[0].length; ++j) {
        if (map[i][j] == B) {
          moving = true;
        }
      }
    }
    return !moving;
  }

  public boolean checkPos(int x, int y) {
    if ((x >= 0) && (x < map.length - 1)) {
      if ((y >= 0) && (y < map[0].length - 1)) {
        // 검사하는 자리가 Fixed나 Border가 아니면
        if (map[x][y] != F && map[x][y] != X) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean moving() {
    moveDown();
    return !isReady();
  }

  public void moveRotate() {
    int[][] rotateBlock = Generator.getRotateBlock(block);
    int x = 0;
    int y = 0;
    for(int i = curY; i < (curY + rotateBlock.length); ++i, ++x) {
      for(int j = curX; j < (curX + rotateBlock[0].length); ++j, ++y) {
        map[i][j] = rotateBlock[x][y];
      }
      y = 0;
    }
    block = rotateBlock;
  }

  public void moveDown() {
    for (int i = END_Y_IDX; i > START_Y_IDX; --i) {
      for (int j = START_X_IDX; j <= END_X_IDX; ++j) {
        map[i][j] = map[i - 1][j];
        map[i - 1][j] = 0;
      }
    }
    curY += 1;
  }

  public void moveLeft() {
    for (int i = START_Y_IDX; i <= END_Y_IDX; ++i) {
      for (int j = START_X_IDX + 1; j <= END_X_IDX; ++j) {
        map[i][j - 1] = map[i][j];
        map[i][j] = 0;
      }
    }
    curX -= 1;
  }

  public void moveRight() {
    for (int i = START_Y_IDX; i <= END_Y_IDX; ++i) {
      for (int j = END_X_IDX; j > START_X_IDX; --j) {
        map[i][j] = map[i][j - 1];
        map[i][j - 1] = 0;
      }
    }
    curX += 1;
  }

  private int[][] preBlock = Generator.getBlock();
  private int[][] block = preBlock;
  @Override
  public void run() {
    boolean isMoving = false;
    while (isRun) {
      if (preview != null) {
        preview.setBlock(preBlock);
        preview.postInvalidate();
      }
      if (!isMoving) {
        block = preBlock;
        setBlock(block);
        preBlock = Generator.getBlock();
      }
      if (moving()) {
        isMoving = true;
      } else {
        isMoving = false;
      }
      postInvalidate();
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void setBlock(int[][] block) {
    int start = ((START_X_IDX + END_X_IDX) / 2) - (block[0].length / 2) + START_X_IDX;
    int x = 0, y = 0;
    for (int i = START_Y_IDX; i < block.length; ++i, ++x) {
      for (int j = start; j < (start + block[0].length); ++j, ++y) {
        map[i][j] = block[x][y];
      }
      y = 0;
    }
    curX = start;
    curY = 0;
  }

  public void doStop() {
    isRun = false;
  }

  public void addPreview(Preview preview) {
    this.preview = preview;
  }
}
