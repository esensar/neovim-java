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

import com.ensarsarajcic.neovim.java.corerpc.client.RPCConnection;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCStreamer;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCStreamerWrapper;
import com.ensarsarajcic.neovim.java.notifications.NeovimStreamNotificationHandler;
import com.ensarsarajcic.neovim.java.notifications.buffer.BufferDetachEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.NeovimRedrawEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.cmdline.CmdlineHideEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.cmdline.CmdlinePosEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.cmdline.CmdlineShowEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {

        SubmissionPublisher<NotificationMessage> submissionPublisher = new SubmissionPublisher<>();
        ReactiveRPCStreamer reactiveRPCStreamer = new ReactiveRPCStreamer() {
            @Override
            public void attach(RPCConnection rpcConnection) {
            }

            @Override
            public CompletableFuture<ResponseMessage> response(RequestMessage.Builder requestMessage) {
                return null;
            }

            @Override
            public Flow.Publisher<RequestMessage> requestsFlow() {
                return null;
            }

            @Override
            public Flow.Publisher<NotificationMessage> notificationsFlow() {
                return submissionPublisher;
            }
        };
        NeovimStreamNotificationHandler neovimStreamNotificationHandler = new NeovimStreamNotificationHandler(
                reactiveRPCStreamer
        );
        neovimStreamNotificationHandler.uiEvents().subscribe(new Flow.Subscriber<>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(NeovimRedrawEvent item) {
                System.out.println(item);
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
        submissionPublisher.submit(new NotificationMessage.Builder("redraw")
                .addArgument(Collections.singletonList(CmdlineHideEvent.NAME))
                .addArgument(Arrays.asList(CmdlinePosEvent.NAME, 1, 1))
                .build());
        submissionPublisher.submit(new NotificationMessage.Builder(BufferDetachEvent.NAME).build());
        Thread.sleep(100000);
    }
}
