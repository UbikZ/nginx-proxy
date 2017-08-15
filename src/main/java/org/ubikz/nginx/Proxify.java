package org.ubikz.nginx;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.EventStream;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.Event;
import org.ubikz.nginx.docker.event.Status;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

class Proxify {
    public static void main(String[] args) throws Exception {
        final DockerClient docker = new DefaultDockerClient("unix:///var/run/docker.sock");
        EventStream eventStream;

        while (true) {
            eventStream = docker.events(DockerClient.EventsFilterParam.type(Event.Type.CONTAINER));
            Event event = eventStream.next();
            Status eventStatus = Status.valueOf(event.status());

            if (Arrays.asList(Status.start, Status.stop, Status.die).contains(eventStatus)) {
                Optional.ofNullable(event.actor()).ifPresent(ExceptionConsumer.handle(actor -> {
                    String containerId = actor.id();
                    ContainerInfo info = docker.inspectContainer(containerId);
                    System.out.println("Container " + info.name() + " is " + eventStatus);
                }));
            }

            eventStream.close();
        }
    }

    @FunctionalInterface
    public interface ExceptionConsumer<T, E extends Exception> {
        static <T, E extends Exception> Consumer<T> handle(ExceptionConsumer<T, E> exceptionConsumer)
                throws E {

            return i -> {
                try {
                    exceptionConsumer.accept(i);
                } catch (Exception ex) {
                    throwCheckedUnchecked(ex);
                    System.out.println("Exception : " + ex);
                }
            };
        }

        @SuppressWarnings("unchecked")
        static <X extends Throwable> void throwCheckedUnchecked(Throwable t) throws X {
            throw (X) t;
        }

        void accept(T t) throws E;
    }
}