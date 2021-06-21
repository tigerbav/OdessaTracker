package com.smirnova.odesatracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smirnova.odesatracker.events.ICallBack;
import com.smirnova.odesatracker.events.MainActivity;
import com.smirnova.odesatracker.events.NameEventScreen;
import com.smirnova.odesatracker.events.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataBase {
    static DataBase dataBaseFirebase;
    private static GoogleSignInOptions gso;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ICallBack callBack;

    public static List<Object> generalView;
    public static List<Object> multiView;
    public static List<Object> savedView;
    public static List<Object> filterView;
    public static List<EventInfo> eventInfoList;
    public static List<String> interestList;

    private DataBase() {
        generalView = new ArrayList<>();
        multiView = new ArrayList<>();
        savedView = new ArrayList<>();
        filterView = new ArrayList<>();
        eventInfoList = new ArrayList<>();
        interestList = new ArrayList<>();
        requestToFirebase();
        requestForCategory();
    }

    public void createUser() {
        if(mUser.getEmail() != null)
            db.collection(Constants.USER)
            .document(mUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Map<String, Object> temp = task.getResult().getData();
                            UserInfo.setMail((String) temp.get(Constants.MAIL));
                            UserInfo.setName((String) temp.get(Constants.NAME));
                        }
                    });
    }

    static public DataBase createOrReturn() {
        if (dataBaseFirebase == null)
            dataBaseFirebase = new DataBase();
        return dataBaseFirebase;
    }

    public void registration(String fullName, String gmail, String password, Context context) {
        mAuth.createUserWithEmailAndPassword(gmail, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //взятие данных зарегестрированного пользователя из Firebase в текущее приложение
                        mUser = mAuth.getCurrentUser();

                        Map<String, String> generalTask = new HashMap<>();
                        generalTask.put(Constants.MAIL, gmail);
                        generalTask.put(Constants.NAME, fullName);

                        if (mUser.getEmail() != null) {
                            db.collection(Constants.USER)
                                    .document(gmail)
                                    .set(generalTask);
                        }


                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, Constants.REPEAT_LOGIN, Toast.LENGTH_LONG).show();
                        Log.w("Error: ", task.getException());
                    }

                });


    }

    public void signIn(String gmail, String password, Context context) {
        mAuth.signInWithEmailAndPassword(gmail, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, Constants.ERROR_PASSWORD_MAIL, Toast.LENGTH_LONG).show();
                        Log.w("Error: ", task.getException());
                    }

                });
    }

    public static void setGSO() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(String.valueOf(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    public void googleSignIn(Activity activity) {
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    public void firebaseAuthWithGoogle(Activity activity, String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = mAuth.getCurrentUser();
                            Intent intent = new Intent(activity, MainActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                        } else {
                            Log.w("Error:", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask, Activity activity) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            dataBaseFirebase.firebaseAuthWithGoogle(activity, account.getIdToken());
        } catch (Exception e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error: ", "signInResult:failed code=" + e);
        }
    }

    private void requestToFirebase() {
        db.collection(Constants.EVENT)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Map<String, Object> temp = document.getData();
                                EventInfo eventInfo = new EventInfo((String) temp.get(Constants.CATEGORY),
                                        (String) temp.get(Constants.COST),
                                        (String) temp.get(Constants.DATE),
                                        (String) temp.get(Constants.DESCRIPTION),
                                        (Boolean) temp.get(Constants.LIKE),
                                        (String) temp.get(Constants.LINK),
                                        (String) temp.get(Constants.LOCATION),
                                        (String) temp.get(Constants.RAITING),
                                        (String) temp.get(Constants.TYPE),
                                        (String) temp.get(Constants.TIME),
                                        document.getId());

                                eventInfoList.add(eventInfo);

                            }
                        } else {
                            Log.d("Error read", "Error getting documents: ", task.getException());
                        }
                        createMultipleList();
                        setSavedList();
                        if (callBack != null) {
                            callBack.setList();
                        }
                    }
                });
    }

    public void requestForCategory() {
        db.collection(Constants.INTERESTS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Map<String, Object> temp = document.getData();
                                final Object s = temp.get(Constants.INTERESTS);
                                interestList.addAll(Arrays.asList(s.toString().replace("]", "").replace("[", "").split(",")));

                            }
                        } else {
                            Log.d("Error read", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void createFilterList(String category) {
        boolean flag = true;
        for (EventInfo eventInfo : eventInfoList) {
            if(eventInfo.getCategory() == null) {
                continue;
            }
            if (category.toLowerCase().contains(eventInfo.getCategory().toLowerCase())) {
                if (flag) {
                    flag = false;
                    filterView.add(new NameEventScreen(category));
                }
                filterView.add(eventInfo);
            }
        }

    }

    private void createListVisible(String val) {
        multiView.add(new NameEventScreen(val));
        for (EventInfo event : eventInfoList) {
            if (event.getType().equals(val)) {
                multiView.add(event);
            }
        }
    }

    private void createMultipleList() {
        createListVisible(Constants.CURRENT_POPULAR_EVENTS);
        createListVisible(Constants.POPULAR_PLACES_LAST);
    }

    private void setSavedList() {
        savedView.add(new NameEventScreen(Constants.SAVED));
        for (EventInfo event : eventInfoList) {
            if (event.isLike()) {
                savedView.add(event);
            }
        }


    }

    public void setCallBack(ICallBack iCallBack) {
        callBack = iCallBack;
    }

    public void setSavedListToDB() {
        Map<String, Object> generalTask = new HashMap<>();
        generalTask.put(Constants.MAIL, UserInfo.getMail());
        generalTask.put(Constants.NAME, UserInfo.getName());
        String[] list = new String[savedView.size() - 1];
        for(int i = 1; i < savedView.size(); i++) {
            EventInfo eventInfo = (EventInfo) savedView.get(i);
            list[i-1] = eventInfo.getEvent();
        }
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < list.length; i++) {
            if(i + 1 == list.length) {
                str.append(list[i]);
            } else {
                str.append(list[i]).append(",");
            }

        }
        generalTask.put(Constants.SAVED, str.toString());

        if (mUser.getEmail() != null) {
            db.collection(Constants.USER)
                    .document(UserInfo.getMail())
                    .set(generalTask);
        }
    }
}
