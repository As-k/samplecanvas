package paintapp.example.com.sampleapp;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

public class ActivityPaintBook extends AppCompatActivity {

    //private static final int CAMERA_REQUEST = 1888;
    //private static final int MY_CAMERA_PERMISSION_CODE = 100;
    //private static int REQUEST_CODE_GALLERY = 1;
    //private static int REQUEST_CODE_CAMERA = 2;
    protected Dialog dialog;
    protected View layout;
    protected String TAG = ActivityPaintBook.class.getSimpleName();
    protected int progress;
    protected float stroke = 6.0f;
    ImageView imgView;
    String Result = "";
    ImageView img_pencil;
  //ImageView imgarc;
    ImageView imgcircle;
    ImageView imgclear;
    ImageView imgcolordialog;
    ImageView imgcolordroper;
    ImageView imgeraser;
  //ImageView imgfillarc;
  //ImageView imgfillcircle;
  //ImageView imgfilloval;
  //ImageView imgfillrect;
  //ImageView imgfillroundrect;
  //ImageView imgline;
  //ImageView imgoval;
    ImageView imgrectangular;
    ImageView imgroundrect;
    //ImageView imgsave;
    //ImageView imgshare;
    ImageView imgstroke;
    ImageView imggallery;
    ImageView imgtext;
    RelativeLayout rel_draw;
    ImageView imgtriangle;
    StringBuilder sb;
    StringBuilder sb_share;
    //TextView txt_arc;
    //TextView txt_arc_fill;
    TextView txt_circle;
    //TextView txt_circle_fill;
    TextView txt_clear;
    TextView txt_color;
    TextView txt_eraser;
    //TextView txt_fill_color;
    //TextView txt_line;
    //TextView txt_oval;
    //TextView txt_oval_fill;
    TextView txt_pencil;
    TextView txt_rectangle;
    //TextView txt_rectangle_fill;
    //TextView txt_save;
    //TextView txt_share;
    TextView txt_square;
    //TextView txt_square_fill;
    TextView txt_stroke;
    TextView txt_gallery;
    TextView txt_triangle;
    TextView txt_text;
    String strImagePath = "no image selected";
    boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    private int ColorAh = -16777216;
    private String fileName;
    private PaintView paintView;
    private float width;
    private float height;
    static Bitmap bmpEditText;
    static EditText etBitmap;
    static ImageView imageViewBitmap;


    protected void onCreate(Bundle savedInstanceState) {

        this.fileName = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintbook);

        if (VERSION.SDK_INT >= 21) {
            getWindow().addFlags(Integer.MIN_VALUE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().toString()).append(File.separator).append(getString(R.string.app_name));
        sb_share = new StringBuilder();
        sb_share.append(Environment.getExternalStorageDirectory().toString()).append(File.separator).append("SharePaint");

        txt_pencil = findViewById(R.id.text_pencil);
        txt_eraser = findViewById(R.id.text_erase);
        txt_text = findViewById(R.id.text_text);
        txt_color = findViewById(R.id.text_color);
        //txt_fill_color = findViewById(R.id.text_colordrop);
        txt_stroke = findViewById(R.id.text_stroke);
        txt_clear = findViewById(R.id.text_clear);
        txt_circle = findViewById(R.id.text_circle);
        txt_square = findViewById(R.id.text_rectangular);
        txt_rectangle = findViewById(R.id.text_roundrectangular);
//        txt_oval = findViewById(R.id.text_oval);
//        txt_arc = findViewById(R.id.text_arc);
//        txt_line = findViewById(R.id.text_line);
//        txt_circle_fill = findViewById(R.id.text_circlefill);
//        txt_square_fill = findViewById(R.id.text_rectangglefill);
//        txt_rectangle_fill = findViewById(R.id.text_roundrectangglefill);
//        txt_oval_fill = findViewById(R.id.text_ovalfill);
//        txt_arc_fill = findViewById(R.id.text_arcfill);
//        txt_save = findViewById(R.id.text_save);
//        txt_share = findViewById(R.id.text_share);
        txt_gallery = findViewById(R.id.text_gallery);
        txt_triangle = findViewById(R.id.text_triangle);

        txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_checked));
        txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
        txt_text.setTextColor(getResources().getColor(R.color.textdrawing_default));
        txt_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
        //txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
        txt_stroke.setTextColor(getResources().getColor(R.color.textdrawing_default));
        txt_clear.setTextColor(getResources().getColor(R.color.textdrawing_default));
        txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
        txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
        txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_save.setTextColor(getResources().getColor(R.color.textdrawing_default));
//        txt_share.setTextColor(getResources().getColor(R.color.textdrawing_default));
        txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_default));
        txt_triangle.setTextColor(getResources().getColor(R.color.textdrawing_default));

        paintView = new PaintView(this);
        rel_draw = findViewById(R.id.relativedraw);
        rel_draw.addView(paintView);

        paintView.togglePencil(true);
        //paintView.toggleFill(false);
        paintView.toggleCircle(false);
        paintView.toggleRectangular(false);
        paintView.toggleRoundRectangular(false);
//        paintView.toggleoval(false);
//        paintView.togglearc(false);
//        paintView.toggleline(false);
        paintView.togglepaintstyle(true);
        paintView.toggletext(false);
        paintView.togglegallery(false);
        paintView.toggletriangle(false);
        paintView.toggleprinttext(false);

        img_pencil = findViewById(R.id.image_pencil);
        imgeraser = findViewById(R.id.image_eraser);
        imgtext = findViewById(R.id.image_text);
        imgcolordialog = findViewById(R.id.image_colorpicker);
//        imgsave = findViewById(R.id.image_save);
//        imgshare = findViewById(R.id.image_share);
        imgclear = findViewById(R.id.image_clear);
        imgstroke = findViewById(R.id.image_stroke);
        imgcolordroper = findViewById(R.id.image_colordroper);
        imgcircle = findViewById(R.id.image_circle);
        imgrectangular = findViewById(R.id.image_rectangular);
        imgroundrect = findViewById(R.id.image_roundrect);
