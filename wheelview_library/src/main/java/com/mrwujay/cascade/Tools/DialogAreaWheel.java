package com.mrwujay.cascade.Tools;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import com.mrwujay.cascade.model.CityModel;
import com.mrwujay.cascade.model.DistrictModel;
import com.mrwujay.cascade.model.ProvinceModel;
import com.mrwujay.cascade.service.XmlParserHandler;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * 省 市 区3级联动选择地址
 * @ClassName: AreaChoice 
 * @Description: TODO
 * @author: Administrator杨重诚
 * @date: 2016-3-28 上午11:48:26
 */
public class DialogAreaWheel implements OnWheelChangedListener {

	private Context context;
	private WheelView mViewProvince;//省的滑动组件
	private WheelView mViewCity;//市的滑动组件
	private WheelView mViewDistrict;//区的滑动组件
	
	private int VisibleItems=7;//设置可见条数
	private TextView showTextView;//显示地址的textView
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	
	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName ="";
	
	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode ="";
	
	/**
	 * 初始化
	 * @Title: init 
	 * @Description: TODO
	 * @param context
	 * @param layout
	 * @param dialogStyle
	 * @param animationStyle
	 * @return: void
	 */
	public void init(Context context) {
		/*if (dialog == null) {
			dialog = new Dialog(context, R.style.dialogPhoto);
 			dialog.setContentView(R.layout.dialog_layout);
			Window window = dialog.getWindow();
			window.setWindowAnimations(R.style.dialogPhotoAnimation);
		}*/
		
	}
	
	/**
	 * 初始化组件
	 * @Title: setUpViews 
	 * @Description: TODO
	 * @return: void
	 */
	public void setUpViews(WheelView mViewProvince,WheelView mViewCity,WheelView mViewDistrict) {
		this.mViewProvince = mViewProvince;
		this.mViewCity = mViewCity;
		this.mViewDistrict = mViewDistrict;
		
		setUpListener();
		setUpListener();//添加change事件
		setUpData();
	}
	
	
	/**
	 * 添加change事件
	 * @Title: setUpListener 
	 * @Description: TODO
	 * @return: void
	 */
	private void setUpListener() {
    	// 添加change事件
    	mViewProvince.addChangingListener(this);
    	// 添加change事件
    	mViewCity.addChangingListener(this);
    	// 添加change事件
    	mViewDistrict.addChangingListener(this);
    }
	
	/**
	 * 初始化数据
	 * @Title: setUpData 
	 * @Description: TODO
	 * @return: void
	 */
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(VisibleItems);
		mViewCity.setVisibleItems(VisibleItems);
		mViewDistrict.setVisibleItems(VisibleItems);
		updateCities();
		updateAreas();
	}
	
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue,boolean isUpdate) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			
			showTextView.setText("当前选中:"+mCurrentProviceName+","+mCurrentCityName+","
					+mCurrentDistrictName+","+mCurrentZipCode);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		mViewDistrict.setCurrentItem(0);
		updateCode();
		
		showTextView.setText("当前选中:"+mCurrentProviceName+","+mCurrentCityName+","
				+mCurrentDistrictName+","+mCurrentZipCode);
	}
	
	/**
	 * 根据当前的区，更新区的名字和区划代码
	 * @Title: updateCode 
	 * @Description: TODO
	 * @return: void
	 */
	private void updateCode() {
		int pCurrent = mViewDistrict.getCurrentItem();
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[pCurrent];
		mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		showTextView.setText("当前选中:"+mCurrentProviceName+","+mCurrentCityName+","
				+mCurrentDistrictName+","+mCurrentZipCode);
	}
	
	

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
	
	/**
	 * 解析省市区的XML数据
	 */
	
    protected void initProvinceDatas()
	{
		List<ProvinceModel> provinceList = null;
    	AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			//*/ 初始化默认选中的省、市、区
			if (provinceList!= null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList!= null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			//*/
			mProvinceDatas = new String[provinceList.size()];
        	for (int i=0; i< provinceList.size(); i++) {
        		// 遍历所有省的数据
        		mProvinceDatas[i] = provinceList.get(i).getName();
        		List<CityModel> cityList = provinceList.get(i).getCityList();
        		String[] cityNames = new String[cityList.size()];
        		for (int j=0; j< cityList.size(); j++) {
        			// 遍历省下面的所有市的数据
        			cityNames[j] = cityList.get(j).getName();
        			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
        			String[] distrinctNameArray = new String[districtList.size()];
        			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
        			for (int k=0; k<districtList.size(); k++) {
        				// 遍历市下面所有区/县的数据
        				DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				// 区/县对于的邮编，保存到mZipcodeDatasMap
        				mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				distrinctArray[k] = districtModel;
        				distrinctNameArray[k] = districtModel.getName();
        			}
        			// 市-区/县的数据，保存到mDistrictDatasMap
        			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
        		}
        		// 省-市的数据，保存到mCitisDatasMap
        		mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
        	}
        } catch (Throwable e) {  
            e.printStackTrace();  
        } finally {
        	
        } 
	}

    /**
     * 显示Dialog 
     * @Title: showDialog 
     * @Description: TODO
     * @param context
     * @param showTextView
     * @return: void
     */
    public  DialogAreaWheel(Context context,TextView showTextView){
    	this.context=context;
    	this.showTextView=showTextView;
    }
}
