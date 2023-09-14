
package TextBoard;

import netscape.javascript.JSObject;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class TextBoard {
    static Scanner scan = new Scanner(System.in);
    static Login login = new Login(false);
    static String title = "";
    static String body = "";
    static  DB_Connect db = new DB_Connect();

    public static void main(String[] args) {
        String menu = "";


        var idx = -1;

        while (!menu.equals("exit")) {
            System.out.print("메뉴를 선택해 주세요.");
            if(login.isStatus())
            {
                System.out.print("["+login.getId()+"(" +
                        db.select("Select NICKNAME from member where ID = " + login.getId(), 1).get(0)[0]  + ")" + "]");
            }

            System.out.println(" (signup : 회원가입, login : 로그인, add : 게시글 추가, list : 게시글 조회, update : 게시글 수정, delete : 게시글 삭제, exit : 프로그램 종료)");

            menu = scan.nextLine();

            switch (menu) {
                case "signup" ->
                {
                    signup();
                }
                case "login" ->
                {
                    login();
                }
                case "logout"->
                {
                    logout();
                }
                case "add" -> {
                    add();
                } case "list" -> {
                    list(false, -1);
                }
                case "update" -> {
                    if (!loginStatus() || isEmpty())
                    {
                        continue;
                    }

                    System.out.println("수정할 게시글의 번호를 입력해 주세요. : ");
                    idx = scan.nextInt();
                    scan.nextLine();

                    update(idx);
                }
                case "delete" -> {
                    if ( !loginStatus() || isEmpty() )
                    {
                        continue;
                    }
                    System.out.println("삭제할 게시글의 번호를 입력해 주세요. : ");
                    idx = scan.nextInt();
                    scan.nextLine();

                    delete(idx);

                }
                case "detail" -> {
                    if(isEmpty())
                    {
                        continue;
                    }
                    System.out.println("상세보기 할 게시글의 번호를 입력해 주세요. : ");
                    idx = scan.nextInt();
                    scan.nextLine();

                    detail(idx);
                }
                case "search" -> {
                    if(isEmpty())
                    {
                        continue;
                    }
                    System.out.println("검색 키워드를 입력해주세요. :");
                    String str = scan.nextLine();
                    boolean res = false;
                    var data = db.select("SELECT num FROM article WHERE title LIKE '%" + str + "%'", 1);
                    if (!data.isEmpty()) {
                        list(false, Integer.valueOf(data.get(0)[0]));
                    }
                    else {
                        System.out.println("검색 결과가 없습니다.");
                    }
                }
                case "exid" -> {
                }
                default -> System.out.println("없는 메뉴입니다.");
            }
        }

        System.out.println("프로그램을 종료합니다.");

    }

    public static void signup()
    {
        System.out.println("==== 회원 가입을 진행합니다 ===");
        while (true)
        {
            System.out.println("ID를 입력해 주세요.");


            String  id = scan.nextLine();

            String pw;
            String name;

            if(db.select("SELECT ID FROM MEMBER WHERE ID = '" + id + "'", 1) == null)
            {
                System.out.println("사용할 수 있는 ID 입니다.");

                System.out.println("비밀번호를 입력해 주세요.");
                pw =  scan.nextLine();

                System.out.println("닉네임을 입력해 주세요.");
                name =  scan.nextLine();

                ArrayList<Datas> arr = new ArrayList<Datas>();
                arr.add(new Datas(id));
                arr.add(new Datas(pw));
                arr.add(new Datas(name));

                db.insert("MEMBER", arr);

                //DataCenter.SetMember(id, new Member(pw, name));
                System.out.println("==== 회원 가입이 완료되었습니다. ===");
                break;
            }
            else {
                System.out.println("중복된 ID 입니다. \n다시 입력해 주세요.");
            }
        }
    }

    public static void login()
    {
        System.out.println("ID를 입력해 주세요.");
        String id = scan.nextLine();
        System.out.println("PW를 입력해 주세요.");
        String pw = scan.nextLine();
        var data = db.select("SELECT ID, PW, NICKNAME FROM MEMBER WHERE ID = '" + id + "'", 3);
        if(data != null)
        {
            if(data.get(0)[1].equals(pw))
            {
                login.login(id);
                System.out.println(data.get(0)[2] + "님 환영합니다.");
            }
        }
        else{
            System.out.println("없는 회원정보입니다.\nID와 비밀번호를 확인해 주세요.");
        }
    }

    public static boolean loginStatus()
    {
        if(!login.isStatus())
        {
            System.out.println("로그인이 필요한 서비스 입니다.");
            return false;
        }
        return true;
    }

    public static void logout()
    {
        if(!loginStatus())
        {
            return;
        }

        login.logout();
        System.out.println("로그아웃 되었습니다.");
    }

    public static boolean idCompair(int idx)
    {
        if(DataCenter.getNews(idx).getId().equals(login.getId()))
        {
            return true;
        }
        System.out.println(DataCenter.getMember(login.getId()).getName() + "님이 쓴 글이 아닙니다.");
        return  false;
    }

    public static void add() {
        if(!loginStatus())
        {
            return;
        }

        System.out.println("제목을 입력해 주세요. : ");
        title = scan.nextLine();
        System.out.println("내용을 입력해 주세요. : ");
        body = scan.nextLine();
        ArrayList<Datas> datas = new ArrayList<Datas>();
        datas.add(new Datas(null));
        datas.add(new Datas(login.getId()));
        datas.add(new Datas(title));
        datas.add(new Datas(body));
        datas.add(new Datas(LocalDateTime.now()));
        datas.add(new Datas(0));
        datas.add(new Datas(0));
        if (db.insert("article", datas)) {
            System.out.println("게시물이 추가 되었습니다.");
        }
    }

    public static void list(Boolean detail, int num) {

        if(detail)
        {
            var data = db.select("SELECT * from article where NUM = " + num, 7);

            System.out.println("=============================");
            System.out.printf("번호 : %s \n", data.get(0)[0]);
            System.out.printf("제목 : %s \n", data.get(0)[1]);
            System.out.printf("내용 : %s \n", data.get(0)[2]);
            System.out.printf("등록시간 : %s \n", data.get(0)[3]);
            System.out.printf("조회수 : %s \n", data.get(0)[4]);
            System.out.printf("추천수 : %s \n", data.get(0)[5]);

            var comm = db.select("SELECT * FROM comments", 3);
            if(comm.size() != 0)
            {
                for(int i = 0; i<comm.size(); i++)
                {
                    if(comm.get(i)[0].equals(String.valueOf(num)))
                    {
                        System.out.println("=============================");
                        System.out.println("댓글 내용 : " + comm.get(i)[1]);
                        System.out.println("댓글 작성일 : " + comm.get(i)[2]);
                    }
                }
            }
        }
        else if(num != -1)
        {
            var data = db.select("select NUM, TITLE from article where NUM = " + num, 2);
            System.out.println("=============================");
            System.out.printf("번호 : %s \n", data.get(0)[0]);
            System.out.printf("제목 : %s \n", data.get(0)[1]);
        }
        else {
            var data = db.select("SELECT NUM, TITLE FROM article", 2);
            for (int i = 0; i < data.size(); i++) {
                System.out.println("=============================");
                System.out.printf("번호 : %s \n", data.get(i)[0]);
                System.out.printf("제목 : %s \n", data.get(i)[1]);
            }
        }

        System.out.println("=============================");
    }

    public static boolean isEmpty()
    {
        if(db.select("SELECT * FROM article", 1).isEmpty())
        {
            System.out.println("등록된 게시글이 없습니다.");
            return true;
        }
        return false;
    }

    public static boolean findNewsIDX(int idx) {
        if(!isEmpty())
        {
            if (!db.select("SELECT * from article where NUM = " + idx, 1).isEmpty()) {
                return true;
            }
            System.out.println("없는 게시글 번호 입니다.");
        }
        return false;
    }

    public static void update(int idx) {
        if(!idCompair(idx))
        {
            System.out.println("본인이 쓴 글만 수정할 수 있습니다.");
            return;
        }

        if (findNewsIDX(idx)) {
            System.out.println("제목을 입력해 주세요. : ");
            title = scan.nextLine();
            System.out.println("내용을 입력해 주세요. : ");
            body = scan.nextLine();

            if(DataCenter.UpdateNews(idx, title,body))
            {
                System.out.println("게시글이 수정되었습니다.");
            }
            else {
                System.out.println("게시글 수정에서 에러가 발생했습니다.");
            }
        }
    }

    public static void delete(int idx) {

        if(!idCompair(idx))
        {
            System.out.println("본인이 쓴 글만 삭제할 수 있습니다.");
            return;
        }

        if (findNewsIDX(idx)) {
            try {
                DataCenter.removeNews(idx);
            } catch (Exception e) {
                System.out.println("게시글 삭제에서 에러가 발생했습니다.");
            }
            System.out.println("게시글이 삭제되었습니다.");
        }
    }

    public static void detail(int idx)
    {
        if(findNewsIDX(idx))
        {
            int sub_menu = -1;
            while (sub_menu != 5) {

                list(true, idx);

                System.out.println("상세보기 추가 기능을 선택해주세요.(1. 댓글 등록, 2. 추천, 3. 수정, 4. 삭제, 5. 목록으로) :");
                try {
                    sub_menu = scan.nextInt();
                    scan.nextLine();
                } catch (Exception e) {
                    scan.nextLine();
                    System.out.println("상세보기 추가 기능은 숫자로만 입력할 수 있습니다.");
                    sub_menu = 0;
                    continue;
                }

                sub_menu = detailMenu(idx, sub_menu);
            }
        }
    }


    public static int detailMenu(int idx, int menu_idx)
    {
        switch (menu_idx) {
            case 1 -> {
                if(!loginStatus())
                {
                    return 0;
                }

                System.out.println("댓글 내용 : ");
                body = scan.nextLine();
                ArrayList<Datas> datas = new ArrayList<Datas>();
                datas.add(new Datas(idx));
                datas.add(new Datas(body));
                datas.add(new Datas(LocalDateTime.now()));
                db.insert("comments", datas);
            }
            case 2 ->
            {
                if(!loginStatus())
                {
                    return 0;
                }
                DataCenter.getNews(idx).GoodPush();
            }
            case 3 -> {
                if(!loginStatus())
                {
                    return 0;
                }
                update(idx);
            }
            case 4 -> {
                if(!loginStatus())
                {
                    return 0;
                }

                delete(idx);
                System.out.println("메인 메뉴로 돌아갑니다.");
                menu_idx = 5;
            }
            case 5 -> {
            }
            default -> System.out.println("없는 메뉴입니다.");
        }
        return menu_idx;
    }
}

