package hu.fitos.tamas.hacklipse.views;


import hu.fitos.tamas.hacklipse.Activator;
import hu.fitos.tamas.hacklipse.controller.HacklipseController;
import hu.fitos.tamas.hacklipse.model.HackerElem;
import hu.fitos.tamas.hacklipse.model.HackerModel;
import hu.fitos.tamas.hacklipse.mvc.HEventBus;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
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
	private IDoubleClickListener doubleClickListener;
	
	private Action action1;
	private Action action2;
	
	private HEventBus eventBus;
	private HackerModel model;
	private IHackViewListener listener;
	
	

	
	
	public void setListener(IHackViewListener listener){
		this.listener = listener;
	}
	
	/**
	 * The constructor.
	 */
	public Hacklipse() {
		System.out.println("!!! CONSTRUCTOR");
	}
	
	
	public void setModel(HackerModel model) {
		this.model = model;
	}
	
	public void setEventBus(HEventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void updateTable(){
		
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(model.getHackerElems());
		
		viewer.setLabelProvider(new ColumnLabelProvider(){
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
		System.out.println("!!! INIT");
	}
	
	public void createPartControl(Composite parent) {
		System.out.println("!!! CREATEPARTCONTROL");
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		build();
		new HacklipseController(this);
		//makeActions();
		//contributeToActionBars();
	}
	
	public void build(){
		doubleClickListener = new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				listener.doubleClicked((HackerElem)obj);
			}
		};
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
		System.out.println("!!! DISPOSE");
	}
	
	public void bind(){
		viewer.addDoubleClickListener(doubleClickListener);
	}
	
	public void unbind(){
		viewer.removeDoubleClickListener(doubleClickListener);
	}
	
	public void openExternalBrowser(String url){
		IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
		try {
			browserSupport.getExternalBrowser().openURL(new URL(url));
		} catch (PartInitException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	

	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Hacklipse",
			message);
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	
	
	
	
	
	
	
	
	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}
	
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				Hacklipse.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		
		ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/nav_refresh.gif");
		action1.setImageDescriptor(imageDescriptor);
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}
	
}