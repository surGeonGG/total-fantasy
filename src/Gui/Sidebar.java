package Gui;

import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.*;

class Sidebar {

    private static final int EASY = 0;
    private static final int HARD = 1;


    NkColor[] table;
    NkColor nkColor2;
    NkColor nkColor3;
    NkColor nkColor4;
    NkColor nkColor5;


    private int op = EASY;

    private IntBuffer compression = BufferUtils.createIntBuffer(1).put(0, 20);

    Sidebar() {

    }

    void layout(NkContext ctx, int x, int y, int WINDOW_WIDTH, int WINDOW_HEIGHT) {
        try ( MemoryStack stack = stackPush() ) {
            NkPanel layout = NkPanel.mallocStack(stack);
            NkRect rect = NkRect.mallocStack(stack);

            if ( nk_begin(ctx, layout, "Demo", nk_rect(x, y, 200, WINDOW_HEIGHT, rect), NK_WINDOW_BORDER)) {

                nk_layout_row_static(ctx, 30, 80, 1);
                if ( nk_button_label(ctx, "button") )
                    System.out.println("button pressed");

                //nk_style_from_table(ctx, nkColor3);
                nk_layout_row_dynamic(ctx, 30, 2);
                if ( nk_option_label(ctx, "easy", op == EASY) ) op = EASY;
                if ( nk_option_label(ctx, "hard", op == HARD) ) op = HARD;

                //nk_style_from_table(ctx, nkColor4);
                nk_layout_row_dynamic(ctx, 25, 1);
                nk_property_int(ctx, "Compression:", 0, compression, 100, 10, 1);

                NkPanel combo = NkPanel.mallocStack(stack);
                nk_layout_row_dynamic(ctx, 20, 1);

                nk_label(ctx, "background:", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 25, 1);

                nk_layout_row_dynamic(ctx, 25, 1);
                nk_label(ctx, "This is a minimal state immediate mode graphical user interface toolkit written in ANSI C and licensed under public domain.", NK_TEXT_ALIGN_LEFT);

                nk_layout_row_dynamic(ctx, 500, 1);
                nk_label_wrap(ctx, "This is a minimal state immediate mode graphical user interface toolkit written in ANSI C and licensed under public domain.");


            }
            nk_end(ctx);
        }
    }
}