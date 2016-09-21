package Gui;

import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.*;

class Sidebar {

    int str = 12, con = 14, dex = 11, wis = 18, intelligence = 21, cha = 9;


    Sidebar() {

    }

    void layout(NkContext ctx, int x, int y, int WINDOW_WIDTH, int WINDOW_HEIGHT) {
        try ( MemoryStack stack = stackPush() ) {
            NkPanel layout = NkPanel.mallocStack(stack);
            NkRect rect = NkRect.mallocStack(stack);

            if ( nk_begin(ctx, layout, "Demo", nk_rect(x, y, 150, WINDOW_HEIGHT, rect), NK_WINDOW_BORDER)) {

                nk_layout_row_dynamic(ctx, 30, 1);

                nk_label(ctx, "Character:", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 25, 1);

                nk_layout_row_dynamic(ctx, 25, 2);
                nk_label(ctx, "STR", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(str), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "CON", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(con), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "DEX", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(dex), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "INT", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(intelligence), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "WIS", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(wis), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "CHA", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(cha), NK_TEXT_ALIGN_LEFT);


            }
            nk_end(ctx);
        }
    }
}