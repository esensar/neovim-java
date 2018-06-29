/*
 * MIT License
 *
 * Copyright (c) 2018 Ensar Sarajčić
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ensarsarajcic.neovim.java.notifications;

import com.ensarsarajcic.neovim.java.api.NeovimStreamApi;
import com.ensarsarajcic.neovim.java.api.types.api.UiOptions;
import com.ensarsarajcic.neovim.java.api.types.msgpack.NeovimJacksonModule;
import com.ensarsarajcic.neovim.java.corerpc.client.ProcessRPCConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.RPCClient;
import com.ensarsarajcic.neovim.java.corerpc.client.RPCConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.TcpSocketRPCConnection;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCClient;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCStreamer;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCStreamerWrapper;
import com.ensarsarajcic.neovim.java.notifications.NeovimStreamNotificationHandler;
import com.ensarsarajcic.neovim.java.notifications.buffer.BufferDetachEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.NeovimRedrawEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.cmdline.CmdlineHideEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.cmdline.CmdlinePosEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.cmdline.CmdlineShowEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException, IOException, ExecutionException {

        RPCConnection rpcConnection = new TcpSocketRPCConnection(new Socket("127.0.0.1", 6666));

        RPCClient rpcClient = new RPCClient.Builder()
                .withObjectMapper(NeovimJacksonModule.createNeovimObjectMapper()).build();

        ReactiveRPCClient reactiveRPCClient = ReactiveRPCClient.createDefaultInstanceWithCustomStreamer(rpcClient);
        reactiveRPCClient.attach(rpcConnection);
        NeovimStreamApi neovimStreamApi = new NeovimStreamApi(
                reactiveRPCClient
        );

        NeovimStreamNotificationHandler neovimStreamNotificationHandler = new NeovimStreamNotificationHandler(
                reactiveRPCClient
        );

        reactiveRPCClient.notificationsFlow().subscribe(new Flow.Subscriber<NotificationMessage>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(NotificationMessage item) {
                System.out.println(item);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        neovimStreamNotificationHandler.uiEvents().subscribe(new Flow.Subscriber<>() {
            private Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(1);
                this.subscription = subscription;
            }

            @Override
            public void onNext(NeovimRedrawEvent item) {
                System.out.println(item);
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("DONE");
            }
        });
        neovimStreamApi.attachUI(100, 100, UiOptions.TERMINAL).thenAccept(System.out::println).get();
    }
}
