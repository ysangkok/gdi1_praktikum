package gruppe16.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class GwtEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final Label mylabel = new Label("lol dawg lol 2");
		RootPanel.get().add(mylabel );

	}

}
