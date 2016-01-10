package com.mobiarch.nf.test;

import javax.inject.Named;
import java.util.concurrent.CompletableFuture;

@Named("async-test")
public class AsyncTest {
	public CompletableFuture<String[]> index() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(10000);
			} catch (Exception e) {

			}
			return new String[]{"Helo", "World"};
		});
	}
}
