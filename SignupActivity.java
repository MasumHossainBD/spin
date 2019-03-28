package thunderapp.masum.spin.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import thunderapp.masum.spin.R;


public class SignupActivity extends AppCompatActivity {

    //      |=================|
    //======|ID Implementation|=========//
    //      |=================|

    @BindView(R.id.ibtnBack)
    ImageButton ibtnBack;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPass)
    EditText etPass;
    @BindView(R.id.etCfPass)
    EditText etCfPass;
    @BindView(R.id.etRef)
    EditText etRef;
    @BindView(R.id.chSingupage)
    CheckBox chSingupage;
    FirebaseAuth auth;
    @BindView(R.id.circle_loading_view)
    AnimatedCircleLoadingView circleLoadingView;
    @BindView(R.id.ibtnSignUp)
    Button ibtnSignUp;
    TextView tvlogin;

String i;
    boolean dublicate=false;
 DatabaseReference databaseReference;
    //      |=====================|
    //======|END ID Implementation|=========//
    //      |=====================|


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();

        tvlogin=findViewById(R.id.tvLoginNow);
        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));

            }
        });


////////////////////////////////getting previous child


        DatabaseReference databaseReference0=FirebaseDatabase.getInstance().getReference("user");
        databaseReference0.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        i=dataSnapshot.getChildrenCount()+"";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick({R.id.ibtnBack, R.id.ibtnSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibtnBack:
                break;
            case R.id.ibtnSignUp:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
                    return;
                }
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
               final String imie = telephonyManager.getDeviceId();
               final String name = etName.getText().toString();
               final String email = etEmail.getText().toString();
               final String address = etAddress.getText().toString();
               final String mobile = etMobile.getText().toString();
               final String password = etPass.getText().toString();
               final String cfpassword = etCfPass.getText().toString();
               final String reference = etRef.getText().toString();


                if (isEmailValid(email) && password.equals(cfpassword) && password.length() >= 6 && chSingupage.isChecked()) {
                    circleLoadingView.startDeterminate();

                  auth.createUserWithEmailAndPassword(email ,password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {


                              FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                              UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(mobile).build();
                              user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {

                                      final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                      Query queryToGetData = dbRef.child("user")
                                              .orderByChild("mobile").equalTo(mobile);
                                      queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                              if(!dataSnapshot.exists()){
                                                  Log.i("123321","number ok");
                                                  dublicate=false;


                                                  final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                                  Query queryToGetData = dbRef.child("user")
                                                          .orderByChild("imie").equalTo(imie);
                                                  queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                          if(!dataSnapshot.exists()){
                                                              Log.i("123321","ok"+imie);
                                                              dublicate=false;

                                                              payrefferer(reference);
                                                              ////////////////////////
                                                              DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user").child(etMobile.getText().toString());


                                                              databaseReference.child("address").setValue(address);
                                                              databaseReference.child("reffered").setValue("0");
                                                              databaseReference.child("approve").setValue("false");
                                                              databaseReference.child("click").setValue("0");
                                                              databaseReference.child("email").setValue(email);
                                                              databaseReference.child("imie").setValue(imie);
                                                              databaseReference.child("invalid").setValue("0");
                                                              databaseReference.child("mobile").setValue(mobile);
                                                              databaseReference.child("name").setValue(name);
                                                              databaseReference.child("point").setValue(0);
                                                              databaseReference.child("spin").setValue("150");
                                                              databaseReference.child("date").setValue("0");
                                                              databaseReference.child("totalreffer").setValue(0);
                                                              databaseReference.child("totalcashout").setValue("0");
                                                         //     databaseReference.child("reffer_code").setValue(getRandomNumberString());

                                                              int count=Integer.parseInt(i)+1;
                                                              /////////////////////////////////////////submit to database////////////////
                                                              databaseReference.child("aa Serial").setValue(""+count);

                                                              sendEmail();






                                                          }
                                                          else {
                                                              dublicate =true;
                                                              Log.i("123321","Phone already used "+dataSnapshot.getValue());
                                                              Toast.makeText(SignupActivity.this, "Phone " +
                                                                      " already used "+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();

                                                          }
                                                      }

                                                      @Override
                                                      public void onCancelled(DatabaseError databaseError) {

                                                      }
                                                  });


                                              }
                                              else {
                                                  dublicate =true;
                                                  Log.i("123321","Phone Number already used "+dataSnapshot.getValue());
                                                  Toast.makeText(SignupActivity.this, "Phone Number already used "+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();

                                              }
                                          }

                                          @Override
                                          public void onCancelled(DatabaseError databaseError) {

                                          }
                                      });
                          





                                  }
                              });



                      }
                  });

                } else {

                    MDToast.makeText(getApplicationContext(), "Please Enter A Valid Email And Password or Check The Box", MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
                }

                break;
        }
    }

    boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void payrefferer(final String number){
        final DatabaseReference databaseReference00= FirebaseDatabase.getInstance().getReference("user").child(number).child("point");
        databaseReference00.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    Log.i("123321",dataSnapshot+" previous");
                    String previousScore = dataSnapshot.getValue()+"";

                Log.i("123321",previousScore+" work previous");
                    int i =Integer.parseInt(previousScore);
                    int finalScore = Integer.parseInt(previousScore) + 100;
                    databaseReference00.setValue(finalScore);

                    circleLoadingView.stopOk();Log.i("123321","success reffer");



                    final DatabaseReference databaseReference001= FirebaseDatabase.getInstance().getReference("user").child(number).child("totalreffer");
                    databaseReference001.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                if (dataSnapshot.exists()) {
                                                                                    String previousScore = dataSnapshot.getValue(String.class);

                                                                                    Log.i("123321", previousScore + " previous");
                                                                                    int i = Integer.parseInt(previousScore);
                                                                                    int finalScore001 = Integer.parseInt(previousScore) + 1;
                                                                                    String fs=finalScore001+"";
                                                                                    databaseReference001.setValue(fs);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }

                                                                        });
                    sendEmail();
              //      startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else {
                    circleLoadingView.stopOk();
                    Log.i("123321","fail reffer "+number);
                    sendEmail();
                   // startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    protected void sendEmail() {

circleLoadingView.stopOk();
        Log.i("Send email", "");
        String[] TO = {"mdgolamzakaria1@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request Confirm AnabeyaSpainwinCash account");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please Confirm My account. Here is my real information.This is my email : "+etEmail.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SignupActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void firedata(String child,String value){
      DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user").child(etMobile.getText().toString());
databaseReference.child(child).setValue(value);
    }
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }}
    
