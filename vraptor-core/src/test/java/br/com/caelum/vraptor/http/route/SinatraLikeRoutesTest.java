package br.com.caelum.vraptor.http.route;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.EnumSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.proxy.DefaultProxifier;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.HttpMethod;

public class SinatraLikeRoutesTest extends AbstractRoutingTest {

	private Proxifier proxifier;
	private @Mock Converters converters;
	private NoTypeFinder typeFinder;
	private @Mock Router router;
	private @Mock ParameterNameProvider nameProvider;
	private PathAnnotationRoutesParser parser;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.proxifier = new DefaultProxifier();
		this.typeFinder = new NoTypeFinder();

		when(router.builderFor(anyString())).thenAnswer(new Answer<DefaultRouteBuilder>() {

			public DefaultRouteBuilder answer(InvocationOnMock invocation) throws Throwable {
				return new DefaultRouteBuilder(proxifier, typeFinder, converters, nameProvider, (String) invocation.getArguments()[0]);
			}
		});

		parser = new PathAnnotationRoutesParser(router);
	}
	
	public static class SinatraLikeController {
		@Get("/home")
		public void home() {
			
		}
	}
	
	@Test
	public void testGetRouteWithPath() {
		List<Route> routes = parser.rulesFor(new DefaultResourceClass(SinatraLikeController.class));
		Route route = getRouteMatching(routes, "/home");
		assertNotNull(route);
		assertThat(route.allowedMethods(), is(EnumSet.of(HttpMethod.GET)));
	}

}
