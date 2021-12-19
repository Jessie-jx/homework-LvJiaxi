package jessie.cs175.hw4_translator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jessie.cs175.hw4_translator.R
import jessie.cs175.hw4_translator.db.AppPreferences

class RootFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_root, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val transaction = fragmentManager?.beginTransaction()

        // When this container fragment is created, we fill it with our first "real" fragment
        if(AppPreferences.isLogin){
            transaction?.replace(R.id.root_frame, AccountFragment.newInstance(AppPreferences.username,AppPreferences.email,AppPreferences.headImages))
        }else{
            transaction?.replace(R.id.root_frame, MyFragment())
        }

        transaction?.commit()
    }
}