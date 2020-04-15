package com.flagtag.wrinkle.fragement;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.SmartEditText;

import java.util.ArrayList;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.graphics.Typeface.NORMAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    SmartEditText text;
    TextView textView;
    Button button;
    Button newSpanButton;
    Button flagButton;
    Editable editable;
    boolean flag = false;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_notification, container, false);

        //button 가져오기
        button = (Button) rootView.findViewById(R.id.aButton);
        newSpanButton = (Button) rootView.findViewById(R.id.newSpanButton);
        flagButton = (Button)rootView.findViewById(R.id.flagButton) ;
        textView = (TextView)rootView.findViewById(R.id.textView);

        flagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

        //start와 end 위치를 띄워주는 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start;
                int end;
                String string = "";

                StyleSpan[] styleSpans = editable.getSpans(0, editable.length(),StyleSpan.class);


                //Toast.makeText(getContext(), styleSpans.length, Toast.LENGTH_SHORT).show();
                for(StyleSpan span: styleSpans){
                    start= editable.getSpanStart(span);
                    end = editable.getSpanEnd(span);
                    string = string.concat("\nstart : "+Integer.toString(start)+", end : " + Integer.toString(end));

                }
                textView.setText("\n"+string);
            }
        });

        newSpanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editable.append("\n잇잇잇잇잇");


            }
        });

        //editText 가져오기
        text = (SmartEditText) rootView.findViewById(R.id.editText);
        //text 글자 주기
        text.setText("가나다라마바사아자차카타파하");
        editable = text.getEditableText();


        //가나다라를 굵게 start: 0 , end : 4
        editable.setSpan(new StyleSpan(BOLD),0, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        editable.setSpan(new StyleSpan(NORMAL),5, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //자차카타를 빨간색으로 start : 8, end : 12
        editable.setSpan(new StyleSpan(BOLD),8, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);




        text.addTextChangedListener(new TextWatcher() {

            int lastCursor, curCursor;
            StyleSpan inclusive;
            StyleSpan[] styleSpans ;
            ArrayList<SpanInfo> spanInfoArr = new ArrayList<>();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastCursor = text.getSelectionStart();

                styleSpans = editable.getSpans(0, editable.length(), StyleSpan.class);
                spanInfoArr.clear();
                for(StyleSpan span : styleSpans){
                    int spanStart = editable.getSpanStart(span);
                    int spanEnd = editable.getSpanEnd(span);
                    int style = span.getStyle();
                    if(editable.getSpanFlags(span) != Spanned.SPAN_EXCLUSIVE_EXCLUSIVE){
                        inclusive = span;
                    }
                    spanInfoArr.add(new SpanInfo(style,spanStart, spanEnd));
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isCusorInSpan = false;
                curCursor = text.getSelectionStart();

                if(inclusive != null){
                    editable.removeSpan(inclusive);
                    inclusive = null;
                }


                StyleSpan[] styleSpans = editable.getSpans(0, editable.length(),StyleSpan.class);
                for(SpanInfo spanInfo: spanInfoArr){
                    if(spanInfo.getStart()<=lastCursor &&spanInfo.getEnd()>=lastCursor){
                        isCusorInSpan = true;
                    }
                    //if(spanInfo.getStart()<=cur)
                    editable.setSpan(new StyleSpan(spanInfo.getStyle()), spanInfo.getStart(), spanInfo.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if(curCursor == lastCursor+1 && isCusorInSpan==false){
                    editable.setSpan(new StyleSpan(BOLD), lastCursor, curCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }


                startToast("lastCursor:" + Integer.toString(lastCursor) + "curCursor" +Integer.toString(curCursor));

            }
        });



        return rootView;


    }

    class SpanInfo{
        int style;
        int start;
        int end;

        public SpanInfo(int style, int start, int end) {
            this.style = style;
            this.start = start;
            this.end = end;
        }

        public int getStyle() {
            return style;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }



    //프래그먼트가 액티비티에 올라올 때 호출되는 함수
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    //프래그먼트가 액티비티에서 사라질 때 호출되는 함수
    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void startToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

    }

}
