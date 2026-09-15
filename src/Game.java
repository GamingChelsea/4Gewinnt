import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

public class Game
{
    Chip[][] board;
    boolean turn;

    int CHIP_SIZE = 10;

    enum Chip
    {
        EMPTY,
        PLAYER_1,
        PLAYER_2,
    }

    Game(){}

    public void run()
    {
        InitWindow(512, 512, "Demo");
        SetTargetFPS(60);

        this.init();

        while (!WindowShouldClose())
        {
            if (IsKeyPressed(KEY_SPACE))
            {
                this.updateBoard();
            }

            BeginDrawing();
            ClearBackground(BLACK);
            for (int y = 5 - 1; y >= 0; y--)
            {
                for (int x = 0; x < 6; x++)
                {
                    if (this.board[x][y] == Chip.EMPTY)
                    DrawRectangle(x*40, y * 40,CHIP_SIZE ,CHIP_SIZE, WHITE);
                    else if (this.board[x][y] == Chip.PLAYER_1)
                    DrawRectangle(x*40, y * 40,CHIP_SIZE ,CHIP_SIZE, BLUE);
                    else
                    DrawRectangle(x*40, y * 40,CHIP_SIZE ,CHIP_SIZE, RED);
                    
                }
            }
            EndDrawing();
        }

        CloseWindow();
    }

    public void init()
    {
        board = new Chip[6][5];
        for (int y = 5 - 1; y >= 0; y--)
        {
            for (int x = 0; x < 6; x++)
            {
                this.board[x][y] = Chip.EMPTY;
            }
        }
    }

    public void resetRound(){}

    public void resetGame(){}

    public void updateBoard()
    {
       for (int y = 5 - 1; y >= 0; y--)
        {
            for (int x = 0; x < 6; x++)
            {
                if (y != 4)
                {
                    if (this.board[x][y + 1] == Chip.EMPTY)
                    {
                        this.board[x][y + 1] = this.board[x][y];
                        this.board[x][y] = Chip.EMPTY;
                    }
                }
            }
        } 
    }

    public void checkWinner(){}

    public void placeChip(Chip player, int pos)
    {
        if(pos >= 0 && pos < 6)
        {
            if(this.board[pos][0] == Chip.EMPTY)
            {
                this.board[pos][0] = player;
            }
        }
    }

    public static void main(String args[])
    {
        Game game = new Game();
        game.run();
    }
}

