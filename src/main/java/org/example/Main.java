package org.example;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        TransactionManager manager = new TransactionManager();
        Scanner sc = new Scanner(System.in);

        // Supplier : 임의의 트랜잭션 생성 (공급)
        Supplier<Transaction> randomTransactionSupplier = () -> {
            int randId = (int)(Math.random() * 1000);
            double randAmount = Math.random() * 100000;
            return new Transaction(randId, "PAYMENT", randAmount);
        }; // 람다식으로 구현

        boolean run = true;
        while (run) {
            System.out.println("\n=== 메뉴 ===");
            System.out.println("1. 트랜잭션 수동 추가");
            System.out.println("2. 트랜잭션 임의(Supplier) 추가");
            System.out.println("3. 특정 유형 필터링(Predicate)");
            System.out.println("4. 금액 변환(Function) 결과 보기");
            System.out.println("5. 모든 트랜잭션 출력(Consumer)");
            System.out.println("6. 종료");
            System.out.print("선택: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("ID 입력: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("유형 입력(PAYMENT/REFUND 등): ");
                    String type = sc.nextLine();
                    System.out.print("금액 입력: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();

                    Transaction t = new Transaction(id, type, amount);
                    manager.addTransaction(t);
                    System.out.println("[info] 트랜잭션이 추가되었습니다.");
                    break;

                case 2:
                    // supplier 이용
                    Transaction randT = randomTransactionSupplier.get();
                    manager.addTransaction(randT);
                    System.out.println("[Info] 임의 트랜잭션 추가: " + randT);
                    break;

                case 3:
                    // Predicate 이용
                    System.out.print("필터링할 유형 입력: ");
                    String filterType = sc.nextLine();
                    Predicate<Transaction> byType = tran -> tran.getType().equalsIgnoreCase(filterType); // Type이 대소문자 비교 없이 같으면 true, 다르면 false <-> equals는 대소문자 비교
                    // Predicate 구현하고
                    // 위의 구현체의 조건에 true가 나오는 애들만 add됨
                    List<Transaction> filtered = manager.filterTransactions(byType);
                    System.out.println("[결과] 필터링된 트랜잭션: " + filtered);
                    break;

                case 4:
                    // Function 이용
                    System.out.print("할인율(%) 입력");
                    double discountPercent = sc.nextDouble();
                    sc.nextLine();

                    // 할인율에 따라 Transaction객체에서 할인된 Double 값을 반환
                    Function<Transaction, Double> discountFunc = tran -> tran.getAmount() * (1 - (discountPercent/100.0));
                    List<Double> discountedAmounts = manager.mapAmounts(discountFunc);
                    System.out.print("[결과] 변환된 금액 목록: " + discountedAmounts);
                    break;

                case 5:
                    // Consumer 이용
                    Consumer<Transaction> printTran = tran -> System.out.println("[Tx] " + tran);
                    manager.processTransactions(printTran);
                    break;

                case 6:
                    run = false;
                    System.out.println("[info] 종료합니다.");
                    break;

                default:
                    System.out.println("[Error] 잘못된 입력입니다.");

            }
        }

        sc.close();

    }
}