/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.losipiuk.jettyrace;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import io.airlift.log.Logger;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Verify.verify;
import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static io.airlift.jaxrs.AsyncResponseHandler.bindAsyncResponse;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Path("/api/test")
public class TestResource
{
    private static final Logger log = Logger.get(TestResource.class);
    private final Executor responseExecutor = Executors.newCachedThreadPool();
    private final ListeningScheduledExecutorService delayExecutor = MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(20));

    @POST
    @Path("readlistener")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void handleWithReadListener(
            @Context HttpServletRequest request,
            @HeaderParam(CONTENT_LENGTH) Integer contentLength,
            @Suspended AsyncResponse asyncResponse)
            throws IOException
    {
        AsyncContext asyncContext = request.getAsyncContext();

        ServletInputStream inputStream;
        try {
            inputStream = asyncContext.getRequest().getInputStream();
        }
        catch (IOException e) {
            log.error(e, "error on getInputStream");
            ((HttpServletResponse) asyncContext.getResponse()).setStatus(SC_INTERNAL_SERVER_ERROR);
            asyncContext.complete();
            return;
        }

        byte[] response = new byte[contentLength];
        ReadListener readListener = new ReadListener()
        {
            private final List<ListenableFuture<Void>> addDataPagesFutures = new ArrayList<>();

            private int bytesRead;

            @Override
            public void onDataAvailable()
                    throws IOException
            {
                // && !inputStream.isFinished() seems unnecessary but it is still
                // added in Jetty 12 examples using ReadListener
                // Keeping for now to see if we still get some spurious internal
                // race conditions with it.
                while (inputStream.isReady() && !inputStream.isFinished()) {
                    if (bytesRead < contentLength) {
                        int readLength = inputStream.read(response, bytesRead, contentLength - bytesRead);
                        if (readLength == -1) {
                            break;
                        }
                        bytesRead += readLength;
                    }
                    else {
                        // we need extra call to read after we read number of bytes denoted by contentLength,
                        // otherwise inputStream will never signal EOF and `onAllDataRead` will not be called.
                        int readLength = inputStream.read();
                        checkState(readLength == -1, "expected EOF but read %s", readLength);
                        break;
                    }
                }
            }

            @Override
            public void onAllDataRead()
            {
                verify(bytesRead == contentLength,
                        "Actual number of bytes read %s not equal to contentLength %s", bytesRead, contentLength);

                SettableFuture<Void> future = SettableFuture.create();
                future.set(null);

                bindAsyncResponse(
                        asyncResponse,
                        Futures.transform(
                                future,
                                ignored -> Response.ok().build(),
                                directExecutor()),
                        responseExecutor);
            }

            @Override
            public void onError(Throwable throwable)
            {
                log.error(throwable, "onError");
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR));
            }
        };

        inputStream.setReadListener(readListener);
    }

    @POST
    @Path("ignoreinput")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void handleIgnoringInput(
            @Context HttpServletRequest request,
            @HeaderParam(CONTENT_LENGTH) Integer contentLength,
            @Suspended AsyncResponse asyncResponse)
    {
        SettableFuture<Void> future = SettableFuture.create();
        future.set(null);

        bindAsyncResponse(
                asyncResponse,
                Futures.transform(
                        future,
                        ignored -> Response.ok().build(),
                        directExecutor()),
                responseExecutor);
    }

    @POST
    @Path("readlistenerlate")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void handleWithReadListenerLate(
            @Context HttpServletRequest request,
            @HeaderParam(CONTENT_LENGTH) Integer contentLength,
            @Suspended AsyncResponse asyncResponse)
            throws IOException
    {
        AsyncContext asyncContext = request.getAsyncContext();

        ServletInputStream inputStream;
        try {
            inputStream = asyncContext.getRequest().getInputStream();
        }
        catch (IOException e) {
            log.error(e, "error on getInputStream");
            ((HttpServletResponse) asyncContext.getResponse()).setStatus(SC_INTERNAL_SERVER_ERROR);
            asyncContext.complete();
            return;
        }

        // SET request timeout to 1s
        asyncResponse.setTimeout(1000, MILLISECONDS);

        byte[] response = new byte[contentLength];
        ReadListener readListener = new ReadListener()
        {
            private int bytesRead;

            @Override
            public void onDataAvailable()
                    throws IOException
            {
                // && !inputStream.isFinished() seems unnecessary but it is still
                // added in Jetty 12 examples using ReadListener
                // Keeping for now to see if we still get some spurious internal
                // race conditions with it.
                while (inputStream.isReady() && !inputStream.isFinished()) {
                    if (bytesRead < contentLength) {
                        int readLength = inputStream.read(response, bytesRead, contentLength - bytesRead);
                        if (readLength == -1) {
                            break;
                        }
                        bytesRead += readLength;
                    }
                    else {
                        // we need extra call to read after we read number of bytes denoted by contentLength,
                        // otherwise inputStream will never signal EOF and `onAllDataRead` will not be called.
                        int readLength = inputStream.read();
                        checkState(readLength == -1, "expected EOF but read %s", readLength);
                        break;
                    }
                }
            }

            @Override
            public void onAllDataRead()
            {
                verify(bytesRead == contentLength,
                        "Actual number of bytes read %s not equal to contentLength %s", bytesRead, contentLength);

                SettableFuture<Void> future = SettableFuture.create();
                delayExecutor.schedule(() -> future.set(null), Duration.of(4000, ChronoUnit.MILLIS));

                bindAsyncResponse(
                        asyncResponse,
                        Futures.transform(
                                future,
                                ignored -> Response.ok().build(),
                                directExecutor()),
                        responseExecutor);
            }

            @Override
            public void onError(Throwable throwable)
            {
                log.error(throwable, "onError");
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR));
            }
        };

        // 10% of requests will set read listener after timeout
        delayExecutor.schedule(() -> inputStream.setReadListener(readListener), 500 + ThreadLocalRandom.current().nextInt(600), MILLISECONDS);
    }
}
