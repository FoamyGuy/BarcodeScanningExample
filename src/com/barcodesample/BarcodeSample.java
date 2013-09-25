package com.barcodesample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BarcodeSample extends Activity {

	private Button scanBtn;
	private TextView resultsTxt;


	/** Called when the activity is first created. */;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		scanBtn = (Button) findViewById(R.id.scanBtn);
		resultsTxt = (TextView) findViewById(R.id.resultsTxt);

		
		/**************************************
		 * When the user clicks (eg press, touch, scroll to with track ball and press enter)
		 * the scan button this onClick() method is going to get 
		 * called. We need to put whatever we want to happen inside of this method..
		 ***************************************/
		scanBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				resultsTxt.setText("Scanning...");
				
				/**************************************
				 * Intents are objects that get passed between applications.
				 * In this example we are going to create an Intent 
				 * that will ask the Barcode Scanner app to scan a code and give us the result.
				 ***************************************/
				
				
				Intent intent = new Intent("com.google.zxing.client.android.SCAN"); 

				/* you can optionally add an extra to the intent that tells it 
				 * what type of code its looking for. Like this:
				 * 
				 * intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				 * 
				 * If you don't put that in it will scan all types.
				 */

				try {
					startActivityForResult(intent, 0);
				} catch (ActivityNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					/**************************************
					 * If BarcodeScanner is not installed we are going to create a 
					 * Dialog to prompt the user to install it from the market.
					 ***************************************/
					new AlertDialog.Builder(v.getContext())
					.setTitle("WARNING:")
					.setMessage("You don't have Barcode Scanner installed. Please install it.")
					.setCancelable(false) // <--- Whether or not you want the back button to close the Dialog.
					.setNeutralButton("Install it now", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							/**************************************
							 * When they click on the button we will open up the
							 * Market and search for BarcodeScanner by package
							 * name.
							 ***************************************/
							Uri uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android");  
							startActivity(new Intent(Intent.ACTION_VIEW, uri));											
						}
					})
					.show(); // <--- Remember to show your the dialog once it is created.
				}

			}
		});



	}

	/*Here is where we come back after the Barcode Scanner is done*/
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				// contents contains whatever the code was
				String contents = intent.getStringExtra("SCAN_RESULT");

				// Format contains the type of code i.e. UPC, EAN, QRCode etc...
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				// Handle successful scan. In this example I just put the results into the TextView
				resultsTxt.setText(format + "\n" + contents);
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel. If the user presses 'back' before a code is scanned.
				resultsTxt.setText("Canceled");
			}
		}
	}




}