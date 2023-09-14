package TextBoard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class DataCenter {
   private static final HashMap<Integer, Text> newsMap = new HashMap<Integer, Text>();

    static boolean isEmptyNews()
    {
        return newsMap.isEmpty();
    }

    static int getNewsSize()
    {
        return newsMap.size();
    }

    static boolean findNews(int idx)
    {
        return newsMap.containsKey(idx);
    }

    static Text getNews(int idx)
    {
        return newsMap.get(idx);
    }

    static boolean SetNews(Text txt)
    {
        try
        {
            newsMap.put(newsMap.size(), txt);
        }
        catch (Exception e)
        {
                return false;
        }
        return true;
    }

    static boolean UpdateNews(int num, String title, String content)
    {
        try
        {
            newsMap.get(num).setTitle(title);
            newsMap.get(num).setBody(content);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    static void removeNews(int idx)
    {
        newsMap.remove(idx);
    }



    private static final HashMap<String, Member> memberMap = new HashMap<String, Member>();

    static boolean findMember(String id)
   {
       return memberMap.containsKey(id);
   }

   static Member getMember(String id)
   {
       return memberMap.get(id);
   }

   static void SetMember(String id, Member member)
   {
       memberMap.put(id, member);
   }
}

class Login
{
    private boolean status;
    private String id;

    Login(boolean status)
    {
        this.status = status;
        this.id = "";
    }

    public void login(String id)
    {
        this.status = true;
        this.id = id;
    }
    public void logout()
    {
        this.status = false;
        this.id = "";
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

class Member
{
    private String PW;
    private String name;


    Member(String pw, String name)
    {
        this.PW = pw;
        this.name = name;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Text {
    private String id;
    private String title;
    private String body;
    private LocalDateTime time;
    private int view;
    private int good;
    private final ArrayList<Comment> comment;

    Text(String id, String title, String body, LocalDateTime time) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.time = time;
        this.view = 0;
        this.good = 0;
        comment = new ArrayList<Comment>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getView() {
        return view;
    }

    public int getGood() {
        return good;
    }

    public void GoodPush() {
        this.good++;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getTime() {
        return time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(String content, LocalDateTime time) {
        this.comment.add(new Comment(content, time));
    }
}


class Comment
{
    private String content;
    private LocalDateTime time;

    Comment(String content, LocalDateTime time)
    {
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}