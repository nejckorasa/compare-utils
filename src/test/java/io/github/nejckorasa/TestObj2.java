package io.github.nejckorasa;

import java.util.Objects;

public class TestObj2 {
    private String strField;
    private Long longField;

    public TestObj2(final String strField, final Long longField) {
        this.strField = strField;
        this.longField = longField;
    }

    public String getStrField() {
        return strField;
    }

    public void setStrField(final String strField) {
        this.strField = strField;
    }

    public Long getLongField() {
        return longField;
    }

    public void setLongField(final Long longField) {
        this.longField = longField;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestObj2)) {
            return false;
        }
        final TestObj2 that = (TestObj2) o;
        return Objects.equals(getStrField(), that.getStrField()) &&
                Objects.equals(getLongField(), that.getLongField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStrField(), getLongField());
    }
}
