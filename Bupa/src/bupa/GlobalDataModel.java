package bupa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class GlobalDataModel {

    //
	public BupaDataObj bupaUserData = new BupaDataObj();
    private Boolean isInit = false;
    
    public int stageWidth=0;
    /**��ʼ��*/
    public void INIT(Context context)
    {
    	
    	if(isInit)
    	{
    		return;
    	}
    }
    
    /**��ӡ*/
    public void trace(Object v)
    {
    	System.out.println(v);
    }
    
    //
	public static void goToURL(Context context,String URL) {
		Uri uriUrl = Uri.parse(URL);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl); 
		context.startActivity(launchBrowser);
	}
    
	/**
	 * ȫ�����ģ�͵���
	 * 
	 * **/
    private static GlobalDataModel uniqueInstance = null;

    public static GlobalDataModel getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GlobalDataModel();
        }
        return uniqueInstance;
     }
    
}
