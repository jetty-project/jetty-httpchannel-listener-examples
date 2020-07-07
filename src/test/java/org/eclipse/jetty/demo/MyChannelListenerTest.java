package org.eclipse.jetty.demo;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Fields;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MyChannelListenerTest
{
    public static final String X_TEST_INFO = "X-TestInfo";
    private Server server;
    private HttpClient client;

    @BeforeEach
    public void setup() throws Exception
    {
        server = Main.newServer(0);
        server.start();

        client = new HttpClient();
        client.start();
    }

    @AfterEach
    public void teardown()
    {
        LifeCycle.stop(client);
        LifeCycle.stop(server);
    }

    @Test
    public void testGetDump(TestInfo testInfo) throws InterruptedException, ExecutionException, TimeoutException
    {
        ContentResponse response = client
            .newRequest(server.getURI().resolve("/dump/foo"))
            .method(HttpMethod.GET)
            .header(X_TEST_INFO, testInfo.getDisplayName())
            .send();
        assertThat("response.status", response.getStatus(), is(HttpStatus.OK_200));
    }

    @Test
    public void testPostDump(TestInfo testInfo) throws InterruptedException, ExecutionException, TimeoutException
    {
        Fields fields = new Fields();
        fields.add("name", "foo");
        char[] bigbuf = new char[4096];
        Arrays.fill(bigbuf, 'x');
        fields.add("buffer", new String(bigbuf));
        FormContentProvider form = new FormContentProvider(fields);

        ContentResponse response = client.POST(server.getURI().resolve("/dump/foo"))
            .header(X_TEST_INFO, testInfo.getDisplayName())
            .content(form)
            .send();

        assertThat("response.status", response.getStatus(), is(HttpStatus.OK_200));
    }
}
