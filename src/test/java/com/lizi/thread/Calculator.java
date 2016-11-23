package com.lizi.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 并行计算
 * 
 * @author Administrator
 *
 */
public class Calculator extends RecursiveTask<Integer> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4466187729653168743L;
	private static final int THRESHOLD = 100;
    private int start;
    private int end;

    public Calculator(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        if((start - end) < THRESHOLD){
            for(int i = start; i< end;i++){
                sum += i;
            }
        }else{
            int middle = (start + end) /2;
            Calculator left = new Calculator(start, middle);
            Calculator right = new Calculator(middle + 1, end);
            left.fork();
            right.fork();

            sum = left.join() + right.join();
        }
        return sum;
    }
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
    	 ForkJoinPool forkJoinPool = new ForkJoinPool();  
         Future<Integer> result = forkJoinPool.submit(new Calculator(0, 10000));  
         //49995000
         System.out.println("result : "+result.get());
        
	}

   
}