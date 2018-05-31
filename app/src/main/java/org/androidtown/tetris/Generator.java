package org.androidtown.tetris;

import android.support.v4.graphics.TypefaceCompatUtil;

import java.util.Arrays;
import java.util.Random;

public class Generator {

  private static int[][][] O = {
    { {0, 0, 0, 0},
      {0, 1, 1, 0},
      {0, 1, 1, 0},
      {0, 0, 0, 0} }
  };

  private static int[][][] I = {
    { {0, 0, 1, 0},
      {0, 0, 1, 0},
      {0, 0, 1, 0},
      {0, 0, 1, 0} },

    { {0, 0, 0, 0},
      {1, 1, 1, 1},
      {0, 0, 0, 0},
      {0, 0, 0, 0} }
  };

  private static int[][][] S = {
    { {0, 0, 0, 0},
      {0, 0, 1, 1},
      {0, 1, 1, 0},
      {0, 0, 0, 0} },

    { {0, 0, 1, 0},
      {0, 0, 1, 1},
      {0, 0, 0, 1},
      {0, 0, 0, 0} }
  };

  private static int[][][] Z = {
    { {0, 0, 0, 0},
      {0, 1, 1, 0},
      {0, 0, 1, 1},
      {0, 0, 0, 0} },

    { {0, 0, 0, 1},
      {0, 0, 1, 1},
      {0, 0, 1, 0},
      {0, 0, 0, 0} }
  };

  private static int[][][] L = {
    { {0, 0, 0, 0},
      {0, 1, 1, 1},
      {0, 1, 0, 0},
      {0, 0, 0, 0} },

    { {0, 0, 1, 0},
      {0, 0, 1, 0},
      {0, 0, 1, 1},
      {0, 0, 0, 0} },

    { {0, 0, 0, 1},
      {0, 1, 1, 1},
      {0, 0, 0, 0},
      {0, 0, 0, 0} },

    { {0, 1, 1, 0},
      {0, 0, 1, 0},
      {0, 0, 1, 0},
      {0, 0, 0, 0} }
  };

  private static int[][][] J = {
    { {0, 0, 0, 0},
      {0, 1, 1, 1},
      {0, 0, 0, 1},
      {0, 0, 0, 0} },

    { {0, 0, 1, 1},
      {0, 0, 1, 0},
      {0, 0, 1, 0},
      {0, 0, 0, 0} },

    { {0, 1, 0, 0},
      {0, 1, 1, 1},
      {0, 0, 0, 0},
      {0, 0, 0, 0} },

    { {0, 0, 1, 0},
      {0, 0, 1, 0},
      {0, 1, 1, 0},
      {0, 0, 0, 0} }
  };

  private static int[][][] T = {
    { {0, 0, 0, 0},
      {0, 1, 1, 1},
      {0, 0, 1, 0},
      {0, 0, 0, 0} },

    { {0, 0, 1, 0},
      {0, 0, 1, 1},
      {0, 0, 1, 0},
      {0, 0, 0, 0} },

    { {0, 0, 1, 0},
      {0, 1, 1, 1},
      {0, 0, 0, 0},
      {0, 0, 0, 0} },

    { {0, 0, 1, 0},
      {0, 1, 1, 0},
      {0, 0, 1, 0},
      {0, 0, 0, 0} }
  };

  public static int[][] getBlock() {
    Random random = new Random(System.currentTimeMillis());
    int[][][][] blocks = {
      O, I, S, Z, L, J, T
    };
    int[][][] block = blocks[random.nextInt(blocks.length)];
    return block[random.nextInt(block.length)];
  }

  public static int[][] getRotateBlock(int[][] temp) {
    int[][][][] blocks = {
      O, I, S, Z, L, J, T
    };
    for(int i = 0; i < blocks.length; ++i) {
      int[][][] block = blocks[i];
      for(int j = 0; j < block.length; ++j) {
        if(Arrays.deepEquals(block[j], temp)) {
          int idx = (j + 1) % block.length;
          return block[idx];
        }
      }
    }
    return null;
  }

  public enum BlockType { O, I, S, Z, L, J, T };

  public static int[][] getBlockByName(BlockType type) {
    Random random = new Random(System.currentTimeMillis());
    int[][][] block = null;
    switch (type) {
      case O: block = O; break;
      case I: block = I; break;
      case S: block = S; break;
      case Z: block = Z; break;
      case L: block = L; break;
      case J: block = J; break;
      case T: block = T; break;
    }
    return block[random.nextInt(block.length)];
  }
}
