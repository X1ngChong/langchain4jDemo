package top.bhui.lanchain4jdemo.func;

import lombok.Data;

@Data
public class Calculator implements Runnable {

    private int a;

    private int b;

    @Override
    public void run() {
        System.out.println(a + b);
    }
}
