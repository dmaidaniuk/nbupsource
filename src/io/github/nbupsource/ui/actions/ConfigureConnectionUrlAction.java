package io.github.nbupsource.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Project",
        id = "io.github.nbupsource.ui.actions.ConfigureConnectionUrlAction"
)
@ActionRegistration(
        displayName = "#CTL_ConfigureConnectionUrlAction"
)
@ActionReference(path = "Projects/Actions", position = 0, separatorBefore = -50, separatorAfter = 50)
@Messages("CTL_ConfigureConnectionUrlAction=Upsource Connection")
public final class ConfigureConnectionUrlAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String txt = "Upsource URL: ";
        String title = "Input URL to Upsource web server";

        NotifyDescriptor.InputLine input = new NotifyDescriptor.InputLine(txt, title);
        input.setInputText("http://your-upsource-host");
        Object result = DialogDisplayer.getDefault().notify(input);
        if (result == NotifyDescriptor.OK_OPTION) {
            String userInput = input.getInputText();
            NotifyDescriptor nd = new NotifyDescriptor.Message(userInput, NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
        }
    }
}
