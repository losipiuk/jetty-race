# jetty-race

## Run server

```shell
./mvnw clean install -DskipTests
./mvnw exec:java -Dexec.mainClass="net.losipiuk.jettyrace.TestServer"
```

## Test 

### POST wiht ReadListener

```shell
ab -n 10000 -c 100 -T "application/octet-stream" -p test.data http://127.0.0.1:8080/api/test/readlistener
```

You should get exceptions logged like this:
```
2024-04-22T11:04:43.233+0200    WARN    http-worker-134 org.eclipse.jetty.ee10.servlet.ServletChannelState      unhandled in state COMPLETING
java.lang.IllegalStateException: java.lang.NullPointerException: Cannot invoke "org.eclipse.jetty.http.HttpField.getHeader()" because "field" is null
        at org.eclipse.jetty.ee10.servlet.ServletChannelState.onError(ServletChannelState.java:874)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.handleException(ServletChannel.java:667)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.handle(ServletChannel.java:584)
        at org.eclipse.jetty.ee10.servlet.AsyncContentProducer.lambda$isReady$0(AsyncContentProducer.java:260)
        at org.eclipse.jetty.server.handler.ContextHandler$ScopedContext.run(ContextHandler.java:1298)
        at org.eclipse.jetty.server.handler.ContextRequest$OnContextDemand.run(ContextRequest.java:74)
        at org.eclipse.jetty.util.thread.SerializedInvoker$Link.run(SerializedInvoker.java:191)
        at org.eclipse.jetty.server.internal.HttpConnection$DemandContentCallback.succeeded(HttpConnection.java:679)
        at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:99)
        at org.eclipse.jetty.io.SelectableChannelEndPoint$1.run(SelectableChannelEndPoint.java:53)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.runTask(AdaptiveExecutionStrategy.java:478)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.consumeTask(AdaptiveExecutionStrategy.java:441)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.tryProduce(AdaptiveExecutionStrategy.java:293)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.run(AdaptiveExecutionStrategy.java:201)
        at org.eclipse.jetty.util.thread.ReservedThreadExecutor$ReservedThread.run(ReservedThreadExecutor.java:311)
        at org.eclipse.jetty.util.thread.MonitoredQueuedThreadPool$1.run(MonitoredQueuedThreadPool.java:73)
        at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:979)
        at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.doRunJob(QueuedThreadPool.java:1209)
        at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1164)
        at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: java.lang.NullPointerException: Cannot invoke "org.eclipse.jetty.http.HttpField.getHeader()" because "field" is null
        at org.eclipse.jetty.ee10.servlet.ServletContextResponse$HttpFieldsWrapper.onAddField(ServletContextResponse.java:567)
        at org.eclipse.jetty.http.HttpFields$Mutable$Wrapper.put(HttpFields.java:1678)
        at org.eclipse.jetty.http.HttpFields$Mutable.computeField(HttpFields.java:1372)
        at org.eclipse.jetty.server.ResponseUtils.ensureNotPersistent(ResponseUtils.java:40)
        at org.eclipse.jetty.server.ResponseUtils.ensureConsumeAvailableOrNotPersistent(ResponseUtils.java:31)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.handle(ServletChannel.java:554)
        ... 17 more
```

```
2024-04-22T11:04:43.225+0200    WARN    http-worker-176 org.eclipse.jetty.ee10.servlet.ServletChannel   /api/test/readlistener
java.lang.NullPointerException: Cannot invoke "org.eclipse.jetty.http.HttpField.getHeader()" because "field" is null
        at org.eclipse.jetty.ee10.servlet.ServletContextResponse$HttpFieldsWrapper.onAddField(ServletContextResponse.java:567)
        at org.eclipse.jetty.http.HttpFields$Mutable$Wrapper.put(HttpFields.java:1678)
        at org.eclipse.jetty.http.HttpFields$Mutable.computeField(HttpFields.java:1372)
        at org.eclipse.jetty.server.ResponseUtils.ensureNotPersistent(ResponseUtils.java:40)
        at org.eclipse.jetty.server.ResponseUtils.ensureConsumeAvailableOrNotPersistent(ResponseUtils.java:31)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.handle(ServletChannel.java:554)
        at org.eclipse.jetty.ee10.servlet.AsyncContentProducer.lambda$isReady$0(AsyncContentProducer.java:260)
        at org.eclipse.jetty.server.handler.ContextHandler$ScopedContext.run(ContextHandler.java:1298)
        at org.eclipse.jetty.server.handler.ContextRequest$OnContextDemand.run(ContextRequest.java:74)
        at org.eclipse.jetty.util.thread.SerializedInvoker$Link.run(SerializedInvoker.java:191)
        at org.eclipse.jetty.server.internal.HttpConnection$DemandContentCallback.succeeded(HttpConnection.java:679)
        at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:99)
        at org.eclipse.jetty.io.SelectableChannelEndPoint$1.run(SelectableChannelEndPoint.java:53)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.runTask(AdaptiveExecutionStrategy.java:478)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.consumeTask(AdaptiveExecutionStrategy.java:441)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.tryProduce(AdaptiveExecutionStrategy.java:293)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.run(AdaptiveExecutionStrategy.java:201)
        at org.eclipse.jetty.util.thread.ReservedThreadExecutor$ReservedThread.run(ReservedThreadExecutor.java:311)
        at org.eclipse.jetty.util.thread.MonitoredQueuedThreadPool$1.run(MonitoredQueuedThreadPool.java:73)
        at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:979)
        at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.doRunJob(QueuedThreadPool.java:1209)
        at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1164)
        at java.base/java.lang.Thread.run(Thread.java:1583)
```

