import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

public class rlDemo
{
    public static void main(String args[])
    {

        InitWindow(512, 512, "Demo");
        SetTargetFPS(60);

        while (!WindowShouldClose())
        {
            BeginDrawing();
            ClearBackground(RAYWHITE);
            EndDrawing();
        }

        CloseWindow();
    }
}