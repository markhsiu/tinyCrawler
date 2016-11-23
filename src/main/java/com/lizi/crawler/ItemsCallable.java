package com.lizi.crawler;

import java.util.concurrent.Callable;

public abstract class  ItemsCallable implements Callable<ItemList> {

	@Override
	public abstract ItemList call() throws Exception ;

}
