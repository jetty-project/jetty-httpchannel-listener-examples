package org.eclipse.jetty.demo;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.server.HttpChannel;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * See APIDocs for {@link HttpChannel.Listener} for meaning of each of these event methods.
 */
public class MyChannelListener implements HttpChannel.Listener
{
    private static final Logger LOG = Log.getLogger(MyChannelListener.class);

    @Override
    public void onRequestBegin(Request request)
    {
        // TODO: might be a good idea to tag the request via an attribute here (so you can keep related events below together).
        request.setAttribute("X-UniqueRequestID", UUID.randomUUID());
        LOG.info("onRequestBegin({})", request);
    }

    @Override
    public void onBeforeDispatch(Request request)
    {
        request.setAttribute("X-Dispatch", System.currentTimeMillis());
        LOG.info("onBeforeDispatch({})", request);
    }

    @Override
    public void onDispatchFailure(Request request, Throwable failure)
    {
        LOG.warn("onDispatchFailure({})", request, failure);
    }

    @Override
    public void onAfterDispatch(Request request)
    {
        long now = System.currentTimeMillis();
        long dispatchStart = (long)request.getAttribute("X-Dispatch");
        long dispatchDuration = now - dispatchStart;
        LOG.info("onAfterDispatch({}) - dispatch duration: {}", request, dispatchDuration);
    }

    @Override
    public void onRequestContent(Request request, ByteBuffer content)
    {
        LOG.info("onRequestContent({}, {})", request, content);
    }

    @Override
    public void onRequestContentEnd(Request request)
    {
        LOG.info("onRequestContentEnd({})", request);
    }

    @Override
    public void onRequestTrailers(Request request)
    {
        LOG.info("onRequestTrailers({})", request);
    }

    @Override
    public void onRequestEnd(Request request)
    {
        // TODO: Request object is done being used for this specific exchange
        LOG.info("onRequestEnd({})", request);
    }

    @Override
    public void onRequestFailure(Request request, Throwable failure)
    {
        LOG.warn("onRequestFailure({})", request, failure);
    }

    @Override
    public void onResponseBegin(Request request)
    {
        LOG.info("onResponseBegin({})", request);
    }

    @Override
    public void onResponseCommit(Request request)
    {
        // Example of obtaining Response object data
        long contentLength = request.getResponse().getContentLength();
        // Example of obtaining Response header values as set by application
        // NOTE: not all headers actually sent are represented in the Response object, as some headers are injected on an as-needed basis during generation.
        String contentType = request.getResponse().getHttpFields().get(HttpHeader.CONTENT_TYPE);
        LOG.info("onResponseCommit({}) - contentLength:{}, contentType:{}", request, contentLength, contentType);
    }

    @Override
    public void onResponseContent(Request request, ByteBuffer content)
    {
        LOG.info("onResponseContent({}, {})", request, content);
    }

    @Override
    public void onResponseEnd(Request request)
    {
        // TODO: Response object is done being used for this specific exchange
        LOG.info("onResponseEnd({})", request);
    }

    @Override
    public void onResponseFailure(Request request, Throwable failure)
    {
        LOG.warn("onResponseFailure({})", request, failure);
    }

    @Override
    public void onComplete(Request request)
    {
        // TODO: release any references to request and/or response objects here
        // as the next step is to release / recycle those objects
        long now = System.currentTimeMillis();
        long requestDuration = now - request.getTimeStamp();
        LOG.info("onComplete({}) - total duration {}", request, requestDuration);
    }
}
