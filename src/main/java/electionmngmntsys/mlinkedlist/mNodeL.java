package electionmngmntsys.mlinkedlist;

import java.util.Objects;

public class mNodeL <T>{
    public T data;
    public mNodeL<T> next;


    public mNodeL(T data, mNodeL<T> next) {
        this.data = data;
        this.next = next;

    }

    public T getContent() {
        return data;
    }

    public mNodeL<T> getNext() {
        return next;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        mNodeL<?> mNodeL = (mNodeL<?>) o;
        return data.equals(mNodeL.data);
    }
}
