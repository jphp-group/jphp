package php.runtime.reflection;

public class DocumentComment {
    protected final String text;

    public DocumentComment(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
