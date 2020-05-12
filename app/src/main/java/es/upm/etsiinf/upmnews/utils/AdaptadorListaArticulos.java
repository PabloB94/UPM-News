package es.upm.etsiinf.upmnews.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.upm.etsiinf.upmnews.DetailsScreen;
import es.upm.etsiinf.upmnews.MainActivity;
import es.upm.etsiinf.upmnews.R;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.model.Image;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

public class AdaptadorListaArticulos extends BaseAdapter {
    Context contexto;
    List<Article> datos;
    private static LayoutInflater inflater = null;

    public AdaptadorListaArticulos(Context contexto, List<Article> datos){
        this.contexto = contexto;
        this.datos = datos;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //LayoutInflater inflater = LayoutInflater.from(contexto);
        if (view == null) {
            view = inflater.inflate(
                    R.layout.article_list_item, viewGroup,false);
        }
        //final View vista = inflater.inflate(R.layout.article_list_item,viewGroup,false);
        TextView titulo = (TextView) view.findViewById(R.id.tituloArticulo);
        titulo.setText(this.datos.get(i).getTitleText());
        TextView resumen = (TextView) view.findViewById(R.id.abstractTV);
        resumen.setText(Html.fromHtml(this.datos.get(i).getAbstractText(),Html.FROM_HTML_MODE_COMPACT));
        TextView categoria = (TextView) view.findViewById(R.id.categoryTV);
        categoria.setText(this.datos.get(i).getCategory());
        ImageView image = (ImageView) view.findViewById(R.id.articuloImageView);;
        Image img = this.datos.get(i).getImage();
        if (img != null) {
            String imgbase64 = img.getImage();
            byte[] decodedString = Base64.decode(imgbase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            int width = decodedByte.getWidth();
            int height = decodedByte.getHeight();
            if(height > width){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) image.getLayoutParams();
                params.weight = 2.0f;
                image.setLayoutParams(params);
            }
            image.setImageBitmap(decodedByte);
        }
        view.setTag(this.datos.get(i).getId());

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Aqui deberia crearse un nuevo intent y pasarle los datos de la imagen o hacer una consulta especifica del id del articulo a la api.
                int duration = Toast.LENGTH_SHORT;
                CharSequence text = v.getTag().toString();
                Toast toast = Toast.makeText(contexto, text, duration);
                toast.show();
                Intent detalleArticulo = new Intent(contexto, DetailsScreen.class);
                detalleArticulo.putExtra( "id", text);
                contexto.startActivity(detalleArticulo);
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

}