//        imgoval = findViewById(R.id.image_oval);
//        imgarc = findViewById(R.id.image_arc);
//        imgline = findViewById(R.id.image_line);
//        imgfillcircle = findViewById(R.id.image_circlefill);
//        imgfillrect = findViewById(R.id.image_rectfill);
//        imgfillroundrect = findViewById(R.id.image_roundrectfill);
//        imgfilloval = findViewById(R.id.image_ovalfill);
//        imgfillarc = findViewById(R.id.image_arcfill);
        imggallery = findViewById(R.id.image_gallery);
        imgtriangle = findViewById(R.id.image_triangle);

        img_pencil.setImageResource(R.drawable.pencil_hover_icon);

        img_pencil.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_checked));
                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
               // txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_triangle.setTextColor(getResources().getColor(R.color.textdrawing_default));

                paintView.togglePencil(true);
               // paintView.toggleFill(false);
                paintView.toggleCircle(false);
                paintView.toggleRectangular(false);
                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
                paintView.togglepaintstyle(true);
                paintView.toggletext(false);
                paintView.togglegallery(false);
                paintView.toggletriangle(false);
                paintView.toggleprinttext(false);

                img_pencil.setImageResource(R.drawable.pencil_hover_icon);
                imgeraser.setImageResource(R.drawable.eraser_icon);
                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
                imgcircle.setImageResource(R.drawable.circle_icon);
                imgrectangular.setImageResource(R.drawable.rect_icon);
                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
                imggallery.setImageResource(R.drawable.gallery);
                imgtriangle.setImageResource(R.drawable.triangle_icon);
            }
        });
        imgcolordroper.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
                //txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_checked));
                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_triangle.setTextColor(getResources().getColor(R.color.textdrawing_default));

                //paintView.toggleFill(true);

                img_pencil.setImageResource(R.drawable.pencil_icon);
                imgeraser.setImageResource(R.drawable.eraser_icon);
                imgcolordroper.setImageResource(R.drawable.color_droper_hover_icon);
                imgcircle.setImageResource(R.drawable.circle_icon);
                imgrectangular.setImageResource(R.drawable.rect_icon);
                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
                imggallery.setImageResource(R.drawable.gallery);
                imgtriangle.setImageResource(R.drawable.triangle_icon);
            }
        });

        imgeraser.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_checked));
                //txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_triangle.setTextColor(getResources().getColor(R.color.textdrawing_default));

                paintView.togglePencil(false);
               // paintView.toggleFill(false);
                paintView.toggleCircle(false);
                paintView.toggleRectangular(false);
                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
                paintView.togglepaintstyle(true);
                paintView.toggletext(false);
                paintView.togglegallery(false);
                paintView.toggletriangle(false);
                paintView.toggleprinttext(false);

                img_pencil.setImageResource(R.drawable.pencil_icon);
                imgeraser.setImageResource(R.drawable.eraser_hover_icon);
                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
                imgcircle.setImageResource(R.drawable.circle_icon);
                imgrectangular.setImageResource(R.drawable.rect_icon);
                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
                imggallery.setImageResource(R.drawable.gallery);
                imgtriangle.setImageResource(R.drawable.triangle_icon);
            }
        });
        imgtext.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_text.setTextColor(getResources().getColor(R.color.textdrawing_checked));
                //txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_triangle.setTextColor(getResources().getColor(R.color.textdrawing_default));

                paintView.togglePencil(true);
                paintView.toggletext(true);
                //paintView.toggleFill(false);
                paintView.toggleCircle(false);
                paintView.toggleRectangular(false);
                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
                paintView.togglepaintstyle(true);
                paintView.togglegallery(false);
                paintView.toggletriangle(false);
                paintView.toggleprinttext(true);

                img_pencil.setImageResource(R.drawable.pencil_hover_icon);
                imgeraser.setImageResource(R.drawable.eraser_icon);
                imgtext.setImageResource(R.drawable.text);
                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
                imgcircle.setImageResource(R.drawable.circle_icon);
                imgrectangular.setImageResource(R.drawable.rect_icon);
                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
                imggallery.setImageResource(R.drawable.gallery);
                imgtriangle.setImageResource(R.drawable.triangle_icon);
                showTextBox(v);
            }
        });

        imgcircle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_text.setTextColor(getResources().getColor(R.color.textdrawing_default));
                //txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_checked));
                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_triangle.setTextColor(getResources().getColor(R.color.textdrawing_default));

                paintView.togglePencil(true);
                //paintView.toggleFill(false);
                paintView.toggletext(false);
                paintView.toggleCircle(true);
                paintView.toggleRectangular(false);
                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
                paintView.togglepaintstyle(true);
                paintView.togglegallery(false);
                paintView.toggletriangle(false);
                paintView.toggleprinttext(false);

                img_pencil.setImageResource(R.drawable.pencil_icon);
                imgeraser.setImageResource(R.drawable.eraser_icon);
                imgtext.setImageResource(R.drawable.text);
                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
                imgcircle.setImageResource(R.drawable.circle_hover_icon);
                imgrectangular.setImageResource(R.drawable.rect_icon);
                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
                imggallery.setImageResource(R.drawable.gallery);
                imgtriangle.setImageResource(R.drawable.triangle_icon);
            }
        });

        imgrectangular.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_text.setTextColor(getResources().getColor(R.color.textdrawing_default));
               // txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_checked));
                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_triangle.setTextColor(getResources().getColor(R.color.textdrawing_default));

                paintView.togglePencil(true);
                //paintView.toggleFill(false);
                paintView.toggletext(false);
                paintView.toggleCircle(false);
                paintView.toggleRectangular(true);
                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
                paintView.togglepaintstyle(true);
                paintView.togglegallery(false);
                paintView.toggletriangle(false);
                paintView.toggleprinttext(false);

                img_pencil.setImageResource(R.drawable.pencil_icon);
                imgeraser.setImageResource(R.drawable.eraser_icon);
                imgtext.setImageResource(R.drawable.text);
                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
                imgcircle.setImageResource(R.drawable.circle_icon);
                imgrectangular.setImageResource(R.drawable.rect_hover_icon);
                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
                imggallery.setImageResource(R.drawable.gallery);
                imgtriangle.setImageResource(R.drawable.triangle_icon);
            }
        });

        imgroundrect.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_text.setTextColor(getResources().getColor(R.color.textdrawing_default));
               // txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_checked));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_triangle.setTextColor(getResources().getColor(R.color.textdrawing_default));

                paintView.togglePencil(true);
              //paintView.toggleFill(false);
                paintView.toggletext(false);
                paintView.toggleCircle(false);
                paintView.toggleRectangular(false);
                paintView.toggleRoundRectangular(true);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
                paintView.togglepaintstyle(true);
                paintView.togglegallery(false);
                paintView.toggletriangle(false);
                paintView.toggleprinttext(false);

                img_pencil.setImageResource(R.drawable.pencil_icon);
                imgeraser.setImageResource(R.drawable.eraser_icon);
                imgtext.setImageResource(R.drawable.text);
                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
                imgcircle.setImageResource(R.drawable.circle_icon);
                imgrectangular.setImageResource(R.drawable.rect_icon);
                imgroundrect.setImageResource(R.drawable.roundrect_hover_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
                imggallery.setImageResource(R.drawable.gallery);
                imgtriangle.setImageResource(R.drawable.triangle_icon);

            }
        });


