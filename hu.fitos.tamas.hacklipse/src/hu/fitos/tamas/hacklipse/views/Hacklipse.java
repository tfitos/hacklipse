package hu.fitos.tamas.hacklipse.views;


import hu.fitos.tamas.hacklipse.Activator;
import hu.fitos.tamas.hacklipse.controller.HacklipseController;
import hu.fitos.tamas.hacklipse.model.HackerElem;
import hu.fitos.tamas.hacklipse.model.HackerModel;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class Hacklipse extends ViewPart {

	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "hu.fitos.tamas.hacklipse.views.Hacklipse";

	private TableViewer viewer;
	private Action refreshAction;
	
	private Action action1;
	private Action action2;
	
	private HackerModel model;
	private IHackViewListener listener;
	
	
	public void setListener(IHackViewListener listener){
		this.listener = listener;
	}
	
	/**
	 * The constructor.
	 */
	public Hacklipse() {
		Activator.getDefault().getLog().log(new Status(IStatus.OK, Activator.PLUGIN_ID, "constructor"));
	}
	
	
	public void setModel(HackerModel model) {
		this.model = model;
	}
	
	public void updateTable(){
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(model.getHackerElems());
		
	}
	
	public void showFailure(final String message){
		viewer.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			}
			@Override
			public void dispose() {
			}
			
			@Override
			public Object[] getElements(Object arg0) {
				String[] msgArr = {message};
				return msgArr;
			}
		});
		viewer.setInput(getViewSite());
	}
	
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		Activator.getDefault().getLog().log(new Status(IStatus.OK, Activator.PLUGIN_ID, "Init"));
	}
	
	public void createPartControl(Composite parent) {
		Activator.getDefault().getLog().log(new Status(IStatus.OK, Activator.PLUGIN_ID, "createPartControl"));
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		build();
		new HacklipseController(this);
	}
	
	public void build(){
		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(viewer.getTable()),
				new TextCellEditor(viewer.getTable()),
				new TextCellEditor(viewer.getTable()) });
		ColumnViewerEditorActivationStrategy strategy = new ColumnViewerEditorActivationStrategy(viewer);
		TableViewerEditor.create(viewer, strategy,
				ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION);
		
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(400);
		column.getColumn().setMoveable(true);
		column.getColumn().setText("Title");
		column.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof HackerElem){
					HackerElem hElem = (HackerElem)element;
					return new StringBuffer().append(String.format("%02d",hElem.getNumber())).append(", ").append(hElem.getTitle()).append(" ").append(hElem.getDomain()).toString();
				}else if(element instanceof String){
					return (String)element;
				}
				return "";
			}
		});
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.getColumn().setText("Comments");
		column.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof HackerElem){
					HackerElem hElem = (HackerElem)element;
					return hElem.getComment();
				}
				return "";
			}
		});
		column.getColumn().addListener(1, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				System.out.println("EVENT 1");
			}
		});
		column.getColumn().addListener(2, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				System.out.println("EVENT 2");
			}
		});
		column.getColumn().addListener(3, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				System.out.println("EVENT 3");
			}
		});
		
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.getColumn().setText("User");
		column.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof HackerElem){
					HackerElem hElem = (HackerElem)element;
					return hElem.getUsername();
				}
				return "";
			}
		});
		
		viewer.getTable().setHeaderVisible(true);
		
		
		refreshAction = new Action() {
			@Override
			public void run() {
				listener.refreshPushed();
			}
		};
		
		refreshAction.setText("Reload news...");
		refreshAction.setToolTipText("Reload news...");
		ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/nav_refresh.gif");
		refreshAction.setImageDescriptor(imageDescriptor);
		
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(refreshAction);
	}
	
	
	
	@Override
	public void dispose() {
		super.dispose();
		Activator.getDefault().getLog().log(new Status(IStatus.OK, Activator.PLUGIN_ID, "Dispose"));
	}
	
	public void bind(){
		viewer.getTable().addListener(SWT.MouseDoubleClick, new Listener() {

			public void handleEvent(Event event) {
				Table table = viewer.getTable();
				TableItem[] selection = table.getSelection();

				if (selection.length != 1) {
					return;
				}

				TableItem item = table.getSelection()[0];
				if(item.getData() instanceof HackerElem){
					HackerElem elem = (HackerElem)item.getData();
					for (int i = 0; i < table.getColumnCount(); i++) {
						if (item.getBounds(i).contains(event.x, event.y)) {
							if(i == 0){
								listener.titleDoubleClicked(elem);
							}else if(i == 1){
								listener.commentsDoubleClicked(elem);
							}else if(i == 2){
								listener.userDoubleClicked(elem);
							}
							return;
						}
					}
				}
			}

		});
	}
	
	public void unbind(){
	}
	
	public void openExternalBrowser(String url){
		IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
		try {
			browserSupport.getExternalBrowser().openURL(new URL(url));
		} catch (PartInitException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		} catch (MalformedURLException e) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
}