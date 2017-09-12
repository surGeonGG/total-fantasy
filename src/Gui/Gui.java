package Gui;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Gui {

    List<GuiElement> guiElements = new ArrayList<>();
    Hashtable<String, Font> fonts = new Hashtable<>();

    public Gui() {
        initFonts();

        TextBox textBox1 = new TextBox(new Vector2f(-1f, 0.9f), new Vector2f(2f, 0.1f));
        TextBox textBox2 = new TextBox(new Vector2f(-0.5f, -0.5f), new Vector2f(1f, 1f));

        textBox1.addText(0, "", fonts.get("garuda"), 1);
        textBox2.addText(0, "A constitutional monarchy is a form of monarchy in which the sovereign " +
                "exercises their authorities in accordance with a written or unwritten constitution.[1] Constitutional" +
                " monarchy differs from absolute monarchy (in which a monarch holds absolute power), in that constitutional" +
                " monarchs are bound to exercise their powers and authorities within the limits prescribed within an" +
                " established legal framework. Constitutional monarchies range from countries such as Morocco, where " +
                "the constitution grants substantial discretionary powers to the sovereign, to countries such as Sweden " +
                "or Denmark where the monarch retains very few formal authorities.", fonts.get("garuda"), 1f);

        guiElements.add(textBox1);
        guiElements.add(textBox2);
    }

    private void initFonts() {
        Font garuda = new Font("garuda");
        Font dejavusans = new Font("dejavusans");
        fonts.put("garuda", garuda);
        fonts.put("dejavu", dejavusans);
    }

    public List<GuiElement> getGuiElements() {
        return guiElements;
    }
}
