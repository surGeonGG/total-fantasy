package Gui;

import Entities.Player;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.*;

class Sidebar {

    void layout(NkContext ctx, int x, int y, int WINDOW_WIDTH, int WINDOW_HEIGHT, Player player) {
        try ( MemoryStack stack = stackPush() ) {
            NkPanel layout = NkPanel.mallocStack(stack);
            NkRect rect = NkRect.mallocStack(stack);

            if ( nk_begin(ctx, layout, "Demo", nk_rect(x, y, 150, WINDOW_HEIGHT, rect), NK_WINDOW_BORDER)) {

                nk_layout_row_dynamic(ctx, 30, 1);

                nk_label(ctx, "Character:", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 25, 1);

                nk_layout_row_dynamic(ctx, 25, 2);
                nk_label(ctx, "STR", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(player.getStats()[0]), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "CON", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(player.getStats()[1]), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "DEX", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(player.getStats()[2]), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "INT", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(player.getStats()[3]), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "WIS", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(player.getStats()[4]), NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, "CHA", NK_TEXT_ALIGN_LEFT);
                nk_label(ctx, Integer.toString(player.getStats()[5]), NK_TEXT_ALIGN_LEFT);


            }
            nk_end(ctx);
        }
    }
}