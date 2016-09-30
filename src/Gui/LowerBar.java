package Gui;

import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.*;

class LowerBar {

    NkColor background;

    LowerBar() {
        background = NkColor.create();
        nk_rgb(28, 48, 62, background);
    }

    void layout(NkContext ctx, int x, int y, int WINDOW_WIDTH, int WINDOW_HEIGHT) {
        try ( MemoryStack stack = stackPush() ) {
            NkPanel layout = NkPanel.mallocStack(stack);
            NkRect rect = NkRect.mallocStack(stack);

            if ( nk_begin(ctx, layout, "Actions:", nk_rect(x, y, WINDOW_WIDTH-148, 100, rect), NK_WINDOW_TITLE)) {

                nk_layout_row_static(ctx, 30, 80, 9);
                if ( nk_button_label(ctx, "Talk") )
                    System.out.println("button pressed");
                if ( nk_button_label(ctx, "Attack") )
                    System.out.println("button pressed");
                if ( nk_button_label(ctx, "Spell") )
                    System.out.println("button pressed");
                if ( nk_button_label(ctx, "Ability") )
                    System.out.println("button pressed");
                if ( nk_button_label(ctx, "Run") )
                    System.out.println("button pressed");

            }
            nk_end(ctx);
        }
    }
}