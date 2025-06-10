import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class App {
    private Scanner scanner = new Scanner(System.in, "EUC-KR");
    private Connection conn;

    public App() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/this_is_java",
                "java",
                "mysql"
            );
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public void list() {
        System.out.println();
        System.out.println("[게시물 목록]");
        System.out.println("--------------------------------");
        System.out.printf("%-6s%-12s%-16s%-40s\n", "no", "writer", "date", "title");
        System.out.println("--------------------------------");
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT bno, btitle, bcontent, bwriter, bdate ");
            sql.append("FROM boards ");
            sql.append("ORDER BY bno DESC ");
            sql.append("LIMIT 5");
            
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Board board = new Board();
                board.setBno(rs.getInt("bno"));
                board.setBtitle(rs.getString("btitle"));
                board.setBcontent(rs.getString("bcontent"));
                board.setBwriter(rs.getString("bwriter"));
                board.setBdate(rs.getDate("bdate"));

                System.out.printf("%-6d%-12s%-16s%-40s\n", 
                    board.getBno(), 
                    board.getBwriter(), 
                    board.getBdate(), 
                    board.getBtitle().toString()
                );
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
        
        mainMenu();
    }

    public void mainMenu() {
        try {
            System.out.println();
            System.out.println("--------------------------------");
            System.out.println("메인 메뉴: 1.Create | 2.Read | 3.Clear | 4.Exit");
            System.out.print("메뉴 선택: ");
            
            if (scanner.hasNextLine()) {
                String menuNo = scanner.nextLine();
                System.out.println();

                switch (menuNo) {
                    case "1" -> create();
                    case "2" -> read();
                    case "3" -> clear();
                    case "4" -> exit();
                    default -> {
                        System.out.println("잘못된 메뉴 번호입니다.");
                        mainMenu();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("오류가 발생했습니다: " + e.getMessage());
            mainMenu();
        }

        list();
    }

    public void create() {
        Board board = new Board();
        System.out.println("[게시물 작성]");
        System.out.print("제목: ");
        board.setBtitle(scanner.nextLine());
        System.out.print("내용: ");
        board.setBcontent(scanner.nextLine());
        System.out.print("작성자: ");
        board.setBwriter(scanner.nextLine());

        // 보조 메뉴 출력
        System.out.println("--------------------------------");
        System.out.println("1. 저장 | 2. 취소");
        System.out.print("선택: ");
        String menuNo = scanner.nextLine();
        switch (menuNo) {
            case "1" -> {
                try {
                    StringBuilder sql = new StringBuilder();
                    sql.append("INSERT INTO boards (btitle, bcontent, bwriter, bdate)");
                    sql.append("VALUES (?, ?, ?, NOW())");
    
                    PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                    pstmt.setString(1, board.getBtitle());
                    pstmt.setString(2, board.getBcontent());
                    pstmt.setString(3, board.getBwriter());
                    pstmt.executeUpdate();
    
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    exit();
                }
            }
            case "2" -> {
            }
        }
    }

    public void read() {
        System.out.println("[게시물 상세]");
        System.out.print("bno: ");
        int bno = Integer.parseInt(scanner.nextLine());

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT bno, btitle, bcontent, bwriter, bdate ");
            sql.append("FROM boards ");
            sql.append("WHERE bno = ? ");

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, bno);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Board board = new Board();
                board.setBno(rs.getInt("bno"));
                board.setBtitle(rs.getString("btitle"));
                board.setBcontent(rs.getString("bcontent"));
                board.setBwriter(rs.getString("bwriter"));
                board.setBdate(rs.getDate("bdate"));

                System.out.println("##########");
                System.out.println("번호: " + board.getBno());
                System.out.println("제목: " + board.getBtitle());
                System.out.println("내용: " + board.getBcontent());
                System.out.println("작성자: " + board.getBwriter());
                System.out.println("작성일: " + board.getBdate());
                System.out.println("##########");

                System.out.println("--------------------------------");
                System.out.println("보조 메뉴: 1. 수정 | 2. 삭제 | 3. 목록");
                System.out.println("메뉴 선택: ");
                String menuNo = scanner.nextLine();
                System.out.println();

                switch (menuNo) {
                    case "1" -> update(board);
                    case "2" -> delete(board);
                }
            }
            
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public void update(Board board) {
        System.out.println("[게시물 수정]");
        System.out.print("제목: ");
        board.setBtitle(scanner.nextLine());
        System.out.print("내용: ");
        board.setBcontent(scanner.nextLine());
        System.out.print("작성자: ");
        board.setBwriter(scanner.nextLine());

        System.out.println("--------------------------------");
        System.out.println("1. 저장 | 2. 취소");
        System.out.print("선택: ");
        String menuNo = scanner.nextLine();
        switch (menuNo) {
            case "1" -> {
                try {
                    StringBuilder sql = new StringBuilder();
                    sql.append("UPDATE boards ");
                    sql.append("SET btitle = ?, bcontent = ?, bwriter = ? ");
                    sql.append("WHERE bno = ? ");
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                    pstmt.setString(1, board.getBtitle());
                    pstmt.setString(2, board.getBcontent());
                    pstmt.setString(3, board.getBwriter());
                    pstmt.setInt(4, board.getBno());
                    pstmt.executeUpdate();

                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    exit();
                }
            }
        }
    }

    public void delete(Board board) {
        try {
            String sql = "DELETE FROM boards WHERE bno = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, board.getBno());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public void clear() {
        System.out.println("[게시물 전체 삭제]");
        System.out.println("--------------------------------");
        System.out.println("1. 삭제 | 2. 취소");
        System.out.print("선택: ");
        String menuNo = scanner.nextLine();
        switch (menuNo) {
            case "1" -> {
                try {
                    String sql= "TRUNCATE TABLE boards";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.executeUpdate();

                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    exit();
                }
            }
        }       
    }

    public void exit() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("프로그램 종료");
        System.exit(0);
    }

    public static void main(String[] args) {
        App app = new App();
        app.list();
    }
}
