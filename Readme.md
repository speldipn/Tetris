# Tetris

### 동작 시현

![](/screenshot/tetris1.gif)

* 테트리스 게임을 안드로이드 기반으로 설계 및 구현
* 총 4단계로 단계별로 500점이 넘어가면 다음판으로 진행되고, 블럭 이동속도가 점차 증가된다.

### 클래스 디자인

#### Const 클래스
* 블럭의 이름과 화면을 그리기 위해 필요한 인스턴스들을 미리 정의해놓고 사용하기 위해 상수로 정의
````java
public enum PType {
    BLOCK, BORDER, FIXED, EMPTY, DONE, GRID,
    BLK_S, BLK_O, BLK_T, BLK_J, BLK_I, BLK_Z, BLK_L
}

static Paint getPaint(PType type) {
  // 사용하려는 유형에 따라 색을 반환
}
````

<br>

---
#### Generator 클래스
* 총 7개의 블럭을 포함하여 회전시킨 뒤에 블럭까지 모두 미리 생성한다.
* 난수를 만들어 랜덤으로 블럭을 생성한다.

````java
private static int[][][] O = {
        {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        }
    };
    private static int[][][] I = {
        {
            {0, 2, 0, 0},
            {0, 2, 0, 0},
            {0, 2, 0, 0},
            {0, 2, 0, 0}
        },
        {
            {0, 0, 0, 0},
            {2, 2, 2, 2},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }
    };
    private static int[][][] S = {
        {
            {0, 0, 0, 0},
            {0, 3, 0, 0},
            {0, 3, 3, 0},
            {0, 0, 3, 0}
        },

        {
            {0, 0, 0, 0},
            {0, 0, 3, 3},
            {0, 3, 3, 0},
            {0, 0, 0, 0}
        }
    };
    private static int[][][] Z = {
        {
            {0, 0, 0, 0},
            {0, 0, 4, 0},
            {0, 4, 4, 0},
            {0, 4, 0, 0}
        },
        {
            {0, 0, 0, 0},
            {0, 4, 4, 0},
            {0, 0, 4, 4},
            {0, 0, 0, 0}
        }
    };
    private static int[][][] L = {
        {
            {0, 0, 0, 0},
            {0, 0, 5, 0},
            {5, 5, 5, 0},
            {0, 0, 0, 0}
        },
        {
            {0, 0, 0, 0},
            {0, 5, 0, 0},
            {0, 5, 0, 0},
            {0, 5, 5, 0}
        },
        {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {5, 5, 5, 0},
            {5, 0, 0, 0}
        },
        {
            {0, 0, 0, 0},
            {5, 5, 0, 0},
            {0, 5, 0, 0},
            {0, 5, 0, 0}
        }
    };
    private static int[][][] J = {
        {
            {0, 0, 0, 0},
            {0, 6, 0, 0},
            {0, 6, 6, 6},
            {0, 0, 0, 0}
        },
        {
            {0, 0, 0, 0},
            {0, 0, 6, 6},
            {0, 0, 6, 0},
            {0, 0, 6, 0}
        },
        {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 6, 6, 6},
            {0, 0, 0, 6}
        },
        {
            {0, 0, 0, 0},
            {0, 0, 6, 0},
            {0, 0, 6, 0},
            {0, 6, 6, 0}
        }
    };
    private static int[][][] T = {
        {
            {0, 0, 0, 0},
            {0, 7, 0, 0},
            {7, 7, 7, 0},
            {0, 0, 0, 0}
        },
        {
            {0, 0, 0, 0},
            {0, 7, 0, 0},
            {0, 7, 7, 0},
            {0, 7, 0, 0}
        },
        {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {7, 7, 7, 0},
            {0, 7, 0, 0}
        },
        {
            {0, 0, 0, 0},
            {0, 7, 0, 0},
            {7, 7, 0, 0},
            {0, 7, 0, 0}
        }
    };
    public enum BlockType {O, I, S, Z, L, J, T}

    static Random random = new Random(System.currentTimeMillis());

    public static int[][] getBlock() {
        // 난수를 발생하여 만든 블럭을 반환
    }

    public static int[][] getRotateBlock(int[][] temp) {
        // 회전된 블럭을 반환
    }
````

<br>

---
#### Preview 클래스
* View를 갖고 있으며, 다음에 나올 블럭을 미리 보여준다.*

````java
int preview[][] = {
        {X, X, X, X, X, X},
        {X, 0, 0, 0, 0, X},
        {X, 0, 0, 0, 0, X},
        {X, 0, 0, 0, 0, X},
        {X, 0, 0, 0, 0, X},
        {X, X, X, X, X, X}
    };
````

<br>

---
#### Stage 클래스
* 게임을 진행하는 메인 클래스로 맵을 갖고 있고, 실시간으로 반영되는 맵의 정보를 View에 그려준다.
* 내려오는 블럭이 동작이 완료되면 다음 블럭이 내려오게 되고, 블럭의 상태, 사용자의 조작 등을 맵에 실시간으로 업데이트한다.
* 사용자 조작에 따른 블럭의 움직임을 결정할 충돌 감지 알고리즘 등 다소 복잡한 연산 알고리즘이 구현되어 있다.

````java
private final int MAP_WIDTH = 12;
private final int MAP_HEIGHT = 24;

private final int EM = 0; // Empty
private final int BK = 1; // Block
private final int FX = 2; // Fixed
private final int DN = 50; // Done
private final int SX = 80; // Side Border
private final int BX = 99; // Bottom Border

private int map[][] = {
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 1
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 2
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 3
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 4
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 5
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 6
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 7
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 8
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 9
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 10
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 11
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 12
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 13
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 14
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 15
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 16
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 17
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 18
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 19
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 20
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 21
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 22
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 23
    {SX, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, SX}, // 24
    {BX, BX, BX, BX, BX, BX, BX, BX, BX, BX, BX, BX}  // 25
};

    private void updatePreview() {
        // 다음에 나올 블럭을 그려준다.
    }
    private void updateBlock(Canvas canvas) {
        // 맵을 화면에 그려준다.
    }
    private boolean isPossibleMove(int x, int y, boolean isRotate) {
        // 충돌 감지 알고리즘
    }
    public void moveRotate(); // 블럭을 회전시킨다.
    public void moveLeft(); // 블럭을 왼쪽으로 이동시킨다.
    public void moveRight(); // 블럭을 오른쪽으로 이동시킨다.
    public void moveDown();  // 블럭을 아래로 더 빨리 이동시킨다.
````