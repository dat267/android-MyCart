package edu.hanu.a2_2001040024.helpers;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class CommonExecutor {
	private static ExecutorService executorService;
	private CommonExecutor() {
	}
	public static synchronized ExecutorService getInstance() {
		if (executorService == null) {
			executorService = Executors.newCachedThreadPool();
		}
		return executorService;
	}
}

