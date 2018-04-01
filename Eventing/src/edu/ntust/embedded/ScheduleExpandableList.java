package karaoke_song.es;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;






import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	/** MENU CONSTANTS **/
	/*private static final int MENU_SETTING = Menu.FIRST,
			MENU_ABOUT = Menu.FIRST + 1, MENU_LOGOUT = Menu.FIRST + 2,
			MENU_PRIVACY = Menu.FIRST + 3, MENU_PRIVATE_MES = Menu.FIRST + 4,
			MENU_GROUP = Menu.FIRST + 5, MENU_REVEAL = Menu.FIRST + 6,
			MENU_HELP = Menu.FIRST + 7, MENU_CONTACTS = Menu.FIRST + 8, MENU_CARD_MANAGE = Menu.FIRST+9,Menu_PERSONAL_CARD = Menu.FIRST+10;
	*/
	private static final int MENU_SETTING = Menu.FIRST,
			MENU_ABOUT = Menu.FIRST + 1, MENU_ModifySQL = Menu.FIRST + 2,
			MENU_HELP = Menu.FIRST + 3;//,MENU_search_YouTube = Menu.FIRST + 4;
	/** CONTEXT MENU CONSTANTS **/
	public final static int MODIFY_context_menu = Menu.FIRST;
	public final static int REMOVE_context_menu = Menu.FIRST + 1;
	public final static int ADD_JOB_context_menu = Menu.FIRST + 2;
	
	
	// Debugging
    private static final String TAG = "BluetoothMgr";
    private static final boolean D = true;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    int prestatus = 0;
    static int reconnect = 0;
    int autoconnect = 0;
    
    static String address;
    
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
  //  private TextView mTitle;
    private ListView mConversationView;
    //private EditText mOutEditText;
    private Button mSendButton, mSendButton1;

    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    private StringBuffer mOutStringBuffer;
    
    EditText search;
	
	Button s1, s2, s3;
	
	ListView lv;
	
	ImageButton a1,a2,a3,a4,a5,a6;
	
	ArrayList<song> songdata;
	
    private File vSDCard = null;

    ArrayAdapter<String> aa;
    
	ArrayList<String> sid = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        //透過activity_main.xml為layout
        setContentView(R.layout.activity_main);
        setupViewComponent();
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

        //輸入的部份，與程式做連結
        search = (EditText) findViewById(R.id.editText1);
        //清除預設開keyboard
        search.clearFocus();
        
        //對應列表
        lv = (ListView) findViewById(R.id.listView1);
        //到資料庫讀取歌單
        songdata = SQLHandler.getAllsong(this);
        
        // 判斷 SD Card 有無插入
        if( Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED) )
        {
          openOptionsDialog("sdcard error");
          Toast.makeText(MainActivity.this, "SD card error", Toast.LENGTH_LONG).show();
          return;
        }
        else
        {
           // 取得 SD Card 位置
           vSDCard = Environment.getExternalStorageDirectory();
           Toast.makeText(MainActivity.this, "SD位置:"+vSDCard, Toast.LENGTH_LONG).show();
        }
        

        //如果找不到歌單,代表必須將歌單匯入資料庫
        if (songdata == null)
        {
        	//loading file
        	File vPath = new File( vSDCard.getParent() + vSDCard.getName() + "/" );
            if( !vPath.exists() )
               vPath.mkdirs();
            
            //開檔讀取SONG.txt
            FileReader rFile;
			try {
				rFile = new FileReader( vSDCard.getParent() + "/" + vSDCard.getName()  +"/Download/SONG.TXT" );				
				if (rFile == null)
	            {
		        	openOptionsDialog("請將廠商提供的資料(SONG.TXT)，放到sdcard目錄中，再重試一次。");
		        	Toast.makeText(MainActivity.this, "請將廠商提供的資料(SONG.TXT)，放到sdcard目錄中，再重試一次。", Toast.LENGTH_LONG).show();
		        	return;
	            }
				
				//將token用,來讀取，放在資料庫中
				BufferedReader br = new BufferedReader(rFile);
                String line = null;

                while((line = br.readLine()) !=null)
                {
                  
                  //一個token一個toek看
                  StringTokenizer stoken = new StringTokenizer( line, "," );
                  Log.i("TAG", line);
                  String sid = "";
                  String name = "";
                  String singer = "";

                  //然後加入到對應的欄位
                  int count=0;
                  while( stoken.hasMoreTokens() )
                  {
                    switch (count)
                    {
                      case 0:
                        sid = stoken.nextToken();
                        break;
                      case 1:
                        name = stoken.nextToken();
                        break;
                      case 2:
                    	singer = stoken.nextToken();
                        break;
                    }
                    count++;
                  }                  
                  Log.i("TAG", sid + "->" + name + "->" + singer);
                  //寫入資料庫，編號，歌手，歌名
                  SQLHandler.insertSong(this, sid, name, singer);
                }
               
                rFile.close();
                
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
	        songdata = SQLHandler.getAllsong(this);
        }
       
        //重新將歌單放入列表中
        reload(0, "");
        
        //按下歌曲的按鈕
        s1 = (Button) findViewById(R.id.button1);
		s1.setOnClickListener(new Button.OnClickListener()
	     {
	        	public void onClick(View v)
	        	{
	        		//讀取歌曲，並重讀列表
	        		String keywords = search.getText().toString();
	        		Log.i("TAG",  "find: " + keywords);
	                reload(2, keywords);
	        	}
	      }
	    );
		
		//按下歌手的按鈕
		s2 = (Button) findViewById(R.id.button3);
		s2.setOnClickListener(new Button.OnClickListener()
	     {
	        	public void onClick(View v)
	        	{
	        		//讀取歌手，並重讀列表
	        		String keywords = search.getText().toString();
	                reload(1, keywords);
	        	}
	      }
	    );
		
		//按下清除的按鈕
		s3 = (Button) findViewById(R.id.button2);
		s3.setOnClickListener(new Button.OnClickListener()
	     {
	        	public void onClick(View v)
	        	{
	        		//清除的按鈕
	        		reload(0, "");
	        	}
	      }
	    );
		
		
		
    }

   

	@Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

      
    }

   
    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
       
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

  
    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                //String message = view.getText().toString();
                //sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    //將index要求的部份，全部份放到列表清單中，然後顯示列表或條件
    void reload(int index, String keywords)
    {
    	//沒有資料返回
    	if (songdata == null) return;
    	
        String[] stockArr = new String[songdata.size()];
        
		ArrayList<String> query = new ArrayList<String>();
    	
		//index = 0代表顯示所有歌曲
    	if (index == 0)
    	{
            sid.clear();
            for (int i=0; i<songdata.size() ; i++)
            {
            	stockArr[i] = songdata.get(i).name + " (" + songdata.get(i).singer + ")";
            	sid.add(songdata.get(i).sid);
            }    		
    	}
    	else if (index == 1)
    	{
    		//index = 0代表顯示keywords條件歌曲
            sid.clear();
            for (int i=0; i<songdata.size() ; i++)
            {
            	if (songdata.get(i).name.contains(keywords))
            	{
	            	query.add(songdata.get(i).name + " (" + songdata.get(i).singer + ")");
	            	sid.add(songdata.get(i).sid);
            	}
            }    		
			
			stockArr = new String[query.size()];
			for (int i=0; i<query.size() ; i++)
            {
            	stockArr[i] = query.get(i);
            }    	
    		
    	}
    	else if (index == 2)
    	{
    		//index = 0代表顯示keywords條件歌手
            sid.clear();
            for (int i=0; i<songdata.size() ; i++)
            {
            	if (songdata.get(i).singer.contains(keywords))
            	{
	            	query.add(songdata.get(i).name + " (" + songdata.get(i).singer + ")");
	            	sid.add(songdata.get(i).sid);
	            	
	            	
            	}
            }    	

            //如果有符合皂就把它用string array存起來
			stockArr = new String[query.size()];
			for (int i=0; i<query.size() ; i++)
            {
            	stockArr[i] = query.get(i);
            }    	
			
    		
    	}
    	
    	//並放入列表中
        aa = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_expandable_list_item_1, stockArr);
        
        lv.setAdapter(aa);
		
        //當列表被按下時
        lv.setOnItemClickListener(new OnItemClickListener() 
	    {          
	    	   @Override  
	    	   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	    	     long arg3) 
	    	   {
	    		   //Toast.makeText(MainActivity.this, "arg2:"+ arg2 + " arg3:" + arg3, Toast.LENGTH_LONG).show();
	    		   //arg2 arg3 means select item x
	    		   
	    		   Intent app = new Intent(MainActivity.this, SongPlayer.class);
	    		   
	    		   Bundle bundle = new Bundle();
	    		   bundle.putString("Data_eurl", songdata.get(arg2).sid);
	    		   bundle.putString("Song_name", songdata.get(arg2).name);
	    		   bundle.putString("Singer", songdata.get(arg2).singer);
	    		   //bundle.putString("Data_eurl", (String) ((TextView) arg1).getText());
	    		   //bundle.putString("Data_eurl", "6XSabEWWFYI");
	    		   app.putExtras(bundle);
	    		   startActivity(app);
	    	   }  
	    });
    	
    }


    //按上問是否要離開
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{  
    		openOptionsDialog();
    		return true;
    	}
		
		return super.onKeyDown(keyCode, event);  
    }

    //show message, ask exit yes or no
    private void openOptionsDialog() {
      
      new AlertDialog.Builder(this)
        .setTitle("您要離開嗎？")
        .setMessage("您要離開嗎？")
        .setNegativeButton("否",
            new DialogInterface.OnClickListener() {
            
              public void onClick(DialogInterface dialoginterface, int i) 
              {
              }
        }
        )
     
        .setPositiveButton("是",
            new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) 
            {
              android.os.Process.killProcess(android.os.Process.myPid());           
              finish();
            }
            
        }
        )
        
        .show();
    }   
    
    //show message
    public void openOptionsDialog(String info)
    {
      new AlertDialog.Builder(this)
      .setTitle("找不到歌曲資料")
      .setCancelable(false)
      .setMessage(info)
      .setPositiveButton("OK",
          new DialogInterface.OnClickListener()
          {
           public void onClick(DialogInterface dialoginterface, int i)
           {
        	   android.os.Process.killProcess(android.os.Process.myPid());           
               finish();
           }
           }
          )
      .show();
    }    
    
    private void setupViewComponent() {
		// TODO Auto-generated method stub
    }
    public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu submenu = menu.addSubMenu(0, MENU_SETTING, 0, "設定").setIcon(
				android.R.drawable.ic_menu_preferences);

		//submenu.add(0, MENU_PRIVATE_MES, 0, "私人訊息");
		//submenu.add(0, MENU_GROUP, 1, "群組");
		submenu.add(0, MENU_HELP, 2, "幫助");
		//submenu.add(0, MENU_REVEAL, 3, "顯示選項");
		//submenu.add(0, MENU_PRIVACY, 4, "隱私選項");
		menu.add(0, MENU_ABOUT, 1, "關於").setIcon(
				android.R.drawable.ic_dialog_info);
		menu.add(0, MENU_ModifySQL,Menu.NONE, "修改資料");
		//menu.add(0, MENU_search_YouTube,Menu.NONE, "搜尋影片");
		//menu.add(0, MENU_LOGOUT, 2, "登出").setIcon(
		//		android.R.drawable.ic_menu_close_clear_cancel);
		//menu.add(0, MENU_CONTACTS,Menu.NONE, "聯絡人");
		//menu.add(0,MENU_CARD_MANAGE, Menu.NONE, "名片管理");
		//menu.add(0,Menu_PERSONAL_CARD, Menu.NONE, "個人名片");

		return super.onCreateOptionsMenu(menu);

	}
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ABOUT:
				new AlertDialog.Builder(MainActivity.this)
				.setTitle("關於此程式")
				.setMessage(R.string.about)
				.setIcon(android.R.drawable.star_big_on)
				.show();
				break;
				
			/*case MENU_LOGOUT:
				Intent intent2 = new Intent();
				intent2.setClass(Main.this, NFCProjectActivity.class);
				startActivity(intent2);
				break;*/
				
			case MENU_HELP:
				new AlertDialog.Builder(MainActivity.this)
				.setTitle("幫助")
				.setMessage(R.string.help)
				.setIcon(android.R.drawable.star_big_on)
				.show();
				break;
			case MENU_ModifySQL:
				Intent intent3 = new Intent();
				intent3.setClass(MainActivity.this, ModifySQL.class);
				startActivity(intent3);
				break;
			/*case MENU_PRIVATE_MES:
				Intent intent3 = new Intent();
				intent3.setClass(Main.this, message.class);
				startActivity(intent3);
				break;*/
			/*case MENU_search_YouTube: 
				Intent intent4 = new Intent();
				intent4.setClass(MainActivity.this, YoutubeSearch.class);
				startActivity(intent4);
				break;*/
			/*case MENU_REVEAL:
				Intent intent6 = new Intent();
				intent6.setClass(Main.this, showdec.class);
				startActivity(intent6);
		        break;*/
			/*case MENU_CONTACTS:
				Intent intent5 = new Intent();
				intent5.setClass(Main.this, contact.class);
				startActivity(intent5);
				break;*/
			/*case MENU_CARD_MANAGE:
				Intent intent7 = new Intent();
				intent7.setClass(Main.this, cardmanage.class);
				startActivity(intent7);
				break;*/
			/*case Menu_PERSONAL_CARD:
				Intent intent = new Intent();
				intent.setClass(Main.this, PersonalCard.class);
				startActivity(intent);
				break;*/
			/*case MENU_PRIVACY:
				Intent intent8 = new Intent();
				intent8.setClass(Main.this, privatedec.class);
				startActivity(intent8);
				break;*/	
				
				
		}
		return super.onOptionsItemSelected(item);
	}
}