```
2024-04-22T11:04:43.220+0200    WARN    http-worker-74  org.eclipse.jetty.ee10.servlet.ServletChannelState      unhandled due to prior sendError
org.glassfish.jersey.server.ContainerException: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.glassfish.jersey.message.internal.OutboundJaxrsResponse$Builder and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
        at org.glassfish.jersey.servlet.internal.ResponseWriter.rethrow(ResponseWriter.java:255)
        at org.glassfish.jersey.servlet.internal.ResponseWriter.failure(ResponseWriter.java:237)
        at org.glassfish.jersey.server.ServerRuntime$Responder.process(ServerRuntime.java:458)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder$3.run(ServerRuntime.java:915)
        at org.glassfish.jersey.internal.Errors$1.call(Errors.java:248)
        at org.glassfish.jersey.internal.Errors$1.call(Errors.java:244)
        at org.glassfish.jersey.internal.Errors.process(Errors.java:292)
        at org.glassfish.jersey.internal.Errors.process(Errors.java:274)
        at org.glassfish.jersey.internal.Errors.process(Errors.java:244)
        at org.glassfish.jersey.process.internal.RequestScope.runInScope(RequestScope.java:266)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder.resume(ServerRuntime.java:950)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder.resume(ServerRuntime.java:901)
        at net.losipiuk.jettyrace.TestResource$1.onError(TestResource.java:131)
        at org.eclipse.jetty.ee10.servlet.HttpInput.run(HttpInput.java:350)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.lambda$handle$1(ServletChannel.java:537)
        at org.eclipse.jetty.server.handler.ContextHandler$ScopedContext.run(ContextHandler.java:1292)
        at org.eclipse.jetty.server.handler.ContextHandler$ScopedContext.run(ContextHandler.java:1285)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.handle(ServletChannel.java:537)
        at org.eclipse.jetty.ee10.servlet.AsyncContentProducer.lambda$isReady$0(AsyncContentProducer.java:260)
        at org.eclipse.jetty.server.handler.ContextHandler$ScopedContext.run(ContextHandler.java:1298)
        at org.eclipse.jetty.server.handler.ContextRequest$OnContextDemand.run(ContextRequest.java:74)
        at org.eclipse.jetty.util.thread.SerializedInvoker$Link.run(SerializedInvoker.java:191)
        at org.eclipse.jetty.server.internal.HttpConnection$DemandContentCallback.succeeded(HttpConnection.java:679)
        at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:99)
        at org.eclipse.jetty.io.SelectableChannelEndPoint$1.run(SelectableChannelEndPoint.java:53)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.runTask(AdaptiveExecutionStrategy.java:478)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.consumeTask(AdaptiveExecutionStrategy.java:441)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.tryProduce(AdaptiveExecutionStrategy.java:293)
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.run(AdaptiveExecutionStrategy.java:201)
        at org.eclipse.jetty.util.thread.ReservedThreadExecutor$ReservedThread.run(ReservedThreadExecutor.java:311)
        at org.eclipse.jetty.util.thread.MonitoredQueuedThreadPool$1.run(MonitoredQueuedThreadPool.java:73)
        at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:979)
        at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.doRunJob(QueuedThreadPool.java:1209)
        at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1164)
        at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.glassfish.jersey.message.internal.OutboundJaxrsResponse$Builder and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
        at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1330)
        at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:414)
        at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:502)
        at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:341)
        at com.fasterxml.jackson.databind.ObjectWriter$Prefetch.serialize(ObjectWriter.java:1574)
        at com.fasterxml.jackson.databind.ObjectWriter.writeValue(ObjectWriter.java:1061)
        at io.airlift.jaxrs.JsonMapper.write(JsonMapper.java:103)
        at io.airlift.jaxrs.AbstractJacksonMapper.writeTo(AbstractJacksonMapper.java:156)
        at io.airlift.jaxrs.JsonMapper.writeTo(JsonMapper.java:41)
        at org.glassfish.jersey.message.internal.WriterInterceptorExecutor$TerminalWriterInterceptor.invokeWriteTo(WriterInterceptorExecutor.java:242)
        at org.glassfish.jersey.message.internal.WriterInterceptorExecutor$TerminalWriterInterceptor.aroundWriteTo(WriterInterceptorExecutor.java:227)
        at org.glassfish.jersey.message.internal.WriterInterceptorExecutor.proceed(WriterInterceptorExecutor.java:139)
        at org.glassfish.jersey.server.internal.JsonWithPaddingInterceptor.aroundWriteTo(JsonWithPaddingInterceptor.java:85)
        at org.glassfish.jersey.message.internal.WriterInterceptorExecutor.proceed(WriterInterceptorExecutor.java:139)
        at org.glassfish.jersey.server.internal.MappableExceptionWrapperInterceptor.aroundWriteTo(MappableExceptionWrapperInterceptor.java:61)
        at org.glassfish.jersey.message.internal.WriterInterceptorExecutor.proceed(WriterInterceptorExecutor.java:139)
        at org.glassfish.jersey.message.internal.MessageBodyFactory.writeTo(MessageBodyFactory.java:1116)
        at org.glassfish.jersey.server.ServerRuntime$Responder.writeResponse(ServerRuntime.java:678)
        at org.glassfish.jersey.server.ServerRuntime$Responder.processResponse(ServerRuntime.java:387)
        at org.glassfish.jersey.server.ServerRuntime$Responder.process(ServerRuntime.java:377)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder$3.run(ServerRuntime.java:913)
        ... 31 more
```