//        imgoval.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_checked));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//
//                paintView.togglePencil(true);
//                paintView.toggleFill(false);
//                paintView.toggleCircle(false);
//                paintView.toggleRectangular(false);
//                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(true);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
//                paintView.togglepaintstyle(true);
//                paintView.toggletext(false);
//
//                img_pencil.setImageResource(R.drawable.pencil_icon);
//                imgeraser.setImageResource(R.drawable.eraser_icon);
//                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
//                imgcircle.setImageResource(R.drawable.circle_icon);
//                imgrectangular.setImageResource(R.drawable.rect_icon);
//                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_hover_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
//            }
//        });
//
//        imgarc.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_checked));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//
//                paintView.togglePencil(true);
//                paintView.toggleFill(false);
//                paintView.toggleCircle(false);
//                paintView.toggleRectangular(false);
//                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(true);
//                paintView.toggleline(false);
//                paintView.togglepaintstyle(true);
//                paintView.toggletext(false);
//
//                img_pencil.setImageResource(R.drawable.pencil_icon);
//                imgeraser.setImageResource(R.drawable.eraser_icon);
//                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
//                imgcircle.setImageResource(R.drawable.circle_icon);
//                imgrectangular.setImageResource(R.drawable.rect_icon);
//                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_hover_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
//            }
//        });
//
//        imgline.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_checked));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//
//                paintView.togglePencil(true);
//                paintView.toggleFill(false);
//                paintView.toggleCircle(false);
//                paintView.toggleRectangular(false);
//                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(true);
//                paintView.togglepaintstyle(true);
//                paintView.toggletext(false);
//
//                img_pencil.setImageResource(R.drawable.pencil_icon);
//                imgeraser.setImageResource(R.drawable.eraser_icon);
//                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
//                imgcircle.setImageResource(R.drawable.circle_icon);
//                imgrectangular.setImageResource(R.drawable.rect_icon);
//                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_hover_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
//            }
//        });
//
//        imgfillcircle.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_checked));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//
//                paintView.togglePencil(true);
//                paintView.toggleFill(false);
//                paintView.toggleCircle(true);
//                paintView.toggleRectangular(false);
//                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
//                paintView.togglepaintstyle(false);
//                paintView.toggletext(false);
//
//                img_pencil.setImageResource(R.drawable.pencil_icon);
//                imgeraser.setImageResource(R.drawable.eraser_icon);
//                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
//                imgcircle.setImageResource(R.drawable.circle_icon);
//                imgrectangular.setImageResource(R.drawable.rect_icon);
//                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_hover_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
//            }
//        });
//
//        imgfillrect.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_checked));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//
//                paintView.togglePencil(true);
//                paintView.toggleFill(false);
//                paintView.toggleCircle(false);
//                paintView.toggleRectangular(true);
//                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
//                paintView.togglepaintstyle(false);
//                paintView.toggletext(false);
//
//                img_pencil.setImageResource(R.drawable.pencil_icon);
//                imgeraser.setImageResource(R.drawable.eraser_icon);
//                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
//                imgcircle.setImageResource(R.drawable.circle_icon);
//                imgrectangular.setImageResource(R.drawable.rect_icon);
//                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_hover_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
//            }
//        });
//
//        imgfillroundrect.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_checked));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//
//                paintView.togglePencil(true);
//                paintView.toggleFill(false);
//                paintView.toggleCircle(false);
//                paintView.toggleRectangular(false);
//                paintView.toggleRoundRectangular(true);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
//                paintView.togglepaintstyle(false);
//                paintView.toggletext(false);
//
//                img_pencil.setImageResource(R.drawable.pencil_icon);
//                imgeraser.setImageResource(R.drawable.eraser_icon);
//                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
//                imgcircle.setImageResource(R.drawable.circle_icon);
//                imgrectangular.setImageResource(R.drawable.rect_icon);
//                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill__hover_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
//            }
//        });
//
//        imgfilloval.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_checked));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//
//                paintView.togglePencil(true);
//                paintView.toggleFill(false);
//                paintView.toggleCircle(false);
//                paintView.toggleRectangular(false);
//                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(true);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
//                paintView.togglepaintstyle(false);
//                paintView.toggletext(false);
//
//                img_pencil.setImageResource(R.drawable.pencil_icon);
//                imgeraser.setImageResource(R.drawable.eraser_icon);
//                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
//                imgcircle.setImageResource(R.drawable.circle_icon);
//                imgrectangular.setImageResource(R.drawable.rect_icon);
//                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_hover_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
//            }
//        });
//
//        imgfillarc.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_checked));
//
//                paintView.togglePencil(true);
//                paintView.toggleFill(false);
//                paintView.toggleCircle(false);
//                paintView.toggleRectangular(false);
//                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(true);
//                paintView.toggleline(false);
//                paintView.togglepaintstyle(false);
//                paintView.toggletext(false);
//
//                img_pencil.setImageResource(R.drawable.pencil_icon);
//                imgeraser.setImageResource(R.drawable.eraser_icon);
//                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
//                imgcircle.setImageResource(R.drawable.circle_icon);
//                imgrectangular.setImageResource(R.drawable.rect_icon);
//                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arc_fill_hover_icon);
//            }
//        });

        imgstroke.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                strokeDialog();
            }
        });
        imgclear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Builder alert = new Builder(ActivityPaintBook.this);
                alert.setIcon(R.drawable.clear_hover_icon);
                alert.setTitle("Clear All");
                alert.setMessage("Are You Sure You Want To Clear All?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        paintView.clear();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
            }
        });

        imgcolordialog.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showColorPickerDialogDemo();
            }
        });

