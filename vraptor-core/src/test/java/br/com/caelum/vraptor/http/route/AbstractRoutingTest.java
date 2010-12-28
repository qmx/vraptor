package br.com.caelum.vraptor.http.route;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class AbstractRoutingTest {

	protected Route getRouteMatching(List<Route> routes, String uri) {
		for (Route route : routes) {
			if (route.canHandle(uri)) {
				return route;
			}
		}
		return null;
	}

	protected Matcher<Route> canHandle(final Class<?> type, final String method) {
		return new TypeSafeMatcher<Route>() {
	
			@Override
			protected void describeMismatchSafely(Route item, Description mismatchDescription) {
			}
	
			@Override
			protected boolean matchesSafely(Route item) {
				try {
					return item.canHandle(type, type.getDeclaredMethod(method));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
	
			public void describeTo(Description description) {
				description.appendText("a route which can handle ").appendValue(type).appendText(".").appendValue(method);
			}
		};
	}

}