public class Call {
    private boolean status = true;

    public void changeStatus() {
        status = !status;
    }

    public boolean isStatus() {
        return status;
    }
}
