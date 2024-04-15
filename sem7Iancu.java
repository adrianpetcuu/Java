package ro.ase.acs.main;

import ro.ase.acs.classes.Addition;
import ro.ase.acs.interfaces.BinaryOperation;
import ro.ase.acs.interfaces.Printable;
import ro.ase.acs.interfaces.Taxable;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args){
        BinaryOperation op;
        op = new Addition();
        System.out.println(op.compute(4, 5));

        //anonymous object
        double result = new Addition().compute(5, 6);
        System.out.println(result);

        //reference to the interface
        op = new BinaryOperation() {
            @Override
            public double compute(double x, double y) {
                return x * y;
            }
        };
        System.out.println(op.compute(4, 5));

        //lambda expression - anonymous function
        op = (x, y) -> x / y;
        System.out.println(op.compute(4, 5));

        Printable p = m -> System.out.println(m);
        p.print("Hello");

        final double value = 100;
        Taxable t = () -> {
            if(value < 0) {
                return 0;
            }
            else
                return value * 0.2;
        };

        List<Integer> list = List.of(1, 3, 5, 1, 2, 4, 8, 8, 10);
        //stream() return a list of Integer
        long r = list.stream().distinct().count();
        System.out.println(r);

        List<Integer> subList = list.stream().
                filter(i -> i % 2 == 0).sorted().collect(Collectors.toList());
        System.out.println(subList);

        List<String> strings = List.of("Maria", "John", "George", "Elizabeth", "Charles");
        String x = strings.stream().map(s -> s.toUpperCase()).sorted().
                reduce((s1, s2) -> s1 + ", " + s2).get();
        System.out.println(x);

        strings.stream().forEach(System.out::println);
    }
}
