package textEditor;

import java.io.File;
import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;

public class TextEditorMainContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof File) {
			return ((File) element).listFiles();
		}
		
		return null;
	}

	@Override
	public Object[] getElements(Object element) {
		if (element instanceof Collection) {
			return ((Collection<?>) element).toArray();
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof File) {
			return ((File) element).getParentFile();
		}
		
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof File) {
			File file = (File) element;
			return file.isDirectory() && file.listFiles() != null;
		} else {
			return false;
		}
	}
}