//        imgsave.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                save();
//            }
//        });

//        imgshare.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                File imageFileToShare = new File(shareImage());
//                Intent share = new Intent("android.intent.action.SEND");
//                share.setType("image/png");
//                share.putExtra("android.intent.extra.STREAM", Uri.fromFile(imageFileToShare));
//                startActivity(Intent.createChooser(share, "Share Image"));
//            }
//        });

        imgtriangle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_text.setTextColor(getResources().getColor(R.color.textdrawing_default));
                //txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_triangle.setTextColor(getResources().getColor(R.color.textdrawing_checked));

                paintView.togglePencil(true);
              //paintView.toggleFill(false);
                paintView.toggletext(false);
                paintView.toggleCircle(false);
                paintView.toggleRectangular(false);
                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
                paintView.togglepaintstyle(true);
                paintView.togglegallery(false);
                paintView.toggletriangle(true);
                paintView.toggleprinttext(false);

                img_pencil.setImageResource(R.drawable.pencil_icon);
                imgeraser.setImageResource(R.drawable.eraser_icon);
                imgtext.setImageResource(R.drawable.text);
                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
                imgcircle.setImageResource(R.drawable.circle_icon);
                imgrectangular.setImageResource(R.drawable.rect_icon);
                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
                imggallery.setImageResource(R.drawable.gallery);
                imgtriangle.setImageResource(R.drawable.triangle_hover);
            }
        });

        imggallery.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                txt_pencil.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_eraser.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_text.setTextColor(getResources().getColor(R.color.textdrawing_default));
               // txt_fill_color.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_circle.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_square.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_rectangle.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_line.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_circle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_square_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_rectangle_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_oval_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
//                txt_arc_fill.setTextColor(getResources().getColor(R.color.textdrawing_default));
                txt_gallery.setTextColor(getResources().getColor(R.color.textdrawing_checked));

                paintView.togglePencil(true);
                //paintView.toggleFill(false);
                paintView.toggletext(false);
                paintView.toggleCircle(false);
                paintView.toggleRectangular(false);
                paintView.toggleRoundRectangular(false);
//                paintView.toggleoval(false);
//                paintView.togglearc(false);
//                paintView.toggleline(false);
                paintView.togglepaintstyle(true);
                paintView.togglegallery(true);
                paintView.toggletriangle( false);
                paintView.toggleprinttext(false);

                img_pencil.setImageResource(R.drawable.pencil_icon);
                imgeraser.setImageResource(R.drawable.eraser_icon);
                imgtext.setImageResource(R.drawable.text);
                imgcolordroper.setImageResource(R.drawable.color_droper_icon);
                imgcircle.setImageResource(R.drawable.circle_icon);
                imgrectangular.setImageResource(R.drawable.rect_icon);
                imgroundrect.setImageResource(R.drawable.roundrect_icon);
//                imgoval.setImageResource(R.drawable.ellipse_icon);
//                imgarc.setImageResource(R.drawable.arc_icon);
//                imgline.setImageResource(R.drawable.line_icon);
//                imgfillcircle.setImageResource(R.drawable.circle_fill_icon);
//                imgfillrect.setImageResource(R.drawable.rectfill_icon);
//                imgfillroundrect.setImageResource(R.drawable.roundrect__fill_icon);
//                imgfilloval.setImageResource(R.drawable.ellipse_fill_icon);
//                imgfillarc.setImageResource(R.drawable.arcfill_icon);
                imgtriangle.setImageResource(R.drawable.triangle_icon);
                imggallery.setImageResource(R.drawable.gallery_hover);
                loadImagefromGallery(v);
                //loadImagefromCamera(v);
            }
        });
    }
    public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");
            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Config type = imgIn.getConfig();

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(10, 10, type);
            map.position(0);
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgIn;
    }

    public void strokeDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.stroke_dialog, (ViewGroup) findViewById(R.id.dialog_root_element));
        SeekBar dialogSeekBar = layout.findViewById(R.id.dialog_seekbar);
        dialogSeekBar.setThumbOffset(convertDipToPixels(9.5f));
        dialogSeekBar.setProgress(((int) this.stroke) * 2);
        setTextView(this.layout, String.valueOf(Math.round(this.stroke)));
        dialogSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                ActivityPaintBook.this.progress = progress / 2;
                ActivityPaintBook.this.setTextView(ActivityPaintBook.this.layout, "" + ActivityPaintBook.this.progress);
                ((Button) ActivityPaintBook.this.layout.findViewById(R.id.dialog_button)).setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ActivityPaintBook.this.stroke = (float) ActivityPaintBook.this.progress;
                        ActivityPaintBook.this.paintView.setStroke(ActivityPaintBook.this.stroke);
                        ActivityPaintBook.this.dialog.dismiss();
                    }
                });
            }
        });
        this.dialog.setContentView(this.layout);
        this.dialog.show();
    }

    protected void setTextView(View layout, String s) {
        ((TextView) layout.findViewById(R.id.stroke_text)).setText(s);
    }

    private int convertDipToPixels(float dip) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) (dip * metrics.density);
    }

    private void showColorPickerDialogDemo() {
        new ColorPickerDialog(this, -1, new ColorPickerDialog.OnColorSelectedListener() {
            public void onColorSelected(int color) {
                ColorAh = color;
                paintView.setColor(ColorAh);
            }
        }).show();
    }

