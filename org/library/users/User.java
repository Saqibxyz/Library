package org.library.users;


public abstract class User {
    protected String userId;
    protected String name;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return "[" + getRole() + "] " + name + " (ID: " + userId + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return this.userId.equals(other.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}