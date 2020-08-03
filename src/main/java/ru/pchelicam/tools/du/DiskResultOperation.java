package ru.pchelicam.tools.du;

/**
 * Contains information about disk operations
 */
public class DiskResultOperation {
    private boolean resultOk;
    private String resMessage;
    private Exception resultException;

    public boolean isResultOk() {
        return resultOk;
    }

    public void setResultOk(boolean resultOk) {
        this.resultOk = resultOk;
    }

    public String getResMessage() {
        return resMessage;
    }

    public void setResMessage(String resMessage) {
        this.resMessage = resMessage;
    }

    public Exception getResultException() {
        return resultException;
    }

    public void setResultException(Exception resultException) {
        this.resultException = resultException;
    }
}
