//// reviewed
//package org.library.users.student;
//
//import org.library.users.User;
//
//public class Student extends User {
//    final private int borrowLimit;
//    private int borrowedCount;
//    public Student(String userId, String name, int customLimit) {
//        super(userId, name);
//        this.borrowLimit = customLimit;
//        this.borrowedCount = 0;
//    }
//    @Override
//    public String getRole() {
//        return "Student";
//    }
//    public boolean canBorrow() {
//        return borrowedCount < borrowLimit;
//    }
//    public int getBorrowedCount(){
//        return borrowedCount;
//    }
//    public void borrowOne() {
//        borrowedCount++;
//    }
//    public void returnOne() {
//        borrowedCount--;
//    }
//    @Override
//    public String toString() {
//        return super.toString() +
//                " [Borrowed: " + borrowedCount +
//                "/" + borrowLimit + "]";
//    }
//}
package org.library.users.student;

import org.library.users.User;

public class Student extends User {
    private final int borrowLimit;
    private int borrowedCount;

    public Student(String userId, String name, String password, int borrowLimit) {
        super(userId, name, password);
        this.borrowLimit = borrowLimit;
        this.borrowedCount = 0;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public boolean canBorrow() {
        return borrowedCount < borrowLimit;
    }

    public boolean canReturn() {
        return borrowedCount>0;
    }

    public void borrowOne() {
        borrowedCount++;
    }

    public void returnOne() {
        borrowedCount--;
    }

    @Override
    public String toString() {
        return super.toString() + " [Borrowed: " + borrowedCount + "/" + borrowLimit + "]";
    }
}
