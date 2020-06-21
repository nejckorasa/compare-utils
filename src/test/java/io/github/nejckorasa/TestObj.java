package io.github.nejckorasa;

import java.util.Objects;

public class TestObj {
    private String strField;
    private Integer intField;

    public TestObj(final String strField, final Integer intField) {
        this.strField = strField;
        this.intField = intField;
    }

    public String getStrField() {
        return strField;
    }

    public void setStrField(final String strField) {
        this.strField = strField;
    }

    public Integer getIntField() {
        return intField;
    }

    public void setIntField(final Integer intField) {
        this.intField = intField;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestObj)) {
            return false;
        }
        final TestObj that = (TestObj) o;
        return Objects.equals(getStrField(), that.getStrField()) &&
                Objects.equals(getIntField(), that.getIntField());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStrField(), getIntField());
    }
}
