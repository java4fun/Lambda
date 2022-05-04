import java.math.BigInteger;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaCheatsheet {


    public static void main(String[] args) {
        // 1. the basics of lambda
        FunctionBasics();

        // 2. LambdaEventHandler
        // btn.setOnAction( event -> System.out.println("Hello World!"));

        // 3. Runnable Simplified
        RunnableSimplified();

        // 4. build-in functions
        BuildInFunctions();

        // 5. Calculations as functional interface
        CalculateFunctions();

        // 6. Collector and Stream API
        StreamApiWithCollector();

        // 7. some special streams
        IntStream.range(1, 4).forEach(System.out::println);

        //find the average of the numbers squared
        Arrays.stream(new int[]{1, 2, 3, 4})
                .map(n -> n * n)
                .average()
                .ifPresent(System.out::println);

        //map doubles to ints
        Stream.of(1.5, 2.3, 3.7)
                .mapToInt(Double::intValue)
                .forEach(System.out::println);

    }

    // 1. the basics of lambda
    static void FunctionBasics() {

        // 1.1 - using the test method of Predicate
        Predicate<String> stringLenLessThan10 = (s) -> s.length() < 10;
        System.out.println(stringLenLessThan10.test("Apples"));

        // 1.2 - Consumer example uses accept method
        Consumer<String> stringConsumer = (s) -> System.out.println(s.toLowerCase());
        stringConsumer.accept("ABCDEFG");

        // 1.3 - Function example
        Function<Integer, String> int2StringConverter = (s) -> Integer.toString(s);
        System.out.println(int2StringConverter.apply(26).length());

        // 1.4 - Supplier example
        Supplier<String> stringSupplier = () -> "Java is fun";
        System.out.println(stringSupplier.get());

        //1.5 - Binary Operator example
        BinaryOperator<Integer> add = (a, b) -> a + b;
        System.out.println(add.apply(2, 3));

        // 1.6 - Unary Operator example
        UnaryOperator<String> toUpper = (msg) -> msg.toUpperCase();
        System.out.println(toUpper.apply("this is my message in upper case"));
    }

    // 2. custom functional interface
    @FunctionalInterface
    interface GreetingFunction {
        void sayMessage(String message);
    }

    // 3. Runnable Simplified
    public static void RunnableSimplified() {
        // Anonymous Inner Class: Runnable
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("run 1");
            }
        };

        // 3.1 - Lambda version of Runnable (no arguments)
        Runnable r2 = () -> System.out.println("run 2");

        // Start both threads
        r1.run();
        r2.run();

        //3.2 - using an existing functional interface BiFunction
        BiFunction<String, String, String> concat = (a, b) -> a + b;
        String sentence = concat.apply("Today is ", "a great day");
        System.out.println(sentence);

        // 3.3 - example of the Consumer functional interface
        Consumer<String> hello = name -> System.out.println("Hello, " + name);
        for (String name : Arrays.asList("Duke", "Mickey", "Minnie")) {
            hello.accept(name);
        }

        // 3.4 - example of passing one value
        GreetingFunction greeting = message ->
                System.out.println("Java Programming " + message);
        greeting.sayMessage("Rocks with lambda expressions");
    }


    // 4. build-in functions
    public static void BuildInFunctions() {

        // 4.1 - IntFunction
        IntFunction<String> intToString = num -> Integer.toString(num);
        System.out.println("expected value 3, actual value: " +
                intToString.apply(123).length());

        // 4.2 - a tactic method reference using ::
        IntFunction<String> intToString2 = Integer::toString;
        System.out.println("expected value 4, actual value:  " +
                intToString2.apply(4567).length());

        // 4.3 - a lambdas made using a constructor
        Function<String, BigInteger> newBigInt = BigInteger::new;
        System.out.println("expected value: 123456789, actual value: "+
                newBigInt.apply("123456789"));

        // 4.4 - a lambda made from an instance method
        Consumer<String> print = System.out::println;
        print.accept("Coming to you directly from a lambda...");

        // 4.5 - these two are the same using the static method concat
        UnaryOperator<String> greeting = x -> "Hello, ".concat(x);
        System.out.println(greeting.apply("World"));
        UnaryOperator<String> makeGreeting = "Hello, "::concat;
        System.out.println(makeGreeting.apply("Peggy"));
    }


    @FunctionalInterface
    public interface Calculate {
        int calc(int x, int y);

    }

    // 5. Calculations as functional interface
    public static void CalculateFunctions() {
        //example of passing multiple values to a method using lambda
        //notice that I do NOT have to specify the data type of a and b
        Calculate add =(a,b)-> a + b;
        Calculate difference = (a,b) -> Math.abs(a-b);
        Calculate divide =(a,b)-> (b != 0 ? a/b : 0);

        System.out.println(add.calc(3,2));
        System.out.println(difference.calc(5,10));
        System.out.println(divide.calc(5, 0));
    }


    // 6. Collector and Stream API
    public static void StreamApiWithCollector() {
        List<String> names = Arrays.asList("Paul", "Jane", "Michaela", "Sam");

        // 6.1 - way to sort prior to Java 8 lambdas
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });

        // 6.2 - first iteration with lambda
        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });

        // 6.3 - now remove the return statement
        Collections.sort(names, (String a, String b) -> b.compareTo(a));

        // 6.4 - now remove the data types and allow the compile to infer the type
        Collections.sort(names, (a, b) -> b.compareTo(a));


        Book book1 = new Book("Miss Peregrine's Home for Peculiar Children",
                "Ranson", "Riggs", 382);
        Book book2 = new Book("Harry Potter and The Sorcerers Stone",
                "JK", "Rowling", 411);
        Book book3 = new Book("The Cat in the Hat",
                "Dr", "Seuss", 45);

        List<Book> books = Arrays.asList(book1, book2, book3);

        // 6.5 - total pages in your book collection
        int total = books.stream()
                .collect(Collectors.summingInt(Book::getPages));
        System.out.println(total);

        //create a list with duplicates
        List<Book> dupBooks = Arrays.asList(book1, book2, book3, book1, book2);
        System.out.println("Before removing duplicates: ");
        System.out.println(dupBooks.toString());

        Collection<Book> noDups = new HashSet<>(dupBooks);
        System.out.println("After removing duplicates: ");
        System.out.println(noDups.toString());


        // 6.6 - aggregate author first names into a list
        List<String> list = books.stream()
                .map(Book::getAuthorLName)
                .collect(Collectors.toList());
        System.out.println(list);

        // 6.7 - example of using Set to eliminate dups and sort automatically
        Set<Integer> numbers = new HashSet<>(Arrays.asList(4, 3, 3, 3, 2, 1, 1, 1));
        System.out.println(numbers.toString());



        // 6.8 - Optional - findFirst (Intermediate vs terminal)
        Arrays.asList("red", "green", "blue")
                .stream()
                .sorted()
                .findFirst()
                .ifPresent(System.out::println);

        // 6.9 - example of Stream.of with a filter
        Stream.of("apple", "pear", "banana", "cherry", "apricot")
                .filter(fruit -> {
                    //  System.out.println("filter: " + fruit);
                    return fruit.startsWith("a"); //predicate
                })
                //if the foreach is removed, nothing will print,
                //the foreach makes it a terminal event
                .forEach(fruit -> System.out.println("Starts with A: " + fruit));

        // 6.10 - using a stream and map operation to create a list of words in caps
        List<String> collected = Stream.of("Java", " Rocks")
                .map(string -> string.toUpperCase())
                .collect(Collectors.toList());
        System.out.println(collected.toString());




    }



}



class Book {
    private String title;
    private String authorFName;
    private String authorLName;
    private int pages;

    public Book(String title, String authorFName, String authorLName,
                int pages) {
        this.title = title;
        this.authorFName = authorFName;
        this.authorLName = authorLName;
        this.pages = pages;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorFName() {
        return authorFName;
    }

    public void setAuthorFName(String authorFName) {
        this.authorFName = authorFName;
    }

    public String getAuthorLName() {
        return authorLName;
    }

    public void setAuthorLName(String authorLName) {
        this.authorLName = authorLName;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
    public String toString()
    {
        return getTitle()+" Written By: "+getAuthorFName()+" " +getAuthorLName()+"\n";
    }
}