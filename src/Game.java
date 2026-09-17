import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;


public class Game
{
    Chip[][] board;
    boolean turn;

    int CHIP_SIZE = 10;

    int zeigerX;

    int frame = 0;

    int lastx;
    int lasty;

    boolean dirty = false;

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

            if (IsKeyPressed(KEY_LEFT))
            {
                this.zeigerX = Math.clamp(this.zeigerX - 1, 0, 5);
            }

            if (IsKeyPressed(KEY_RIGHT))
            {
                this.zeigerX = Math.clamp(this.zeigerX + 1, 0, 5);
            }

            if (IsKeyPressed(KEY_X) && !this.dirty)
                {
                    if(turn == false)
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
            for (int y = 5 - 1; y >= 0; y--)
            {
                for (int x = 0; x < 6; x++)
                {
                    if (this.board[x][y] == Chip.EMPTY)
                    DrawRectangle(x*40, y * 40 + 40,CHIP_SIZE ,CHIP_SIZE, WHITE);
                    else if (this.board[x][y] == Chip.PLAYER_1)
                    DrawRectangle(x*40, y * 40 + 40,CHIP_SIZE ,CHIP_SIZE, BLUE);
                    else
                    DrawRectangle(x*40, y * 40 + 40,CHIP_SIZE ,CHIP_SIZE, RED);
                    
                }
            }
            DrawRectangle(40 * zeigerX, 10, CHIP_SIZE, CHIP_SIZE, BEIGE);
            EndDrawing();

            this.frame++;
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
        this.zeigerX = 1;
    }

    public void resetRound(){}

    public void resetGame(){}

    public void updateBoard()
    {
        while (dirty)
        {
            for (int y = 0; y < 5; y++)
            {
                // theoretisch warten
                // System.out.println(y);
                if (y == 4)
                {
                    lasty = 4;
                    break;
                }

                if (this.board[this.zeigerX][y + 1] == Chip.EMPTY){
                    // System.out.println("Frei");
                    this.board[this.zeigerX][y + 1] = this.board[this.zeigerX][y];
                    this.board[this.zeigerX][y] = Chip.EMPTY;
                } 
                else
                {
                    // System.out.println("Belegt");
                    lasty = y;
                    break;
                }
            }
            this.dirty = false;
        }
        checkWinner();
    }

    public void checkWinner()
    {
        int streak = 0;
        Chip currentChip = this.board[this.zeigerX][this.lasty];
        System.out.println(lasty);
        // Horizontal
        for (int x = 0; x < 6; x++)
        {
            if (this.board[x][this.lasty] == currentChip)
            {
                streak++;
                // System.out.println("CURRENT STREAK" + streak);
            }
            else if (!(streak >= 4))
            {
                streak = 0;
                // System.out.println("STREAK VERLOREN");
            }     
        }
        if (streak >= 4)
        {
            // System.out.println("GEWONNEN");
        }
        
        // Vertical
        for (int y = 0; y < 5; y++)
        {
            if (this.board[this.lastx][y] == currentChip)
            {
                streak++;
                // System.out.println("CURRENT STREAK" + streak);
            }
            else if (!(streak >= 4))
            {
                streak = 0;
                // System.out.println("STREAK VERLOREN");
            }     
        }
        if (streak >= 4)
        {
            // System.out.println("GEWONNEN");
        }

        // Diagonal
    }

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

