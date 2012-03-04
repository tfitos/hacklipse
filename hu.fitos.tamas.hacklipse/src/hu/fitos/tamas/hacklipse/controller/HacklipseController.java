package hu.fitos.tamas.hacklipse.controller;

import hu.fitos.tamas.hacklipse.events.HModelChangedEvent;
import hu.fitos.tamas.hacklipse.events.HModelChangedEvent.HModelChangedEventType;
import hu.fitos.tamas.hacklipse.model.HackerElem;
import hu.fitos.tamas.hacklipse.model.HackerModel;
import hu.fitos.tamas.hacklipse.mvc.HEventBus;
import hu.fitos.tamas.hacklipse.mvc.HEventListener;
import hu.fitos.tamas.hacklipse.service.HModelService;
import hu.fitos.tamas.hacklipse.service.HackerNewsRetriever;
import hu.fitos.tamas.hacklipse.views.Hacklipse;
import hu.fitos.tamas.hacklipse.views.IHackViewListener;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class HacklipseController implements IHackViewListener{

	private HEventBus eventBus;
	private HModelService modelService;
	private Hacklipse view;
	private HackerModel model;
	
	
	public HacklipseController(Hacklipse view) {

		this.view = view;
		this.model = new HackerModel();
		// Inject the dependencies
		eventBus = new HEventBus();
		modelService = new HModelService();
		modelService.setRetriever(new HackerNewsRetriever());
		modelService.setHackerModel(model);
		modelService.setEventBus(eventBus);
		view.setModel(model);
		view.setEventBus(eventBus);
		view.setListener(this);
		view.bind();
		bind();
		
		modelService.init();
		
	}
	
	public HEventBus getEventBus() {
		return eventBus;
	}
	
	public HackerModel getModel() {
		return model;
	}
	
	protected void bind(){
		eventBus.addEventListener(HModelChangedEventType.RETRIEVED, new HEventListener<HModelChangedEvent>() {
			@Override
			public void actionPerformed(HModelChangedEvent e) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						view.updateTable();
					}
				});
			}
		});
		
		eventBus.addEventListener(HModelChangedEventType.RETRIEVE_FAILED, new HEventListener<HModelChangedEvent>() {
			@Override
			public void actionPerformed(final HModelChangedEvent e) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						view.showFailure("Failed to retrieve content: " + e.getReason());
					}
				});
			}
		});
		
	}
	
	@Override
	public void doubleClicked(HackerElem elem) {
		if(elem != null && elem.getUrl() != null){
			view.openExternalBrowser(elem.getUrl());
		}
	}
	
	@Override
	public void refreshPushed() {
		modelService.retrieveNews();
	}
	
	
}
