package codersafterdark.reskillable.api.data;

public final class GenericLockKey implements LockKey {
    private Class<? extends LockKey> type;

    public GenericLockKey(Class<? extends LockKey> type) { //Type should never be null but in case it is we do handle it below
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof GenericLockKey && (this.type == null ? ((GenericLockKey) o).type == null : this.type.equals(((GenericLockKey) o).type));
    }

    @Override
    public int hashCode() {
        return this.type == null ? super.hashCode() : this.type.hashCode();
    }
}