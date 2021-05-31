package textEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;


	public class TextEditorMain {
		
		private Shell textEditorShell;
		private CLabel explorerDirLabel;
		private TreeViewer contentViewer;
		private File currentFile;
		private Text fileContent;
		private String editorDir = System.getProperty("user.home");

		public static void main(String[] args) {
			try {
				TextEditorMain app = new TextEditorMain();
				app.open();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void open() {
			Display display = Display.getDefault();
			createContents();
			
			textEditorShell.open();
		
			while (!textEditorShell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
		
		private void createContents() {
			
			textEditorShell = new Shell(Display.getDefault());
			textEditorShell.setText("TextEditor: The greatest gedit replacement yet");
			textEditorShell.setLayout(new GridLayout(1, false));
			
			Composite topBar = new Composite(textEditorShell, SWT.NONE);
			
			RowLayout menuLayout = new RowLayout();
			menuLayout.center = true;
			topBar.setLayout(menuLayout);
			
			//Created the "File" Menu
			Menu menuBar = new Menu(textEditorShell, SWT.BAR);
			MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
			fileMenuHeader.setText("&File");
			
			Menu fileMenu = new Menu(textEditorShell, SWT.DROP_DOWN);
			fileMenuHeader.setMenu(fileMenu);
			
			MenuItem changeDirItem = new MenuItem(fileMenu, SWT.PUSH);
			changeDirItem.setText("&Change Directory");
			
			MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
			fileSaveItem.setText("&Save");
			
			MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
			fileExitItem.setText("E&xit");
			
			changeDirItem.addSelectionListener(new changeDirItemListener());
			fileSaveItem.addSelectionListener(new fileSaveItemListener());
			fileExitItem.addSelectionListener(new fileExitItemListener());
			
			
			CLabel mainLabel = new CLabel(topBar, SWT.NONE);
			mainLabel.setText("Current Directory:");
			explorerDirLabel = new CLabel(topBar, SWT.NONE);
			explorerDirLabel.setText(editorDir);
			
			textEditorShell.setMenuBar(menuBar);
			
			//Creating the file/text viewers
			Composite mainExplorerWindow = new Composite(textEditorShell, SWT.NONE);
			mainExplorerWindow.setLayout(new GridLayout(2, true));
			GridData fillGrid = new GridData(SWT.FILL, SWT.FILL, true, true);
			mainExplorerWindow.setLayoutData(fillGrid);
		
			
			contentViewer = new TreeViewer(mainExplorerWindow);
			fileContent = new Text(mainExplorerWindow, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);	
			
			contentViewer.getTree().setLayoutData(fillGrid);
			fileContent.setLayoutData(fillGrid);
			fileContent.setEditable(true);
			
			contentViewer.setContentProvider(new TextEditorMainContentProvider());
			contentViewer.setLabelProvider(new LabelProvider() {
				
				@Override
				public String getText(Object element) {
					if (element instanceof File) {
						File file = (File) element;
						return file.getName();
					}
					
					return null;
				}
				
			});
			
			contentViewer.getTree().addSelectionListener(new SelectionAdapter() {
			
				@Override
				public void widgetSelected(SelectionEvent e) {
					TreeItem item = (TreeItem) e.item;
					
					if (item.getData() instanceof File) {
						File selectedFile = (File) item.getData();
						currentFile = selectedFile;
						
						int extensionStartIndex = selectedFile.getName().indexOf('.');
						if (extensionStartIndex > 0) {
							String fileExt = selectedFile.getName().substring(extensionStartIndex + 1);
							if (fileExt.contentEquals("txt")) {							
								FileInputStream fileStream;
								try {
									fileStream = new FileInputStream(selectedFile);
									fileContent.setText(new String(fileStream.readAllBytes()));
								} catch (IOException e1) {
									e1.printStackTrace();
								}							
							}
						}		
					}
					
					contentViewer.refresh();
				}
				
			});
			
			//this could be added as a function instead of just chilling here
			//like initiliazeViewer();
			File startDirectory = new File(editorDir);
			contentViewer.setInput(Arrays.asList(startDirectory.listFiles()));
					
		}
		
		class fileExitItemListener implements SelectionListener {
		    public void widgetSelected(SelectionEvent event) {
		      textEditorShell.close();
		      Display.getDefault().dispose();
		    }

		    public void widgetDefaultSelected(SelectionEvent event) {
		    	textEditorShell.close();
		      Display.getDefault().dispose();
		    }
		  }

		  class fileSaveItemListener implements SelectionListener {
		    public void widgetSelected(SelectionEvent event) {
		    	if (currentFile != null) {
		    		FileOutputStream outStream = null;
		    		 try {
		    			 outStream = new FileOutputStream(currentFile);
		    			 byte[] bytesArray = fileContent.getText().getBytes();

		    			 outStream.write(bytesArray);
		    			 outStream.flush();
		    			 
		    		 }catch (IOException e1) {
							e1.printStackTrace();
		    		 }		
		    		 
		    		 finally {
		    			  try {
		    			     if (outStream != null){
		    			    	 outStream.close();
		    			     }
		    		      }catch (IOException e1) {
								e1.printStackTrace();
		    		      }	
		    		 }
		    		
		    	}
		    }

		    public void widgetDefaultSelected(SelectionEvent event) {
		    	if (currentFile != null) {
		    		FileOutputStream outStream = null;
		    		 try {
		    			 outStream = new FileOutputStream(currentFile);
		    			 byte[] bytesArray = fileContent.getText().getBytes();

		    			 outStream.write(bytesArray);
		    			 outStream.flush();
		    			 
		    			 
		    		 }catch (IOException e1) {
							e1.printStackTrace();
		    		 }		
		    		 
		    		 finally {
		    			  try {
		    			     if (outStream != null){
		    			    	 outStream.close();
		    			     }
		    		      }catch (IOException e1) {
								e1.printStackTrace();
		    		      }	
		    		 }
		    		
		    	}
		    }
		  }
		  
		  class changeDirItemListener implements SelectionListener {
			    public void widgetSelected(SelectionEvent event) {
			      
			    	ChangeDirDialog dialog = new ChangeDirDialog(textEditorShell);
					
					if (dialog.open() == Window.OK) {
						editorDir = dialog.getDir();
						File newDir = new File(editorDir);
					
						if (newDir.listFiles() != null) {
							contentViewer.setInput(Arrays.asList(newDir.listFiles()));
							explorerDirLabel.setText(editorDir);
							currentFile = null;
							fileContent.setText("");
						}
					}
			    }

			    public void widgetDefaultSelected(SelectionEvent event) {
			    	
			    	ChangeDirDialog dialog = new ChangeDirDialog(textEditorShell);
					
					if (dialog.open() == Window.OK) {
						editorDir = dialog.getDir();
						File newDir = new File(editorDir);
					
						if (newDir.listFiles() != null) {
							contentViewer.setInput(Arrays.asList(newDir.listFiles()));
							explorerDirLabel.setText(editorDir);
							currentFile = null;
							fileContent.setText("");
						}
					}
			    }
		  }

	}
	
	
