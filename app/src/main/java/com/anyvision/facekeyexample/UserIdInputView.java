package com.anyvision.facekeyexample;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anyvision.sesame.utils.AnvSurfaceType;
import com.anyvision.sesame.utils.GradientHelper;

public class UserIdInputView extends FrameLayout {
    public static final String EMPTY_STRING = "";
    public static final String INPUT_CAN_T_BE_EMPTY = "Input Can't Be Empty";
    //    private IUserIdInputListner listner;
    private EditText userIdInput;
    private TextView userIdInputTitle;

    public UserIdInputView(@NonNull Context context) {
        super(context);
        init();
    }

    public UserIdInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserIdInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.user_id_input_dialog, this);

        getRootView().setBackground(GradientHelper.getBackgroundGradient(getContext(), AnvSurfaceType.DarkSurface));
        final View looksGoodBtn = findViewById(R.id.looksGoodBtn);
        View userIdBackBtn = findViewById(R.id.userIdBackBtn);
        userIdBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                listner.backPressUserIdAlert();
            }
        });
        userIdInput = findViewById(R.id.userIdInput);
        looksGoodBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userIdInput.getText())) {
                    userIdInput.setError(INPUT_CAN_T_BE_EMPTY);
                    return;
                }
//                listner.looksGoodClicked(userIdInput.getText().toString());
            }
        });
        userIdInputTitle = findViewById(R.id.userIdInputTitle);
    }
}
//        disableKeyboardForUserInput();
//        keyboardView = findViewById(R.id.keyboardView);
//        keyboardView.setListner(new CustomKeyboardView.ICustomKeyboardListner() {
//            @Override
//            public void keyEvent(int keyCode) {
//                if(keyCode == KeyEvent.KEYCODE_A) {
//                    looksGoodBtn.performClick();
//                    return;
//                }
//                userIdInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
//            }
//        });
//    }

//    private void disableKeyboardForUserInput() {
//        userIdInput.setOnTouchListener(new OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int inType = userIdInput.getInputType();
//                userIdInput.setInputType(InputType.TYPE_NULL);
//                userIdInput.onTouchEvent(event);
//                userIdInput.setInputType(inType);
//                return true;
//            }
//        });
//    }


//    public void setListner(IUserIdInputListner listner) {
//        this.listner = listner;
//    }
//
//
//    public void setViewTitle(String title){
//        userIdInputTitle.setText(title);
//    }
//
//    public interface IUserIdInputListner{
//        void looksGoodClicked(String userId);
//        void backPressUserIdAlert();
//    }
//}
