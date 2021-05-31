package textEditor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ChangeDirDialog extends Dialog {

	private Text newDirInput;
	private String dir = "";
	
	protected ChangeDirDialog(Shell parentShell) {
		super(parentShell);
	}

	public String getDir() {
		return dir;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite inputArea = (Composite) super.createDialogArea(parent);
		
		GridLayout inputAreaLayout = new GridLayout(2, false);
		inputAreaLayout.marginRight = 15;
		inputAreaLayout.marginLeft = 15;
		inputArea.setLayout(inputAreaLayout);
		
		Label inputLabel = new Label(inputArea, SWT.NONE);
		inputLabel.setText("Change directory to: ");
		
		newDirInput = new Text(inputArea, SWT.BORDER);
		newDirInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		newDirInput.setText(dir);
		newDirInput.addModifyListener(e -> {
			Text textWidget = (Text)e.getSource();
			dir = textWidget.getText();
		});
		
		return inputArea;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 120);
	}
}