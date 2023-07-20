import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Concurrency {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		List<CompletableFuture<CompletableFuture<String>>> resultList = new ArrayList<>();

		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			int idx = i;
			CompletableFuture<CompletableFuture<String>> result = CompletableFuture.supplyAsync(() -> {
				return child(idx);
			}, es);
			resultList.add(result);
		}

		// CompletableFuture.allOf(resultList);
		CompletableFuture.allOf(resultList.toArray(new CompletableFuture[resultList.size()]));
		// for (CompletableFuture<CompletableFuture<String>> result : resultList) {
		// 	result.join();
		// }
		// resultList.stream().map(CompletableFuture::join).collect(Collectors.toList());
		// resultList.stream().map(CompletableFuture::join);

		for (CompletableFuture<CompletableFuture<String>> result : resultList) {
			System.out.println(result.get().get());
		}


	}

	private static CompletableFuture<String> child(int i) {
		for (int j = 0; j < 10; j++) {
			int interval = (int)Math.ceil(Math.random() * 1000);
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        System.out.println(i + " " + j + " " + interval);
		}

		return CompletableFuture.completedFuture("OK " + i + " " + new Date());
	}

}
