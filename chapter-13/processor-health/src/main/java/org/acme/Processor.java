package org.acme;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.inject.Singleton;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletionStage;

@Singleton
public class Processor {

  private int count = 1;

  @Incoming("ticks")
  @Outgoing("processed")
  @Acknowledgment(Acknowledgment.Strategy.MANUAL)
  CompletionStage<Message<String>> process(Message<Long> message) throws Exception {
    if (count++ % 8 == 0) {
      System.out.println("Random failure to process a record.");
      message.nack(new Throwable("Random failure to process a record.")).toCompletableFuture().join();
      return null;
    }

    return message.ack().thenApply(s -> {
      String value = String.valueOf(message.getPayload());
      try {
        value += " consumed in pod (" + InetAddress.getLocalHost().getHostName() + ")";
      } catch (UnknownHostException e) {
        throw new RuntimeException(e);
      }

      return message.withPayload(value);
    });
  }
}
