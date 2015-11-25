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
import net.arnx.jsonic.JSONException;

class IchiranSend
{
	public int id;
	public String title;
}

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
	public IchiranRecv ichiranRecv;

}

class RecvData2
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

        //記事の受け取り＆送信処理
        RecvData recvData = null;
        try {
			recvData = JSON.decode(request.getInputStream(),RecvData.class);
		} catch (Exception e1) {
		}

        //記事一覧
        if(recvData == null || recvData.cmd.equals("read"))
        {
        	//一覧受け取り
            try {
    			//データの送信処理
    			ArrayList<IchiranSend> list3 = new ArrayList<IchiranSend>();
    			ResultSet res = mOracle.query("select * from db_kigi order by id desc");
    			while(res.next())
    			{
    				IchiranSend ichiranSend = new IchiranSend();
    				ichiranSend.id = res.getInt(1);
    				ichiranSend.title = res.getString(2);
    				list3.add(ichiranSend);
    			}
    			//JSON形式に変換
                String json2 = JSON.encode(list3);
                //出力
                out.println(json2);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
        }else if(recvData.cmd.equals("read2"))
        {
            //記事内容一覧
            //記事の受け取り＆送信処理
            IchiranRecv ichiranRecv = recvData.ichiranRecv;
           // if("write".equals(ichiranRecv.id))
            {
            	String sql = String.format("select * from db_kigi where id = '%d' ",ichiranRecv.id);
            	ResultSet res = mOracle.query(sql);
            	try {
					if(res.next())
					{
					    KijiSend kijiSend = new KijiSend();

							kijiSend.title = res.getString(2);
							kijiSend.news = res.getString(3);
							//JSON形式に変換
					        String json = JSON.encode(kijiSend);
					        //出力
					        out.println(json);

					}
					res.close();
				} catch (JSONException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

            }

        }



        //データの受け取り処理
       try {
			RecvData2 recvData2 = JSON.decode(request.getInputStream(),RecvData2.class);
			if("write".equals(recvData2))
			{
				//RecvData ichiranRecv = new RecvData();
				IchiranRecv ichiranRecv = recvData.ichiranRecv;
				//書き込み処理
				String sql = String.format("insert into db_exam values('%s',db_exam_seq.nextval,'%s','%s')",
						ichiranRecv.id,recvData2.name,recvData2.msg);
				mOracle.execute(sql);
			}
		} catch (Exception e1) {
			// TODO 自動生成された catch ブロック
		e1.printStackTrace();
		}


      try {
		//データの送信処理
    	  IchiranRecv ichiranRecv = recvData.ichiranRecv;
		ArrayList<SendData> list2 = new ArrayList<SendData>();
		String sql = String.format("select * from db_exam where kiji_id = '%s'  order by id ",ichiranRecv.id);
		ResultSet res = mOracle.query(sql);
		while(res.next())
		{
			SendData sendData = new SendData();
			sendData.id = res.getInt(1);
			sendData.name = res.getString(2);
			sendData.msg = res.getString(3);
			list2.add(sendData);
		}
		//JSON形式に変換
          String json2 = JSON.encode(list2);
          //出力
            out.println(json2);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
