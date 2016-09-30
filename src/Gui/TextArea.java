package Gui;

import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.*;

public class TextArea {

    private static String text = "You're the best around!";


    void layout(NkContext ctx, int x, int y, int WINDOW_WIDTH, int WINDOW_HEIGHT) {
        try ( MemoryStack stack = stackPush() ) {
            NkPanel layout = NkPanel.mallocStack(stack);
            NkRect rect = NkRect.mallocStack(stack);

            if ( nk_begin(ctx, layout, "Text Area", nk_rect(x, y, WINDOW_WIDTH-148, WINDOW_HEIGHT-196, rect),
                NK_WINDOW_TITLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_CLOSED)) {

                nk_layout_row_dynamic(ctx, 500, 1);
                nk_label_wrap(ctx, text);

            }
            nk_end(ctx);
        }
    }


    public void changeText(String newText) {
        text = newText;
    }
}