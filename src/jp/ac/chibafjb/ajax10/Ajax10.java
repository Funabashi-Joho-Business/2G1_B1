package jp.ac.chibafjb.ajax10;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;


class IchiranRecv
{
	public int id;
}

class KijiSend
{
	public int id;
	public String title;
	public String news;
}

class RecvData
{
	public String cmd;
	public String name;
	public String msg;
}

class SendData
{
	public int id;
	public String name;
	public String msg;
}

/**
 * Servlet implementation class Ajax10
 */
@WebServlet("/Ajax10")
public class Ajax10 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private jp.ac.chibafjb.ajax10.Oracle mOracle;
	private final String DB_ID = "x14g020";
	private final String DB_PASS = "furutama0811";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Ajax10() {
        super();
        // TODO Auto-generated constructor stub
    }
	public void init() throws ServletException {
		// TODO 自動生成されたメソッド・スタブ
		super.init();


		try{
			mOracle = new Oracle();
			mOracle.connect("ux4", DB_ID, DB_PASS);

			//テーブルが無ければ作成

			if(!mOracle.isTable("db_kigi")&& !mOracle.isTable("db_exam"))


			if(!mOracle.isTable("db_exam10")/*&&!mOracle.isTable("db_coment01")*/)

			if(/*!mOracle.isTable("db_kigi")&&*/ !mOracle.isTable("db_exam10"))

			{
				mOracle.execute("create table db_kigi(id number,title varchar2(200),news varchar2(4000))");
				mOracle.execute("create sequence db_kiji_seq");
				mOracle.execute("create table db_exam(kiji_id number,com_id number,name varchar2(200),msg varchar2(200))");
				mOracle.execute("create sequence db_exam_seq");
			}
		} catch (Exception e) {
			System.err.println("認証に失敗しました");
		}
	}

	@Override
	public void destroy() {
		//DB切断
		mOracle.close();
		// TODO 自動生成されたメソッド・スタブ
		super.destroy();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		action(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		action(request,response);
	}
	private void action(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//出力ストリームの作成
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();

        //記事の受け取り処理
        IchiranRecv ichiranRecv = JSON.decode(request.getInputStream(),IchiranRecv.class);
        if("write".equals(ichiranRecv.id))
        {
        	String d = String.valueOf(ichiranRecv.id);
        	ResultSet id = mOracle.query("select * from db_kigi where id = '%s' ",d);
        }
        	/*while(id.next()){
        KijiSend kijiSend = new KijiSend();
        kijiSend.title = id.getString(2);*/

        //データの受け取り処理
        RecvData recvData = JSON.decode(request.getInputStream(),RecvData.class);
        if("write".equals(recvData.cmd))
        {
        	//書き込み処理
        	String sql = String.format("insert into db_exam values(db_exam_seq.nextval,'%s','%s')",
        			recvData.name,recvData.msg);
        	mOracle.execute(sql);
        }


        try {
			//データの送信処理
			ArrayList<SendData> list = new ArrayList<SendData>();
			ResultSet res = mOracle.query("select * from db_exam order by id ");
			while(res.next())
			{
				SendData sendData = new SendData();
				sendData.id = res.getInt(1);
				sendData.name = res.getString(2);
				sendData.msg = res.getString(3);
				list.add(sendData);
			}
			//JSON形式に変換
            String json = JSON.encode(list);
            //出力
            out.println(json);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
