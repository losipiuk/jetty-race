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

import com.google.inject.Binder;
import io.airlift.bootstrap.ApplicationConfigurationException;
import io.airlift.bootstrap.Bootstrap;
import io.airlift.configuration.AbstractConfigurationAwareModule;
import io.airlift.event.client.EventModule;
import io.airlift.http.server.HttpServerModule;
import io.airlift.jaxrs.JaxrsModule;
import io.airlift.json.JsonModule;
import io.airlift.log.Level;
import io.airlift.log.Logger;
import io.airlift.log.Logging;
import io.airlift.node.testing.TestingNodeModule;
import io.airlift.tracetoken.TraceTokenModule;
import io.airlift.tracing.TracingModule;

import static io.airlift.jaxrs.JaxrsBinder.jaxrsBinder;

public final class TestServer
{
    private static final Logger log = Logger.get(TestServer.class);

    private TestServer() {}

    public static void main(String[] args)
    {
        // Configure logging levels here:
        Logging logging = Logging.initialize();
        logging.setLevel("org.eclipse.jetty", Level.WARN);

        Bootstrap app = new Bootstrap(
                new TestingNodeModule(),
                new HttpServerModule(),
                new JaxrsModule(),
                new JsonModule(),
                new TraceTokenModule(),
                new EventModule(),
                new TracingModule("blah", "some version"),
                new AbstractConfigurationAwareModule() {
                    @Override
                    protected void setup(Binder binder)
                    {
                        jaxrsBinder(binder).bind(TestResource.class);
                    }
                });

        try {
            app.doNotInitializeLogging().initialize();
            log.info("======== SERVER STARTED ========");
        }
        catch (ApplicationConfigurationException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
        catch (Throwable e) {
            log.error(e);
            System.exit(1);
        }
    }
}
