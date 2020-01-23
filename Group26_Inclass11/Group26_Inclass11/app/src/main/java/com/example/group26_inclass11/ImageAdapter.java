package com.example.group26_inclass11;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import static com.example.group26_inclass11.MainActivity.recyclerview;
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    ArrayList<Uri> sData;
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReference().child( "images/" );

    public ImageAdapter(ArrayList<Uri> sData) {
        this.sData = sData;
    }

    static ArrayList<Uri> uriList = new ArrayList<>();
    static RecyclerView.Adapter mAdapter;


    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.image_item, parent, false );
        ViewHolder vh = new ViewHolder( v );
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Uri s = sData.get( position );
        holder.img = s;
        Picasso.get().load( s ).into( holder.iv_image );
    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        Uri img;

        public ViewHolder(@NonNull final View itemView) {
            super( itemView );
            iv_image = itemView.findViewById( R.id.im_image );

            itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    StorageReference httpsReference = storage.getReferenceFromUrl( img + "" );
                    httpsReference.delete().addOnSuccessListener( new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        get_images();
                        }
                    } ).addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    } );
                    return true;
                }
            } );

        }

        void get_images() {
            uriList.clear();

            mAdapter = new ImageAdapter( uriList );
            mAdapter.notifyDataSetChanged();
            recyclerview.removeAllViews();
            storageRef.listAll()

                    .addOnSuccessListener( new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            if(listResult.getItems().size() < 1){
                                mAdapter = new ImageAdapter(uriList);
                                recyclerview.setAdapter(mAdapter);
                            }

                            for (StorageReference item : listResult.getItems()) {
                                StorageReference gsReference = storage.getReferenceFromUrl( item + "" );
                                gsReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        uriList.add( uri );
                                        mAdapter = new ImageAdapter( uriList );
                                        recyclerview.setAdapter( mAdapter );
//                                    recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                                    }
                                } ).addOnFailureListener( new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                } );
                            }


                        }
                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Uh-oh, an error occurred!
                        }
                    } );
        }
    }
}