//    public String save() {
//        FileOutputStream fileoutputstream1;
//        String s;
//        this.paintView.setDrawingCacheEnabled(true);
//        this.paintView.invalidate();
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int i = displaymetrics.heightPixels;
//        Bitmap bitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(this.paintView.getDrawingCache()), displaymetrics.widthPixels, i, true);
//        File file = new File(this.sb.toString());
//        if (file.isDirectory()) {
//            file.mkdir();
//            fileoutputstream1 = null;
//            s = "drawingpaintbook" + "_photo_" + System.currentTimeMillis() + ".png";
//        } else {
//            file.mkdir();
//            fileoutputstream1 = null;
//            s = "drawingpaintbook" + "_photo_" + System.currentTimeMillis() + ".png";
//        }
//        try {
//            fileoutputstream1 = new FileOutputStream(new File(file, s));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        FileOutputStream fileoutputstream = fileoutputstream1;
//        bitmap.compress(CompressFormat.PNG, 100, fileoutputstream);
//        StringBuilder stringbuilder1 = new StringBuilder();
//        stringbuilder1.append(this.sb.toString()).append(File.separator).append(s);
//        try {
//            fileoutputstream.flush();
//            fileoutputstream.close();
//        } catch (IOException e2) {
//            e2.printStackTrace();
//        }
//        MediaScannerConnection.scanFile(this, new String[]{"" + stringbuilder1}, null, new OnScanCompletedListener() {
//            public void onScanCompleted(String s1, Uri uri) {
//            }
//        });
//        Toast.makeText(this, "Image Save Sucessfull", Toast.LENGTH_SHORT).show();
//        this.rel_draw.destroyDrawingCache();
//        return "" + stringbuilder1;
//    }

    private void setTitle() {
        if (this.fileName == null) {
            if (this.paintView.pencil.booleanValue()) {
                setTitle("ActivityMagicPaint. - Pencil");
            } else {
                setTitle("ActivityMagicPaint. - Eraser");
            }
        } else if (this.paintView.pencil.booleanValue()) {
            setTitle("ActivityMagicPaint. - Pencil - " + this.fileName);
        } else {
            setTitle("ActivityMagicPaint. - Eraser - " + this.fileName);
        }
    }

//    private String shareImage() {
//        FileOutputStream fileoutputstream1;
//        String s;
//        this.paintView.setDrawingCacheEnabled(true);
//        this.paintView.invalidate();
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int i = displaymetrics.heightPixels;
//        Bitmap bitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(this.paintView.getDrawingCache()), displaymetrics.widthPixels, i, true);
//        File file = new File(this.sb.toString());
//        if (file.isDirectory()) {
//            file.mkdir();
//            fileoutputstream1 = null;
//            s = "sharepaintbook" + "_photo_" + System.currentTimeMillis() + ".png";
//        } else {
//            file.mkdir();
//            fileoutputstream1 = null;
//            s = "sharepaintbook" + "_photo_" + System.currentTimeMillis() + ".png";
//        }
//        try {
//            fileoutputstream1 = new FileOutputStream(new File(file, s));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        FileOutputStream fileoutputstream = fileoutputstream1;
//        bitmap.compress(CompressFormat.PNG, 100, fileoutputstream);
//        StringBuilder stringbuilder1 = new StringBuilder();
//        stringbuilder1.append(this.sb.toString()).append(File.separator).append(s);
//        try {
//            fileoutputstream.flush();
//            fileoutputstream.close();
//        } catch (IOException e2) {
//            e2.printStackTrace();
//        }
//        // new String[1][0] = "" + stringbuilder1;
//        this.rel_draw.destroyDrawingCache();
//        return "" + stringbuilder1;
//    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//    public void loadImagefromCamera(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CODE_CAMERA);
//    }

    public void loadImagefromGallery(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }
    }

    @TargetApi(19)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == 1) {

            boolean isImageFromGoogleDrive = false;
            Uri uri = data.getData();

            if (isKitKat) {

                if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        strImagePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else {
                        Pattern DIR_SEPORATOR = Pattern.compile("/");
                        Set<String> rv = new HashSet<>();
                        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
                        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
                        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
                        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
                            if (TextUtils.isEmpty(rawExternalStorage)) {
                                rv.add("/storage/sdcard0");
                            } else {
                                rv.add(rawExternalStorage);
                            }
                        } else {
                            String rawUserId;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                rawUserId = "";
                            } else {
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                String[] folders = DIR_SEPORATOR.split(path);
                                String lastFolder = folders[folders.length - 1];
                                boolean isDigit = false;
                                try {
                                    Integer.valueOf(lastFolder);
                                    isDigit = true;
                                } catch (NumberFormatException ignored) {
                                }
                                rawUserId = isDigit ? lastFolder : "";
                            }
                            if (TextUtils.isEmpty(rawUserId)) {
                                rv.add(rawEmulatedStorageTarget);
                            } else {
                                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
                            }
                        }
                        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                            String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                            Collections.addAll(rv, rawSecondaryStorages);
                        }
                        String[] temp = rv.toArray(new String[rv.size()]);

                        for (int i = 0; i < temp.length; i++) {
                            File tempf = new File(temp[i] + "/" + split[1]);
                            if (tempf.exists()) {
                                strImagePath = temp[i] + "/" + split[1];
                            }
                        }
                    }
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    String id = DocumentsContract.getDocumentId(uri);
                    Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = {
                            column
                    };

                    try {
                        cursor = getContentResolver().query(contentUri, projection, null, null,
                                null);
                        if (cursor != null && cursor.moveToFirst()) {
                            final int column_index = cursor.getColumnIndexOrThrow(column);
                            strImagePath = cursor.getString(column_index);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String docId = DocumentsContract.getDocumentId(uri);
                    String[] split = docId.split(":");
                    String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};

                    Cursor cursor = null;
                    String column = "_data";
                    String[] projection = {column};

                    try {
                        cursor = getContentResolver().query(contentUri, projection, selection, selectionArgs,
                                null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            strImagePath = cursor.getString(column_index);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                } else if ("com.google.android.apps.docs.storage".equals(uri.getAuthority())) {
                    isImageFromGoogleDrive = true;
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                Cursor cursor = null;
                String column = "_data";
                String[] projection = {column};

                try {
                    cursor = getContentResolver().query(uri, projection, null, null,
                            null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(column);
                        strImagePath = cursor.getString(column_index);
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                strImagePath = uri.getPath();
            }


            if (isImageFromGoogleDrive) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    Display display = getWindowManager().getDefaultDisplay();
                    this.paintView.bgImageGallery = Bitmap.createScaledBitmap(bitmap, display.getWidth(), display.getHeight(), false);
                    //this.paintView.clear();
                    //  this.paintView.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                File f = new File(strImagePath);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
                Display display = getWindowManager().getDefaultDisplay();
                this.paintView.bgImageGallery = Bitmap.createScaledBitmap(bitmap, display.getWidth(), display.getHeight(), false);
                //this.paintView.clear();
                // this.paintView.invalidate();
            }
        }
    }

    public void showTextBox(View view)
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textDialogView = factory.inflate(R.layout.text, null);

        etBitmap =  textDialogView.findViewById(R.id.editText1);
        etBitmap.setHint("Enter the text");

        imageViewBitmap = textDialogView.findViewById(R.id.imageView1);
        // Move this up to onCreate
        Bitmap ab = BitmapFactory.decodeResource(getResources(),(R.drawable.ger)) ;
        bmpEditText = convertToMutable(ab); // Initialize it here with the contents of ab. This effectively clones it and makes it mutable.
        ab = null; // Dispose of ab.

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        //adb.setIcon(R.mipmap.ic_launcher);
        //adb.setTitle("TEXT_BOX");
        adb.setView(textDialogView);
        //adb.setCancelable(false);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(ActivityPaintBook.this, "TEXT", Toast.LENGTH_SHORT).show();
                // set edittext variable value as true
                dialog.dismiss();

                paintView.printtext=true;

            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                paintView.printtext=false;
            }
        });
        adb.create().show();
    }

