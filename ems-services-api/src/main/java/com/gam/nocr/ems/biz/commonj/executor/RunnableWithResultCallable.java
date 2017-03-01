package com.gam.nocr.ems.biz.commonj.executor;
import java.util.concurrent.Callable;


public class RunnableWithResultCallable<T> implements Callable<T> {

	private Runnable task;
	private T result;

	public RunnableWithResultCallable(Runnable task, T result) {
		this.task = task;
		this.result = result;
	}

	public T call() throws Exception {
		this.task.run();
		return result;
	}

}
