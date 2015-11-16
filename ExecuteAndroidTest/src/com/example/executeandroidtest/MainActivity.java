package com.example.executeandroidtest;


import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends Activity {

	public static final String TAG = "ExtcuteAndroidTest";
	
	//运行标志
	public String autoRunLabel = "AutoRun";
	public static boolean autoRunFlagChecked = false;	//是否取得过自动执行标志位
	public static boolean autoRunFlag = false;	//自动执行脚本
	//总体超时时间
	public String timeoutLabel = "totalTimeout";
	public static boolean timeoutChecked = false;
	public static int timeout = 900;
	
	//测试路径
	public static final String TestDirPath = "/data/executeAndroidTest/";
	public static final String TestResPath = TestDirPath + "TestResources";
	public static final String TestLogPath = TestDirPath + "TestLog";
	public static final String TestScriptPath = TestDirPath + "TestScript";
	
	public static FileManager fileManager = new FileManager();
	
	public static LinkedList<TestcaseInfo> testCaseList = new LinkedList<TestcaseInfo>();
	
	private Button startButton;
	private CheckBox selectAll, deleteLog;
	private Spinner spinner_RunMode, spinner_RunTime;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        spinner_RunMode=(Spinner)findViewById(R.id.spinner_RunMode);
        //createFromResouce将返回ArrayAdapter<CharSequence>，具有三个参数：
        //第一个是context，也就是application的环境，可以设置为this，也可以通过getContext()获取.
        //第二个参数是从data source中的array ID，也就是我们在strings中设置的ID号；
        //第三个参数是spinner未展开的UI格式
        ArrayAdapter<CharSequence> adapter_0 = ArrayAdapter.createFromResource( this, R.array.RunMode_array, android.R.layout.simple_spinner_item);
        adapter_0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_RunMode.setAdapter(adapter_0);
        
        spinner_RunTime=(Spinner)findViewById(R.id.spinner_RunTime);
        ArrayAdapter<CharSequence> adapter_1 = ArrayAdapter.createFromResource( this, R.array.RunTime_array, android.R.layout.simple_spinner_item);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_RunTime.setAdapter(adapter_1);
        
        selectAll = (CheckBox)findViewById(R.id.selectAll);
        deleteLog = (CheckBox)findViewById(R.id.deleteLog);
        startButton = (Button)findViewById(R.id.startButton);
        
        //读取TestcaseConfig.cfg，得到测试例列表
        if(!LoadConfigFlie(TestDirPath + "TestcaseConfig.cfg")) {
        	Log.v(TAG, "Read TestcaseConfig.cfg failed, please check it!!!");
        	finish();
        	return;
        }
        	
        //根据测试列表，初始化TableLayout01
        TableLayout testcaseLayout = (TableLayout)findViewById(R.id.TableLayout01);
        for(int i=0; i < testCaseList.size(); i++){
        	TableRow testcaseRow = new TableRow(this);
        	testcaseLayout.addView(testcaseRow);
			
			CheckBox caseSelected = new CheckBox(this);
			caseSelected.setText(testCaseList.get(i).TestcaseDesc);
			if(Integer.parseInt(testCaseList.get(i).SelectState) == 0) 
				caseSelected.setChecked(false);
			else
			{
				caseSelected.setChecked(true);
			}

			//自动执行时，不可选择测试例
			if(autoRunFlag) 
				caseSelected.setEnabled(false);
			else 
				caseSelected.setEnabled(true);
			
			testcaseRow.addView(caseSelected);
			
			TextView testcaseStatus = new TextView(this);
			testcaseStatus.setText("Waiting");
			testcaseStatus.setGravity(Gravity.RIGHT);
			testcaseRow.addView(testcaseStatus);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	public boolean LoadConfigFlie(String Path)
	{
		Log.v(TAG, "Begin to LoadConfigFlie");
		testCaseList.clear();
		String file = fileManager.readFromSDcard(Path);
		if(file == null || file.equals("") )
		{
			Log.e(TAG, "LoadConfigFlie error, File is null.");
			return false;
		}

		String[] fileline = file.split("\n");
		if(fileline == null || fileline.equals("") || fileline.length<=0)
		{
			Log.e(TAG, "LoadConfigFlie error, File line is null.");
			return false;
		}
		
		for(int i=0;i<fileline.length;i++)
		{
			if(!autoRunFlagChecked && fileline[i].indexOf(autoRunLabel) >= 0)
			{
				String resultStr = fileline[i].substring(fileline[i].indexOf("=")+1,fileline[i].indexOf("]")).trim();
				Log.v(TAG, "LoadConfigFlie autoRunFlag = " + resultStr);
				int result = Integer.parseInt(resultStr);
				if ( 0 != result ) autoRunFlag = true;
				else autoRunFlag = false;
				autoRunFlagChecked = true;
			}else if( !timeoutChecked && fileline[i].indexOf(timeoutLabel) >= 0)
			{
				String resultStr = fileline[i].substring(fileline[i].indexOf("=")+1,fileline[i].indexOf("]")).trim();
				Log.v(TAG, "LoadConfigFlie totle timeout = " + resultStr);
				timeout = Integer.parseInt(resultStr);
				timeoutChecked = true;
			}else
			{
				TestcaseInfo tmp = new TestcaseInfo();
				int infoindex = 0;
				int startindex = 0;
				int endindex = 0;
				while( startindex>=0 && endindex>=0 )
				{
					startindex = fileline[i].indexOf("[",endindex);
					if( startindex < endindex ) break;
					endindex = fileline[i].indexOf("]",startindex);
					if( endindex < startindex ) break;				
					if( endindex - startindex <= 1 ) break;
						
					String tmpstr = fileline[i].substring(startindex+1, endindex);
					
					if(infoindex == 0) tmp.TestcaseName = tmpstr; //用例编号
					else if(infoindex == 1)tmp.TestcaseDesc = tmpstr; //用例描述
					else if(infoindex == 2)tmp.TestcaseType = tmpstr; //脚本名或者包名
					else if(infoindex == 3)	tmp.SelectState = tmpstr; //选择状态
					infoindex ++;
				}
				if (infoindex >=3){
					tmp.TestcaseResult = false; //初始用例结果都为fail
					testCaseList.addLast(tmp);
					Log.v(TAG, "LoadConfigFlie case number = " + testCaseList.size());
				}
			}
		}
		Log.v(TAG, "LoadConfigFlie leave");
		if(!autoRunFlagChecked || !timeoutChecked )
		{
			Log.e(TAG, "LoadConfigFlie get fail.");
			return false;
		}else return true;
	}
}

class TestcaseInfo
{
	public String TestcaseName; 	//用例编号
	public String TestcaseType; 	//package、shell、Instrument分类
	public String TestcaseDesc;		//用例描述
	public String SelectState;		//用例选择状态
	public boolean TestcaseResult;	//用例结果
}
