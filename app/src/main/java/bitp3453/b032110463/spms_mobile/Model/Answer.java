package bitp3453.b032110463.spms_mobile.Model;

public class Answer {
    private String text;
    private int no;

    public int getAttachmentId() {
        return attachmentId;
    }

    private int attachmentId;

    public Answer(){
        no = -1;
    }

    public Answer( int no,String text,int attachmentId) {
        this.text = text;
        this.no = no;
        this.attachmentId = attachmentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
