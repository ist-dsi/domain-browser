package module.domainBrowser.presentationTier.component.funStuff;

import java.util.Random;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Quote extends VerticalLayout {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private static final String[][] QUOTES = new String[][] {
            new String[] { "With great power, comes great responsibility.", "Stan Lee (Peter Parker)" },
            new String[] { "Use the Force, Luke.", "George Lucas (Obi-Wan)" },
            new String[] { "This is all your fault.", "George Lucas (C-3PO)" },
            new String[] { "Help me, Obi-Wan Kenobi; you're my only hope.", "George Lucas (Princess Leia)" },
            new String[] { "I sense something; a presence I've not felt since...", "George Lucas (Darth Vader)" },
            new String[] { "The Force is strong with this one.", "George Lucas (Darth Vader)" },
            new String[] { "The Force will be with you, always.", "George Lucas (Obi-Wan)" },
            new String[] { "I have a very bad feeling about this.", "George Lucas (Luke)" },
            new String[] { "That malfunctioning little twirp, this is all his fault.", "George Lucas (C-3PO)" },
            new String[] { "Your eyes can deceive you; don't trust them.", "George Lucas (Obi-Wan)" } };

    private static String[] random() {
        return QUOTES[RANDOM.nextInt(QUOTES.length)];
    }

    private final String[] q;

    public Quote() {
        q = random();
    }

    @Override
    public void attach() {
        super.attach();
        setSizeFull();
        final StringBuilder sb = new StringBuilder();
        sb.append("<i>\"");
        sb.append(q[0]);
        sb.append("\"</i>");
        final Component line1 = new Label(sb.toString(), Label.CONTENT_XHTML);
        addComponent(line1);
        setComponentAlignment(line1, Alignment.MIDDLE_LEFT);
        final Component line2 = new Label("-- " + q[1]);
        addComponent(line2);
        setComponentAlignment(line2, Alignment.MIDDLE_RIGHT);
    }

}
