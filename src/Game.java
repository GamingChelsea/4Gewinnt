import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

public class Game {
    Chip[][] board;
    boolean turn;
    boolean needToReset = false;

    int boardWidth = 10;
    int boardHeight = 12;

    int CHIP_SIZE = 10;

    int zeigerX = 0;

    int frame = 0;

    int lastx;
    int lasty;

    int current_streak = 0;

    boolean dirty = false;

    Player[] players = new Player[2];

    enum Chip {
        EMPTY,
        PLAYER_1,
        PLAYER_2,
        Test_1,
        Test_2,
    }

    Game() {
    }

    private void run() {
        InitWindow(512, 512, "Demo");
        SetTargetFPS(60);

        this.init();

        while (!WindowShouldClose()) {
            if (IsKeyPressed(KEY_R)) {
                this.resetRound();
            }
            if (IsKeyPressed(KEY_E)) {
                this.resetGame();
            }
            if (IsKeyPressed(KEY_LEFT)) {
                this.zeigerX = Math.clamp(this.zeigerX - 1, 0, this.board.length - 1);
            }

            if (IsKeyPressed(KEY_RIGHT)) {
                this.zeigerX = Math.clamp(this.zeigerX + 1, 0, this.board.length - 1);
            }

            if (IsKeyPressed(KEY_X) && !this.dirty) {
                if (turn == false)
                    this.placeChip(Chip.PLAYER_1, this.zeigerX);
                else
                    this.placeChip(Chip.PLAYER_2, this.zeigerX);
                turn = !turn;
                this.dirty = true;
                this.lastx = this.zeigerX;
                updateBoard();
            }

            BeginDrawing();
            ClearBackground(BLACK);
            for (int y = this.board[0].length - 1; y >= 0; y--) {
                for (int x = 0; x < this.board.length; x++) {
                    if (this.board[x][y] == Chip.EMPTY)
                        DrawRectangle(x * 40, y * 40 + 40, CHIP_SIZE, CHIP_SIZE, WHITE);
                    else if (this.board[x][y] == Chip.PLAYER_1)
                        DrawRectangle(x * 40, y * 40 + 40, CHIP_SIZE, CHIP_SIZE, BLUE);
                    else if (this.board[x][y] == Chip.PLAYER_2)
                        DrawRectangle(x * 40, y * 40 + 40, CHIP_SIZE, CHIP_SIZE, RED);
                    else if (this.board[x][y] == Chip.Test_1)
                        DrawRectangle(x * 40, y * 40 + 40, CHIP_SIZE, CHIP_SIZE, GREEN);
                    else
                        DrawRectangle(x * 40, y * 40 + 40, CHIP_SIZE, CHIP_SIZE, ORANGE);

                }
            }
            DrawRectangle(40 * zeigerX, 10, CHIP_SIZE, CHIP_SIZE, BEIGE);

            if (this.needToReset) {
                ClearBackground(BLACK);
                DrawText("Press R to reset round", 10, 10, 24, WHITE);
                DrawText("Press E to reset game", 10, 40, 24, WHITE);
            }

            // Score
            DrawText(Integer.toString(players[0].getWins()), 10, 400, 24, BLUE);
            DrawText(Integer.toString(players[1].getWins()), 10, 450, 24, RED);
            EndDrawing();

            this.frame++;
        }

        CloseWindow();
    }

    private void init() {
        board = new Chip[boardWidth][boardHeight];
        for (int y = this.board[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < this.board.length; x++) {
                this.board[x][y] = Chip.EMPTY;
            }
        }
        players[0] = new Player("Beul");
        players[1] = new Player("Fred");
    }

    private void resetRound() {
        for (int y = this.board[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < this.board.length; x++) {
                this.board[x][y] = Chip.EMPTY;
            }
        }
        this.turn = false;
        this.zeigerX = 0;
        this.dirty = false;
        this.needToReset = false;
    }

    private void resetGame() {
        for (int y = this.board[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < this.board.length; x++) {
                this.board[x][y] = Chip.EMPTY;
            }
        }
        players[0].setWins(0);
        players[1].setWins(0);
        this.turn = false;
        this.zeigerX = 0;
        this.dirty = false;
        this.needToReset = false;
    }

    private void updateBoard() {
        while (dirty) {
            for (int y = 0; y < this.board[0].length; y++) {
                // theoretisch warten
                // System.out.println(y);
                if (y == this.board[0].length - 1) {
                    lasty = this.board[0].length - 1;
                    break;
                }

                if (this.board[this.zeigerX][y + 1] == Chip.EMPTY) {
                    // System.out.println("Frei");
                    this.board[this.zeigerX][y + 1] = this.board[this.zeigerX][y];
                    this.board[this.zeigerX][y] = Chip.EMPTY;
                } else {
                    // System.out.println("Belegt");
                    lasty = y;
                    break;
                }
            }
            this.dirty = false;
        }
        checkWinner();
    }

    private void checkWinner() {
        int x_pos = this.lastx, y_pos = this.lasty;

        int[][] directions = { { 1, 0 }, { 0, 1 }, { 1, 1 }, { 1, -1 } };
        for (int[] direction : directions) {
            for (int j = -3; j <= 3; j++) {
                int x = x_pos + (direction[0] * j);
                int y = y_pos + (direction[1] * j);
                if (x < 0 || x >= this.board.length || y < 0 || y >= this.board[0].length) {
                    continue;
                }
                if (countFour(x, y, this.board[lastx][lasty])) {
                    System.out.println("Gewonnen!");
                    if (this.board[lastx][lasty] == Chip.PLAYER_1) {
                        players[0].setWins(players[0].getWins() + 1);
                    } else if (this.board[lastx][lasty] == Chip.PLAYER_2) {
                        players[1].setWins(players[1].getWins() + 1);
                    }
                    this.needToReset = true;
                }
            }
            this.current_streak = 0;
        }

    }

    private boolean countFour(int x, int y, Chip expected) {
        if (this.board[x][y] == expected) {
            this.current_streak++;
        } else {
            this.current_streak = 0;
        }
        if (this.current_streak >= 4) {
            return true;
        }
        return false;
    }

    private void placeChip(Chip player, int pos) {
        if (pos >= 0 && pos < this.board.length) {
            if (this.board[pos][0] == Chip.EMPTY) {
                this.board[pos][0] = player;
            }
        }
    }

    public static void main(String args[]) {
        Game game = new Game();
        game.run();
    }
    if(1==1)
    {
        System.out.println("1 == 1 OMG WOASSS");
    }
}
