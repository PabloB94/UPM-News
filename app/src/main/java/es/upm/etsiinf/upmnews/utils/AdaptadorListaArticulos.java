package es.upm.etsiinf.upmnews.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import es.upm.etsiinf.upmnews.DetailsScreen;
import es.upm.etsiinf.upmnews.EditCreateForm;
import es.upm.etsiinf.upmnews.R;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.model.Image;
import es.upm.etsiinf.upmnews.utils.async.DeleteArticleTask;

import static es.upm.etsiinf.upmnews.utils.SerializationUtils.createScaledImage;


public class AdaptadorListaArticulos extends BaseAdapter {
    private Context context;
    private List<Article> datos;
    private static LayoutInflater inflater = null;
    private Boolean visibleExtras = false;
    private String topic = "All";

    public AdaptadorListaArticulos(Context context, List<Article> datos, Boolean loggedin){
        this.context = context;
        this.datos = datos;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.visibleExtras = loggedin;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(
                    R.layout.article_list_item, viewGroup,false);
        }
        //final View vista = inflater.inflate(R.layout.article_list_item,viewGroup,false);
        if(visibleExtras) view.findViewById(R.id.extraButtons).setVisibility(View.VISIBLE);
        TextView titulo = view.findViewById(R.id.tituloArticulo);
        titulo.setText(this.datos.get(i).getTitleText());
        TextView resumen = view.findViewById(R.id.abstractTV);
        resumen.setText(Html.fromHtml(this.datos.get(i).getAbstractText(),Html.FROM_HTML_MODE_COMPACT));
        TextView categoria = view.findViewById(R.id.categoryTV);
        String category = this.datos.get(i).getCategory();
        categoria.setText(category);
        setColor(category, categoria);

           ImageView image = view.findViewById(R.id.articuloImageView);;
           Image img = this.datos.get(i).getImage();
            if (img != null) {
                byte[] decodedString = Base64.decode(img.getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Bitmap scaledImage = createScaledImage(decodedByte,2000,2000);
                image.setImageBitmap(scaledImage);
                image.setVisibility(View.VISIBLE);
            }else{
                image.setVisibility(View.INVISIBLE);
            }

        view.setTag(this.datos.get(i).getId());

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Aqui deberia crearse un nuevo intent y pasarle los datos de la imagen o hacer una consulta especifica del id del articulo a la api.
                CharSequence text = v.getTag().toString();
                Intent detalleArticulo = new Intent(context, DetailsScreen.class);
                detalleArticulo.putExtra( "id", text);
                context.startActivity(detalleArticulo);
            }
        });

        view.findViewById(R.id.deleteArticleButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                ViewParent parent = v.getParent();
                View grandparent = (View) parent.getParent();
                final String tag = grandparent.getTag().toString();
                // Setting Dialog Title
                alertDialog2.setTitle(R.string.confirm_delete);
                // Setting Dialog Message
                alertDialog2.setMessage(R.string.delete_query);
                // Setting Icon to Dialog
                alertDialog2.setIcon(R.drawable.delete);
                // Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteArticleTask task = new DeleteArticleTask(tag, context);
                                task.execute();
                            }
                        });
                // Setting Negative "NO" Btn
                alertDialog2.setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                // Showing Alert Dialog
                alertDialog2.show();
            }
        });

        view.findViewById(R.id.editArticleButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ViewParent parent = v.getParent();
                View grandparent = (View) parent.getParent();
                final CharSequence tag = grandparent.getTag().toString();
                Intent editArticle = new Intent(context, EditCreateForm.class);
                editArticle.putExtra( "id", tag);
                context.startActivity(editArticle);
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setDatos(List<Article> nuevosDatos){
        this.datos = nuevosDatos;
    }

    public void filter(String topic) {
        List<Article> filtered = new ArrayList<>();
        if(!topic.equals("All")){
            for (Article article : datos) {
                if (article.getCategory().equals(topic)) {
                    filtered.add(article);
                }
            }
            datos = filtered;
        }
    }

    private void setColor(String category, TextView label){
        switch (category){
            case "Sports":
                label.setBackgroundColor(ContextCompat.getColor(context,R.color.Sports));
                break;
            case "National":
                label.setBackgroundColor(ContextCompat.getColor(context,R.color.National));
                break;
            case "Economy":
                label.setBackgroundColor(ContextCompat.getColor(context,R.color.Economy));
                break;
            case "Technology":
                label.setBackgroundColor(ContextCompat.getColor(context,R.color.Technology));
                break;
        }
    }

}
