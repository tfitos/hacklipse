package hu.fitos.tamas.hacklipse.service;

import hu.fitos.tamas.hacklipse.events.HModelChangedEvent;
import hu.fitos.tamas.hacklipse.events.HModelChangedEvent.HModelChangedEventType;
import hu.fitos.tamas.hacklipse.model.HackerElem;
import hu.fitos.tamas.hacklipse.model.HackerModel;
import hu.fitos.tamas.hacklipse.mvc.HEventBus;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

public class HModelService {
	
	private HEventBus eventBus;
	private HackerModel hackerModel;
	private IHRetriever retriever;
	
	public HModelService() {
	}
	
	public void setEventBus(HEventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void setRetriever(IHRetriever retriever) {
		this.retriever = retriever;
	}
	
	public void setHackerModel(HackerModel hackerModel) {
		this.hackerModel = hackerModel;
	}
	
	public void init(){
		retrieveNews();
	}
	
	public void retrieveNews(){
		Job job = new Job("Retrieving content of Hacker News...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {

				try{
					List<HackerElem> list = retriever.retrieve();
					hackerModel.setHackerElems(list);
					eventBus.fireEvent(new HModelChangedEvent(HModelChangedEventType.RETRIEVED));
				}catch(final IOException e){
					HModelChangedEvent event = new HModelChangedEvent(HModelChangedEventType.RETRIEVE_FAILED);
					event.setReason(e.getMessage());
					eventBus.fireEvent(event);
				}
				return Status.OK_STATUS;
			}
		};

		// Start the Job
		job.schedule();
	}
	
	public HackerModel getHackerModel() {
		return hackerModel;
	}
	
	
	
	
}