//    public class FloodFill {
//        public void floodFill(Bitmap image, Point node, int targetColor, int replacementColor) {
//            int width = image.getWidth();
//            int height = image.getHeight();
//            int target = targetColor;
//            int replacement = replacementColor;
//            if (target != replacement) {
//                Queue<Point> queue = new LinkedList();
//                do {
//                    int x = node.x;
//                    int y = node.y;
//                    while (x > 0 && image.getPixel(x - 1, y) == target) {
//                        x--;
//                    }
//                    boolean spanUp = false;
//                    boolean spanDown = false;
//                    while (x < width && image.getPixel(x, y) == target) {
//                        image.setPixel(x, y, replacement);
//                        if (!spanUp && y > 0 && image.getPixel(x, y - 1) == target) {
//                            queue.add(new Point(x, y - 1));
//                            spanUp = true;
//                        } else if (spanUp && y > 0 && image.getPixel(x, y - 1) != target) {
//                            spanUp = false;
//                        }
//                        if (!spanDown && y < height - 1 && image.getPixel(x, y + 1) == target) {
//                            queue.add(new Point(x, y + 1));
//                            spanDown = true;
//                        } else if (spanDown && y < height - 1 && image.getPixel(x, y + 1) != target) {
//                            spanDown = false;
//                        }
//                        x++;
//                    }
//                    node = (Point) queue.poll();
//                } while (node != null);
//            }
//        }
//    }

    private class PaintView extends View {
        private static final float TOUCH_TOLERANCE = 0.8f;
       // protected Boolean arc;
        protected Boolean circle;
        protected Boolean colorfill;
        //protected Boolean line;
        //protected Boolean oval;
        protected Boolean pencil;
        protected Boolean rectangular;
        protected Boolean roundrect;
        protected Boolean triangle;
        protected Boolean setpaintstyle;
        protected Boolean text;
        protected Boolean gallery;
        protected Boolean printtext;
        Bitmap bitimage;
        Point p1;
        ProgressDialog pd;
        ArrayList<Path> paths;
        private Bitmap bgImage;
        private Bitmap bgImageGallery;
        private Bitmap bmp;
        private Paint bmpPaint;
        private Canvas canvas;
        private int colour;
        private Context context;
        private float initX;
        private float initY;
        private float mX;
        private float mY;
        private Paint paint;
        private Path path;
        private float radius;
        private RectF rect;
        private float rectX;
        private float rectX1;
        private float rectY;
        private float rectY1;
        private float triX;
        private float triY;
        private float triX1;
        private float stroke;
        private int widthCanvas;
        private int heightCanvas;

        private PaintView(Context c) {
            super(c);
            pencil = false;
            circle = false;
            rectangular = false;
            roundrect = false;
//            oval = false;
//            arc = false;
//            line = false;
            gallery = false;
            triangle = false;
            setpaintstyle = false;
            printtext=false;
            text = false;
            setDrawingCacheEnabled(true);
            context = c;
            path = new Path();
            this.paths = new ArrayList();
            bmpPaint = new Paint();
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(-16777216);
            paint.setStyle(Style.STROKE);
            paint.setStrokeJoin(Join.ROUND);
            paint.setStrokeCap(Cap.ROUND);
            paint.setStrokeWidth(6.0f);
            pd = new ProgressDialog(context);
            colour = context.getResources().getColor(R.color.black_dark);
            setFocusable(true);
            setFocusableInTouchMode(true);
            stroke = 16.0f;
            p1 = new Point();
            rect = new RectF();
        }

        public void updateCanvas(Canvas canvas, Paint paint1) {
            //canvas.drawColor (Color.WHITE) ;
            //canvas.drawBitmap ( bmpEditText , 600 ,800  , paint) ;
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawText(etBitmap.getText().toString(),500,300,paint);
            // Everything has been drawn to bmp, so we can set that here, now.
            //imageViewBitmap.setImageBitmap(bmpEditText);
        }
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            widthCanvas = w;
            heightCanvas = h;
            bgImage = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            canvas = new Canvas();
            canvas = new Canvas(bgImage);
            Log.i(TAG, "onSizeChanged");
        }

        private void touchStart(float x, float y) {
            if (pencil) {
                path.reset();
                path.moveTo(x, y);
                mX = x;
                mY = y;
                Log.e("ACTION_DOWN", "X: " + x + ", Y: " + y);
            }
            if (circle) {
                initX = x;
                initY = y;
                radius = 1.0f;
                Log.i(TAG, "X: , Y: , radius:");
            }
            if (rectangular) {
                rectX = x;
                rectY = y;
                rectX1 = x + 5.0f;
                rectY1 = y + 5.0f;
                Log.i(TAG, "X: , Y: , X1: , Y1:");
            }
            if (roundrect) {
                rectX = x;
                rectY = y;
                rectX1 = x + 5.0f;
                rectY1 = y + 5.0f;
                Log.i(TAG, "X: , Y: , X1: , Y1:");
            }
//            if (oval) {
//                rectX = x;
//                rectY = y;
//                rectX1 = x + 5.0f;
//                rectY1 = y + 5.0f;
//            }
//            if (arc) {
//                rectX = x;
//                rectY = y;
//                rectX1 = x + 5.0f;
//                rectY1 = y + 5.0f;
//            }
//            if (line) {
//                rectX = x;
//                rectY = y;
//                rectX1 = x + 5.0f;
//                rectY1 = y + 5.0f;
//            }
            if (text) {
                rectX = x;
                rectY = y;
                rectX1 = x + 5.0f;
                rectY1 = y + 5.0f;
            }
//            if (colorfill) {
//                p1.x = (int) x;
//                p1.y = (int) y;
//                new TheTask(bgImage, p1, bgImage.getPixel((int) x, (int) y), paint.getColor()).execute(new Void[0]);
//                invalidate();
//            }
        }

        public void drawTriangle(Canvas canvas, Paint paint, float triX, float triY,float width) {
            Log.i(TAG, "drawTriangle");
            float halfWidth = width / 2;
            Path path = new Path();
            path.moveTo(triX, triY - halfWidth); // Top
            path.lineTo(triX - halfWidth, triY + halfWidth); // Bottom left
            path.lineTo(triX + halfWidth, triY + halfWidth); // Bottom right
            path.lineTo(triX, triY - halfWidth); // Back to Top
            path.close();
            canvas.drawPath(path, paint);
        }

        private void touchUp() {
            if (pencil) {
                path.lineTo(mX, mY);
                canvas.drawPath(path, paint);
                Log.e("ACTION_DOWN", "X: " + mX + ", Y: " + mY);
            }
            if (circle) {
                canvas.drawCircle(initX, initY, radius, paint);
                Log.i(TAG, "X: , Y: , radius:");
            }
            if (rectangular) {
                canvas.drawRect(rectX, rectY, rectX1, rectY1, paint);
            }
            if (roundrect) {
                rect.set(rectX, rectY, rectX1, rectY1);
                canvas.drawRoundRect(rect, 0.0f, 0.0f, paint);
                Log.e("ACTION_DOWN", "left " + rectX + " right " + rectY + " top " + rectX1 + " bottom " + rectY1);
            }
//            if (oval) {
//                rect.set(rectX, rectY, rectX1, rectY1);
//                canvas.drawOval(rect, paint);
//            }
//            if (arc) {
//                rect.set(rectX, rectY, rectX1, rectY1);
//                canvas.drawArc(rect, 90.0f, 270.0f, true, paint);
//            }
//            if (line) {
//                canvas.drawLine(rectX, rectY, rectX1, rectY1, paint);
//            }
            if (text) {
                bmpPaint.setTextSize(stroke);
                bmpPaint.setColor(colour);
                canvas.drawText(Result, rectX, rectY, bmpPaint);
            }
            if (triangle) {
                Log.i(TAG, "if(triangle)");
                width = triX1 - triX;
                drawTriangle(canvas, paint, triX, triY, width);
            }
            this.paths.add(this.path);
            path.reset();
        }

        private void touchMove(float x, float y) {
            radius = (float) Math.sqrt(Math.pow((double) (x - initX), 2.0d) + Math.pow((double) (y - initY), 2.0d));
            rectX1 = (float) Math.sqrt(Math.pow((double) x, 2.0d));
            rectY1 = (float) Math.sqrt(Math.pow((double) y, 2.0d));
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                path.quadTo(mX, mY, (mX + x) / 2.0f, (mY + y) / 2.0f);
                mX = x;
                mY = y;
            }
        }

        public boolean onTouchEvent(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStart(x, y);
                    touchMove(x + TOUCH_TOLERANCE, TOUCH_TOLERANCE + y);
                    if (circle) {
                        pencil = false;
                    }
                    if (rectangular) {
                        pencil = false;
                    }
                    if (roundrect) {
                        pencil = false;
                    }
//                    if (oval) {
//                        pencil = false;
//                    }
//                    if (arc) {
//                        pencil = false;
//                    }
//                    if (line) {
//                        pencil = false;
//                    }
                    if (text) {
                        pencil = false;
                    }
                    if (gallery) {
                        pencil = false;
                    }
                    if (triangle) {
                        Log.i(TAG, "ontouch");
                        pencil = false;
                    }
                    if (printtext) {
                        pencil = false;
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp();
//                    Log.e("ACTION_UP", "X: "+x+", Y: "+y);
                    if (!circle) {
                        pencil = true;
                    }
                    if (!rectangular) {
                        pencil = true;
                    }
                    if (!roundrect) {
                        pencil = true;
                    }
//                    if (!oval) {
//                        pencil = true;
//                    }
//                    if (!arc) {
//                        pencil = true;
//                    }
//                    if (!line) {
//                        pencil = true;
//                    }
                    if (!text) {
                        pencil = true;
                    }
                    if (!gallery) {
                        pencil = true;
                    }
                    if (!triangle) {
                        Log.i(TAG, "motionevent");
                        pencil = true;
                    }
                    if (!printtext) {
                        pencil = true;
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMove(x, y);
//                    Log.e("ACTION_MOVE", "X: "+x+", Y: "+y);
                    invalidate();
                    break;
            }
            return true;
        }

        protected void onDraw(Canvas canvas) {

            Log.i(TAG, "ondraw called");
            canvas.drawColor(-1);

            if (bgImageGallery != null) {
                canvas.drawBitmap(bgImageGallery, 0f, 0f, bmpPaint);
            }

            if(bmpEditText!=null)
            {
                canvas.drawBitmap(bmpEditText, 0.0f, 0.0f, bmpPaint);
            }
                canvas.drawBitmap(bgImage, 0.0f, 0.0f, bmpPaint);

            if (pencil) {
                canvas.drawPath(path, paint);
            }
            if (circle) {
                canvas.drawCircle(initX, initY, radius, paint);

            }
            if (rectangular) {
                canvas.drawRect(rectX, rectY, rectX1, rectY1, paint);
            }
            if (roundrect) {
                rect.set(rectX, rectY, rectX1, rectY1);
                canvas.drawRoundRect(rect, 0.0f, 0.0f, paint);
                width = rect.width();
                height = rect.height();
                //Log.e(TAG, "width: "+width+", height: "+height);
            }
//            if (oval) {
//                rect.set(rectX, rectY, rectX1, rectY1);
//                canvas.drawOval(rect, paint);
//            }
//            if (arc) {
//                rect.set(rectX, rectY, rectX1, rectY1);
//                canvas.drawArc(rect, 90.0f, 270.0f, true, paint);
//            }
//            if (line) {
//                canvas.drawLine(rectX, rectY, rectX1, rectY1, paint);
//            }
            if (text) {
                bmpPaint.setTextSize(stroke);
                bmpPaint.setColor(colour);
                canvas.drawText(Result, rectX, rectY, bmpPaint);
            }
            if (triangle) {
                Log.i(TAG, "ondraw");
                width = triX1 - triX;
                drawTriangle(canvas, paint, triX, triY, width);
            }
            if (printtext) {
                updateCanvas(canvas, paint);
            }
        }

        protected void togglePencil(Boolean b) {
            if (b) {
                paint.setXfermode(null);
                pencil = true;
            } else {
                paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
                pencil = false;
            }
            setTitle();
        }

//        protected void toggleFill(Boolean b) {
//            if (b) {
//                colorfill = true;
//            } else {
//                colorfill = false;
//            }
//        }

        protected void toggleCircle(Boolean b) {
            if (b) {
                circle = true;
            } else {
                circle = false;
            }
        }

        protected void toggleRectangular(Boolean b) {
            if (b) {
                rectangular = true;
            } else {
                rectangular = false;
            }
        }

        protected void toggleRoundRectangular(Boolean b) {
            if (b) {
                roundrect = true;
            } else {
                roundrect = false;
            }
        }

//        protected void toggleoval(Boolean b) {
//            if (b) {
//                oval = true;
//            } else {
//                oval = false;
//            }
//        }
//
//        protected void togglearc(Boolean b) {
//            if (b) {
//                arc = true;
//            } else {
//                arc = false;
//            }
//        }
//
//        protected void toggleline(Boolean b) {
//            if (b) {
//                line = true;
//            } else {
//                line = false;
//            }
//        }

        protected void togglepaintstyle(Boolean b) {
            if (b) {
                paint.setStyle(Style.STROKE);
                setpaintstyle = true;
                return;
            }
            paint.setStyle(Style.FILL);
            setpaintstyle = false;
        }

        protected void toggletext(Boolean b) {
            if (b) {
                text = true;
            } else {
                text = false;
            }
        }
        protected void toggletriangle(Boolean b) {
            if (b) {
                triangle = true;
            } else {
                triangle = false;
            }
        }

        protected void togglegallery(Boolean b) {
            if (b) {
                text = true;
            } else {
                text = false;
            }
        }
        protected void toggleprinttext(Boolean b) {
            if (b) {
                printtext = true;
            } else {
                printtext = false;
            }
        }

        public void setStroke(float s) {
            paint.setStrokeWidth(s);
            stroke = s;
        }

        protected int getColor() {
            return colour;
        }

        public void setColor(int c) {
            paint.setColor(c);
            colour = c;
        }

        protected void clear() {
            path = new Path();
            bgImage = Bitmap.createBitmap(widthCanvas, heightCanvas, Config.ARGB_8888);
            canvas = new Canvas();
            canvas = new Canvas(bgImage);
            canvas.drawColor(-1);

            if (bgImageGallery != null) {
                // canvas.drawBitmap(bgImageGallery, 0.0f, 0.0f, null);
                //canvas.save();
            }
            if (bgImage != null) {
                canvas.drawBitmap(bgImage, 0.0f, 0.0f, null);
                //canvas.save();
            }
            invalidate();
        }

//        class TheTask extends AsyncTask<Void, Integer, Void> {
//            Bitmap bmp;
//            Point pt;
//            int replacementColor;
//            int targetColor;
//
//            public TheTask(Bitmap bm, Point p, int sc, int tc) {
//                bmp = bm;
//                pt = p;
//                replacementColor = tc;
//                targetColor = sc;
//                pd.setMessage("Filling....");
//                pd.show();
//            }
//
//            protected void onPreExecute() {
//                pd.show();
//            }
//
//            protected void onProgressUpdate(Integer... values) {
//            }
//
//            protected Void doInBackground(Void... params) {
//                new FloodFill().floodFill(bmp, pt, targetColor, replacementColor);
//                return null;
//            }
//
//            protected void onPostExecute(Void result) {
//                pd.dismiss();
//                invalidate();
//            }
//        }
    }
}