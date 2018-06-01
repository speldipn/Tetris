package org.androidtown.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

class Stage extends View implements Runnable {

  private final int B = 1;
  private final int F = 2;
  private final int X = 99;
  private int blockWidth;
  private int blockHeight;
  private int curX;
  private int curY;

  private boolean isRun = true;

  public class BlockPos {
    int x;
    int y;
  }

  private BlockPos blockPos = new BlockPos();
  private BlockPos prevBlockPos = new BlockPos();

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
    updateBlock(canvas);
    drawStage(canvas);
  }

  private void updateBlock(Canvas canvas) {

    for (int i = prevBlockPos.y; i < (block.length + prevBlockPos.y); ++i) {
      for (int j = prevBlockPos.x; j < (block[0].length + prevBlockPos.x); ++j) {
        map[i][j] = 0;
      }
    }

    for (int i = blockPos.y; i < (block.length + blockPos.y); ++i) {
      for (int j = blockPos.x; j < (block[0].length + blockPos.x); ++j) {
        map[i][j] = 0;
        map[i][j] = block[i - blockPos.y][j - blockPos.x];
      }
    }
  }

  public void drawStage(Canvas canvas) {
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

  public boolean moving() {
    return true;
  }

  private boolean isPossibleMove(int x, int y, boolean isRotate) {
    int[][] target = new int[block.length][block[0].length];
    for (int i = 0; i < block.length; ++i) {
      for (int j = 0; j < block[0].length; ++j) {
        int tempY = i + y;
        int tempX = j + x;
        int val = block[i + y][j + x];

      }
    }

    //1. 이동하려는 방향으로 맵의 데이터를 복사
    for (int i = 0; i < block.length; ++i) {
      for (int j = 0; j < block[0].length; ++j) {
        target[i][j] = map[blockPos.y + i + y][blockPos.x + j];
      }
    }
    // 현재 블록과 복사 데이터를 비교
    for (int i = 0; i < block.length; ++i) {
      for (int j = 0; j < block[0].length; ++j) {
        if (block[i][j] + target[i][j] == block[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  public void moveRotate() {
    if (isPossibleMove(0, 0, true)) {
      block = Generator.getRotateBlock(block);
      invalidate();
    }
  }

  public void moveDown() {
    if (isPossibleMove(0, 1, false)) {
      prevBlockPos.y = blockPos.y;
      blockPos.y += 1;
      invalidate();
    }
  }

  public void moveLeft() {
    if (isPossibleMove(-1, 0, false)) {
      prevBlockPos.x = blockPos.x;
      blockPos.x -= 1;
      invalidate();
    }
  }

  public void moveRight() {
    if (isPossibleMove(1, 0, false)) {
      prevBlockPos.x = blockPos.x;
      blockPos.x += 1;
      invalidate();
    }
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
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }


  private final int START_X_IDX = 1;
  private final int END_X_IDX = 10;
  private final int START_Y_IDX = 0;
  private final int END_Y_IDX = 22;

  private void setBlock(int[][] block) {
    int start = ((START_X_IDX + END_X_IDX) / 2) - (block[0].length / 2) + START_X_IDX;
    int x = 0, y = 0;
    for (int i = START_Y_IDX; i < block.length; ++i, ++x) {
      for (int j = start; j < (start + block[0].length); ++j, ++y) {
        map[i][j] = block[x][y];
      }
      y = 0;
    }
    // 생성된 블럭의 위치를 초기화.
    blockPos.x = start;
    blockPos.y = 0;
    prevBlockPos.x = start;
    prevBlockPos.y = 0;
  }

  public void doStop() {
    isRun = false;
  }

  public void addPreview(Preview preview) {
    this.preview = preview;
  }
}
