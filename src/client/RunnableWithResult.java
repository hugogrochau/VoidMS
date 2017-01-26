package client;

public interface RunnableWithResult extends Runnable {
    Object getResult();
    boolean isDone();
}
