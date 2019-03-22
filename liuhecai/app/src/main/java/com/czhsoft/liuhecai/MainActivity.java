package com.czhsoft.liuhecai;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{


    private static Button button1;
    private static TextView nowTextView;
    private static TextView resTextView;
    private static TextView nextTextView;

    public static Handler handler = null;

    /**
     * Handler send Msg
     */
    private void InitHandler() {
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what)
                {
                    case 0:
                        Toast.makeText(getApplicationContext(), "不好，查询失败...", 1).show();
                        break;
                    case 1:// network
                        nowTextView.setText(msg.getData().getString("time1"));
                        resTextView.setText(msg.getData().getString("time2"));
                        nextTextView.setText(msg.getData().getString("time3"));
                        Toast.makeText(getApplicationContext(), "妈妈,六合彩查询出来了,再也不用出门问别人...", 1).show();
                        break;
                    default:
                        break;
                }
            }

        };
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }

        InitHandler();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);

            nowTextView = (TextView) rootView.findViewById(R.id.textViewNow);
            resTextView = (TextView) rootView.findViewById(R.id.textViewJieguo);
            nextTextView = (TextView) rootView.findViewById(R.id.textViewNext);

            button1 = (Button) rootView.findViewById(R.id.button1);

            button1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            //1.http://bm78.com/kj/bmjg.js?_=
                            //2.http://0000kj.com/php/data.txt?_=
                            //3.https://www.kj548.com/chajian/bmjg.js?_=
                            String urlDate="https://www.kj548.com/chajian/bmjg.js?_="+new Date().getDay();
                            try {
                                //封装访问服务器的地址
                                URL url =new URL(urlDate);
                                try {
                                    //打开对服务器的连接
                                    HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("GET");
                                    conn.setUseCaches(false);
                                    conn.setFollowRedirects(false);
                                    //连接服务器
                                    conn.connect();

                                    //得到输入流
                                    InputStream is=conn.getInputStream();
                                    //创建包装流
                                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                                    //定义String类型用于储存单行数据
                                    String line=null;
                                    //创建StringBuffer对象用于存储所有数据
                                    StringBuffer sb=new StringBuffer();
                                    while((line=br.readLine())!=null){
                                        sb.append(line);
                                    }

//			                        System.out.println(sb.toString());

                                    Log.i("Thread", sb.toString());

                                    JSONObject myJsonObject = new JSONObject(sb.toString());

                                    String valueString= myJsonObject.getString("k");

                                    Log.i("K", valueString);

                                    String[] reStrings = valueString.split(",");

                                    if(reStrings.length==13)
                                    {
                                        Message msg1 = new Message();
                                        msg1.what = 1;
                                        Bundle bundle=new Bundle();
                                        bundle.putString("time1", "今期是第:"+reStrings[0]+"期");
                                        bundle.putString("time2", "开马结果:"+reStrings[1]+"-"+reStrings[2]+"-"+reStrings[3]+"-"+reStrings[4]+"-"+reStrings[5]+"-"+reStrings[6]+"-"+reStrings[7]);
                                        bundle.putString("time3", "下一期:"+reStrings[8]+"期   开马时间:"+reStrings[9]+"月"+reStrings[10]+"号"+"星期"+reStrings[11]+reStrings[12]);
                                        msg1.setData(bundle);
                                        MainActivity.handler.sendMessage(msg1);


//			                        	nowTextView.setText("今期是第:"+reStrings[0]+"期");
//			                        	resTextView.setText("开马结果:"+reStrings[1]+"-"+reStrings[2]+"-"+reStrings[3]+"-"+reStrings[4]+"-"+reStrings[5]+"-"+reStrings[6]+"-"+reStrings[7]);
//			                        	nextTextView.setText("下一期:"+reStrings[8]+"期   开马时间:"+reStrings[9]+"月"+reStrings[10]+"号"+"星期"+reStrings[11]+reStrings[12]);
//
//			                        	Toast.makeText(rootView.getContext(), "六合彩查询结束，请看结果...", 1).show();
//
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();

                                    Message msg1 = new Message();
                                    msg1.what = 0;
                                    MainActivity.handler.sendMessage(msg1);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();

                                    Message msg1 = new Message();
                                    msg1.what = 0;
                                    MainActivity.handler.sendMessage(msg1);

                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();

                                Message msg1 = new Message();
                                msg1.what = 0;
                                MainActivity.handler.sendMessage(msg1);

                            }
                        }
                    }).start();



                }
            });



            return rootView;
        }
    }

}
