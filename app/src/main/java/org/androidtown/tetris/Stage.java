package org.androidtown.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.View;

import java.util.LinkedList;
import java.util.Queue;

class Stage extends View implements Runnable {
  private final int MAP_WIDTH = 12;
  private final int MAP_HEIGHT = 24;

  private final int START_X_IDX = 1;
  private final int END_X_IDX = 10;
  private final int START_Y_IDX = 0;
  private final int END_Y_IDX = 22;

  private final int EM = 0; // Empty
  private final int BK = 1; // Block
  private final int FX = 2; // Fixed
  private final int DN = 50; // Done
  private final int SX = 80; // Side Border
  private final int BX = 99; // Bottom Border

  private int blockWidth;
  private int blockHeight;
  private int curX;
  private int curY;

  private boolean isRun = true;
  private int score = 0;

  public class BlockPos {
    int x;
    int y;
  }

  private int[][] preBlock;
  private int[][] block;

  private BlockPos blockPos = new BlockPos();

  private enum COMMAND {Rotation, Left, Right, Down}

  Queue<COMMAND> msgQ = new LinkedList<>();

  int map[][] = {
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX},
//    {SX, EM, EM, 54, EM, 54, EM, EM, 54, EM, EM, SX},
//    {SX, EM, EM, 54, EM, 54, EM, EM, 54, EM, EM, SX},
//    {SX, EM, EM, 54, EM, 54, EM, EM, 54, EM, EM, SX},
//    {SX, EM, EM, 54, EM, 54, EM, EM, 54, EM, EM, SX},
//    {SX, 54, 54, 54, 54, 54, 54, 54, 54, 54, EM, SX},
    {BX, BX, BX, BX, BX, BX, BX, BX, BX, BX, BX, BX}
  };

  private Preview preview = null;

  Handler handler = null;

  public Stage(Context ctx, int blockWidth, int blockHeight, Handler handler) {
    super(ctx);
    this.blockWidth = blockWidth;
    this.blockHeight = blockHeight;
    this.handler = handler;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    updateBlock(canvas);
    drawStage(canvas);
  }

  private void updatePreview() {
    if (preview != null) {
      preBlock = Generator.getBlock();
      preview.setBlock(preBlock);
      preview.postInvalidate();
    }
  }

  private boolean checkPos(int x, int y) {
    if (x < START_X_IDX || x > END_X_IDX) {
      return false;
    }
    if (y < START_Y_IDX || y > END_Y_IDX) {
      return false;
    }
    return true;
  }

  private boolean notDone(int x, int y) {
    if (((map[x][y] / DN) == 0) && ((map[x][y] % DN) == map[x][y])) {
      return true;
    }
    return false;
  }

  private void updateBlock(Canvas canvas) {
    // 현재 블록을 그려준다.
    for (int i = blockPos.y; i < (block.length + blockPos.y); ++i) {
      for (int j = blockPos.x; j < (block[0].length + blockPos.x); ++j) {
        if (checkPos(j, i)) {
          if (notDone(i, j)) {
            map[i][j] = block[i - blockPos.y][j - blockPos.x];
          }
        }
      }
    }
  }

  public void drawStage(Canvas canvas) {
    int row = map.length;
    int col = map[0].length;
    int start = 3;
    for (int i = start; i < row; ++i) {
      for (int j = 0; j < col; ++j) {
        int left = (j * blockWidth) + Const.GRIDWIDTH;
        int right = (left + blockWidth) - Const.GRIDWIDTH;
        int top = ((i - start) * blockHeight) + Const.GRIDWIDTH;
        int bottom = (top + blockHeight) - Const.GRIDWIDTH;
        canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.GRID));
        if (map[i][j] == SX | map[i][j] == BX) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BORDER));
        } else if (map[i][j] % DN == 1) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_O));
        } else if (map[i][j] % DN == 2) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_I));
        } else if (map[i][j] % DN == 3) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_S));
        } else if (map[i][j] % DN == 4) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_Z));
        } else if (map[i][j] % DN == 5) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_L));
        } else if (map[i][j] % DN == 6) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_J));
        } else if (map[i][j] % DN == 7) {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.BLK_T));
        } else {
          canvas.drawRect(left, top, right, bottom, Const.getPaint(Const.PType.EMPTY));
        }
      }
    }
  }

  private boolean isPossibleMove(int x, int y, boolean isRotate) {
    // 현재 위치의 맵에서 블록을 제거
    for (int i = 0; i < block.length; ++i) {
      for (int j = 0; j < block[0].length; ++j) {
        if (block[i][j] > 0) {
          map[blockPos.y + i][blockPos.x + j] = 0;
        }
      }
    }
    // 이동하려는 방향으로 이동한 후에 맵을 가져오고
    int[][] target = new int[block.length][block[0].length];
    for (int i = 0; i < block.length; ++i) {
      for (int j = 0; j < block[0].length; ++j) {
        int _x = blockPos.x + j + x;
        int _y = blockPos.y + i + y;
        if ((_x >= 0 && _x < MAP_WIDTH) && (_y >= 0 && _y < MAP_HEIGHT)) {
          target[i][j] = map[_y][_x];
        } else {
          target[i][j] = SX;
        }
      }
    }
    int[][] temp = null;
    if (isRotate) {
      temp = Generator.getRotateBlock(block);
    } else {
      temp = block;
    }
    // 이동하려는 블럭이 이동할 수 있는지 확인
    int dirX = 0;
    for (int i = 0; i < temp.length; ++i) {
      for (int j = 0; j < temp[0].length; ++j) {
        if (temp[i][j] > 0) {
          int val = temp[i][j] + target[i][j];
          if (val != temp[i][j]) {
            if (isRotate) {
              if (val > BX) {  // 아래 테두리에 닿거나
                return false;
              } else if (val > SX) {// 이미 쌓여진 불록에 닿거나
                dirX += (j <= 1) ? 1 : (-1);
                int doneBlkCnt = 0;
                for (int m = 0; m < temp.length; ++m) {
                  for (int n = 0; n < temp[0].length; ++n) {
                    if (temp[m][n] > 0) {
                      int value = temp[m][n] + map[blockPos.y + m][blockPos.x + n + dirX];
                      if (value < SX && value > DN) {
                        doneBlkCnt += 1;
                      }
                    }
                  }
                }
                if (doneBlkCnt > 0) {
                  return false;
                }
              } else if (val > DN) {
                return false;
              }
            } else {
              return false;
            }
          }
        }
      }
    }
    if (isRotate) {
      blockPos.x += dirX;
    }
    return true;
  }

  public void moveRotate() {
    msgQ.add(COMMAND.Rotation);
  }

  private void __moveRotate() {
    if (isPossibleMove(0, 0, true)) {
      block = Generator.getRotateBlock(block);
      postInvalidate();
    }
  }

  public void moveDown() {
    msgQ.add(COMMAND.Down);
  }

  private void __moveDown() {
    if (isPossibleMove(0, 1, false)) {
      blockPos.y += 1;
    } else {
      if(blockPos.y < 4) {
        handler.sendEmptyMessage(MainActivity.END);
      }
      score += 10;
      blockDone();
      removeLine();
      block = preBlock;
      setBlock(block);
      updatePreview();
    }
    postInvalidate();
  }

  private void blockDone() {
    for (int i = 0; i < block.length; ++i) {
      for (int j = 0; j < block[0].length; ++j) {
        if (block[i][j] > 0) {
          map[blockPos.y + i][blockPos.x + j] = DN + block[i][j];
        }
      }
    }
  }

  public void moveLeft() {
    msgQ.add(COMMAND.Left);
  }

  private void __moveLeft() {
    if (isPossibleMove(-1, 0, false)) {
      blockPos.x -= 1;
      postInvalidate();
    }
  }

  public void moveRight() {
    msgQ.add(COMMAND.Right);
  }

  private void __moveRight() {
    if (isPossibleMove(1, 0, false)) {
      blockPos.x += 1;
      postInvalidate();
    }
  }

  @Override
  public void run() {
    while (isRun) {
      if (msgQ.peek() != null) {
        COMMAND cmd = msgQ.poll();
        switch (cmd) {
          case Rotation:
            __moveRotate();
            break;
          case Left:
            __moveLeft();
            break;
          case Right:
            __moveRight();
            break;
          case Down:
            __moveDown();
            break;
        }
      } else {
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void removeLine() {
    int clearedLnCnt = 0;
    int doneCnt = 0;
    for (int i = END_Y_IDX; i >= START_Y_IDX; --i) {
      doneCnt = 0;
      for (int j = START_X_IDX; j <= END_X_IDX; ++j) {
        if (((map[i][j] / DN) == 1) && ((map[i][j] % DN) < 10)) {
          doneCnt += 1;
        }
      }
      if (doneCnt >= (END_X_IDX - START_X_IDX + 1)) {
        clearedLnCnt += 1;
        // 정리해야될 라인이 있다면
        for (int j = i - 1; j >= START_Y_IDX; --j) {
          System.arraycopy(map[j], 0, map[j + 1], 0, map[j].length);
        }
        i += 1;
      }
    }

    if (clearedLnCnt > 0) {
      score += (clearedLnCnt * 50);
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
    // 생성된 블럭의 위치를 초기화.
    blockPos.x = start;
    blockPos.y = 0;
  }

  public void doStop() {
    isRun = false;
  }

  public void addPreview(Preview preview) {
    this.preview = preview;
    updatePreview();
    block = preBlock;
    setBlock(block);
    updatePreview();
  }

  public int getScore() {
    return score;
  }
}