### POST where handler does not read input

```shell
ab -n 10000 -c 100 -T "application/octet-stream" -p test.data http://127.0.0.1:8080/api/test/ignoreinput
```

You get exceptions like below and client hangs until timoeut
```
2024-04-22T11:07:51.962+0200    WARN    pool-5-thread-74        org.eclipse.jetty.ee10.servlet.ServletChannel   /api/test/ignoreinput                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        [0/35408]
java.lang.NullPointerException: Cannot invoke "org.eclipse.jetty.http.HttpField.getHeader()" because "field" is null
        at org.eclipse.jetty.ee10.servlet.ServletContextResponse$HttpFieldsWrapper.onAddField(ServletContextResponse.java:567)
        at org.eclipse.jetty.http.HttpFields$Mutable$Wrapper.put(HttpFields.java:1678)
        at org.eclipse.jetty.http.HttpFields$Mutable.computeField(HttpFields.java:1372)
        at org.eclipse.jetty.server.ResponseUtils.ensureNotPersistent(ResponseUtils.java:40)
        at org.eclipse.jetty.server.ResponseUtils.ensureConsumeAvailableOrNotPersistent(ResponseUtils.java:31)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.handle(ServletChannel.java:554)
        at org.eclipse.jetty.server.handler.ContextHandler$ScopedContext.run(ContextHandler.java:1298)
        at org.eclipse.jetty.server.handler.ContextHandler$ScopedContext.run(ContextHandler.java:1285)
        at org.eclipse.jetty.ee10.servlet.ServletChannelState.runInContext(ServletChannelState.java:1257)
        at org.eclipse.jetty.ee10.servlet.ServletChannelState.complete(ServletChannelState.java:783)
        at org.eclipse.jetty.ee10.servlet.AsyncContextState.complete(AsyncContextState.java:61)
        at org.glassfish.jersey.servlet.async.AsyncContextDelegateProviderImpl$ExtensionImpl.complete(AsyncContextDelegateProviderImpl.java:102)
        at org.glassfish.jersey.servlet.internal.ResponseWriter.commit(ResponseWriter.java:173)
        at org.glassfish.jersey.server.ContainerResponse.close(ContainerResponse.java:404)
        at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
        at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:179)
        at java.base/java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:1024)
        at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
        at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:174)
        at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:596)
        at org.glassfish.jersey.server.ServerRuntime$Responder.release(ServerRuntime.java:775)
        at org.glassfish.jersey.server.ServerRuntime$Responder.process(ServerRuntime.java:378)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder$3.run(ServerRuntime.java:913)
        at org.glassfish.jersey.internal.Errors$1.call(Errors.java:248)
        at org.glassfish.jersey.internal.Errors$1.call(Errors.java:244)
        at org.glassfish.jersey.internal.Errors.process(Errors.java:292)
        at org.glassfish.jersey.internal.Errors.process(Errors.java:274)
        at org.glassfish.jersey.internal.Errors.process(Errors.java:244)
        at org.glassfish.jersey.process.internal.RequestScope.runInScope(RequestScope.java:266)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder.resume(ServerRuntime.java:950)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder.resume(ServerRuntime.java:901)
        at io.airlift.jaxrs.AsyncResponseHandler$1.onSuccess(AsyncResponseHandler.java:99)
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1137)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
        at java.base/java.lang.Thread.run(Thread.java:1583)


2024-04-22T11:07:51.966+0200    WARN    pool-5-thread-74        org.eclipse.jetty.ee10.servlet.ServletChannelState      unhandled in state COMPLETING
java.lang.IllegalStateException: java.lang.NullPointerException: Cannot invoke "org.eclipse.jetty.http.HttpField.getHeader()" because "field" is null
        at org.eclipse.jetty.ee10.servlet.ServletChannelState.onError(ServletChannelState.java:874)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.handleException(ServletChannel.java:667)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.handle(ServletChannel.java:584)
        at org.eclipse.jetty.server.handler.ContextHandler$ScopedContext.run(ContextHandler.java:1298)
        at org.eclipse.jetty.server.handler.ContextHandler$ScopedContext.run(ContextHandler.java:1285)
        at org.eclipse.jetty.ee10.servlet.ServletChannelState.runInContext(ServletChannelState.java:1257)
        at org.eclipse.jetty.ee10.servlet.ServletChannelState.complete(ServletChannelState.java:783)
        at org.eclipse.jetty.ee10.servlet.AsyncContextState.complete(AsyncContextState.java:61)
        at org.glassfish.jersey.servlet.async.AsyncContextDelegateProviderImpl$ExtensionImpl.complete(AsyncContextDelegateProviderImpl.java:102)
        at org.glassfish.jersey.servlet.internal.ResponseWriter.commit(ResponseWriter.java:173)
        at org.glassfish.jersey.server.ContainerResponse.close(ContainerResponse.java:404)
        at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
        at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:179)
        at java.base/java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:1024)
        at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
        at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:174)
        at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:596)
        at org.glassfish.jersey.server.ServerRuntime$Responder.release(ServerRuntime.java:775)
        at org.glassfish.jersey.server.ServerRuntime$Responder.process(ServerRuntime.java:378)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder$3.run(ServerRuntime.java:913)
        at org.glassfish.jersey.internal.Errors$1.call(Errors.java:248)
        at org.glassfish.jersey.internal.Errors$1.call(Errors.java:244)
        at org.glassfish.jersey.internal.Errors.process(Errors.java:292)
        at org.glassfish.jersey.internal.Errors.process(Errors.java:274)
        at org.glassfish.jersey.internal.Errors.process(Errors.java:244)
        at org.glassfish.jersey.process.internal.RequestScope.runInScope(RequestScope.java:266)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder.resume(ServerRuntime.java:950)
        at org.glassfish.jersey.server.ServerRuntime$AsyncResponder.resume(ServerRuntime.java:901)
        at io.airlift.jaxrs.AsyncResponseHandler$1.onSuccess(AsyncResponseHandler.java:99)
        at com.google.common.util.concurrent.Futures$CallbackListener.run(Futures.java:1137)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
        at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: java.lang.NullPointerException: Cannot invoke "org.eclipse.jetty.http.HttpField.getHeader()" because "field" is null
        at org.eclipse.jetty.ee10.servlet.ServletContextResponse$HttpFieldsWrapper.onAddField(ServletContextResponse.java:567)
        at org.eclipse.jetty.http.HttpFields$Mutable$Wrapper.put(HttpFields.java:1678)
        at org.eclipse.jetty.http.HttpFields$Mutable.computeField(HttpFields.java:1372)
        at org.eclipse.jetty.server.ResponseUtils.ensureNotPersistent(ResponseUtils.java:40)
        at org.eclipse.jetty.server.ResponseUtils.ensureConsumeAvailableOrNotPersistent(ResponseUtils.java:31)
        at org.eclipse.jetty.ee10.servlet.ServletChannel.handle(ServletChannel.java:554)
        ... 33 more
```


