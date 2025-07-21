package org.library;
public class Student extends User {
    private static final int DEFAULT_BORROW_LIMIT = 5;
    final private int borrowLimit;
    private int borrowedCount;
    public Student(String userId, String name) {
        super(userId, name);
        this.borrowLimit = DEFAULT_BORROW_LIMIT;
        this.borrowedCount = 0;
    }
    public Student(String userId, String name, int customLimit) {
        super(userId, name);
        this.borrowLimit = customLimit;
        this.borrowedCount = 0;
    }
    @Override
    public String getRole() {
        return "Student";
    }
    public boolean canBorrow() {
        return borrowedCount >= borrowLimit;
    }
    public void borrowOne() {
        if (canBorrow()) {
            throw new IllegalStateException("Borrow limit reached");
        }
        borrowedCount++;
    }
    public void returnOne() {
        if (borrowedCount <= 0) {
            throw new IllegalStateException("No borrowed books to return");
        }
        borrowedCount--;
    }
    @Override
    public String toString() {
        return super.toString() +
                " [Borrowed: " + borrowedCount +
                "/" + borrowLimit + "]";
    }
}
