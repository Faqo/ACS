package alma.acs.eventbrowser.views;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import alma.acs.eventbrowser.Application;
import alma.acs.eventbrowser.model.AdminConsumer;
import alma.acs.eventbrowser.model.EventModel;
import alma.acs.exceptions.AcsJException;
import alma.acs.nc.Consumer;

public class EventDetailView extends ViewPart {
	private static final int QUEUE_DRAIN_LIMIT = 1000;
	private TableViewer viewer;
	private ArrayList<AdminConsumer> consumers;

	private EventModel em;
	private Action clearEvents;
	private Action printEventDetails;

	public static final String ID = "alma.acs.eventbrowser.views.eventdetail";

	public EventDetailView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		final int TABLE_CAPACITY = 10000;
		try {
			em = EventModel.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.VIRTUAL);

		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setItemCount(TABLE_CAPACITY);
		/*
		 * "Time "+timeStamp+" "+m_channelName+" "+component+" "+count+"
		 * "+channelEventCount+" " +" "+evtTypeName+"
		 * "+evtCounter.get(evtTypeName)
		 */

		TableViewerColumn tvcol = new TableViewerColumn(viewer, SWT.NONE, 0);
		tvcol.setLabelProvider(new TimeStampLabelProvider());
		TableColumn col = tvcol.getColumn();
		col.setText("Timestamp");
		col.setWidth(180);
		col.setAlignment(SWT.LEFT);

		tvcol = new TableViewerColumn(viewer, SWT.NONE, 1);
		tvcol.setLabelProvider(new EventSourceLabelProvider());
		col = tvcol.getColumn();
		col.setText("Event source");
		col.setWidth(100);
		col.setAlignment(SWT.LEFT);

		tvcol = new TableViewerColumn(viewer, SWT.NONE, 2);
		tvcol.setLabelProvider(new CountLabelProvider());
		col = tvcol.getColumn();
		col.setText("# Events in channel");
		col.setWidth(50);
		col.setAlignment(SWT.LEFT);

		tvcol = new TableViewerColumn(viewer, SWT.NONE, 3);
		tvcol.setLabelProvider(new EventTypeLabelProvider());
		col = tvcol.getColumn();
		col.setText("Event type");
		col.setWidth(50);
		col.setAlignment(SWT.LEFT);

		tvcol = new TableViewerColumn(viewer, SWT.NONE, 4);
		tvcol.setLabelProvider(new EventTypeCountLabelProvider());
		col = tvcol.getColumn();
		col.setText("# Events this type");
		col.setWidth(50);
		col.setAlignment(SWT.LEFT);

		viewer.setContentProvider(new EventDetailViewContentProvider());
		// viewer.setComparator(new ServiceViewerComparator());
		getSite().setSelectionProvider(viewer); // In order to be able to display event detail
		viewer.setInput(getViewSite());

		makeActions();
		hookContextMenu();

		Runnable t = new Runnable() {
			int i = 0;
			public Runnable r = new Runnable() {
				public void run() {
					final Display display = viewer.getControl().getDisplay();
					if (!display.isDisposed()) {
						ArrayList<EventData> c = new ArrayList<EventData>(QUEUE_DRAIN_LIMIT);
						int numberDrained = Application.equeue.drainTo(c, QUEUE_DRAIN_LIMIT);
						viewer.add(c.toArray());
//						System.out.println("Table item count: "
//								+ viewer.getTable().getItemCount()
//								+ "; number of events drained from queue: "+numberDrained);
					}
				}
			};

			public void run() {
				final Display display = viewer.getControl().getDisplay();

				while (true) {
					if (display.isDisposed())
						return;
					display.asyncExec(r);
					try {
						Thread.sleep(1000);
//						System.out.println("Event detail iteration " + ++i);
//						System.out.println("Queue has "
//								+ Application.equeue.remainingCapacity()
//								+ " slots left.");
					} catch (InterruptedException e) {
						System.out.println("Monitoring was interrupted!");
						break;
						// Application.setMonitoring(false);
						// startMonitoringAction.setEnabled(true);
					}
				}
				// Application.setMonitoring(false);
				// startMonitoringAction.setEnabled(true);
			}
		};
		final Thread th = new Thread(t);
//		th.start();
		consumers = em.getAllConsumers();
		
		if (consumers != null) {
			for (AdminConsumer consumer : consumers) {
				try {
					consumer.consumerReady();
				} catch (AcsJException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		th.start();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(clearEvents);
		manager.add(printEventDetails);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {
		clearEvents = new Action() {
			public void run() {
				final Display display = viewer.getControl().getDisplay();
				if (!display.isDisposed())
					display.asyncExec(new Runnable() {
						public void run() {
							viewer.getTable().removeAll();
							viewer.refresh();
						};
					});
			}
		};
		clearEvents.setText("Clear events");
		clearEvents.setToolTipText("Clear all events from the table");
		clearEvents.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_TOOL_DELETE));
		printEventDetails = new Action() {
			public void run() {
				AdminConsumer.setPrintDetails(!AdminConsumer.getPrintDetails());
			};
		};
		printEventDetails.setText("Toggle event details");
		printEventDetails.setToolTipText("Toggle display of event details to STDOUT");

	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();

	}

	@Override
	public void dispose() {
		super.dispose();
		for (AdminConsumer consumer : consumers) {
			consumer.disconnect();
		}
	}

}
