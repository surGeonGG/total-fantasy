package Gui;

import Main.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class Gui {

    Stack<GuiWindow> guiWindows = new Stack<>();
    Hashtable<String, Font> fonts = new Hashtable<>();
    Hashtable<String, Vector4f> colors = new Hashtable<>();

    public Gui() {
        initFonts();
        initColors();

        TextBox textBox1 = new TextBox(new Vector2f(-1f, 0.9f), new Vector2f(2f, 0.1f), colors.get("black"));
        TextBox textBox2 = new TextBox(new Vector2f(-0.5f, -0.5f), new Vector2f(1f, 1f), colors.get("black"));

        textBox1.addText(0, "", fonts.get("garuda"), 1);
        textBox2.addText(0, "A constitutional monarchy is a form of monarchy in which the sovereign " +
                "exercises their authorities in accordance with a written or unwritten constitution.[1] Constitutional" +
                " monarchy differs from absolute monarchy (in which a monarch holds absolute power), in that constitutional" +
                " monarchs are bound to exercise their powers and authorities within the limits prescribed within an" +
                " established legal framework. Constitutional monarchies range from countries such as Morocco, where " +
                "the constitution grants substantial discretionary powers to the sovereign, to countries such as Sweden " +
                "or Denmark where the monarch retains very few formal authorities.", fonts.get("garuda"), 1f);

        guiWindows.push(textBox1);
        guiWindows.add(textBox2);
    }

    private void initColors() {
        colors.put("black", new Vector4f(0,0,0,1));
        colors.put("white", new Vector4f(1,1,1,1));

    }

    private void initFonts() {
        Font garuda = new Font("garuda");
        Font dejavusans = new Font("dejavusans");
        fonts.put("garuda", garuda);
        fonts.put("dejavu", dejavusans);
    }

    public List<GuiWindow> getGuiWindows() {
        return guiWindows;
    }

    public boolean guiWindowAtMouseCoord() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(Window.getWindowID(), posX, posY);
        float mouseX = (float) posX.get();
        float mouseY = (float) posY.get();
        for (int i = guiWindows.size() - 1; i >= 0; i--) {
            if (guiWindows.get(i).coordWithinWindow(mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }

    public GuiElement getGuiElementAtMouseCoord() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(Window.getWindowID(), posX, posY);
        float mouseX = (float) posX.get();
        float mouseY = (float) posY.get();
        for (int i = guiWindows.size() - 1; i >= 0; i--) {
            if (guiWindows.get(i).coordWithinWindow(mouseX, mouseY)) {
                return guiWindows.get(i).getElementAt(mouseX, mouseY);
            }
        }
        return null;
    }
}
