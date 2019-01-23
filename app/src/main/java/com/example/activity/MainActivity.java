package com.example.activity;

import java.util.ArrayList;
import java.util.Random;

import com.example.adapter.RecentAdapter;
import com.example.database.DataProcessing;
import com.example.database.DataRecent;
import com.example.effect.Slide;
import com.example.items.DeviceItem;
import com.example.items.RecentItem;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;
import android.widget.Toast;

import example.com.devicemanagerims.R;

public class MainActivity extends AppCompatActivity implements
		OnChartValueSelectedListener {

	Button btnListAvailableDevice, btnListBorrowDevice, btnListAllDevice,
			btnListLostDevice, btnScanDevice;
	DataProcessing listTable;
	String scanContent = null;
	//Slide slideIntro;
	ListView listview_recent;
	ArrayList<RecentItem> all_recent_list = new ArrayList<RecentItem>();
	Context context;
	DataRecent data_recent;
	RecentAdapter adapter_recent;

	// /chart
	private PieChart mChart;
	private Typeface tf;
	
	TextSwitcher text_switch;    
    int check=0;
    Random r = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		 text_switch=(TextSwitcher) findViewById(R.id.tw_my_app_title);
		 context = MainActivity.this;	
	     text_switch.setInAnimation(this, android.R.anim.slide_in_left);
	     text_switch.setOutAnimation(this, android.R.anim.slide_out_right);
	     
	     text_switch.setFactory(new ViewFactory() {
	            
	            public View makeView() {
	                
	                TextView myText = new TextView(context);
	                myText.setGravity(Gravity.TOP | Gravity.CENTER);
	                myText.setTextSize(20);
	                myText.setTextColor(Color.BLACK);
	                return myText;
	            }
	        });		
			text_switch.setText("Device Management");
			Thread t = new Thread() {

				  @Override
				  public void run() {
				    try {
				      while (!isInterrupted()) {
				        Thread.sleep(3000);
				        runOnUiThread(new Runnable() {
				          @Override
				          public void run() {
				        	  
				        	  check = r.nextInt(10);
				        	  switch (check) {
				        	  case 0:
				        		  	text_switch.setText("SVMC");
				        		  	break;
				        	  case 1:
									text_switch.setText("Help you manage your devices");
									break;
				        	  case 2:
									text_switch.setText("Make life easy");
									break;
				        	  case 3:
									text_switch.setText("Message Team");
									break;
				        	  case 4:
									text_switch.setText("Scan barcode to get device");
									break;
				        	  case 5:
									text_switch.setText("All is barcode");
									break;
				        	  case 6:
									text_switch.setText("Do right things");
									break;
				        	  case 7:
									text_switch.setText("Think positively");
									break;
				        	  case 8:
									text_switch.setText("Love your ways");
									break;
				        	  case 9:
									text_switch.setText("");
									break;

				        	  default:
				        		  	break;
							}
				            
				          }
				        });
				      }
				    } catch (InterruptedException e) {
				    }
				  }
				};

			t.start();	 
		
		
		btnListAvailableDevice = (Button) findViewById(R.id.btnAvailableDevice);
		btnListBorrowDevice = (Button) findViewById(R.id.btnBorrowDevice);
		btnListAllDevice = (Button) findViewById(R.id.btnAllDevice);
		btnListLostDevice = (Button) findViewById(R.id.btnLostDevice);
		btnScanDevice = (Button) findViewById(R.id.btnScanDevice);		
		
		ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
	        @Override
	        public void getOutline(View view, Outline outline) {
	            
	        	int size = getResources().getDimensionPixelSize(R.dimen.shape_size);
	            outline.setRoundRect(0, 0, size, size, size / 2);
	        }
	    };
	    btnListAvailableDevice.setOutlineProvider(viewOutlineProvider);	
		btnListAvailableDevice.setClipToOutline(true);
		
		btnListBorrowDevice.setOutlineProvider(viewOutlineProvider);	
		btnListBorrowDevice.setClipToOutline(true);
		
		btnListAllDevice.setOutlineProvider(viewOutlineProvider);	
		btnListAllDevice.setClipToOutline(true);
		
		btnListLostDevice.setOutlineProvider(viewOutlineProvider);	
		btnListLostDevice.setClipToOutline(true);
		
		btnScanDevice.setOutlineProvider(viewOutlineProvider);	
		btnScanDevice.setClipToOutline(true);
		
		
		listview_recent = (ListView) findViewById(R.id.listview_recent);

		//slideIntro.setText("Device Management");
		
		data_recent = new DataRecent(context);
		all_recent_list = data_recent.getAllHistory();
		adapter_recent = new RecentAdapter(context, all_recent_list);
		listview_recent.setAdapter(adapter_recent);
		listview_recent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Intent history = new Intent(MainActivity.this, History.class);						
				startActivity(history);
				overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
			}
		});

		// get data
		listTable = new DataProcessing(context);
		ArrayList<Float> list = new ArrayList<Float>();
		list = listTable.getStatusOfAllTable();
		// calculate total device
		float total_device = 0;
		float total_available = 0, total_borrow = 0, total_lost = 0;
		for (int i = 0; i < list.size(); i++) {
			total_device = total_device + list.get(i);
		}
		total_available = list.get(0);
		total_borrow = list.get(1);
		total_lost = list.get(2);

		// //////////////////////////////////////////////////////
		mChart = (PieChart) findViewById(R.id.chart1);
		Description description = new Description();
		description.setText("");
		mChart.setUsePercentValues(true);
		mChart.setDescription(description);
		mChart.setDragDecelerationFrictionCoef(0.95f);
		// tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		// mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(),
		// "OpenSans-Light.ttf"));
		mChart.setDrawHoleEnabled(true);
		//mChart.setHoleColorTransparent(true);
		mChart.setTransparentCircleColor(Color.WHITE);
		mChart.setTransparentCircleAlpha(110);
		mChart.setHoleRadius(45f);
		mChart.setTransparentCircleRadius(65f);
		mChart.setDrawCenterText(true);
		mChart.setRotationAngle(40);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(true);
		// add a selection listener
		mChart.setOnChartValueSelectedListener(this);
		// mChart.setCenterText("Message Team \nhas "+String.valueOf((int)total_device)+" devices");
		mChart.setCenterTextColor(Color.BLACK);
		mChart.setCenterTextSize(16f);

		Legend l = mChart.getLegend();
		l.setEnabled(false);

		// //////////////////////////////////////////////////////
		ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

		for (int i = 0; i < list.size(); i++) {
			yVals1.add(new PieEntry((list.get(i)), i));
		}
		ArrayList<String> xVals = new ArrayList<String>();
		xVals.add(String.valueOf((int) total_available) + " available");
		xVals.add(String.valueOf((int) total_borrow) + " borrow");
		xVals.add(String.valueOf((int) total_lost) + " lost");

		PieDataSet dataSet = new PieDataSet(yVals1, null);

		dataSet.setSliceSpace(3f);
		dataSet.setSelectionShift(5f);

		// add a lot of colors
		ArrayList<Integer> colors = new ArrayList<Integer>();
		int outColor[] = { Color.rgb(76, 173, 80), // mau xanh la cay
				Color.rgb(255, 193, 8), // mau da cam
				Color.rgb(255, 87, 36), // mau do
				Color.rgb(34, 152, 242) // mau xanh da troi
		};
		for (int c : outColor)
			colors.add(c);
		// for (int c : ColorTemplate.JOYFUL_COLORS)
		// colors.add(c);
		// for (int c : ColorTemplate.COLORFUL_COLORS)
		// colors.add(c);
		// for (int c : ColorTemplate.LIBERTY_COLORS)
		// colors.add(c);
		// for (int c : ColorTemplate.PASTEL_COLORS)
		// colors.add(c);
		colors.add(ColorTemplate.getHoloBlue());

		dataSet.setColors(colors);
		//PieData data = new PieData(xVals, dataSet);
		PieData data = new PieData( dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(15f);
		data.setValueTextColor(Color.BLACK);
		data.setValueTypeface(tf);
		mChart.setData(data);
		// undo all highlights
		mChart.highlightValues(null);
		mChart.invalidate();
		mChart.animateY(1800, Easing.EasingOption.EaseInOutQuad);
		// //////////////////////////////////////////////////////
		// list available device
		btnListAvailableDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent listAvailable = new Intent(MainActivity.this, AvailableActivity.class);
				startActivity(listAvailable);
				overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
			}
		});
		// list borrow device
		btnListBorrowDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent listBorrow = new Intent(MainActivity.this, BorrowActivity.class);
				startActivity(listBorrow);
				overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
			}
		});
		// list all device
		btnListAllDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent listAll = new Intent(MainActivity.this, AllDeviceActivity.class);
				startActivity(listAll);
				overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
			}
		});
		// list lost device
		btnListLostDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent listLost = new Intent(MainActivity.this,	LostActivity.class);
				startActivity(listLost);
				overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
			}
		});

		btnScanDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// instantiate ZXing integration class
				IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
				// start scanning
				scanIntegrator.initiateScan();
				overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
			}
		});

	}

	@Override
	public void onValueSelected(Entry e, Highlight h) {

	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onResume() {

		// get recent data
		all_recent_list = data_recent.getAllRecent();
		adapter_recent = new RecentAdapter(context, all_recent_list);
		listview_recent.setAdapter(adapter_recent);

		// get data
		DataProcessing listTable = new DataProcessing(this);
		ArrayList<Float> list = new ArrayList<Float>();
		list = listTable.getStatusOfAllTable();
		float total_device = 0;
		float total_available = 0, total_borrow = 0, total_lost = 0;
		for (int i = 0; i < list.size(); i++) {
			total_device = total_device + list.get(i);
		}
		total_available = list.get(0);
		total_borrow = list.get(1);
		total_lost = list.get(2);
		// //////////////////////////////////////////////////////
		ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

		for (int i = 0; i < list.size(); i++) {
			yVals1.add(new PieEntry((list.get(i)), i));
		}
		// mChart.setCenterText("Message Team \nhas "+String.valueOf((int)total_device)+" devices");
		ArrayList<String> xVals = new ArrayList<String>();
		if (list.get(0) == 0)
			xVals.add("");
		else
			xVals.add(String.valueOf((int) total_available) + " available");
		if (list.get(1) == 0)
			xVals.add("");
		else
			xVals.add(String.valueOf((int) total_borrow) + " borrow");
		if (list.get(2) == 0)
			xVals.add("");
		else
			xVals.add(String.valueOf((int) total_lost) + " lost");
		PieDataSet dataSet = new PieDataSet(yVals1, null);
		dataSet.setSliceSpace(2);
		// add a lot of colors
		ArrayList<Integer> colors = new ArrayList<Integer>();
		int outColor[] = { Color.rgb(76, 173, 80), // mau xanh la cay
				Color.rgb(255, 193, 8), // mau da cam
				Color.rgb(255, 87, 36), // mau do
				Color.rgb(34, 152, 242) // mau xanh da troi
		};
		for (int c : outColor)
			colors.add(c);
		colors.add(ColorTemplate.getHoloBlue());

		dataSet.setColors(colors);
		//PieData data = new PieData(xVals, dataSet);
		PieData data = new PieData(dataSet);

		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(15f);
		data.setValueTextColor(Color.BLACK);
		data.setValueTypeface(tf);
		mChart.setData(data);
		// undo all highlights
		mChart.highlightValues(null);
		mChart.invalidate();

		mChart.animateY(1800, Easing.EasingOption.EaseInOutCubic);
		Legend l = mChart.getLegend();
		l.setEnabled(false);
		super.onResume();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent1) {
		// retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent1);
		// check we have a valid result
		if (scanningResult != null) {
			// get content from Intent Result
			scanContent = scanningResult.getContents();

			if (scanContent != null) {

				if (scanContent.matches("\\d+") == true) {
					DeviceItem item = listTable.detectDevice(scanContent);
					if (item == null) {
						Intent addNewAvaileble = new Intent(MainActivity.this,
								AddNewDevice.class);
						addNewAvaileble.putExtra("imei", scanContent);
						startActivity(addNewAvaileble);
						overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
					} else {
						if (item.getIs_lost() != null)// device was lost
						{
							Intent intent = new Intent(MainActivity.this,
									RevertDevice.class);
							// Pass all data imei
							intent.putExtra("imei", (item.getImei()));
							// Pass all data name_of_model
							intent.putExtra("name_of_model",(item.getName_of_model()));
							// Pass all data owner
							intent.putExtra("owner", (item.getOwner()));
							// Pass all data avatar_of_owner
							intent.putExtra("avatar_of_owner",(item.getAvatar_of_owner()));
							// Pass all data day_of_owner
							intent.putExtra("day_of_owner",	(item.getDay_of_owner()));
							// Pass all data is_borrow
							intent.putExtra("is_borrow", (item.getIs_borrow()));
							// Pass all data borrower
							intent.putExtra("borrower", (item.getBorrower()));
							// Pass all data avatar_of_borrower
							intent.putExtra("avatar_of_borrower",(item.getAvatar_of_borrower()));
							// Pass all data day_of_borrow
							intent.putExtra("day_of_borrow",(item.getDay_of_borrow()));
							// Pass all data is_lost
							intent.putExtra("is_lost", (item.getIs_lost()));
							// Pass all data day_of_lost
							intent.putExtra("day_of_lost",(item.getDay_of_lost()));
							// passs default data
							intent.putExtra("avatar_from_camera", "from_main");
							// start Activity
							startActivity(intent);
							overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
						} else {
							if (item.getIs_borrow() == null)// device is
															// available
							{
								Intent intent = new Intent(MainActivity.this,
										BorrowDevice.class);
								// Pass all data imei
								intent.putExtra("imei", (item.getImei()));
								// Pass all data name_of_model
								intent.putExtra("name_of_model",(item.getName_of_model()));
								// Pass all data owner
								intent.putExtra("owner", (item.getOwner()));
								// Pass all data avatar_of_owner
								intent.putExtra("avatar_of_owner",(item.getAvatar_of_owner()));
								// Pass all data day_of_owner
								intent.putExtra("day_of_owner",	(item.getDay_of_owner()));
								// Pass all data is_borrow
								intent.putExtra("is_borrow",(item.getIs_borrow()));
								// Pass all data borrower
								intent.putExtra("borrower", (item.getBorrower()));
								// Pass all data avatar_of_borrower
								intent.putExtra("avatar_of_borrower", (item.getAvatar_of_borrower()));
								// Pass all data day_of_borrow
								intent.putExtra("day_of_borrow",(item.getDay_of_borrow()));
								// Pass all data is_lost
								intent.putExtra("is_lost", (item.getIs_lost()));
								// Pass all data day_of_lost
								intent.putExtra("day_of_lost",	(item.getDay_of_lost()));
								// passs default data
								intent.putExtra("avatar_from_camera",
										"from_main");
								// start Activity
								startActivity(intent);
								overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
							} else// device was borrowed
							{

								Intent intent = new Intent(MainActivity.this,
										ReturnDevice.class);
								// Pass all data imei
								intent.putExtra("imei", (item.getImei()));
								// Pass all data name_of_model
								intent.putExtra("name_of_model",(item.getName_of_model()));
								// Pass all data owner
								intent.putExtra("owner", (item.getOwner()));
								// Pass all data avatar_of_owner
								intent.putExtra("avatar_of_owner", (item.getAvatar_of_owner()));
								// Pass all data day_of_owner
								intent.putExtra("day_of_owner",	(item.getDay_of_owner()));
								// Pass all data is_borrow
								intent.putExtra("is_borrow", (item.getIs_borrow()));
								// Pass all data borrower
								intent.putExtra("borrower", (item.getBorrower()));
								// Pass all data avatar_of_borrower
								intent.putExtra("avatar_of_borrower",(item.getAvatar_of_borrower()));
								// Pass all data day_of_borrow
								intent.putExtra("day_of_borrow",(item.getDay_of_borrow()));
								// Pass all data is_lost
								intent.putExtra("is_lost", (item.getIs_lost()));
								// Pass all data day_of_lost
								intent.putExtra("day_of_lost",	(item.getDay_of_lost()));
								// start Activity
								startActivity(intent);
								overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
							}
						}

					}
				} else {
					// instantiate ZXing integration class
					IntentIntegrator scanIntegrator = new IntentIntegrator(
							MainActivity.this);
					// start scanning
					scanIntegrator.initiateScan();
				}
			}
		} else {
			// invalid scan data or scan canceled
			Toast.makeText(getApplicationContext(), "No scan data received!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPointerCaptureChanged(boolean hasCapture) {

	}
}
